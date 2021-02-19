package com.stratio.anescobar.mafia.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mafioso extends GenericDto implements Serializable {

	private static final long serialVersionUID = -4171247198380541364L;

	private String fullName;

	private String alias;

	private int status;

	private Mafioso substitute;

	private Long ingressDateTime;

	private int reporters;

}
