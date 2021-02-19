package com.stratio.anescobar.mafia;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.stratio.anescobar.mafia.components.MafiaHierarchyJgraphtService;
import com.stratio.anescobar.mafia.domain.Mafioso;

import lombok.Getter;

@SpringBootTest
class ApplicationTests {

	@Autowired
	@Getter
	private MafiaHierarchyJgraphtService mafiaHierarchyJgraphtService;

	@Test
	void contextLoads() {
	}

	@Test
	public void testMafiaHierarchyJgraphtService() {
		Mafioso mafioso = new Mafioso();
		mafioso.setAlias("anescobar");
		mafioso.setFullName("Antonio Escobar");
		mafioso.setStatus(0);

		getMafiaHierarchyJgraphtService().cleanGraph();

		getMafiaHierarchyJgraphtService().ingressInOrganizationMafioso(mafioso, new Date().getTime());

		List<Mafioso> mafiosos = getMafiaHierarchyJgraphtService().getMafiosos();

		assertTrue(mafiosos.size() == 1);

	}

	@Test
	public void testMafiaHierarchyJgraphtReporters() {
		Mafioso mafioso = new Mafioso();
		mafioso.setAlias("anescobar");
		mafioso.setFullName("Antonio Escobar");
		mafioso.setStatus(0);

		Mafioso mafioso2 = new Mafioso();
		mafioso2.setAlias("gema");
		mafioso2.setFullName("Gema Delgado");
		mafioso2.setStatus(0);

		Mafioso mafioso3 = new Mafioso();
		mafioso3.setAlias("Andres");
		mafioso3.setFullName("Andres Escobar");
		mafioso3.setStatus(0);

		getMafiaHierarchyJgraphtService().cleanGraph();

		getMafiaHierarchyJgraphtService().ingressInOrganizationMafioso(mafioso, new Date().getTime());
		getMafiaHierarchyJgraphtService().ingressInOrganizationMafioso(mafioso2, new Date().getTime());
		getMafiaHierarchyJgraphtService().ingressInOrganizationMafioso(mafioso3, new Date().getTime());

		getMafiaHierarchyJgraphtService().reporting(mafioso2, mafioso);
		getMafiaHierarchyJgraphtService().reporting(mafioso3, mafioso);

		List<Mafioso> mafiosos = getMafiaHierarchyJgraphtService().getReportingList(mafioso);

		assertTrue(mafiosos.size() == 2);

	}

	@Test
	public void testMafiaHierarchyJgraphtPrintDiagram() throws IOException {

		File imgFile = new File("src/test/resources/graph.png");
		imgFile.createNewFile();
		Mafioso mafioso = new Mafioso();
		mafioso.setAlias("anescobar");
		mafioso.setFullName("Antonio Escobar");
		mafioso.setStatus(0);

		Mafioso mafioso2 = new Mafioso();
		mafioso2.setAlias("gema");
		mafioso2.setFullName("Gema Delgado");
		mafioso2.setStatus(0);

		Mafioso mafioso3 = new Mafioso();
		mafioso3.setAlias("Andres");
		mafioso3.setFullName("Andres Escobar");
		mafioso3.setStatus(0);

		getMafiaHierarchyJgraphtService().cleanGraph();

		getMafiaHierarchyJgraphtService().ingressInOrganizationMafioso(mafioso, new Date().getTime());
		getMafiaHierarchyJgraphtService().ingressInOrganizationMafioso(mafioso2, new Date().getTime());
		getMafiaHierarchyJgraphtService().ingressInOrganizationMafioso(mafioso3, new Date().getTime());

		getMafiaHierarchyJgraphtService().reporting(mafioso2, mafioso);
		getMafiaHierarchyJgraphtService().reporting(mafioso3, mafioso);

		getMafiaHierarchyJgraphtService().printGraph();

	}

}
