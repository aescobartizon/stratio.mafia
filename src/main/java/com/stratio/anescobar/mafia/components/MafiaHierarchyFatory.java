package com.stratio.anescobar.mafia.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.Setter;

public class MafiaHierarchyFatory implements AbstractFactory<MafiaHierarchy> {

	@Autowired(required = false)
	private List<MafiaHierarchy> mafiaHierarchyServices;

	@Getter
	@Setter
	@Value("${mafia.graph.implementation:Jgrapht}")
	private String implementationConfig;

	@Override
	public MafiaHierarchy getService(String implementation) {
		return mafiaHierarchyServices.stream().filter(p -> p.getImplementation().equals(implementation)).findFirst()
				.orElseThrow(() -> new UnsupportedOperationException("Version not implemented"));
	}

	@Override
	public MafiaHierarchy getService() {
		return mafiaHierarchyServices.stream().filter(p -> p.getImplementation().equals(implementationConfig)).findFirst()
				.orElseThrow(() -> new UnsupportedOperationException("Version not implemented"));
	}
}
