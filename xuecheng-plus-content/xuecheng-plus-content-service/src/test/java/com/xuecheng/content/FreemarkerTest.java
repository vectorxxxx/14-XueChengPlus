package com.xuecheng.content;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.service.CoursePublishService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author VectorX
 * @version V1.0
 * @description freemarker测试
 * @date 2024-04-22 14:20:31
 */
@SpringBootTest
public class FreemarkerTest
{
    @Autowired
    CoursePublishService coursePublishService;

    /**
     * 测试页面静态化
     *
     * @throws IOException
     * @throws TemplateException
     */
    @Test
    public void testGenerateHtmlByTemplate() throws IOException, TemplateException {
        // 1、配置freemarker
        Configuration configuration = new Configuration(Configuration.getVersion());

        // 2、加载模板
        // 得到classpath路径
        String classpath = this
                .getClass()
                .getResource("/")
                .getPath();
        // 选指定模板路径,classpath下templates下
        configuration.setDirectoryForTemplateLoading(new File(classpath + "/templates/"));
        // 设置字符编码
        configuration.setDefaultEncoding("utf-8");

        // 指定模板文件名称
        Template template = configuration.getTemplate("course_template.ftl");

        // 3、准备数据
        CoursePreviewDto coursePreviewInfo = coursePublishService.getCoursePreviewInfo(128L);

        Map<String, Object> map = new HashMap<>();
        map.put("model", coursePreviewInfo);

        // 4、静态化
        // 参数1：模板，参数2：数据模型
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        System.out.println(content);
        // 将静态化内容输出到文件中
        InputStream inputStream = IOUtils.toInputStream(content);
        // 输出流
        FileOutputStream outputStream = new FileOutputStream(
                "D:\\workspace-mine\\14-XueChengPlus\\xuecheng-plus-content\\xuecheng-plus-content-service\\src\\test\\resources\\templates\\course_template.html");
        IOUtils.copy(inputStream, outputStream);
    }
}
