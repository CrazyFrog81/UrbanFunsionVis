/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.model;

import ch.ethz.fcl.urbanfusion.model.map.MapLoader;
import ch.ethz.fcl.urbanfusion.model.road.MRTNetworkLoader;
import ch.ethz.fcl.urbanfusion.model.road.RoadLoader;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.mesh.IMesh;

public class SGModel {
	private final MapLoader map_loader = new MapLoader(this);
	private final RoadLoader road_loader = new RoadLoader(this);
	private final MRTNetworkLoader mrt_loader = new MRTNetworkLoader(this);
	// private PublicTransitLoader transit_loader;

	public SGModel() {
		map_loader.load();
		road_loader.load();
		mrt_loader.load();
	}

	public void addToScene(IScene scene) {
		scene.add3DObjects(map_loader.getMapMesh());
		scene.add3DObjects(map_loader.getBoundaryMeshs());
		scene.add3DObject(road_loader.getRoadMesh());
		scene.add3DObjects(mrt_loader.getNodeMeshs());
		scene.add3DObjects(mrt_loader.getLinkMeshs());
	}
	
	public void hide(IScene scene, IMesh... mesh){
		scene.remove3DObjects(mesh);
	}

	public MapLoader getMap() {
		return map_loader;
	}

	public RoadLoader getRoad() {
		return road_loader;
	}

	public MRTNetworkLoader getMRT() {
		return mrt_loader;
	}
}
