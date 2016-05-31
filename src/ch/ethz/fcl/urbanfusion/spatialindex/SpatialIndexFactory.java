/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex;

public class SpatialIndexFactory {
	public static ISpatialIndex newInstance(String id){
		ISpatialIndex si = null;
		String classname = "ch.ethz.fcl.metrobuzz.spatialindex."+id;
		try{
			si = (ISpatialIndex)Class.forName(classname).newInstance();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return si;
	}
}
