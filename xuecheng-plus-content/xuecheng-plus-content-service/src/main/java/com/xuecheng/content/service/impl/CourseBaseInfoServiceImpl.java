package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.*;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.*;
import com.xuecheng.content.service.CourseBaseInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    /**
     * 删除课程相关的基本信息、营销信息、课程计划、课程教师信息
     *
     * @param courseId
     */
    @Transactional
    @Override
    public void deleteCourseInfo(Long courseId) {
        // 课程的审核状态为未提交时方可删除。
        // [
        //     {
        //         "code": "202001",
        //         "desc": "审核未通过"
        //     },
        //     {
        //         "code": "202002",
        //         "desc": "未提交"
        //     },
        //     {
        //         "code": "202003",
        //         "desc": "已提交"
        //     },
        //     {
        //         "code": "202004",
        //         "desc": "审核通过"
        //     }
        // ]
        final String auditStatus = courseBaseMapper
                .selectById(courseId)
                .getAuditStatus();
        if (!"202002".equals(auditStatus)) {
            XueChengPlusException.cast("课程审核状态已提交，无法删除");
        }

        // 删除课程基本信息
        courseBaseMapper.deleteById(courseId);

        // 删除课程营销信息
        courseMarketMapper.deleteById(courseId);

        // 删除课程计划
        teachplanMapper.delete(new LambdaQueryWrapper<Teachplan>().eq(Teachplan::getCourseId, courseId));

        // 删除课程教师信息
        courseTeacherMapper.delete(new LambdaQueryWrapper<CourseTeacher>().eq(CourseTeacher::getCourseId, courseId));
    }

    /**
     * 修改课程信息
     *
     * @param companyId
     * @param dto
     * @return {@link CourseBaseInfoDto}
     */
    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto) {
        // 课程id
        Long courseId = dto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            XueChengPlusException.cast("课程不存在");
        }

        // 校验本机构只能修改本机构的课程
        if (!courseBase
                .getCompanyId()
                .equals(companyId)) {
            XueChengPlusException.cast("本机构只能修改本机构的课程");
        }

        // 更新课程基本信息
        BeanUtils.copyProperties(dto, courseBase);
        courseBase.setChangeDate(LocalDateTime.now());
        courseBaseMapper.updateById(courseBase);

        // 更新课程营销信息
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto, courseMarket);
        saveCourseMarket(courseMarket);

        // 查询课程信息
        return this.getCourseBaseInfo(courseId);
    }

    /**
     * 添加课程基本信息
     *
     * @param companyId    教学机构id
     * @param addCourseDto 课程基本信息
     * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
     */
    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto) {
        // 合法性校验
        if (StringUtils.isBlank(addCourseDto.getName())) {
            throw new XueChengPlusException("课程名称为空");
        }
        if (StringUtils.isBlank(addCourseDto.getMt())) {
            throw new XueChengPlusException("课程分类为空");
        }
        if (StringUtils.isBlank(addCourseDto.getSt())) {
            throw new XueChengPlusException("课程分类为空");
        }
        if (StringUtils.isBlank(addCourseDto.getGrade())) {
            throw new XueChengPlusException("课程等级为空");
        }
        if (StringUtils.isBlank(addCourseDto.getTeachmode())) {
            throw new XueChengPlusException("教育模式为空");
        }
        if (StringUtils.isBlank(addCourseDto.getUsers())) {
            throw new XueChengPlusException("适应人群为空");
        }
        final String charge = addCourseDto.getCharge();
        if (StringUtils.isBlank(charge)) {
            throw new XueChengPlusException("收费规则为空");
        }

        // 新增对象
        CourseBase courseBaseNew = new CourseBase();
        BeanUtils.copyProperties(addCourseDto, courseBaseNew);
        // 审核状态
        courseBaseNew.setAuditStatus("202002");
        // 发布状态
        courseBaseNew.setStatus("203001");
        // 机构id
        courseBaseNew.setCompanyId(companyId);
        // 添加时间
        courseBaseNew.setCreateDate(LocalDateTime.now());
        // 插入课程基本信息表
        int insert = courseBaseMapper.insert(courseBaseNew);
        if (insert <= 0) {
            throw new XueChengPlusException("新增课程基本信息失败");
        }

        // 课程营销信息
        CourseMarket courseMarketNew = new CourseMarket();
        BeanUtils.copyProperties(addCourseDto, courseMarketNew);
        Long courseId = courseBaseNew.getId();
        courseMarketNew.setId(courseId);
        int i = saveCourseMarket(courseMarketNew);
        if (i <= 0) {
            throw new XueChengPlusException("保存课程营销信息失败");
        }

        // 查询课程基本信息及营销信息并返回
        return getCourseBaseInfo(courseId);
    }

    /**
     * 保存课程营销信息
     *
     * @param courseMarketNew
     * @return int
     */
    private int saveCourseMarket(CourseMarket courseMarketNew) {
        //收费规则
        String charge = courseMarketNew.getCharge();
        if (StringUtils.isBlank(charge)) {
            throw new XueChengPlusException("收费规则没有选择");
        }
        //收费规则为收费
        if (charge.equals("201001")) {
            if (courseMarketNew.getPrice() == null || courseMarketNew.getPrice() <= 0) {
                throw new XueChengPlusException("课程为收费价格不能为空且必须大于0");
            }
        }

        //根据id从课程营销表查询
        CourseMarket courseMarketObj = courseMarketMapper.selectById(courseMarketNew.getId());
        if (courseMarketObj == null) {
            return courseMarketMapper.insert(courseMarketNew);
        }
        else {
            BeanUtils.copyProperties(courseMarketNew, courseMarketObj);
            courseMarketObj.setId(courseMarketNew.getId());
            return courseMarketMapper.updateById(courseMarketObj);
        }
    }

    /**
     * 根据课程id查询课程基本信息，包括基本信息和营销信息
     *
     * @param courseId
     * @return {@link CourseBaseInfoDto}
     */
    @Override
    public CourseBaseInfoDto getCourseBaseInfo(long courseId) {
        // 基本信息
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            return null;
        }
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);

        // 营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        if (courseMarket != null) {
            BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);
        }

        // 分类名称
        CourseCategory courseCategoryBySt = courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setStName(courseCategoryBySt.getName());
        CourseCategory courseCategoryByMt = courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(courseCategoryByMt.getName());

        return courseBaseInfoDto;
    }

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
