/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.model.map;

import java.util.ArrayList;
import java.util.List;

import ch.ethz.fcl.urbanfusion.model.SingaporeScenario;
import ch.ethz.fcl.urbanfusion.spatialindex.object2d.Point2D;
import ch.ethz.fcl.urbanfusion.spatialindex.object2d.Polygon;
import ch.ethz.fcl.urbanfusion.util.MathUtil;
import ch.fhnw.ether.scene.mesh.DefaultMesh;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.IMesh.Primitive;
import ch.fhnw.ether.scene.mesh.IMesh.Queue;
import ch.fhnw.ether.scene.mesh.geometry.DefaultGeometry;
import ch.fhnw.ether.scene.mesh.material.ColorMaterial;
import ch.fhnw.ether.scene.mesh.material.LineMaterial;
import ch.fhnw.util.color.RGBA;

public class MapLoader {
	SingaporeScenario ss;

	private float world_min_x = Float.MAX_VALUE, world_min_y = Float.MAX_VALUE,
			world_max_x = Float.MIN_VALUE, world_max_y = Float.MIN_VALUE;
	private float view_min_x = -0.9f, view_max_x = 0.9f;
	private float view_min_y, view_max_y;

	private IMesh map_mesh = null;
	private List<IMesh> boundary_mesh_list = null;

	public MapLoader(SingaporeScenario ss) {
		this.ss = ss;
	}

	public void init() {
		loadMapZones();
		loadMapBoundary();
	}

	private void loadMapZones() {
		List<Polygon> sg_zones = ShpReader.loadShpZones(
				"res/Singapore_Map_Delaunary", "Singapore_Delaunary");

		int zone_count = 0;

		for (Polygon zone : sg_zones) {
			if (zone.getPoints().size() != 4) {
				continue;
			}

			for (Point2D p : zone.getPoints()) {
				world_min_x = Math.min(p.getX(), world_min_x);
				world_min_y = Math.min(p.getY(), world_min_y);
				world_max_x = Math.max(p.getX(), world_max_x);
				world_max_y = Math.max(p.getY(), world_max_y);
			}

			zone_count++;
		}

		float yx_ratio = (world_max_y - world_min_y)
				/ (world_max_x - world_min_x);
		view_min_y = -yx_ratio * (view_max_x - view_min_x) / 2;
		view_max_y = yx_ratio * (view_max_x - view_min_x) / 2;

		float[] vertices = new float[9 * zone_count];
		int zone_index = 0;
		for (Polygon zone : sg_zones) {
			if (zone.getPoints().size() != 4) {
				continue;
			}

			for (int v_index = 0; v_index < zone.getPoints().size()
					- 1; v_index++) {
				Point2D p = zone.getPoints().get(v_index);

				vertices[zone_index * 9 + v_index * 3] = getViewX(p.getX());
				vertices[zone_index * 9 + v_index * 3 + 1] = getViewY(p.getY());
				vertices[zone_index * 9 + v_index * 3 + 2] = 0f;
			}

			zone_index++;
		}

		DefaultGeometry g = DefaultGeometry.createV(vertices);
		map_mesh = new DefaultMesh(Primitive.TRIANGLES,
				new ColorMaterial(new RGBA(0.6f, 0.6f, 0.6f, 1f)), g,
				IMesh.Flag.DONT_CULL_FACE);
		map_mesh.setName("Singapore Map");
	}

	private void loadMapBoundary() {
		boundary_mesh_list = new ArrayList<IMesh>();

		List<Polygon> sg_zones = ShpReader.loadShpZones("res/Singapore_Map",
				"subZoneUTM48N");

		for (int zone_index = 0; zone_index < sg_zones.size(); zone_index++) {
			Polygon zone = sg_zones.get(zone_index);

			float[] vertices = new float[zone.getPoints().size() * 3];
			for (int v_index = 0; v_index < zone.getPoints()
					.size(); v_index++) {
				Point2D p = zone.getPoints().get(v_index);

				vertices[v_index * 3] = getViewX(p.getX());
				vertices[v_index * 3 + 1] = getViewY(p.getY());

				vertices[v_index * 3 + 2] = 0;
			}

			DefaultGeometry g = DefaultGeometry.createV(vertices);
			boundary_mesh_list.add(new DefaultMesh(Primitive.LINE_STRIP,
					new LineMaterial(RGBA.BLACK), g));
		}
	}

	public float getViewX(float world_x) {
		return MathUtil.map(world_x, world_min_x, world_max_x, view_min_x,
				view_max_x);
	}

	public float getViewY(float world_y) {
		return MathUtil.map(world_y, world_min_y, world_max_y, view_min_y,
				view_max_y);
	}

	public IMesh getMapMesh() {
		return map_mesh;
	}

	public List<IMesh> getBoundaryMeshs() {
		return boundary_mesh_list;
	}
}
