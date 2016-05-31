/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.util;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;

/**
 * Using JCoord library to convert between different coordinate system.
 * Reference: http://www.jstott.me.uk/
 * 
 * @author Zeng Wei
 * @email zeng@arch.ethz.ch
 * 
 */
public class CoordUtil {
	public static void main(String[] args) {
		double[] utm = LatLngToUTM(1.2934895279731098, 103.772917958038);

		System.out.println(utm[0] + " " + utm[1]);

		double[] latlng = UTMToLatLng(355986.45140770136, 148020.35491986413);

		System.out.println(latlng[0] + " " + latlng[1]);
	}

	public static double[] LatLngToUTM(double lat, double lng) {
		LatLng latlng = new LatLng(lat, lng);

		UTMRef utm = latlng.toUTMRef();

		StringUtil.debug(
				"Lat zone " + utm.getLatZone() + " Lng zone "
						+ utm.getLngZone(), false);

		return new double[] { utm.getEasting(), utm.getNorthing() };
	}

	public static double[] UTMToLatLng(double easting, double northing) {
		// Singapore is in N lat zone and 48 lng zone
		UTMRef utm = new UTMRef(easting, northing, 'N', 48);

		LatLng latlng = utm.toLatLng();

		return new double[] { latlng.getLat(), latlng.getLng() };
	}
}
