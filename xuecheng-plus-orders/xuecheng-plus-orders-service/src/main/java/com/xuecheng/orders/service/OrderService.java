package com.xuecheng.orders.service;

import com.xuecheng.orders.model.dto.AddOrderDto;
import com.xuecheng.orders.model.dto.PayRecordDto;
import com.xuecheng.orders.model.dto.PayStatusDto;
import com.xuecheng.orders.model.po.XcPayRecord;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-29 16:59:47
 */
public interface OrderService
{
    /**
     * 创建商品订单
     *
     * @param addOrderDto 订单信息
     * @return PayRecordDto 支付交易记录(包括二维码)
     */
    PayRecordDto createOrder(String userId, AddOrderDto addOrderDto);

    /**
     * @param payNo 交易记录号
     * @return com.xuecheng.orders.model.po.XcPayRecord
     * @description 查询支付交易记录
     * @author Mr.M
     * @date 2022/10/20 23:38
     */
    XcPayRecord getPayRecordByPayno(String payNo);

    /**
     * 请求支付宝查询支付结果
     *
     * @param payNo 支付记录id
     * @return 支付记录信息
     */
    PayRecordDto queryPayResult(String payNo);

    /**
     * 保存支付宝支付结果
     *
     * @param payStatusDto 支付结果信息
     * @return void
     */
    void saveAliPayStatus(PayStatusDto payStatusDto);

}
