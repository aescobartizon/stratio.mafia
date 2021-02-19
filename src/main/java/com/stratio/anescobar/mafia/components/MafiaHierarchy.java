package com.stratio.anescobar.mafia.components;

import java.io.IOException;
import java.util.List;

import com.stratio.anescobar.mafia.domain.Mafioso;

/**
 * The Interface MafiaHierarchy.
 */
public interface MafiaHierarchy {

	/**
	 * Gets the implementation.
	 *
	 * @return the implementation
	 */
	String getImplementation();

	/**
	 * Clean graph.
	 */
	void cleanGraph();

	/**
	 * Reporting.
	 *
	 * @param source the source
	 * @param target the target
	 */
	void reporting(Mafioso source, Mafioso target);

	/**
	 * Reporting.
	 *
	 * @param fullNameSource the full name source
	 * @param fullNameTarget the full name target
	 */
	void reporting(String fullNameSource, String fullNameTarget);

	/**
	 * Gets the reporting list.
	 *
	 * @param mafioso the mafioso
	 * @return the reporting list
	 */
	List<Mafioso> getReportingList(Mafioso mafioso);

	/**
	 * Gets the reporting by name.
	 *
	 * @param mafiosoFullName the mafioso full name
	 * @return the reporting by name
	 */
	List<Mafioso> getReportingByName(String mafiosoFullName);

	/**
	 * Gets the mafioso by name.
	 *
	 * @param mafiosoFullName the mafioso full name
	 * @return the mafioso by name
	 */
	Mafioso getMafiosoByName(String mafiosoFullName);

	/**
	 * Gets the mafiosos.
	 *
	 * @return the mafiosos
	 */
	List<Mafioso> getMafiosos();

	/**
	 * Update status mafioso.
	 *
	 * @param fullName the full name
	 * @param status   the status
	 */
	void updateStatusMafioso(String fullName, MAFIOSOS_STATUS status);

	/**
	 * Gets the bosses.
	 *
	 * @return the bosses
	 */
	List<Mafioso> getBosses();

	/**
	 * Gets the active bosses.
	 *
	 * @return the active bosses
	 */
	List<Mafioso> getActiveBosses();

	/**
	 * Prints the graph.
	 */
	void printGraph() throws IOException;

	/**
	 * The Enum MAFIOSOS_STATUS.
	 */
	public enum MAFIOSOS_STATUS {

		FREE(0), ENCARCELADO(1), ASSESINADO(2);

		private int code;

		MAFIOSOS_STATUS(int code) {
			this.code = code;
		}

		public int code() {
			return code;
		}
	}
}
