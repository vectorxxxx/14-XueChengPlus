package com.xuecheng.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.media.model.po.MediaProcess;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface MediaProcessMapper extends BaseMapper<MediaProcess>
{
    /**
     * 根据分片参数获取待处理任务@param shardTotal 分片总数
     * <p>
     * status: 状态 1:未处理，4:处理中 2：处理成功 3处理失败
     *
     * @param shardTotal 分片大小
     * @param shardIndex 分片索引
     * @param count      任务数
     * @return java.util.List<com.xuecheng.media.model.po.MediaProcess>
     */
    @Select("select * from media_process where id % #{shardTotal} = #{shardIndex} and (status = '1' or status = '3') and fail_count < 3 limit #{count}")
    List<MediaProcess> selectListByShardIndex(
            @Param("shardTotal")
                    int shardTotal,
            @Param("shardIndex")
                    int shardIndex,
            @Param("count")
                    int count);

    /**
     * 开启一个任务
     * <p>
     * 数据库实现分布锁：乐观锁
     * <p>
     * 什么是乐观锁、悲观锁？
     * <p>
     * synchronized是一种悲观锁，在执行被synchronized包裹的代码时需要首先获取锁，没有拿到锁则无法执行，是总悲观的认为别的线程会去抢，所以要悲观锁。
     * <p>
     * 乐观锁的思想是它不认为会有线程去争抢，尽管去执行，如果没有执行成功就再去重试。
     * <p>
     * status: 状态 1:未处理，4:处理中 2：处理成功 3处理失败
     *
     * @param id 任务id
     * @return 更新记录数
     */
    @Update("update media_process set status='4' where id=#{id} and (status='1' or status='3') and fail_count<3 ")
    int startTask(
            @Param("id")
                    long id);

}
