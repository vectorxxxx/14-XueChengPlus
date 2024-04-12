package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description 课程计划service接口实现类
 * @date 2024-04-10 11:15:58
 */
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService
{

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    /**
     * 上移课程计划
     *
     * @param id
     */
    @Override
    public void moveupTeachplan(String id) {
        final Teachplan teachplan = baseMapper.selectById(id);
        final List<Teachplan> teachplanList = baseMapper.selectList(new LambdaQueryWrapper<Teachplan>()
                .eq(Teachplan::getParentid, teachplan.getParentid())
                .orderByAsc(Teachplan::getOrderby));

        // 如果查询到的课程计划数量小于2，则无法交换位置
        if (teachplanList.size() < 2) {
            return;
        }

        // 找到当前课程计划在列表中的索引
        int currentIndex = teachplanList.indexOf(teachplan);

        // 如果当前课程计划是第一个，无法向上移动
        if (currentIndex == 0) {
            return;
        }

        // 获取上一个课程计划
        Teachplan previousTeachplan = teachplanList.get(currentIndex - 1);

        // 交换排序字段值
        int tempOrderby = teachplan.getOrderby();
        teachplan.setOrderby(previousTeachplan.getOrderby());
        previousTeachplan.setOrderby(tempOrderby);

        // 更新数据库中的排序字段值
        baseMapper.updateById(teachplan);
        baseMapper.updateById(previousTeachplan);
    }

    /**
     * 下移课程计划
     *
     * @param id
     */
    @Override
    public void movedownTeachplan(String id) {
        final Teachplan teachplan = baseMapper.selectById(id);
        final List<Teachplan> teachplanList = baseMapper.selectList(new LambdaQueryWrapper<Teachplan>()
                .eq(Teachplan::getParentid, teachplan.getParentid())
                .orderByAsc(Teachplan::getOrderby));

        // 如果查询到的课程计划数量小于2，则无法交换位置
        if (teachplanList.size() < 2) {
            return;
        }

        // 找到当前课程计划在列表中的索引
        int currentIndex = teachplanList.indexOf(teachplan);

        // 如果当前课程计划是第一个，无法向下移动
        if (currentIndex == teachplanList.size() - 1) {
            return;
        }

        // 获取下一个课程计划
        Teachplan previousTeachplan = teachplanList.get(currentIndex + 1);

        // 交换排序字段值
        int tempOrderby = teachplan.getOrderby();
        teachplan.setOrderby(previousTeachplan.getOrderby());
        previousTeachplan.setOrderby(tempOrderby);

        // 更新数据库中的排序字段值
        baseMapper.updateById(teachplan);
        baseMapper.updateById(previousTeachplan);
    }

    @Transactional
    @Override
    public void delTeachplan(String id) {
        final Integer grade = baseMapper
                .selectById(id)
                .getGrade();

        // 删除第一级别的章时要求章下边没有小节方可删除。
        if (grade == 1) {
            final Integer count = baseMapper.selectCount(new LambdaQueryWrapper<Teachplan>().eq(Teachplan::getParentid, id));
            if (count > 0) {
                XueChengPlusException.cast("课程计划信息还有子级信息，无法操作");
            }
        }
        // 删除第二级别的小节的同时需要将其它关联的视频信息也删除。
        else if (grade == 2) {
            teachplanMediaMapper.delete(new LambdaQueryWrapper<TeachplanMedia>().eq(TeachplanMedia::getTeachplanId, id));
        }

        // 删除课程计划
        baseMapper.deleteById(id);
    }

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
            Teachplan teachplan = baseMapper.selectById(id);
            BeanUtils.copyProperties(teachplanDto, teachplan);
            baseMapper.updateById(teachplan);
        }
        else {
            Teachplan teachplanNew = new Teachplan();
            // 设置排序号
            // 取出同父同级别的课程计划数量
            int count = getTeachplanOrderBy(teachplanDto.getCourseId(), teachplanDto.getParentid());
            teachplanNew.setOrderby(count + 1);
            BeanUtils.copyProperties(teachplanDto, teachplanNew);
            baseMapper.insert(teachplanNew);
        }
    }

    /**
     * 获取最新的排序号
     *
     * @param courseId 课程id
     * @param parentId 父课程计划id
     * @return int 最新排序号
     */
    private int getTeachplanOrderBy(long courseId, long parentId) {
        return baseMapper
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
        return baseMapper.selectTreeNodes(courseId);
    }

    @Transactional
    @Override
    public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto) {
        // 教学计划id
        Long teachplanId = bindTeachplanMediaDto.getTeachplanId();
        Teachplan teachplan = baseMapper.selectById(teachplanId);
        if (teachplan == null) {
            XueChengPlusException.cast("教学计划不存在");
        }

        Integer grade = teachplan.getGrade();
        if (grade != 2) {
            XueChengPlusException.cast("只允许第二级教学计划绑定媒资文件");
        }

        // 先删除原来该教学计划绑定的媒资
        teachplanMediaMapper.delete(new LambdaQueryWrapper<TeachplanMedia>().eq(TeachplanMedia::getTeachplanId, teachplanId));

        // 再添加教学计划与媒资的绑定关系
        TeachplanMedia teachplanMedia = new TeachplanMedia();
        teachplanMedia.setCourseId(teachplan.getCourseId());
        teachplanMedia.setTeachplanId(teachplanId);
        teachplanMedia.setMediaId(bindTeachplanMediaDto.getMediaId());
        teachplanMedia.setMediaFilename(bindTeachplanMediaDto.getFileName());
        teachplanMedia.setCreateDate(LocalDateTime.now());
        teachplanMediaMapper.insert(teachplanMedia);
        return teachplanMedia;
    }

    @Override
    public void unbindMedia(Integer teachplanId, String mediaId) {
        teachplanMediaMapper.delete(new LambdaQueryWrapper<TeachplanMedia>()
                .eq(TeachplanMedia::getTeachplanId, teachplanId)
                .eq(TeachplanMedia::getMediaId, mediaId));
    }

}

