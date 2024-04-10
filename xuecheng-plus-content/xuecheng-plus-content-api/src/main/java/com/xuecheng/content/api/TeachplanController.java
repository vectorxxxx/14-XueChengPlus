package com.xuecheng.content.api;

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

}
