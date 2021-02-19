package com.stratio.anescobar.mafia;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.stratio.anescobar.mafia.components.MafiaHierarchy.MAFIOSOS_STATUS;
import com.stratio.anescobar.mafia.components.impl.jgrapht.MafiaHierarchyJgraphtService;
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

	@Test
	public void testMafiaHierarchyIsBoss() throws IOException {

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

		getMafiaHierarchyJgraphtService().setMafiaBossReporters(2);

		getMafiaHierarchyJgraphtService().ingressInOrganizationMafioso(mafioso, new Date().getTime());
		getMafiaHierarchyJgraphtService().ingressInOrganizationMafioso(mafioso2, new Date().getTime());
		getMafiaHierarchyJgraphtService().ingressInOrganizationMafioso(mafioso3, new Date().getTime());

		getMafiaHierarchyJgraphtService().reporting(mafioso2, mafioso);
		getMafiaHierarchyJgraphtService().reporting(mafioso3, mafioso);

		List<Mafioso> bosses = getMafiaHierarchyJgraphtService().getBosses();

		assertTrue(bosses.size() == 1);

	}

	@Test
	public void testMafiaHierarchyReplaceBossExist() throws IOException {

		getMafiaHierarchyJgraphtService().cleanGraph();

		// Fijamos en 2 el numero de gente que informa para considerar Jefe
		getMafiaHierarchyJgraphtService().setMafiaBossReporters(2);

		Long dateTime = new Date().getTime();

		for (int i = 0; i < 3; i++) {

			Mafioso mafioso = new Mafioso();
			mafioso.setAlias("anescobar" + i);
			mafioso.setFullName("Antonio Escobar" + i);
			mafioso.setStatus(0);

			Mafioso mafioso2 = new Mafioso();
			mafioso2.setAlias("gema" + i);
			mafioso2.setFullName("Gema Delgado" + i);
			mafioso2.setStatus(0);

			Mafioso mafioso3 = new Mafioso();
			mafioso3.setAlias("Andres" + i);
			mafioso3.setFullName("Andres Escobar" + i);
			mafioso3.setStatus(0);

			dateTime = dateTime + 1800000L;

			getMafiaHierarchyJgraphtService().ingressInOrganizationMafioso(mafioso, dateTime);
			getMafiaHierarchyJgraphtService().ingressInOrganizationMafioso(mafioso2, dateTime);
			getMafiaHierarchyJgraphtService().ingressInOrganizationMafioso(mafioso3, dateTime);

			getMafiaHierarchyJgraphtService().reporting(mafioso2, mafioso);
			getMafiaHierarchyJgraphtService().reporting(mafioso3, mafioso);

		}

		// En principio hay 3 jefes con 2 mafiosos que le reportan
		assertTrue(getMafiaHierarchyJgraphtService().getActiveBosses().size() == 3);
		assertTrue(getMafiaHierarchyJgraphtService().getBosses().size() == 3);

		// El primer jefe es encarcelado
		getMafiaHierarchyJgraphtService().updateStatusMafioso("Antonio Escobar0", MAFIOSOS_STATUS.ENCARCELADO);

		// El número de jefes sigue siendo el mismo pero en Activo solo 2
		assertTrue(getMafiaHierarchyJgraphtService().getActiveBosses().size() == 2);
		assertTrue(getMafiaHierarchyJgraphtService().getBosses().size() == 3);

		// Tenemos el substito del mafioso
		Mafioso substitute = getMafiaHierarchyJgraphtService().getMafiosoByName("Antonio Escobar0").getSubstitute();

		// Ahora el substituro debe de tener 4 reportadores y solo hay dos jefes activos
		assertTrue(getMafiaHierarchyJgraphtService().getReportingByName(substitute.getFullName()).size() == 4);
		assertTrue(getMafiaHierarchyJgraphtService().getActiveBosses().size() == 2);

		// Vuelvo a liberar al jefe
		getMafiaHierarchyJgraphtService().updateStatusMafioso("Antonio Escobar0", MAFIOSOS_STATUS.FREE);

		// vuelve a haber 3 jefes activos y el sustituto ahora sigue teniendo los 2
		// anteriores y el jefe desencarcelado tiene sus dos anteriores
		assertTrue(getMafiaHierarchyJgraphtService().getActiveBosses().size() == 3);
		assertTrue(getMafiaHierarchyJgraphtService().getBosses().size() == 3);
		assertTrue(getMafiaHierarchyJgraphtService().getReportingByName(substitute.getFullName()).size() == 2);
		assertTrue(getMafiaHierarchyJgraphtService().getReportingByName("Antonio Escobar0").size() == 2);

		// Se van a encarcerlar todos los jefes por los que todos los reporteros pararan
		// a estar cargo del mafioso que promociona del ultimo jefe
		getMafiaHierarchyJgraphtService().updateStatusMafioso("Antonio Escobar0", MAFIOSOS_STATUS.ENCARCELADO);
		assertTrue(getMafiaHierarchyJgraphtService().getActiveBosses().size() == 2);
		assertTrue(getMafiaHierarchyJgraphtService().getBosses().size() == 3);
		getMafiaHierarchyJgraphtService().updateStatusMafioso("Antonio Escobar1", MAFIOSOS_STATUS.ENCARCELADO);
		assertTrue(getMafiaHierarchyJgraphtService().getActiveBosses().size() == 1);
		assertTrue(getMafiaHierarchyJgraphtService().getBosses().size() == 3);
		getMafiaHierarchyJgraphtService().updateStatusMafioso("Antonio Escobar2", MAFIOSOS_STATUS.ENCARCELADO);

		Mafioso newBoss = getMafiaHierarchyJgraphtService().getActiveBosses().get(0);

		// se comprueba que el nuevo jefe era un lacayo, hay 4 jefes ahora y activos 1
		assertTrue(newBoss.getFullName().contains("Gema Delgado") || newBoss.getFullName().contains("Andres Escobar"));
		assertTrue(getMafiaHierarchyJgraphtService().getActiveBosses().size() == 1);
		assertTrue(getMafiaHierarchyJgraphtService().getBosses().size() == 4);

		// Salen de la carcel en una fuga y se recupera la situacion inicial
		getMafiaHierarchyJgraphtService().updateStatusMafioso("Antonio Escobar0", MAFIOSOS_STATUS.FREE);
		getMafiaHierarchyJgraphtService().updateStatusMafioso("Antonio Escobar2", MAFIOSOS_STATUS.FREE);
		getMafiaHierarchyJgraphtService().updateStatusMafioso("Antonio Escobar1", MAFIOSOS_STATUS.FREE);
		assertTrue(getMafiaHierarchyJgraphtService().getActiveBosses().size() == 3);
		assertTrue(getMafiaHierarchyJgraphtService().getBosses().size() == 3);
	}

}
