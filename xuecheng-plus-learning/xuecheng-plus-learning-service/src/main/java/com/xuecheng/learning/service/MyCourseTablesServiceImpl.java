package com.xuecheng.learning.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.learning.feignclient.ContentServiceClient;
import com.xuecheng.learning.mapper.XcChooseCourseMapper;
import com.xuecheng.learning.mapper.XcCourseTablesMapper;
import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.model.po.XcChooseCourse;
import com.xuecheng.learning.model.po.XcCourseTables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-28 16:20:38
 */
@Slf4j
@Service
public class MyCourseTablesServiceImpl implements MyCourseTablesService
{
    @Autowired
    private XcChooseCourseMapper xcChooseCourseMapper;

    @Autowired
    private XcCourseTablesMapper xcCourseTablesMapper;

    @Autowired
    private ContentServiceClient contentServiceClient;

    @Autowired
    private MyCourseTablesService myCourseTablesService;

    @Autowired
    private MyCourseTablesServiceImpl currentProxy;

    @Transactional
    @Override
    public XcChooseCourseDto addChooseCourse(String userId, Long courseId) {
        //查询课程信息
        CoursePublish coursepublish = contentServiceClient.getCoursepublish(courseId);
        //课程收费标准
        String charge = coursepublish.getCharge();
        //选课记录
        XcChooseCourse chooseCourse;
        //[{"code":"201000","desc":"免费"},{"code":"201001","desc":"收费"}]
        if ("201000".equals(charge)) {
            //添加免费课程
            chooseCourse = addFreeCourse(userId, coursepublish);
            //添加到我的课程表
            XcCourseTables xcCourseTables = addCourseTables(chooseCourse);
        }
        else {
            //添加收费课程
            chooseCourse = addChargeCoruse(userId, coursepublish);
        }

        // 获取学习资格
        XcChooseCourseDto xcChooseCourseDto = new XcChooseCourseDto();
        BeanUtils.copyProperties(chooseCourse, xcChooseCourseDto);
        XcCourseTablesDto xcCourseTablesDto = getLearningStatus(userId, courseId);
        xcChooseCourseDto.setLearnStatus(xcCourseTablesDto.getLearnStatus());
        return xcChooseCourseDto;
    }

    @Override
    public XcCourseTablesDto getLearningStatus(String userId, Long courseId) {
        // 返回的结果
        XcCourseTablesDto courseTablesDto = new XcCourseTablesDto();

        // 查询我的课程表，如果查不到说明没有选课
        XcCourseTables xcCourseTables = getXcCourseTables(userId, courseId);
        if (xcCourseTables == null) {
            //"code":"702002","desc":"没有选课或选课后没有支付"
            courseTablesDto.setLearnStatus("702002");
            return courseTablesDto;
        }

        // 如果查到了，判断是否过期，如果过期不能继续学习，没有过期可以继续学习
        boolean before = xcCourseTables
                .getValidtimeEnd()
                .isBefore(LocalDateTime.now());
        if (before) {
            //"code":"702003","desc":"已过期需要申请续期或重新支付"
            BeanUtils.copyProperties(xcCourseTables, courseTablesDto);
            courseTablesDto.setLearnStatus("702003");
            return courseTablesDto;
        }

        //"code":"702001","desc":"正常学习"
        BeanUtils.copyProperties(xcCourseTables, courseTablesDto);
        courseTablesDto.setLearnStatus("702001");
        return courseTablesDto;

    }

    /**
     * 添加免费课程,免费课程加入选课记录表、我的课程表
     *
     * @param userId
     * @param coursepublish
     * @return {@link XcChooseCourse}
     */
    public XcChooseCourse addFreeCourse(String userId, CoursePublish coursepublish) {
        // 查询选课记录表是否存在免费的且选课成功的订单
        List<XcChooseCourse> xcChooseCourses = xcChooseCourseMapper.selectList(new LambdaQueryWrapper<XcChooseCourse>()
                .eq(XcChooseCourse::getUserId, userId)
                .eq(XcChooseCourse::getCourseId, coursepublish.getId())
                //[{"code":"700001","desc":"免费课程"},{"code":"700002","desc":"收费课程"}]
                .eq(XcChooseCourse::getOrderType, "700001")
                //[{"code":"701001","desc":"选课成功"},{"code":"701002","desc":"待支付"}]
                .eq(XcChooseCourse::getStatus, "701001"));
        if (!CollectionUtils.isEmpty(xcChooseCourses)) {
            return xcChooseCourses.get(0);
        }

        // 添加选课记录信息
        XcChooseCourse xcChooseCourse = new XcChooseCourse();
        xcChooseCourse.setCourseId(coursepublish.getId());
        xcChooseCourse.setCourseName(coursepublish.getName());
        xcChooseCourse.setCoursePrice(0f);//免费课程价格为0
        xcChooseCourse.setUserId(userId);
        xcChooseCourse.setCompanyId(coursepublish.getCompanyId());
        xcChooseCourse.setOrderType("700001");//免费课程
        xcChooseCourse.setCreateDate(LocalDateTime.now());
        xcChooseCourse.setStatus("701001");//选课成功
        xcChooseCourse.setValidDays(365);//免费课程默认365
        xcChooseCourse.setValidtimeStart(LocalDateTime.now());
        xcChooseCourse.setValidtimeEnd(LocalDateTime
                .now()
                .plusDays(365));
        xcChooseCourseMapper.insert(xcChooseCourse);

        return xcChooseCourse;
    }

