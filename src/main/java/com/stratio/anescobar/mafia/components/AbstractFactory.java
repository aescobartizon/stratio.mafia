package com.stratio.anescobar.mafia.components;

/**
 * A factory for creating Abstract objects.
 *
 * @param <T> the generic type
 */
public interface AbstractFactory<T> {

	/**
	 * Gets the service.
	 *
	 * @param version the version
	 * @return the service
	 */
	T getService(String implementation);
}
