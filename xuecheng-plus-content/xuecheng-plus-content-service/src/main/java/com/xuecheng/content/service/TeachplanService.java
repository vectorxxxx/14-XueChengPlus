package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;

import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description 课程基本信息管理业务接口
 * @date 2024-04-10 11:15:27
 */
public interface TeachplanService
{
    /**
     * 保存课程计划
     *
     * @param teachplanDto 课程计划信息
     */
    void saveTeachplan(SaveTeachplanDto teachplanDto);

    /**
     * 查询课程计划树型结构
     *
     * @param courseId 课程id
     * @return List<TeachplanDto>
     */
    List<TeachplanDto> findTeachplanTree(long courseId);
}
