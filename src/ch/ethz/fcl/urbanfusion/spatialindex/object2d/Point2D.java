/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex.object2d;

import javax.vecmath.Vector2d;

import ch.ethz.fcl.urbanfusion.util.VectorUtil;

public class Point2D implements IObject2D {
	private float x;
	private float y;

	public Point2D() {
		this.x = 0;
		this.y = 0;
	}

	public Point2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float distance(Object p) {
		double square = Math.pow(this.x - ((Point2D) p).getX(), 2)
				+ Math.pow(this.y - ((Point2D) p).getY(), 2);
		return (float) Math.sqrt(square);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void shift(float x, float y) {
		this.x += x;
		this.y += y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override
	public boolean intersects(Rectangle rec) {
		return false;
	}

	@Override
	public boolean contains(Rectangle rec) {
		return false;
	}

	@Override
	public Rectangle getBound() {
		return new Rectangle(x, y, x, y);
	}

	@Override
	public boolean contains(Point2D point) {
		return false;
	}

	public Vector2d getDir(Point2D p2) {
		return new Vector2d(p2.getX() - x, p2.getY() - y);
	}

	public Point2D getPointAt(Point2D end, float rate) {
		float new_x = x + rate * (end.x - x);
		float new_y = y + rate * (end.y - y);

		return new Point2D(new_x, new_y);
	}

	public Point2D getPointAt(Vector2d vec, float length) {
		vec.normalize();
		float new_x = x + (float) vec.x * length;
		float new_y = y + (float) vec.y * length;

		return new Point2D(new_x, new_y);
	}

	/**
	 * return a point rotating to end point referencing center point
	 * 
	 * @param end
	 * @param center
	 * @param ratio
	 * @return
	 */
	public Point2D getPointAt(Point2D end, Point2D center, float ratio) {
		Vector2d v1 = new Vector2d(x - center.x, y - center.y);
		Vector2d v2 = new Vector2d(end.x - center.x, end.y - center.y);

		double angle = VectorUtil.angle(v1, v2);

		return VectorUtil.rotate(this, center, angle * ratio);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Point2D))
			return false;
		Point2D p = (Point2D) o;
		if (p.getX() != x)
			return false;
		if (p.getY() != y)
			return false;

		return true;
	}
}
