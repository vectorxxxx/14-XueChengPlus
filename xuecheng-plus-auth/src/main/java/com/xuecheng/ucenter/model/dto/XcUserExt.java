package com.xuecheng.ucenter.model.dto;

import com.xuecheng.ucenter.model.po.XcUser;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VectorX
 * @version 1.0.0
 * @description 用户扩展信息
 * @date 2024/04/24
 * @see XcUser
 */
@Data
public class XcUserExt extends XcUser
{
    private static final long serialVersionUID = 668584812040466336L;

    //用户权限
    List<String> permissions = new ArrayList<>();
}
