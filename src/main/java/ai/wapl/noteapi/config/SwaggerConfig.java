package ai.wapl.noteapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String HEADER_USER_ID = "WAPL-User-Id";
    private static final String HEADER_ACCOUNT_ID = "WAPL-Account-Id";

    @Value("${auth.domain}")
    private String AUTH_DOMAIN;

    @Value("${auth.realm-name}")
    private String AUTH_REALM;

    @Value("${auth.client-id}")
    private String AUTH_CLIENT_ID;

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(AUTH_CLIENT_ID)
                .realm(AUTH_REALM)
                .appName(AUTH_CLIENT_ID)
                .scopeSeparator(",")
                .additionalQueryStringParams(null)
                .useBasicAuthenticationWithAccessCodeGrant(false)
                .build();
    }

    // @Bean
    // public Docket api() {
    // return new Docket(DocumentationType.OAS_30)
    // .useDefaultResponseMessages(false)
    // .securitySchemes(securitySchemes())
    // .securityContexts(securityContexts())
    // .select()
    // .apis(RequestHandlerSelectors.basePackage("ai.wapl.noteapi"))
    // .paths(PathSelectors.any())
    // .build()
    // .apiInfo(apiInfo());

    // }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Practice Swagger")
                .description("practice swagger config")
                .version("1.0")
                .build();
    }

    private List<AuthorizationScope> scopes() {

        return Arrays.asList(
                new AuthorizationScope("read_access", "read data"),
                new AuthorizationScope("write_access", "modify data"));
    }

    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> securitySchemeList = new ArrayList<>();

        SecurityScheme securityScheme = new OAuth2SchemeBuilder("password")
                .name("WAPL Auth")
                .tokenUrl(AUTH_DOMAIN + "/realms/" + AUTH_REALM + "/protocol/openid-connect/token")
                .scopes(scopes())
                .build();

        securitySchemeList.add(securityScheme);
        securitySchemeList.addAll(httpAuthenticationSchemes());

        return securitySchemeList;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityReference> securityReferences = new ArrayList<>();

        securityReferences.add(SecurityReference.builder().reference("WAPL Auth")
                .scopes(scopes().toArray(new AuthorizationScope[] {})).build());
        securityReferences.addAll(defaultAuth());

        SecurityContext context = SecurityContext.builder()
                .operationSelector(operationContext -> true)
                .securityReferences(securityReferences)
                .build();

        List<SecurityContext> ret = new ArrayList<>();
        ret.add(context);
        return ret;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[2];
        authorizationScopes[0] = authorizationScope;
        authorizationScopes[1] = authorizationScope;
        return Arrays.asList(
                new SecurityReference(HEADER_USER_ID, authorizationScopes),
                new SecurityReference(HEADER_ACCOUNT_ID, authorizationScopes));
    }

    private List<SecurityScheme> httpAuthenticationSchemes() {

        ArrayList<SecurityScheme> securitySchemeList = new ArrayList<>();
        securitySchemeList.add(
                new ApiKey(HEADER_USER_ID, HEADER_USER_ID, "header"));
        securitySchemeList.add(
                new ApiKey(HEADER_ACCOUNT_ID, HEADER_ACCOUNT_ID, "header"));
        return securitySchemeList;
    }

}
