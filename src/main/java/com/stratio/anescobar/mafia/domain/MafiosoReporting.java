package com.stratio.anescobar.mafia.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MafiosoReporting extends GenericDto implements Serializable {

	private static final long serialVersionUID = 4053997962843622715L;

	private Mafioso source;

	private Mafioso taget;

	private int importantLevel = 0;

}
