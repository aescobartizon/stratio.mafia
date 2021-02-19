package com.stratio.anescobar.mafia.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class MafiaHierarchyFatory implements AbstractFactory<MafiaHierarchy> {

	@Autowired(required = false)
	private List<MafiaHierarchy> mafiaHierarchyServices;

	@Override
	public MafiaHierarchy getService(String implementation) {
		return mafiaHierarchyServices.stream().filter(p -> p.getImplementation().equals(implementation)).findFirst()
				.orElseThrow(() -> new UnsupportedOperationException("Version not implemented"));
	}
}
