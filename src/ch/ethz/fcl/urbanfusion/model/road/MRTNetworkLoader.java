/*
* Copyright (C) 2010 - 2016 ETH Zurich
* Unauthorized copying of this file, via any medium is strictly prohibited
* Proprietary and confidential
* Written by Zeng Wei (zeng@arch.ethz.ch)
*/

package ch.ethz.fcl.urbanfusion.model.road;

import java.util.ArrayList;
import java.util.List;

import ch.ethz.fcl.urbanfusion.config.IColors;
import ch.ethz.fcl.urbanfusion.model.SGModel;
import ch.ethz.fcl.urbanfusion.model.road.basemap.Edge;
import ch.ethz.fcl.urbanfusion.model.road.basemap.Node;
import ch.fhnw.ether.scene.mesh.DefaultMesh;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.IMesh.Primitive;
import ch.fhnw.ether.scene.mesh.geometry.DefaultGeometry;
import ch.fhnw.ether.scene.mesh.material.ColorMaterial;
import ch.fhnw.util.color.RGB;
import ch.fhnw.util.color.RGBA;

public class MRTNetworkLoader {
	SGModel ss;

	private List<Node> mrt_nodes;
	private List<Edge> mrt_links;

	private List<IMesh> node_meshs;
	private List<IMesh> link_meshs;

	public MRTNetworkLoader(SGModel ss) {
		this.ss = ss;

	}

	public void load() {
		mrt_nodes = new ArrayList<Node>();
		mrt_links = new ArrayList<Edge>();
		for (Node node : ss.getRoad().getNodes()) {
			String id = node.getId();
			if (id.contains("EW") || id.contains("NS") || id.contains("NE")
					|| id.contains("CC") || id.contains("CG")
					|| id.contains("PE") || id.contains("PTC")
					|| id.contains("SE") || id.contains("SW")
					|| id.contains("SEN")) {

				mrt_nodes.add(node);
			}
		}

		for (Edge e : ss.getRoad().getEdges()) {
			String id = e.getId();
			if (id.contains("EW") || id.contains("NS") || id.contains("NE")
					|| id.contains("CC") || id.contains("CG")
					|| id.contains("PE") || id.contains("PTC")
					|| id.contains("SE") || id.contains("SW")
					|| id.contains("SEN")) {

				mrt_links.add(e);
			}
		}

		loadNodeMesh();
		loadLinkMesh();
	}

	private void loadNodeMesh() {
		node_meshs = new ArrayList<IMesh>();

		for (Node mrt_node : mrt_nodes) {
			if (mrt_node.getId().contains("SE0")
					|| mrt_node.getId().contains("PTC")) {
				continue;
			}

			float cx = ss.getMap().getViewX(mrt_node.getX());
			float cy = ss.getMap().getViewY(mrt_node.getY());
			float size = 0.01f;

			String[] line_ids = mrt_node.getId().split("/");
			float x = cx - size * (line_ids.length - 1) / 2;

			if (mrt_node.getId().contains("CG0"))
				x += size / 2;

			for (int i = 0; i < line_ids.length; i++) {
				if (line_ids[i].equals("CG0"))
					continue;

				node_meshs.add(getNodeMesh(x, cy, size, mrt_node.getId(),
						line_ids[i]));

				x += size;
			}
		}
	}

	private void loadLinkMesh() {
		link_meshs = new ArrayList<IMesh>();

		for (Edge e : mrt_links) {
			Node from = e.getStart();
			Node to = e.getEnd();

			float[] vertices = new float[6];
			vertices[0] = ss.getMap().getViewX(from.getX());
			vertices[1] = ss.getMap().getViewY(from.getY());
			vertices[2] = 0.0001f;

			vertices[3] = ss.getMap().getViewX(to.getX());
			vertices[4] = ss.getMap().getViewY(to.getY());
			vertices[5] = 0.0001f;

			DefaultGeometry g = DefaultGeometry.createV(vertices);
			RGB color = new RGB(1, 0, 1);
			if ((from.getId().contains("EW") && to.getId().contains("EW"))
					|| from.getId().contains("CG")
							&& to.getId().contains("CG")) {
				color = IColors.MRT_EW;
			} else if ((from.getId().contains("NS")
					&& to.getId().contains("NS"))) {
				color = IColors.MRT_NS;
			} else if ((from.getId().contains("NE")
					&& to.getId().contains("NE"))) {
				color = IColors.MRT_NE;
			} else if ((from.getId().contains("CC")
					&& to.getId().contains("CC"))) {
				color = IColors.MRT_CC;
			} else {
				color = IColors.LRT;
			}
			IMesh link_mesh = new DefaultMesh(Primitive.LINES,
					new ColorMaterial(new RGBA(color)), g);
			link_mesh.setName(e.getId());

			link_meshs.add(link_mesh);
		}
	}

	private IMesh getNodeMesh(float px, float py, float size, String full_id,
			String line_id) {
		float[] vertices = new float[18];

		// add edge in the edge mesh
		vertices[0] = px - size / 2;
		vertices[1] = py - size / 2;
		vertices[2] = 0.001f;

		vertices[3] = px - size / 2;
		vertices[4] = py + size / 2;
		vertices[5] = 0.001f;

		vertices[6] = px + size / 2;
		vertices[7] = py + size / 2;
		vertices[8] = 0.001f;

		vertices[9] = px + size / 2;
		vertices[10] = py + size / 2;
		vertices[11] = 0.001f;

		vertices[12] = px + size / 2;
		vertices[13] = py - size / 2;
		vertices[14] = 0.001f;

		vertices[15] = px - size / 2;
		vertices[16] = py - size / 2;
		vertices[17] = 0.001f;

		DefaultGeometry g = DefaultGeometry.createV(vertices);
		RGB color = new RGB(1, 0, 1);
		if (line_id.contains("EW") || line_id.contains("CG")) {
			color = IColors.MRT_EW;
		} else if (line_id.contains("NS")) {
			color = IColors.MRT_NS;
		} else if (line_id.contains("NE")) {
			color = IColors.MRT_NE;
		} else if (line_id.contains("CC")) {
			color = IColors.MRT_CC;
		} else {
			color = IColors.LRT;
		}

		IMesh node_mesh = new DefaultMesh(Primitive.TRIANGLES,
				new ColorMaterial(new RGBA(color)), g,
				IMesh.Flag.DONT_CULL_FACE);
		node_mesh.setName(full_id);

		return node_mesh;
	}

	public List<Node> getMRTNodes() {
		return mrt_nodes;
	}

	public List<Edge> getMRTLinks() {
		return mrt_links;
	}

	public List<IMesh> getNodeMeshs() {
		return node_meshs;
	}

	public List<IMesh> getLinkMeshs() {
		return link_meshs;
	}
}
