package com.stratio.anescobar.mafia.components;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationService {

	@Bean
	@ConditionalOnMissingBean
	public MafiaHierarchyFatory getMafiaHierarchyFatory() {
		return new MafiaHierarchyFatory();
	}

}
