package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;

import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description 课程基本信息管理业务接口
 * @date 2024-04-10 11:15:27
 */
public interface TeachplanService extends IService<Teachplan>
{
    /**
     * 上移课程计划
     *
     * @param id
     */
    void moveupTeachplan(String id);

    /**
     * 下移课程计划
     *
     * @param id
     */
    void movedownTeachplan(String id);

    /**
     * 删除课程计划
     *
     * @param id 课程计划id
     */
    void delTeachplan(String id);

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

    /**
     * 教学计划绑定媒资
     *
     * @param bindTeachplanMediaDto
     * @return com.xuecheng.content.model.po.TeachplanMedia
     */
    TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);

    /**
     * 教学计划解除绑定媒资
     *
     * @param teachplanId
     * @param mediaId
     */
    void unbindMedia(Integer teachplanId, String mediaId);
}
