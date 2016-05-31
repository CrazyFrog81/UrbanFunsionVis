/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.util;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3f;

import ch.ethz.fcl.urbanfusion.spatialindex.object2d.Point2D;

public class VectorUtil {
	public static float cos(Vector2d v1, Vector2d v2) {
		float result = (float) (v1.dot(v2) / (v1.length() * v2.length()));
		return result;
	}

	public static Vector3f rotate(Vector3f v, Vector3f axis, float angle) {
		// Rotate the point (x,y,z) around the vector (u,v,w)
		// Function RotatePointAroundVector(x#,y#,z#,u#,v#,w#,a#)
		float ux = axis.x * v.x;
		float uy = axis.x * v.y;
		float uz = axis.x * v.z;
		float vx = axis.y * v.x;
		float vy = axis.y * v.y;
		float vz = axis.y * v.z;
		float wx = axis.z * v.x;
		float wy = axis.z * v.y;
		float wz = axis.z * v.z;
		float sa = (float) Math.sin(angle);
		float ca = (float) Math.cos(angle);
		float x = axis.x
				* (ux + vy + wz)
				+ (v.x * (axis.y * axis.y + axis.z * axis.z) - axis.x
						* (vy + wz)) * ca + (-wy + vz) * sa;
		float y = axis.y
				* (ux + vy + wz)
				+ (v.y * (axis.x * axis.x + axis.z * axis.z) - axis.y
						* (ux + wz)) * ca + (wx - uz) * sa;
		float z = axis.z
				* (ux + vy + wz)
				+ (v.z * (axis.x * axis.x + axis.y * axis.y) - axis.z
						* (ux + vy)) * ca + (-vx + uy) * sa;

		return new Vector3f(x, y, z);
	}

	/**
	 * angle from v1 to v2 anti clockwise: [0, PI) clockwise: [-PI, 0)
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double angle(Vector2d v1, Vector2d v2) {
		double angle1 = Math.atan2(v1.y, v1.x);
		double angle2 = Math.atan2(v2.y, v2.x);
		double angle = angle2 - angle1;

		if (angle < -Math.PI)
			angle = 2 * Math.PI + angle;
		else if (angle > Math.PI)
			angle = angle - 2 * Math.PI;
		else if (angle == Math.PI)
			angle = -Math.PI;
		return angle;
	}

	/**
	 * 
	 * @param vec
	 * @param angle
	 *            angle in radians (Math.Pi), minus is clockwise,
	 * @return
	 */
	public static Vector2d rotate(Vector2d vec, double angle) {
		double rx = (vec.x * Math.cos(angle)) - (vec.y * Math.sin(angle));
		double ry = (vec.x * Math.sin(angle)) + (vec.y * Math.cos(angle));
		return new Vector2d(rx, ry);
	}

	/**
	 * 
	 * @param input_p
	 * @param origin
	 * @param angle
	 * @return
	 */
	public static Point2D rotate(Point2D input_p, Point2D origin, double angle) {
		float x = (float) (input_p.getX() * Math.cos(angle) - input_p.getY()
				* Math.sin(angle) - origin.getX() * Math.cos(angle)
				+ origin.getY() * Math.sin(angle) + origin.getX());
		float y = (float) (input_p.getX() * Math.sin(angle) + input_p.getY()
				* Math.cos(angle) - origin.getX() * Math.sin(angle)
				- origin.getY() * Math.cos(angle) + origin.getY());

		return new Point2D(x, y);
	}
}
