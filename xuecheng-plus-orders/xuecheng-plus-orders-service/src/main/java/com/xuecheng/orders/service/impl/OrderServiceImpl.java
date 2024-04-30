package com.xuecheng.orders.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.utils.IdWorkerUtils;
import com.xuecheng.base.utils.QRCodeUtil;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MqMessageService;
import com.xuecheng.orders.config.AlipayConfig;
import com.xuecheng.orders.config.PayNotifyConfig;
import com.xuecheng.orders.mapper.XcOrdersGoodsMapper;
import com.xuecheng.orders.mapper.XcOrdersMapper;
import com.xuecheng.orders.mapper.XcPayRecordMapper;
import com.xuecheng.orders.model.dto.AddOrderDto;
import com.xuecheng.orders.model.dto.PayRecordDto;
import com.xuecheng.orders.model.dto.PayStatusDto;
import com.xuecheng.orders.model.po.XcOrders;
import com.xuecheng.orders.model.po.XcOrdersGoods;
import com.xuecheng.orders.model.po.XcPayRecord;
import com.xuecheng.orders.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-29 17:00:19
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService
{
    @Autowired
    private XcOrdersMapper ordersMapper;

    @Autowired
    private XcOrdersGoodsMapper ordersGoodsMapper;

    @Autowired
    private XcPayRecordMapper payRecordMapper;

    @Autowired
    private OrderServiceImpl currentProxy;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MqMessageService mqMessageService;

    @Value("${pay.qrcodeurl}")
    private String qrcodeurl;

    @Value("${pay.alipay.APP_ID}")
    String APP_ID;
    @Value("${pay.alipay.APP_PRIVATE_KEY}")
    String APP_PRIVATE_KEY;

    @Value("${pay.alipay.ALIPAY_PUBLIC_KEY}")
    String ALIPAY_PUBLIC_KEY;

    /**
     * 创建订单
     *
     * @param userId
     * @param addOrderDto
     * @return {@link PayRecordDto}
     */
    @Transactional
    @Override
    public PayRecordDto createOrder(String userId, AddOrderDto addOrderDto) {
        // 1、创建商品订单
        XcOrders orders = saveXcOrders(userId, addOrderDto);
        if (orders == null) {
            XueChengPlusException.cast("订单创建失败");
        }
        if (orders
                .getStatus()
                .equals("600002")) {
            XueChengPlusException.cast("订单已支付");
        }

        // 2、生成支付记录
        XcPayRecord payRecord = createPayRecord(orders);

        // 3、生成二维码
        String qrCode = null;
        try {
            //url要可以被模拟器访问到，url为下单接口(稍后定义)
            String url = String.format(qrcodeurl, payRecord.getPayNo());
            qrCode = new QRCodeUtil().createQRCode(url, 200, 200);
            log.info("qrCode:{}", qrCode);
        }
        catch (IOException e) {
            XueChengPlusException.cast("生成二维码出错");
        }

        PayRecordDto payRecordDto = new PayRecordDto();
        BeanUtils.copyProperties(payRecord, payRecordDto);
        payRecordDto.setQrcode(qrCode);
        return payRecordDto;
    }

    /**
     * 保存订单
     *
     * @param userId
     * @param addOrderDto
     * @return {@link XcOrders}
     */
    @Transactional
    public XcOrders saveXcOrders(String userId, AddOrderDto addOrderDto) {
        // 幂等性处理
        XcOrders order = getOrderByBusinessId(addOrderDto.getOutBusinessId());
        if (order != null) {
            return order;
        }

        // 生成订单号
        long orderId = IdWorkerUtils
                .getInstance()
                .nextId();

        // 添加订单
        order = new XcOrders();
        order.setId(orderId);
        order.setTotalPrice(addOrderDto.getTotalPrice());
        order.setCreateDate(LocalDateTime.now());
        order.setStatus("600001");//未支付
        order.setUserId(userId);
        order.setOrderType(addOrderDto.getOrderType());
        order.setOrderName(addOrderDto.getOrderName());
        order.setOrderDetail(addOrderDto.getOrderDetail());
        order.setOrderDescrip(addOrderDto.getOrderDescrip());
        order.setOutBusinessId(addOrderDto.getOutBusinessId());//选课记录id
        ordersMapper.insert(order);

        // 添加订单明细
        String orderDetailJson = addOrderDto.getOrderDetail();
        List<XcOrdersGoods> xcOrdersGoodsList = JSON.parseArray(orderDetailJson, XcOrdersGoods.class);
        xcOrdersGoodsList.forEach(goods -> {
            XcOrdersGoods xcOrdersGoods = new XcOrdersGoods();
            BeanUtils.copyProperties(goods, xcOrdersGoods);
            xcOrdersGoods.setOrderId(orderId);//订单号
            ordersGoodsMapper.insert(xcOrdersGoods);
        });

        return order;
    }

    /**
     * 根据业务id查询订单
     *
     * @param businessId
     * @return {@link XcOrders}
     */
    public XcOrders getOrderByBusinessId(String businessId) {
        return ordersMapper.selectOne(new LambdaQueryWrapper<XcOrders>().eq(XcOrders::getOutBusinessId, businessId));
    }

    /**
     * 创建支付记录
     *
     * @param orders
     * @return {@link XcPayRecord}
     */
    public XcPayRecord createPayRecord(XcOrders orders) {
        if (orders == null) {
            XueChengPlusException.cast("订单不存在");
        }
        if (orders
                .getStatus()
                .equals("600002")) {
            XueChengPlusException.cast("订单已支付");
        }

        // 生成支付交易流水号
        long payNo = IdWorkerUtils
                .getInstance()
                .nextId();

        // 添加支付记录
        XcPayRecord payRecord = new XcPayRecord();
        payRecord.setPayNo(payNo);
        payRecord.setOrderId(orders.getId());//商品订单号
        payRecord.setOrderName(orders.getOrderName());
        payRecord.setTotalPrice(orders.getTotalPrice());
        payRecord.setCurrency("CNY");
        payRecord.setCreateDate(LocalDateTime.now());
        payRecord.setStatus("601001");//未支付
        payRecord.setUserId(orders.getUserId());
        payRecordMapper.insert(payRecord);

        return payRecord;
    }

    /**
     * 查询支付交易记录
     *
     * @param payNo 交易记录号
     * @return com.xuecheng.orders.model.po.XcPayRecord
     */
    @Override
    public XcPayRecord getPayRecordByPayno(String payNo) {
        return payRecordMapper.selectOne(new LambdaQueryWrapper<XcPayRecord>().eq(XcPayRecord::getPayNo, payNo));
    }

    /**
     * 请求支付宝查询支付结果
     *
     * @param payNo
     * @return {@link PayRecordDto}
     */
    @Override
    public PayRecordDto queryPayResult(String payNo) {
        XcPayRecord payRecord = getPayRecordByPayno(payNo);
        if (payRecord == null) {
            XueChengPlusException.cast("请重新点击支付获取二维码");
        }

        // 支付状态
        String status = payRecord.getStatus();
        // 如果支付成功直接返回
        // [{"code":"601001","desc":"未支付"},{"code":"601002","desc":"已支付"},{"code":"601003","desc":"已退款"}]
        if ("601002".equals(status)) {
            PayRecordDto payRecordDto = new PayRecordDto();
            BeanUtils.copyProperties(payRecord, payRecordDto);
            return payRecordDto;
        }

        // 从支付宝查询支付结果
        PayStatusDto payStatusDto = queryPayResultFromAlipay(payNo);
        // 保存支付结果
        currentProxy.saveAliPayStatus(payStatusDto);

        // 重新查询支付记录
        payRecord = getPayRecordByPayno(payNo);
        PayRecordDto payRecordDto = new PayRecordDto();
        BeanUtils.copyProperties(payRecord, payRecordDto);
        return payRecordDto;

    }

    /**
     * 请求支付宝查询支付结果
     *
     * @param payNo 支付交易号
     * @return 支付结果
     */
    public PayStatusDto queryPayResultFromAlipay(String payNo) {
        //========请求支付宝查询支付结果=============
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, APP_ID, APP_PRIVATE_KEY, "json", AlipayConfig.CHARSET, ALIPAY_PUBLIC_KEY,
                AlipayConfig.SIGNTYPE); //获得初始化的AlipayClient
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", payNo);
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            if (!response.isSuccess()) {
                XueChengPlusException.cast("请求支付查询查询失败");
            }
        }
        catch (AlipayApiException e) {
            log.error("请求支付宝查询支付结果异常:{}", e.getMessage(), e);
            XueChengPlusException.cast("请求支付查询查询失败");
        }

        // 获取支付结果
        String resultJson = response.getBody();
        // 转map
        Map<String, Object> resultMap = JSON.parseObject(resultJson, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> alipay_trade_query_response = (Map<String, Object>) resultMap.get("alipay_trade_query_response");
        // 支付结果
        String trade_status = (String) alipay_trade_query_response.get("trade_status");
        String total_amount = (String) alipay_trade_query_response.get("total_amount");
        String trade_no = (String) alipay_trade_query_response.get("trade_no");

        // 保存支付结果
        PayStatusDto payStatusDto = new PayStatusDto();
        payStatusDto.setOut_trade_no(payNo);
        payStatusDto.setTrade_status(trade_status);
        payStatusDto.setApp_id(APP_ID);
        payStatusDto.setTrade_no(trade_no);
        payStatusDto.setTotal_amount(total_amount);
        return payStatusDto;
    }

    @Transactional
    @Override
    public void saveAliPayStatus(PayStatusDto payStatusDto) {
        // 支付流水号
        String payNo = payStatusDto.getOut_trade_no();
        XcPayRecord payRecordByPayno = getPayRecordByPayno(payNo);
        if (payRecordByPayno == null) {
            XueChengPlusException.cast("支付记录找不到");
        }
        // 拿到相关联的订单id
        Long orderId = payRecordByPayno.getOrderId();
        XcOrders xcOrders = ordersMapper.selectById(orderId);
        if (xcOrders == null) {
            XueChengPlusException.cast("找不到相关联的订单");
        }
        // 支付状态
        String statusFromDb = payRecordByPayno.getStatus();
        // 如果数据库支付的状态已经是成功了，不再处理了
        if ("601002".equals(statusFromDb)) {
            return;
        }

        // 支付成功
        String trade_status = payStatusDto.getTrade_status();
        log.debug("收到支付结果:{},支付记录:{}}", payStatusDto, payRecordByPayno);
        if (trade_status.equals("TRADE_SUCCESS")) {
            // 更新支付记录表的状态为支付成功
            payRecordByPayno.setStatus("601002");
            // 支付宝的订单号
            payRecordByPayno.setOutPayNo(payStatusDto.getTrade_no());
            // 第三方支付渠道编号
            payRecordByPayno.setOutPayChannel("Alipay");
            // 支付成功时间
            payRecordByPayno.setPaySuccessTime(LocalDateTime.now());
            payRecordMapper.updateById(payRecordByPayno);

            // 更新订单表的状态为支付成功
            xcOrders.setStatus("600002");// 订单状态为交易成功
            ordersMapper.updateById(xcOrders);

            // 将消息写到数据库
            MqMessage mqMessage = mqMessageService.addMessage(PayNotifyConfig.MESSAGE_TYPE, xcOrders.getOutBusinessId(), xcOrders.getOrderType(), null);
            // 发送消息
            notifyPayResult(mqMessage);
        }

    }

    @Override
    public void notifyPayResult(MqMessage message) {
        // 1、消息体，转json
        String msg = JSON.toJSONString(message);
        // 设置消息持久化
        Message msgObj = MessageBuilder
                .withBody(msg.getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();
        // 2、全局唯一的消息ID，需要封装到CorrelationData中
        CorrelationData correlationData = new CorrelationData(message
                .getId()
                .toString());
        // 3、添加callback
        correlationData
                .getFuture()
                .addCallback(result -> {
                    if (result.isAck()) {
                        // 3.1.ack，消息成功
                        log.debug("通知支付结果消息发送成功, ID:{}", correlationData.getId());
                        //删除消息表中的记录
                        mqMessageService.completed(message.getId());
                    }
                    else {
                        // 3.2.nack，消息失败
                        log.error("通知支付结果消息发送失败, ID:{}, 原因{}", correlationData.getId(), result.getReason());
                    }
                }, ex -> log.error("消息发送异常, ID:{}, 原因{}", correlationData.getId(), ex.getMessage()));
        // 发送消息
        rabbitTemplate.convertAndSend(PayNotifyConfig.PAYNOTIFY_EXCHANGE_FANOUT, "", msgObj, correlationData);

    }

}
