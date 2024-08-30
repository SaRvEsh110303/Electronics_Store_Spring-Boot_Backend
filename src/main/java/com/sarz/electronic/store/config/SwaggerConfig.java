package com.sarz.electronic.store.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Scheme1",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@OpenAPIDefinition(
        info = @Info(
                title = "Electronic Store API",
                description = "This project is for backend of the Electronic Store",
                version = "2.0x",
                contact = @Contact(
                        name = "Sarvesh Nitin Zade",
                        email = "sarveshzade11@gmail.com",
                        url = "sarz-electronics-store.com"
                ),
                license = @License(
                        name = "OPEN LICENSE",
                        url = "license.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "This is External Docs",
                url = "sarz-electronics-store.com"
        )
)
public class SwaggerConfig {


//    private String schemeName="bearerScheme";
//    @Bean
//    public OpenAPI openAPI(){
//       return new OpenAPI()
//               .addSecurityItem(new SecurityRequirement()
//                       .addList(schemeName)
//                       )
//               .components(new Components()
//                       .addSecuritySchemes(schemeName,new SecurityScheme()
//                               .name(schemeName)
//                               .type(SecurityScheme.Type.HTTP)
//                               .bearerFormat("JWT")
//                               .scheme("bearer")
//                       )
//                       )
//                .info(new Info().title("Electronic Store API")
//                        .description("This is Electronic Store Project API developed by SARVESH")
//                        .version("1.0")
//                        .contact(new Contact().name("Sarvesh Zade").email("sarveshzade11@gmail.com").url("sarvesh.com"))
//                        .license(new License().name("APACHE"))
//                ).externalDocs(new ExternalDocumentation().url("Sarz.Electronic.Store").description("This is external url"))
//        ;
//    }
}
