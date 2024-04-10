package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description 课程计划service接口实现类
 * @date 2024-04-10 11:15:58
 */
@Service
public class TeachplanServiceImpl implements TeachplanService
{

    @Autowired
    TeachplanMapper teachplanMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }
}

