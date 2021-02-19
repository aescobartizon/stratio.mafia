package com.stratio.anescobar.mafia.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

public class AbstractGenericService {

	/**
	 * Gets the logger.
	 *
	 * @return the logger
	 */
	@Getter
	private Logger logger = LoggerFactory.getLogger(AbstractGenericService.class);

}
