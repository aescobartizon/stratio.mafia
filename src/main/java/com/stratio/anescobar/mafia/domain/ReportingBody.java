package com.stratio.anescobar.mafia.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportingBody implements Serializable {

	private static final long serialVersionUID = -7603199853020069566L;

	private String fullNameReporter;

	private String fullNameBoss;

	public ReportingBody() {
		super();
	}

}
