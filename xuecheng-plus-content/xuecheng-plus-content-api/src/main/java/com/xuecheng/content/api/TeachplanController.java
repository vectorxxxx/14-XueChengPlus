package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description 课程计划编辑接口
 * @date 2024-04-10 11:02:00
 */
@Api(value = "课程计划编辑接口",
     tags = "课程计划编辑接口")
@RestController
public class TeachplanController
{
    @Autowired
    private TeachplanService teachplanService;

    @ApiOperation("下移课程计划")
    @PostMapping("/teachplan/movedown/{id}")
    public void movedownTeachplan(
            @PathVariable
                    String id) {
        teachplanService.movedownTeachplan(id);
    }

    @ApiOperation("上移课程计划")
    @PostMapping("/teachplan/moveup/{id}")
    public void moveupTeachplan(
            @PathVariable
                    String id) {
        teachplanService.moveupTeachplan(id);
    }

    @ApiOperation("删除课程计划")
    @DeleteMapping("/teachplan/{id}")
    public void delTeachplan(
            @PathVariable
                    String id) {
        teachplanService.delTeachplan(id);
    }

    @ApiOperation("课程计划创建或修改")
    @PostMapping("/teachplan")
    public void saveTeachplan(
            @RequestBody
                    SaveTeachplanDto teachplanDto) {
        teachplanService.saveTeachplan(teachplanDto);
    }

    @ApiOperation("查询课程计划树形结构")
    @ApiImplicitParam(value = "courseId",
                      name = "课程Id",
                      required = true,
                      dataType = "Long",
                      paramType = "path")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(
            @PathVariable
                    Long courseId) {
        return teachplanService.findTeachplanTree(courseId);
    }

    @ApiOperation(value = "课程计划和媒资信息绑定")
    @PostMapping("/teachplan/association/media")
    public void associationMedia(
            @RequestBody
                    BindTeachplanMediaDto bindTeachplanMediaDto) {
        teachplanService.associationMedia(bindTeachplanMediaDto);
    }

    @ApiOperation(value = "课程计划和媒资信息解除绑定")
    @DeleteMapping("/teachplan/association/media/{teachplanId}/{mediaId}")
    public void unbindMedia(
            @PathVariable
                    Integer teachplanId,
            @PathVariable
                    String mediaId) {
        teachplanService.unbindMedia(teachplanId, mediaId);
    }

}
