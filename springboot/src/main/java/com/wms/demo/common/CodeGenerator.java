package com.wms.demo.common;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.model.ClassAnnotationAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class CodeGenerator {
    /*
        参数配置 表名 作者 工作目录 父类包名称 数据库url 数据库账号 数据库密码
    */
    public static String tableName = "storage";
    public static String author = "pengbo";
    public static String workspaceParent = "com.wms.demo";
    public static String MySQL_url = "jdbc:mysql://mysql6.sqlpub.com:3311/madgod?useUnicode=true&useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
    public static String usename = "madgod";
    public static String password = "JAl5GKI9UnkPBYJk";

    private static Path resolveModuleRoot() {
        Path current = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();

        if (Files.exists(current.resolve(Paths.get("src", "main", "java")))) {
            return current;
        }

        Path springbootModule = current.resolve("springboot");
        if (Files.exists(springbootModule.resolve(Paths.get("src", "main", "java")))) {
            return springbootModule;
        }

        return current;
    }

    public static void main(String[] args) {
        Path moduleRoot = resolveModuleRoot();
        String javaOutputDir = moduleRoot.resolve(Paths.get("src", "main", "java")).toString();
        String xmlOutputDir = moduleRoot.resolve(Paths.get("src", "main", "resources", "mapper")).toString();

//        // 打印路径，方便确认生成位置是否正确
//        System.out.println("CodeGenerator moduleRoot: " + moduleRoot);
//        System.out.println("CodeGenerator javaOutputDir: " + javaOutputDir);
//        System.out.println("CodeGenerator xmlOutputDir: " + xmlOutputDir);

        FastAutoGenerator.create(MySQL_url, usename, password)
            .globalConfig(builder ->
                builder.author(author)
                    .disableOpenDir()
                    .outputDir(javaOutputDir)
            )
            .packageConfig(builder ->
                builder.pathInfo(Collections.singletonMap(OutputFile.xml, xmlOutputDir))
                    .parent(workspaceParent)
                    .entity("entity")
                    .mapper("mapper")
                    .service("service")
                    .serviceImpl("service.impl")
            )
            .strategyConfig(builder ->
                builder.addInclude(tableName)
                    .enableSkipView()
                    .entityBuilder().enableLombok(new ClassAnnotationAttributes("@Data","lombok.Data"))
                    .mapperBuilder().mapperAnnotation(Mapper.class)
                    .controllerBuilder()
                    .mapperBuilder()
                    .mapperXmlTemplate("/templates/simple-mapper.xml")
            )
            .templateEngine(new FreemarkerTemplateEngine())
            .execute();
    }
}