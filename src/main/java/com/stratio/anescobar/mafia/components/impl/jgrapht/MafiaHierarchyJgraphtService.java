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

	public void cleanGraph() {
		Graph<Mafioso, MafiaEdge<Mafioso>> graph = new SimpleGraph<>(MafiaEdge.class);
	}

	public void ingressInOrganizationMafioso(Mafioso mafioso, Long ingressDateTime) {

		checkIngressInOrganizationMafioso(mafioso, ingressDateTime);

		mafioso.setIngressDateTime(ingressDateTime);

		if (getMafiosoByName(mafioso.getFullName()) == null) {
			graph.addVertex(mafioso);
		}
	}

	private void checkIngressInOrganizationMafioso(Mafioso mafioso, Long ingressDateTime) {
		if (mafioso == null || mafioso.getFullName() == null || mafioso.getFullName().isEmpty() || ingressDateTime == null) {
			throw new IllegalArgumentException("Error new mafioso arguments");
		}
	}

	public void reporting(Mafioso source, Mafioso target) {

		if (getMafiosoByName(source.getFullName()) == null) {
			ingressInOrganizationMafioso(source, new Date().getTime());
		}

		if (getMafiosoByName(source.getFullName()) == null) {
			ingressInOrganizationMafioso(target, new Date().getTime());
		}

		graph.addEdge(source, target);

	}

	public void reporting(String fullNameSource, String fullNameTarget) {

		if (getMafiosoByName(fullNameSource) == null) {
			ingressInOrganizationMafioso(getMafiosoByName(fullNameSource), new Date().getTime());
		}

		if (getMafiosoByName(fullNameTarget) == null) {
			ingressInOrganizationMafioso(getMafiosoByName(fullNameTarget), new Date().getTime());
		}

		graph.addEdge(getMafiosoByName(fullNameSource), getMafiosoByName(fullNameTarget));
	}

	public List<Mafioso> getReportingList(Mafioso mafioso) {

		List<Mafioso> reporters = new ArrayList<>();

		graph.edgesOf(mafioso).forEach(p -> reporters.add(p.getReporter().deepClone()));

		return reporters;
	}

	public List<Mafioso> getReportingByName(String mafiosoFullName) {

		List<Mafioso> reporters = new ArrayList<>();

		Mafioso vertex = graph.vertexSet().stream().filter(p -> p.getFullName().equals(mafiosoFullName)).findFirst().orElse(null);

		if (vertex != null) {
			graph.edgesOf(vertex).forEach(p -> reporters.add(p.getReporter()));
		}

		return reporters;
	}

	public Mafioso getMafiosoByName(String mafiosoFullName) {
		return graph.vertexSet().stream().filter(p -> p.getFullName().equals(mafiosoFullName)).findFirst().orElse(null);
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
				recoveryBoss(vertex);
			} else {
				Mafioso succesor = getBossSuccesor(vertex);

				if (succesor != null) {
					replaceBoss(vertex, succesor, status);
				} else {
					if (graph.edgesOf(vertex).size() > 0) {
						replaceBoss(vertex, graph.edgesOf(vertex).stream().findFirst().get().getReporter(), status);
					}
				}
			}

		} else if (vertex != null && vertex.getStatus() == status.code()) {
			getLogger().info("The mafios is the same status");
			throw new IllegalArgumentException("The mafios does not exists in the sistem");
		} else {
			getLogger().info("The mafios does not exists in the sistem");
			throw new IllegalArgumentException("The mafios does not exists in the sistem");
		}
	}

	private void recoveryBoss(Mafioso vertex) {

		vertex.setStatus(MAFIOSOS_STATUS.FREE.code());

		if (vertex.getSubstitute() != null) {
			Mafioso substitureVertex = graph.vertexSet().stream().filter(p -> p.getFullName().equals(vertex.getSubstitute().getFullName())).findFirst().get();
			graph.edgesOf(vertex).forEach(p -> graph.removeEdge(p.getReporter(), substitureVertex));
			vertex.setSubstitute(null);
		}
	}

	private void replaceBoss(Mafioso vertex, Mafioso succesor, MAFIOSOS_STATUS status) {

		vertex.setStatus(status.code());

		Mafioso succesorVertex = graph.vertexSet().stream().filter(p -> p.getFullName().equals(succesor.getFullName())).findFirst().get();
		vertex.setSubstitute(succesor);

		for (MafiaEdge<Mafioso> edge : graph.edgesOf(vertex)) {
			if (!edge.getReporter().getFullName().equals(succesorVertex.getFullName())) {
				graph.addEdge(edge.getReporter(), succesorVertex);
			}
		}

		succesorVertex.setReporters(graph.edgesOf(succesorVertex).size());
	}

	private Mafioso getBossSuccesor(Mafioso vertex) {
		List<Mafioso> succesors = getActiveBosses().stream().filter(p -> !p.getFullName().equals(vertex.getFullName())).collect(Collectors.toList());
		if (succesors != null && !succesors.isEmpty()) {
			succesors.sort((h1, h2) -> h1.getIngressDateTime().compareTo(h2.getIngressDateTime()));
			return succesors.get(0);
		} else {
			return null;
		}

	}

	private void checkUpdateStatusMafiosoArguments(String fullName, MAFIOSOS_STATUS status) {
		if (fullName == null || fullName.isEmpty()) {
			throw new IllegalArgumentException("Arguments Errors Expception");
		}
	}

	public List<Mafioso> getBosses() {

		mafiaBosses.clear();

		graph.vertexSet().forEach((Mafioso mafioso) -> {

			if (isBoss(mafioso)) {
				Mafioso result = mafioso.deepClone();
				result.setReporters(graph.edgesOf(mafioso).size());
				mafiaBosses.add(result);
			}
		});

		return mafiaBosses;
	}

	public List<Mafioso> getActiveBosses() {

		return getBosses().stream().filter(p -> p.getStatus() == MAFIOSOS_STATUS.FREE.code()).collect(Collectors.toList());
	}

	private boolean isBoss(Mafioso mafioso) {

		return graph.edgesOf(mafioso).stream().filter(p -> p.getBoss().getFullName().equals(mafioso.getFullName())).collect(Collectors.toList()).size() >= mafiaBossReporters;

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