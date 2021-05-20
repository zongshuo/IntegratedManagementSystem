package com.zongshuo.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-20
 * @Time: 10:52
 * @Description:
 * Swagger生成api文档
 * 是一款RESTFUL接口的，基于YAML、JSON语言的文档在线自动生成、代码生成工具。是符合标准的并且语言无关的。
 * Swagger-UI：可视化的UI界面用于展示描述文件，可以在该界面对接口进行查阅和简单的请求，该项目支持在线导入描述文件和本地部署项目。
 * Swagger-Editor：编辑swagger描述文件的编辑器，支持实时预览描述文件的更新效果。也提供了在线编辑和本地部署两种方式。
 * Swagger-Inspector：是一个可以对接口进行测试的在线postman。但会返回更多信息，也会保存请求的实际请求参数。
 * Swagger-Codegn：可以将描述文件生成html或wiki格式的接口文档，同时也能生成多种语言的服务端和客户端代码。
 * Swagger-hub：集成了上面各个项目的功能，可以保存 不同版本的接口文档。与GitHub相似。
 *
 * SpringFox-Swagger：spring基于swagger规范编写的可以将springMVC和springboot的项目代码自动生成JSON格式的描述文件。
 * http://localhost:port/swagger-ui.html:swagger页面
 *
 * Docket：swagger实例，用于配置swagger
 * SecuritySchemas与SecurityContexts：配置全局的Authorization参数，支持Swagger可以访问我们的安全api。
 * @Api：作用于类上，会在Swagger-ui中增加一个目录，子目录为类内的接口
 * @ApiOpeartion：作用在方法上，生成api的子目录
 * @ApiModule：作用在实体类上，vo/bo
 * @ApiModuleProperty：作用在实体类的属性上，描述属性信息
 * @ApiParam：作用在方法参数上，描述参数信息
 * @ApiImplicitParams：作用在方法上，对象内部包含多一个方法参数说明
 * @ApiImplicitParam：放在ApiImplicitParams中，每个参数对应一个该注解
 *
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket createDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                //设置接口文档分组名称
                .groupName("综合管理系统")
                .apiInfo(getApiInfo())
                .forCodeGeneration(true)
                //增加展示api的条件
                .select()
                //设置基本包路径
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                //控制那些路径的接口可以访问
                .paths(PathSelectors.any())
                .build()
                //增加全局的Authorization
//                .securitySchemes()
//                .securityContexts()
                ;
    }

    private ApiInfo getApiInfo(){
        return new ApiInfoBuilder()
                //文档标题
                .title("综合管理系统API文档")
                //文档说明
                .description("本文档包含综合管理系统各组成部分的服务接口信息")
                //文档版本
                .version("0.1")
                .termsOfServiceUrl("")
                .build();
    }
}
