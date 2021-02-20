package com.stratio.anescobar.mafia.components;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class ConfigurationService {

	@Bean
	@ConditionalOnMissingBean
	public MafiaHierarchyFatory getMafiaHierarchyFatory() {
		return new MafiaHierarchyFatory();
	}

	/**
	 * Api.
	 *
	 * @return the docket
	 */
	@Bean
	@ConditionalOnMissingBean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo()).select().apis(RequestHandlerSelectors.basePackage("com")).paths(PathSelectors.any()).build();
	}

	private ApiInfo getApiInfo() {

		Contact contact = new Contact("Antonio Escobar Tizón", "https://www.linkedin.com/public-profile/settings?trk=d_flagship3_profile_self_view_public_profile",
				"aescobartizon@gamil.com");

		return new ApiInfoBuilder().title("MAFIA").description("Detección de Jefes de la Mafia").version("1.0.0-SNAPSHOT").license("Licence").licenseUrl("").contact(contact)
				.build();
	}

}
