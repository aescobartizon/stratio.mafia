package com.stratio.anescobar.mafia.components.impl.jgrapht;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import com.stratio.anescobar.mafia.components.AbstractGenericService;
import com.stratio.anescobar.mafia.components.MafiaHierarchy;
import com.stratio.anescobar.mafia.domain.MafiaEdge;
import com.stratio.anescobar.mafia.domain.Mafioso;

import lombok.Getter;
import lombok.Setter;

@Service
public class MafiaHierarchyJgraphtService extends AbstractGenericService implements MafiaHierarchy {

	private Graph<Mafioso, MafiaEdge<Mafioso>> graph = new SimpleGraph<>(MafiaEdge.class);

	@Getter
	private String implementation = "Jgrapht";

	private List<Mafioso> mafiaBosses = new ArrayList<>();

	@Value("${mafia.boss.reporters:50}")
	@Getter
	@Setter
	private int mafiaBossReporters;

	@Getter
	@Setter
	private Long lastBossSeach = null;

	public void cleanGraph() {
		Graph<Mafioso, MafiaEdge<Mafioso>> graph = new SimpleGraph<>(MafiaEdge.class);
	}

	public void ingressInOrganizationMafioso(Mafioso mafioso, Long ingressDateTime) {

		mafioso.setIngressDateTime(ingressDateTime);

		if (!graph.containsVertex(mafioso)) {
			graph.addVertex(mafioso);
		}
	}

	public void reporting(Mafioso source, Mafioso target) {

		lastBossSeach = null;

		if (!graph.containsVertex(source)) {
			ingressInOrganizationMafioso(source, new Date().getTime());
		}

		if (!graph.containsVertex(target)) {
			ingressInOrganizationMafioso(target, new Date().getTime());
		}

		graph.addEdge(source, target);

	}

	public List<Mafioso> getReportingList(Mafioso mafioso) {

		List<Mafioso> reporters = new ArrayList<>();

		graph.edgesOf(mafioso).forEach(p -> reporters.add(p.getReporter().deepClone()));

		return reporters;
	}

	public List<Mafioso> getMafiosos() {

		List<Mafioso> reporters = new ArrayList<>();

		graph.vertexSet().forEach(p -> reporters.add(p.deepClone()));

		return reporters;
	}

	public void updateStatusMafioso(String fullName, MAFIOSOS_STATUS status) {

		checkUpdateStatusMafiosoArguments(fullName, status);

		Mafioso vertex = graph.vertexSet().stream().filter(p -> p.getFullName().equals(fullName)).findFirst().orElse(null);

		if (vertex != null && vertex.getStatus() != status.code()) {
			if (status.code() == MAFIOSOS_STATUS.FREE.code()) {
				// TODO recuperarsubordinados
			} else {
				// TODO sustituir jefe
			}

		} else if (vertex != null && vertex.getStatus() == status.code()) {
			getLogger().info("The mafios is the same status");
			throw new IllegalArgumentException("The mafios does not exists in the sistem");
		} else {
			getLogger().info("The mafios does not exists in the sistem");
			throw new IllegalArgumentException("The mafios does not exists in the sistem");
		}
	}

	private void checkUpdateStatusMafiosoArguments(String fullName, MAFIOSOS_STATUS status) {
		if (fullName == null || fullName.isEmpty()) {
			throw new IllegalArgumentException("Arguments Errors Expception");
		}
	}

	public List<Mafioso> getBosses() {

		if (lastBossSeach == null) {
			mafiaBosses.clear();

			graph.vertexSet().forEach((Mafioso mafioso) -> {

				if (isBoss(mafioso)) {
					Mafioso result = mafioso.deepClone();
					result.setReporters(graph.edgesOf(mafioso).size());
					mafiaBosses.add(result);
				}
			});
		}
		lastBossSeach = new Date().getTime();
		return mafiaBosses;
	}

	public List<Mafioso> getActiveBosses() {

		if (lastBossSeach == null) {
			mafiaBosses.clear();

			graph.vertexSet().forEach((Mafioso mafioso) -> {

				if (isBoss(mafioso)) {
					Mafioso result = mafioso.deepClone();
					result.setReporters(graph.edgesOf(mafioso).size());
					mafiaBosses.add(result);
				}
			});
		}
		lastBossSeach = new Date().getTime();
		return mafiaBosses.stream().filter(p -> p.getStatus() == MAFIOSOS_STATUS.FREE.code()).collect(Collectors.toList());
	}

	private boolean isBoss(Mafioso mafioso) {

		return graph.edgesOf(mafioso).size() >= mafiaBossReporters;

	}

	public void printGraph() throws IOException {

		Graph<String, MafiaEdge<Mafioso>> graph1 = new SimpleGraph<>(MafiaEdge.class);
		graph.vertexSet().forEach(p -> graph1.addVertex(p.getFullName()));
		graph.edgeSet().forEach(p -> graph1.addEdge(p.getReporter().getFullName(), p.getBoss().getFullName()));

		JGraphXAdapter<String, MafiaEdge<Mafioso>> graphAdapter = new JGraphXAdapter<String, MafiaEdge<Mafioso>>(graph1);

		mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
		layout.execute(graphAdapter.getDefaultParent());

		BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
		File imgFile = new File("src/main/resources/graph.png");
		ImageIO.write(image, "PNG", imgFile);

	}

}
