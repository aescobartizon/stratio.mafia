package com.stratio.anescobar.mafia.components.impl.neo4j;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import com.stratio.anescobar.mafia.components.AbstractGenericService;
import com.stratio.anescobar.mafia.components.MafiaHierarchy;
import com.stratio.anescobar.mafia.domain.Mafioso;

import lombok.Getter;

@Service
public class MafiaHierarchyNeo4jService extends AbstractGenericService implements MafiaHierarchy {

	@Getter
	private String implementation = "neo4j";

	@Override
	public void cleanGraph() {
		throw new NotImplementedException();

	}

	@Override
	public void reporting(Mafioso source, Mafioso target) {
		throw new NotImplementedException();

	}

	@Override
	public void reporting(String fullNameSource, String fullNameTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Mafioso> getReportingList(Mafioso mafioso) {
		throw new NotImplementedException();
	}

	@Override
	public List<Mafioso> getReportingByName(String mafiosoFullName) {
		throw new NotImplementedException();
	}

	@Override
	public Mafioso getMafiosoByName(String mafiosoFullName) {
		throw new NotImplementedException();
	}

	@Override
	public List<Mafioso> getMafiosos() {
		throw new NotImplementedException();
	}

	@Override
	public void updateStatusMafioso(String fullName, MAFIOSOS_STATUS status) {
		throw new NotImplementedException();

	}

	@Override
	public List<Mafioso> getBosses() {
		throw new NotImplementedException();
	}

	@Override
	public List<Mafioso> getActiveBosses() {
		throw new NotImplementedException();
	}

	@Override
	public void printGraph() throws IOException {
		throw new NotImplementedException();

	}

}
