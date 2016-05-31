/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.main;

import ch.ethz.fcl.urbanfusion.model.SingaporeScenario;
import ch.fhnw.ether.controller.DefaultController;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.scene.DefaultScene;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.camera.Camera;
import ch.fhnw.ether.scene.camera.ICamera;
import ch.fhnw.ether.view.IView;
import ch.fhnw.ether.view.gl.DefaultView;
import ch.fhnw.util.math.Vec3;

public class UrbanFusionVis {
	public static void main(String[] args) {
		new UrbanFusionVis();
	}

	public UrbanFusionVis() {
		SingaporeScenario ss = new SingaporeScenario();
		ss.init();

		// Create controller
		IController controller = new DefaultController();

		controller.run(time -> {
			// Create view
			IView view = new DefaultView(controller, 0, 0, 1500, 1000,
					IView.RENDER_VIEW, "Urban Data Fusion Visualization");

			IScene scene = new DefaultScene(controller);
			controller.setScene(scene);

			// Create and add camera
			ICamera camera = new Camera(new Vec3(0, 0, 2), Vec3.ZERO);
			scene.add3DObject(camera);
			controller.setCamera(view, camera);

			scene.add3DObjects(ss.getMap().getMapMesh());
			scene.add3DObjects(ss.getMap().getBoundaryMeshs());
			scene.add3DObject(ss.getRoad().getRoadMesh());
		});
	}
}
