package com.xuecheng.content.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author VectorX
 * @version V1.0
 * @description freemarker测试
 * @date 2024-04-13 09:07:48
 */
@RestController
@Slf4j
public class FreemarkerController
{
    @GetMapping("/testfreemarker")
    public ModelAndView test() {
        ModelAndView modelAndView = new ModelAndView();
        //设置模型数据
        modelAndView.addObject("name", "小明");
        //设置模板名称
        modelAndView.setViewName("test");
        return modelAndView;
    }
}
