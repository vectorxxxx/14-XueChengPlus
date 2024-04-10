package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
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
    private TeachplanMapper teachplanMapper;

    /**
     * 保存课程计划
     *
     * @param teachplanDto 课程计划信息
     */
    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {
        // 课程计划id
        Long id = teachplanDto.getId();
        // 修改课程计划
        if (id != null) {
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplanDto, teachplan);
            teachplanMapper.updateById(teachplan);
        }
        else {
            Teachplan teachplanNew = new Teachplan();
            // 设置排序号
            // 取出同父同级别的课程计划数量
            int count = getTeachplanCount(teachplanDto.getCourseId(), teachplanDto.getParentid());
            teachplanNew.setOrderby(count + 1);
            BeanUtils.copyProperties(teachplanDto, teachplanNew);
            teachplanMapper.insert(teachplanNew);
        }
    }

    /**
     * 获取最新的排序号
     *
     * @param courseId 课程id
     * @param parentId 父课程计划id
     * @return int 最新排序号
     */
    private int getTeachplanCount(long courseId, long parentId) {
        return teachplanMapper
                .selectList(new LambdaQueryWrapper<Teachplan>()
                        .select(Teachplan::getOrderby)
                        .eq(Teachplan::getCourseId, courseId)
                        .eq(Teachplan::getParentid, parentId))
                .stream()
                .map(Teachplan::getOrderby)
                //  .max(Integer::compare)
                .max(Comparator.naturalOrder())
                .orElse(0);
    }

    @Override
    public List<TeachplanDto> findTeachplanTree(long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }
}

