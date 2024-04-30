package com.xuecheng.orders.model.dto;

import com.xuecheng.orders.model.po.XcPayRecord;
import lombok.Data;
import lombok.ToString;

/**
 * @author VectorX
 * @version 1.0.0
 * @description 支付记录dto
 * @date 2024/04/29
 * @see XcPayRecord
 */
@Data
@ToString
public class PayRecordDto extends XcPayRecord
{

    //二维码
    private String qrcode;

}
