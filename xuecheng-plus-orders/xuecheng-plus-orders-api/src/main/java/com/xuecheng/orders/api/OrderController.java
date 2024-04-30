package com.xuecheng.orders.api;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.orders.config.AlipayConfig;
import com.xuecheng.orders.model.dto.AddOrderDto;
import com.xuecheng.orders.model.dto.PayRecordDto;
import com.xuecheng.orders.model.dto.PayStatusDto;
import com.xuecheng.orders.model.po.XcPayRecord;
import com.xuecheng.orders.service.OrderService;
import com.xuecheng.orders.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author VectorX
 * @version V1.0
 * @description 订单支付接口
 * @date 2024-04-29 16:58:25
 */
@Api(value = "订单支付接口",
     tags = "订单支付接口")
@RestController
@Slf4j
public class OrderController
{
    @Autowired
    private OrderService orderService;

    @Value("${pay.alipay.APP_ID}")
    private String APP_ID;
    @Value("${pay.alipay.APP_PRIVATE_KEY}")
    private String APP_PRIVATE_KEY;
    @Value("${pay.alipay.ALIPAY_PUBLIC_KEY}")
    private String ALIPAY_PUBLIC_KEY;

    /**
     * 生成支付二维码
     *
     * @param addOrderDto
     * @return {@link PayRecordDto}
     */
    @ApiOperation("生成支付二维码")
    @PostMapping("/generatepaycode")
    @ResponseBody
    public PayRecordDto generatePayCode(
            @RequestBody
                    AddOrderDto addOrderDto) {
        // 登录用户
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        if (user == null) {
            XueChengPlusException.cast("请登录后继续选课");
        }

        return orderService.createOrder(user.getId(), addOrderDto);
    }

    /**
     * 扫码下单接口
     *
     * @param payNo
     * @param httpResponse
     * @throws IOException
     */
    @ApiOperation("扫码下单接口")
    @GetMapping("/requestpay")
    public void requestpay(String payNo, HttpServletResponse httpResponse) throws IOException {
        // 如果payNo不存在则提示重新发起支付
        XcPayRecord payRecord = orderService.getPayRecordByPayno(payNo);
        if (payRecord == null) {
            XueChengPlusException.cast("请重新点击支付获取二维码");
        }
        // 支付状态:
        // [
        //     {
        //         "code": "600001",
        //         "desc": "未支付"
        //     },
        //     {
        //         "code": "600002",
        //         "desc": "已支付"
        //     },
        //     {
        //         "code": "600003",
        //         "desc": "已关闭"
        //     },
        //     {
        //         "code": "600004",
        //         "desc": "已退款"
        //     },
        //     {
        //         "code": "600005",
        //         "desc": "已完成"
        //     }
        // ]
        String status = payRecord.getStatus();
        if ("601002".equals(status)) {
            XueChengPlusException.cast("订单已支付，请勿重复支付。");
        }

        // 1、构造sdk的客户端对象
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, APP_ID, APP_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, ALIPAY_PUBLIC_KEY,
                AlipayConfig.SIGNTYPE);//获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        //        alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
        //        alipayRequest.setNotifyUrl("http://tjxt-user-t.itheima.net/xuecheng/orders/paynotify");//在公共参数中设置回跳和通知地址
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", payRecord.getPayNo());
        bizContent.put("total_amount", payRecord.getTotalPrice());
        bizContent.put("subject", payRecord.getOrderName());
        bizContent.put("product_code", "QUICK_WAP_WAY");
        alipayRequest.setBizContent(bizContent.toJSONString());//填充业务参数

        // 2、请求支付宝下单接口,发起http请求
        String form = "";
        try {
            form = client
                    .pageExecute(alipayRequest)
                    .getBody(); //调用SDK生成表单
        }
        catch (AlipayApiException e) {
            e.printStackTrace();
        }

        // 3、将表单输出到页面
        httpResponse.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
        final PrintWriter writer = httpResponse.getWriter();
        writer.write(form);
        writer.flush();
        writer.close();
    }

    @ApiOperation("查询支付结果")
    @GetMapping("/payresult")
    @ResponseBody
    public PayRecordDto payresult(String payNo) throws IOException {
        // 调用支付宝接口查询
        return orderService.queryPayResult(payNo);
    }

    @ApiOperation("接收支付结果通知")
    @PostMapping("/receivenotify")
    public void receivenotify(HttpServletRequest request, HttpServletResponse out) throws UnsupportedEncodingException, AlipayApiException {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ?
                           valueStr + values[i] :
                           valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        // 验签
        boolean verify_result = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");

        if (verify_result) {//验证成功

            //商户订单号
            String out_trade_no = new String(request
                    .getParameter("out_trade_no")
                    .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //支付宝交易号
            String trade_no = new String(request
                    .getParameter("trade_no")
                    .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //交易状态
            String trade_status = new String(request
                    .getParameter("trade_status")
                    .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //appid
            String app_id = new String(request
                    .getParameter("app_id")
                    .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //total_amount
            String total_amount = new String(request
                    .getParameter("total_amount")
                    .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            //交易成功处理
            if (trade_status.equals("TRADE_SUCCESS")) {

                PayStatusDto payStatusDto = new PayStatusDto();
                payStatusDto.setOut_trade_no(out_trade_no);
                payStatusDto.setTrade_status(trade_status);
                payStatusDto.setApp_id(app_id);
                payStatusDto.setTrade_no(trade_no);
                payStatusDto.setTotal_amount(total_amount);

                //处理逻辑。。。
                orderService.saveAliPayStatus(payStatusDto);

            }
        }

    }

}
