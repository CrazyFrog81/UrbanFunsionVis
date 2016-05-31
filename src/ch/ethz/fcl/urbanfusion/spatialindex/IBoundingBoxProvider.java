/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex;

import ch.ethz.fcl.urbanfusion.spatialindex.object3d.BoundingBox;

public interface IBoundingBoxProvider {
	public BoundingBox getBoundingBox();
}
