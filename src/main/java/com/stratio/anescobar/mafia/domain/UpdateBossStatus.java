package com.stratio.anescobar.mafia.domain;

import java.io.Serializable;

import com.stratio.anescobar.mafia.components.MafiaHierarchy.MAFIOSOS_STATUS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBossStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fullNameBoss;

	private MAFIOSOS_STATUS mafiosoStatus;

	public UpdateBossStatus() {
		super();
	}

}
