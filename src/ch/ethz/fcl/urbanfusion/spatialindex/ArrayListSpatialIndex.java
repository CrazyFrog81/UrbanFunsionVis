/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex;

import java.util.ArrayList;
import java.util.Iterator;

import ch.ethz.fcl.urbanfusion.spatialindex.object3d.BoundingBox;
import ch.ethz.fcl.urbanfusion.spatialindex.object3d.IObject3D;

/*
 * *a simple implementation of spatial index using array list
 */
public class ArrayListSpatialIndex implements ISpatialIndex {
	ArrayList<IObject3D> objectList = new ArrayList<IObject3D>();
	BoundingBox bb = new BoundingBox();

	@Override
	public void add(IObject3D object, int id) {
		objectList.add(object);
		if(objectList.size() == 1)
			bb.copy(object.getBoundingBox());
		else
			bb.add(object.getBoundingBox());
	}

	@Override
	public boolean delete(IObject3D object, int id) {
		if (objectList.contains(object)) {
			objectList.remove(object);
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<Integer> getIntersectingObjects(IObject3D object3d) {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		Iterator<IObject3D> it = objectList.iterator();
		int id = 0;
		while(it.hasNext()){
			IObject3D object = (IObject3D) it.next();
			if(object3d.intersects(object.getBoundingBox()))
				indexes.add(id);
			id++;
		}				
		return indexes;
	}

	@Override
	public ArrayList<Integer> getContainingObjects(IObject3D object3d) {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		Iterator<IObject3D> it = objectList.iterator();
		int id = 0;
		while(it.hasNext()){
			IObject3D object = (IObject3D) it.next();
			if(object3d.contains(object.getBoundingBox()))
				indexes.add(id);
			id++;
		}				
		return indexes;
	}

	@Override
	public int size() {
		return objectList.size();
	}

	@Override
	public BoundingBox getBoundingBox() {
		return bb;
	}

	@Override
	public ArrayList<Integer> getObjects() {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int i = 0; i < size(); i++)
			indexes.add(i);
		return indexes;
	}

	@Override
	public void init() {
		objectList = new ArrayList<IObject3D>();
	}

}
