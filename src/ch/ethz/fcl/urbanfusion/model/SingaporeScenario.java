/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.model;

import ch.ethz.fcl.urbanfusion.model.map.MapLoader;
import ch.ethz.fcl.urbanfusion.model.road.RoadLoader;
import ch.ethz.fcl.urbanfusion.model.transit.PublicTransitLoader;

public class SingaporeScenario {
	private MapLoader map_loader;
	private RoadLoader road_loader;
	private PublicTransitLoader transit_loader;

	public SingaporeScenario() {
		map_loader = new MapLoader(this);
		road_loader = new RoadLoader(this);
		transit_loader = new PublicTransitLoader();
	}

	public void init() {
		map_loader.init();
		road_loader.init();
	}

	public MapLoader getMap() {
		return map_loader;
	}

	public RoadLoader getRoad() {
		return road_loader;
	}
}
