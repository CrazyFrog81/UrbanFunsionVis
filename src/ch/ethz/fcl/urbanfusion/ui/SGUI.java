/*
* Copyright (C) 2010 - 2016 ETH Zurich
* Unauthorized copying of this file, via any medium is strictly prohibited
* Proprietary and confidential
* Written by Zeng Wei (zeng@arch.ethz.ch)
*/

package ch.ethz.fcl.urbanfusion.ui;

import java.io.IOException;
import java.util.Map;

import ch.ethz.fcl.urbanfusion.model.SGModel;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.controller.event.IKeyEvent;
import ch.fhnw.ether.controller.event.IPointerEvent;
import ch.fhnw.ether.controller.tool.AbstractTool;
import ch.fhnw.ether.controller.tool.ITool;
import ch.fhnw.ether.controller.tool.NavigationTool;
import ch.fhnw.ether.controller.tool.PickUtilities;
import ch.fhnw.ether.controller.tool.PickUtilities.PickMode;
import ch.fhnw.ether.scene.I3DObject;

public class SGUI {
	private final SGModel model;

	public SGUI(SGModel model) {
		this.model = model;
	}

	public void enable(IController controller) throws IOException {
		model.addToScene(controller.getScene());

		ITool naviTool = new NavigationTool(controller,
				new AbstractTool(controller) {
					@Override
					public void keyPressed(IKeyEvent e) {
						try {
							if (e.getKeyCode() == IKeyEvent.VK_1) {
								model.hide(controller.getScene(),
										model.getMap().getMapMesh());
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}

					@Override
					public void pointerPressed(IPointerEvent e) {
						int x = e.getX();
						int y = e.getY();
						Map<Float, I3DObject> pickables = PickUtilities
								.pickFromScene(PickMode.POINT, x, y, 0, 0,
										e.getView());
						if (pickables.isEmpty())
							System.out.println("no pick");
						else
							controller.getScene().remove3DObject(
									pickables.values().iterator().next());
					}
				});

		controller.setTool(naviTool);
	}
}
