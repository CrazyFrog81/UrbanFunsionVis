/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex.object3d;

import ch.ethz.fcl.urbanfusion.spatialindex.IBoundingBoxProvider;

public interface IObject3D extends IBoundingBoxProvider {
	public boolean intersects(BoundingBox bb);
	public boolean contains(BoundingBox bb);
	public void render();
}
