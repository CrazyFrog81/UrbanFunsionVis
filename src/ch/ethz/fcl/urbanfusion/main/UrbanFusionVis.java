/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.main;

import java.io.IOException;

import ch.ethz.fcl.urbanfusion.model.SGModel;
import ch.ethz.fcl.urbanfusion.ui.SGUI;
import ch.fhnw.ether.controller.DefaultController;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.platform.Platform;
import ch.fhnw.ether.scene.DefaultScene;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.camera.Camera;
import ch.fhnw.ether.scene.camera.ICamera;
import ch.fhnw.ether.view.DefaultView;
import ch.fhnw.ether.view.IView;
import ch.fhnw.util.math.Vec3;

public class UrbanFusionVis {
	public static void main(String[] args) {
		new UrbanFusionVis();
	}

	public UrbanFusionVis() {
		SGModel sm = new SGModel();
		SGUI ui = new SGUI(sm);
		
		// Init platform
		Platform.get().init();

		// Create controller
		IController controller = new DefaultController();

		controller.run(time -> {
			// Create view
			IView view = new DefaultView(controller, 0, 0, 1500, 1000,
					IView.RENDER_VIEW, "Urban Data Fusion Visualization");

			IScene scene = new DefaultScene(controller);
			controller.setScene(scene);

			// Create and add camera
			ICamera camera = new Camera(new Vec3(0, 0, 1.5), Vec3.ZERO);
			scene.add3DObject(camera);
			controller.setCamera(view, camera);
			
			try {
				ui.enable(controller);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		Platform.get().run();
	}
}