    /**
     * 添加到我的课程表
     *
     * @param xcChooseCourse 选课记录
     * @return com.xuecheng.learning.model.po.XcCourseTables
     */
    public XcCourseTables addCourseTables(XcChooseCourse xcChooseCourse) {
        // 选课记录完成且未过期可以添加课程到课程表
        String status = xcChooseCourse.getStatus();
        if (!"701001".equals(status)) {
            XueChengPlusException.cast("选课未成功，无法添加到课程表");
        }

        // 查询我的课程表
        XcCourseTables xcCourseTables = getXcCourseTables(xcChooseCourse.getUserId(), xcChooseCourse.getCourseId());
        if (xcCourseTables != null) {
            return xcCourseTables;
        }

        XcCourseTables xcCourseTablesNew = new XcCourseTables();
        xcCourseTablesNew.setChooseCourseId(xcChooseCourse.getId());
        xcCourseTablesNew.setUserId(xcChooseCourse.getUserId());
        xcCourseTablesNew.setCourseId(xcChooseCourse.getCourseId());
        xcCourseTablesNew.setCompanyId(xcChooseCourse.getCompanyId());
        xcCourseTablesNew.setCourseName(xcChooseCourse.getCourseName());
        xcCourseTablesNew.setCreateDate(LocalDateTime.now());
        xcCourseTablesNew.setValidtimeStart(xcChooseCourse.getValidtimeStart());
        xcCourseTablesNew.setValidtimeEnd(xcChooseCourse.getValidtimeEnd());
        xcCourseTablesNew.setCourseType(xcChooseCourse.getOrderType());
        xcCourseTablesMapper.insert(xcCourseTablesNew);

        return xcCourseTablesNew;

    }

    /**
     * 根据课程和用户查询我的课程表中某一门课程
     *
     * @param userId
     * @param courseId
     * @return com.xuecheng.learning.model.po.XcCourseTables
     */
    public XcCourseTables getXcCourseTables(String userId, Long courseId) {
        return xcCourseTablesMapper.selectOne(new LambdaQueryWrapper<XcCourseTables>()
                .eq(XcCourseTables::getUserId, userId)
                .eq(XcCourseTables::getCourseId, courseId));

    }

    /**
     * 添加收费课程
     *
     * @param userId
     * @param coursepublish
     * @return {@link XcChooseCourse}
     */
    public XcChooseCourse addChargeCoruse(String userId, CoursePublish coursepublish) {
        // 如果存在待支付交易记录直接返回
        List<XcChooseCourse> xcChooseCourses = xcChooseCourseMapper.selectList(new LambdaQueryWrapper<XcChooseCourse>()
                .eq(XcChooseCourse::getUserId, userId)
                .eq(XcChooseCourse::getCourseId, coursepublish.getId())
                //[{"code":"700001","desc":"免费课程"},{"code":"700002","desc":"收费课程"}]
                .eq(XcChooseCourse::getOrderType, "700002")
                //[{"code":"701001","desc":"选课成功"},{"code":"701002","desc":"待支付"}]
                .eq(XcChooseCourse::getStatus, "701002"));
        if (xcChooseCourses != null && xcChooseCourses.size() > 0) {
            return xcChooseCourses.get(0);
        }

        XcChooseCourse xcChooseCourse = new XcChooseCourse();
        xcChooseCourse.setCourseId(coursepublish.getId());
        xcChooseCourse.setCourseName(coursepublish.getName());
        xcChooseCourse.setCoursePrice(coursepublish.getPrice());
        xcChooseCourse.setUserId(userId);
        xcChooseCourse.setCompanyId(coursepublish.getCompanyId());
        xcChooseCourse.setOrderType("700002");//收费课程
        xcChooseCourse.setCreateDate(LocalDateTime.now());
        xcChooseCourse.setStatus("701002");//待支付
        xcChooseCourse.setValidDays(coursepublish.getValidDays());
        xcChooseCourse.setValidtimeStart(LocalDateTime.now());
        xcChooseCourse.setValidtimeEnd(LocalDateTime
                .now()
                .plusDays(coursepublish.getValidDays()));
        xcChooseCourseMapper.insert(xcChooseCourse);
        return xcChooseCourse;
    }

}
