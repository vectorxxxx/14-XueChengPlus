package com.xuecheng.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.mapper.CoursePublishPreMapper;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CoursePublishPre;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description 课程预览、发布接口
 * @date 2024-04-15 20:07:04
 */
@Service
public class CoursePublishServiceImpl implements CoursePublishService
{
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;

    @Autowired
    private TeachplanService teachplanService;

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Autowired
    private CoursePublishPreMapper coursePublishPreMapper;

    @Override
    public void commitAudit(Long companyId, Long courseId) {
        // 1、约束校验
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        // 1.1、当前审核状态为已提交不允许再次提交
        if ("202003".equals(courseBase.getAuditStatus())) {
            XueChengPlusException.cast("当前为等待审核状态，审核完成可以再次提交。");
        }
        // 1.2、本机构只允许提交本机构的课程
        if (!courseBase
                .getCompanyId()
                .equals(companyId)) {
            XueChengPlusException.cast("不允许提交其它机构的课程。");
        }
        // 1.3、课程图片是否填写
        if (StringUtils.isEmpty(courseBase.getPic())) {
            XueChengPlusException.cast("提交失败，请上传课程图片");
        }
        // 1.4、查询课程计划信息
        List<TeachplanDto> teachplanTree = teachplanService.findTeachplanTree(courseId);
        if (teachplanTree.size() <= 0) {
            XueChengPlusException.cast("提交失败，还没有添加课程计划");
        }

        // 2、添加课程预发布记录
        CoursePublishPre coursePublishPre = new CoursePublishPre();
        // 2.1、课程基本信息
        CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);
        BeanUtils.copyProperties(courseBaseInfo, coursePublishPre);
        // 2.2、课程营销信息
        coursePublishPre.setMarket(JSON.toJSONString(courseMarketMapper.selectById(courseId)));
        // 2.4、课程计划信息
        coursePublishPre.setTeachplan(JSON.toJSONString(teachplanTree));

        // 设置预发布记录状态,已提交
        coursePublishPre.setStatus("202003");
        // 教学机构id
        coursePublishPre.setCompanyId(companyId);
        // 提交时间
        coursePublishPre.setCreateDate(LocalDateTime.now());

        // 添加课程预发布记录
        CoursePublishPre coursePublishPreUpdate = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPreUpdate == null) {
            coursePublishPreMapper.insert(coursePublishPre);
        }
        else {
            coursePublishPreMapper.updateById(coursePublishPre);
        }

        // 3、更新课程基本表的审核状态
        courseBase.setAuditStatus("202003");
        courseBaseMapper.updateById(courseBase);
    }

    /**
     * 获取课程预览信息
     *
     * @param courseId 课程id
     * @return com.xuecheng.content.model.dto.CoursePreviewDto
     */
    @Override
    public CoursePreviewDto getCoursePreviewInfo(Long courseId) {
        // 课程基本信息、营销信息
        CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);

        // 课程计划信息
        List<TeachplanDto> teachplanTree = teachplanService.findTeachplanTree(courseId);

        CoursePreviewDto coursePreviewDto = new CoursePreviewDto();
        coursePreviewDto.setCourseBase(courseBaseInfo);
        coursePreviewDto.setTeachplans(teachplanTree);
        return coursePreviewDto;
    }
}
