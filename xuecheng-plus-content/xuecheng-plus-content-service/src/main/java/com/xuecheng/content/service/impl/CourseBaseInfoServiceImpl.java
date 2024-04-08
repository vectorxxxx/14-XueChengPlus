package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-08 10:23:29
 */
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService
{
    @Autowired
    private CourseBaseMapper courseBaseMapper;

    /**
     * 课程查询接口
     *
     * @param pageParams           分页条件
     * @param queryCourseParamsDto 查询条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.content.model.po.CourseBase>
     */
    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {
        // 查询条件
        final LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<CourseBase>()
                // 根据课程名称查询
                .like(StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()), CourseBase::getName, queryCourseParamsDto.getCourseName())
                // 根据课程审核状态查询
                .eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()), CourseBase::getAuditStatus, queryCourseParamsDto.getAuditStatus())
                // 根据课程发布状态查询
                .eq(StringUtils.isNotEmpty(queryCourseParamsDto.getPublishStatus()), CourseBase::getStatus, queryCourseParamsDto.getPublishStatus());

        // 分页条件
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());

        // 查询结果
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);

        // 数据列表
        List<CourseBase> list = pageResult.getRecords();
        // 数据总数
        long total = pageResult.getTotal();

        // 构建结果集
        return new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());
    }
}
