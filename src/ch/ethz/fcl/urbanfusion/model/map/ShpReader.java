/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.model.map;

import java.util.ArrayList;
import java.util.List;

import ch.ethz.fcl.urbanfusion.spatialindex.object2d.Polygon;
import ch.ethz.fcl.urbanfusion.util.StringUtil;
import diewald_shapeFile.files.dbf.DBF_File;
import diewald_shapeFile.files.shp.SHP_File;
import diewald_shapeFile.files.shp.shapeTypes.ShpPolygon;
import diewald_shapeFile.files.shx.SHX_File;
import diewald_shapeFile.shapeFile.ShapeFile;

public class ShpReader {
	public static List<Polygon> loadShpZones(String folderName, String fileName) {
		DBF_File.LOG_INFO = false;
		DBF_File.LOG_ONLOAD_HEADER = false;
		DBF_File.LOG_ONLOAD_CONTENT = false;

		SHX_File.LOG_INFO = false;
		SHX_File.LOG_ONLOAD_HEADER = false;
		SHX_File.LOG_ONLOAD_CONTENT = false;

		SHP_File.LOG_INFO = false;
		SHP_File.LOG_ONLOAD_HEADER = false;
		SHP_File.LOG_ONLOAD_CONTENT = false;

		ShapeFile shapefile;

		List<Polygon> zones = new ArrayList<Polygon>();

		// LOAD SHAPE FILE (.shp, .shx, .dbf)
		try {
			shapefile = new ShapeFile(folderName, fileName).READ();

			int number_of_shapes = shapefile.getSHP_shapeCount();
			StringUtil.debug("parts " + shapefile.getSHP_shape().size(), false);
			StringUtil.debug("parts dbf " + shapefile.getDBF_fieldCount(),
					false);

			for (int i = 0; i < number_of_shapes; i++) {
				ShpPolygon shape = shapefile.getSHP_shape(i);
				double[][][] parts;
				if (shape.getNumberOfParts() > 1) {
					parts = shape.getPointsAs3DArray();
					for (int j = 0; j < parts.length; j++)
						zones.add(readZone(parts[j]));
				} else {
					Polygon zone = readZone(shape.getPoints());
					if (zone != null)
						zones.add(zone);
				}
			}

			StringUtil.debug("subzone size " + zones.size(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return zones;
	}

	private static Polygon readZone(double[][] points) {
		Polygon zone = new Polygon();
		for (int j = 0; j < points.length; j++) {
			float x = (float) points[j][0];
			float y = (float) points[j][1];
			zone.addPoint(x, y);
		}

		return zone;
	}
}
