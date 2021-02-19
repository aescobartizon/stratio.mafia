package com.stratio.anescobar.mafia.domain;

import org.jgrapht.graph.DefaultEdge;

public class MafiaEdge<T> extends DefaultEdge {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5087341285988286592L;

	public MafiaEdge() {
		super();
	}

	public T getBoss() {
		return (T) this.getTarget();
	}

	public T getReporter() {
		return (T) this.getSource();
	}

}
