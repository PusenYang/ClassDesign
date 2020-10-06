package four.classd.cd.config;

import org.aspectj.weaver.ast.Or;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger 配置类
 * 访问：http://localhost:8080/rds1123/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String LOGIN_TAG = "登录注册相关接口";
    public static final String ADMIN_TAG = "管理员相关接口";
    public static final String ORDER_TAG = "订单相关接口";
    public static final String DESIGN_TAG = "调配站相关接口";
    public static final String RECEIVE_TAG = "接收站相关接口";
    public static final String INFO_TAG = "辅助信息接口";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .tags(new Tag(LOGIN_TAG,"登录注册相关接口"))
                .tags(new Tag(ADMIN_TAG,"管理员相关接口, 主要是对站点资格的审查"))
                .tags(new Tag(ORDER_TAG,"订单相关接口, 包括物资单和调配单"))
                .tags(new Tag(DESIGN_TAG,"调配站相关接口"))
                .tags(new Tag(RECEIVE_TAG,"接收站相关接口"))
                .tags(new Tag(INFO_TAG,"辅助信息接口, 包括疫情信息和位置信息"))
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("华中区物资信息调度系统-接口文档").version("1.0").build();
    }
}
