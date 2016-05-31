/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.model.road;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ch.ethz.fcl.urbanfusion.model.SingaporeScenario;
import ch.ethz.fcl.urbanfusion.model.road.basemap.Edge;
import ch.ethz.fcl.urbanfusion.model.road.basemap.Node;
import ch.fhnw.ether.scene.mesh.DefaultMesh;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.IMesh.Primitive;
import ch.fhnw.ether.scene.mesh.IMesh.Queue;
import ch.fhnw.ether.scene.mesh.geometry.DefaultGeometry;
import ch.fhnw.ether.scene.mesh.material.ColorMaterial;
import ch.fhnw.util.color.RGBA;

public class RoadLoader {
	SingaporeScenario ss;

	private Map<String, Node> node_map = null; // key: node id; value: node

	private List<Edge> edges = null;
	private IMesh road_mesh = null;

	private List<Node> mrtNodes;

	public RoadLoader(SingaporeScenario ss) {
		this.ss = ss;

		node_map = new HashMap<String, Node>();

		edges = new ArrayList<Edge>();

		mrtNodes = new ArrayList<Node>();
	}

	public void init() {
		loadRoad();
		generateRoadMesh();
	}

	private void loadRoad() {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		try {
			InputStream is = getClass().getResource("output_network.xml")
					.openStream();
			try {
				SAXParser parser = parserFactory.newSAXParser();
				parser.parse(is, new DefaultHandler() {
					@Override
					public InputSource resolveEntity(String publicId,
							String systemId) throws org.xml.sax.SAXException,
									java.io.IOException {
						System.out.println(
								"Ignoring: " + publicId + ", " + systemId);
						return new InputSource(new java.io.StringReader(""));
					}

					@Override
					public void startElement(String uri, String localName,
							String name, Attributes attributes)
									throws SAXException {
						if (name.equals("node")) {
							addNode(attributes);
						} else if (name.equals("link")) {
							addEdge(attributes);
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println("loaded " + getNodes().size() + " nodes "
		// + getEdges().size() + " edges");
	}

	private void generateRoadMesh() {
		List<Edge> public_transit_edges = new ArrayList<Edge>();

		// render only the public transit network
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream("weightedEdgeIndex")));
			br.readLine();
			String line = br.readLine();
			String[] index = line.split("\t");
			for (int i = 0; i < index.length; i++)
				public_transit_edges.add(edges.get(Integer.parseInt(index[i])));
		} catch (IOException e) {
			e.printStackTrace();
		}

		float[] vertices = new float[6 * public_transit_edges.size()];
		for (int index = 0; index < public_transit_edges.size(); index++) {
			Edge e = public_transit_edges.get(index);

			// add edge in the edge mesh
			vertices[index * 6 + 0] = ss.getMap().getViewX(e.getStart().getX());
			vertices[index * 6 + 1] = ss.getMap().getViewY(e.getStart().getY());
			vertices[index * 6 + 2] = 0;

			vertices[index * 6 + 3] = ss.getMap().getViewX(e.getEnd().getX());
			vertices[index * 6 + 4] = ss.getMap().getViewY(e.getEnd().getY());
			vertices[index * 6 + 5] = 0;
		}

		DefaultGeometry g = DefaultGeometry.createV(vertices);
		road_mesh = new DefaultMesh(Primitive.LINES,
				new ColorMaterial(new RGBA(1, 1, 1, 1f)), g);
		road_mesh.setName("Road Network");
	}

	private void addNode(Attributes attributes) {
		// attributes: id, x, y, type
		String id = attributes.getValue("id");
		String x = attributes.getValue("x");
		String y = attributes.getValue("y");

		try {
			float fx = Float.parseFloat(x);
			float fy = Float.parseFloat(y);

			Node node = new Node(id, fx, fy);
			if (node_map.get(id) != null)
				System.out.println(id);

			node_map.put(id, node);

			if (id.contains("EW") || id.contains("NS") || id.contains("NE")
					|| id.contains("CC") || id.contains("CG")
					|| id.contains("PE") || id.contains("PTC")
					|| id.contains("SE") || id.contains("SW")
					|| id.contains("SEN")) {
				mrtNodes.add(node);
			}

		} catch (NumberFormatException e) {
			System.out.println(
					"warning: ignoring node " + id + " (invalid position)");
		}
	}

	private void addEdge(Attributes attributes) {
		String id = attributes.getValue("id");
		String from = attributes.getValue("from");
		String to = attributes.getValue("to");

		Node f = node_map.get(from);
		Node t = node_map.get(to);

		if (f == null || t == null)
			return;

		Edge edge = new Edge(id, f, t);
		edges.add(edge);
	}

	public Collection<Node> getNodes() {
		return node_map.values();
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public List<Node> getMRTNodes() {
		return mrtNodes;
	}

	public IMesh getRoadMesh() {
		return road_mesh;
	}
}
