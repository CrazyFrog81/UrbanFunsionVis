/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex.object2d;

public interface IObject2D{
	public boolean intersects(Rectangle rec);
	public boolean contains(Rectangle rec);
	public boolean contains(Point2D point);
	public Rectangle getBound();
}
