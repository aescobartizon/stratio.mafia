package com.stratio.anescobar.mafia.components;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.stereotype.Service;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import com.stratio.anescobar.mafia.domain.MafiaEdge;
import com.stratio.anescobar.mafia.domain.Mafioso;

import lombok.Getter;

@Service
public class MafiaHierarchyJgraphtService extends AbstractGenericService implements MafiaHierarchy {

	private Graph<Mafioso, MafiaEdge<Mafioso>> graph = new SimpleGraph<>(MafiaEdge.class);

	@Getter
	private String implementation = "Jgrapht";

	private List<Mafioso> bosses = new ArrayList<>();

	@Getter
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

		if (graph.containsVertex(source) && graph.containsVertex(target)) {
			graph.addEdge(source, target);
		}
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
//
//	public List<Mafioso> getBosses() {
//		if(lastBossSeach == null) {
//			bosses.clear();
//			graph.vertexSet().forEach((){
//				p
//			} );
//		}
//
//	}
//
//	public Mafioso isBoss(Mafioso mafioso, Set<MafiaEdge> reporters) {
//		
//		Mafioso result = mafioso.deepClone();
//		if(reporters.size() > 50) {
//			result.setReporters(reporters.size());
//			return result; 
//		}
//
//	}

	public void printGraph() throws IOException {

		Graph<String, MafiaEdge<Mafioso>> graph1 = new SimpleGraph<>(MafiaEdge.class);
		graph.vertexSet().forEach(p -> graph1.addVertex(p.getFullName()));
		graph.edgeSet().forEach(p -> graph1.addEdge(p.getReporter().getFullName(), p.getBoss().getFullName()));

		JGraphXAdapter<String, MafiaEdge<Mafioso>> graphAdapter = new JGraphXAdapter<String, MafiaEdge<Mafioso>>(graph1);

		mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
		layout.execute(graphAdapter.getDefaultParent());

		BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
		File imgFile = new File("src/resources/graph.png");
		ImageIO.write(image, "PNG", imgFile);

	}

}
