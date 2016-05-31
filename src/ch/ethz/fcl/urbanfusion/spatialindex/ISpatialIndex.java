/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex;

import java.util.ArrayList;

import ch.ethz.fcl.urbanfusion.spatialindex.object3d.IObject3D;

public interface ISpatialIndex extends IBoundingBoxProvider {
	public void init();
	public void add(IObject3D object, int id);
	public boolean delete(IObject3D object, int id);
	
	public ArrayList<Integer> getIntersectingObjects(IObject3D bb);
	public ArrayList<Integer> getContainingObjects(IObject3D bb);
	
	public ArrayList<Integer> getObjects();
	
	public int size();
}
