/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex.object3d;

import org.lwjgl.opengl.GL11;

import ch.ethz.fcl.urbanfusion.spatialindex.object2d.Point2D;
import ch.ethz.fcl.urbanfusion.spatialindex.object2d.Rectangle;

public class LineSegment3D implements IObject3D {
	private Point3D p1, p2;
	private int id;

	public LineSegment3D() {
		p1 = new Point3D();
		p2 = new Point3D();
	}

	public LineSegment3D(Point3D p1, Point3D p2, int id) {
		this.p1 = p1;
		this.p2 = p2;
		this.id = id;
	}

	@Override
	public BoundingBox getBoundingBox() {
		float minX = Math.min(p1.getX(), p2.getX());
		float maxX = Math.max(p1.getX(), p2.getX());
		float minY = Math.min(p1.getY(), p2.getY());
		float maxY = Math.max(p1.getY(), p2.getY());
		float minZ = Math.min(p1.getZ(), p2.getZ());
		float maxZ = Math.max(p1.getZ(), p2.getZ());
		return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}

	@Override
	public boolean intersects(BoundingBox bb) {
		Rectangle rec = new Rectangle(bb.getMinX(), bb.getMinY(), bb.getMaxX(),
				bb.getMaxY());
		Point2D p1xy = new Point2D(p1.getX(), p1.getY());
		Point2D p2xy = new Point2D(p2.getX(), p2.getY());
		if (rec.intersectLine(p1xy, p2xy) != null)
			return true;
		return false;
	}

	@Override
	public boolean contains(BoundingBox bb) {
		return false;
	}

	public float intersects(LineSegment3D line) {
		return intersects(line.getPoints()[0], line.getPoints()[1]);
	}

	public float intersects(Point3D p3, Point3D p4) {
		float numerator12 = (p4.getX() - p3.getX()) * (p1.getY() - p3.getY())
				- (p4.getY() - p3.getY()) * (p1.getX() - p3.getX());
		float denominator12 = (p4.getY() - p3.getY()) * (p2.getX() - p1.getX())
				- (p4.getX() - p3.getX()) * (p2.getY() - p1.getY());

		float numerator34 = (p2.getX() - p1.getX()) * (p1.getY() - p3.getY())
				- (p2.getY() - p1.getY()) * (p1.getX() - p3.getX());
		float denominator34 = (p4.getY() - p3.getY()) * (p2.getX() - p1.getX())
				- (p4.getX() - p3.getX()) * (p2.getY() - p1.getY());

		if (denominator12 == 0 || denominator34 == 0)
			return -1;
		else if (numerator12 / denominator12 >= 0
				&& numerator12 / denominator12 < 1
				&& numerator34 / denominator34 >= 0
				&& numerator34 / denominator34 < 1) {
			float t = p3.getZ() + numerator34 / denominator34
					* (p4.getZ() - p3.getZ());
			if (t >= Math.min(p1.getZ(), p2.getZ())
					&& t < Math.max(p1.getZ(), p2.getZ())) {
				return t;
			}
		}

		return -1;
	}

	public Point3D[] getPoints() {
		Point3D[] points = { p1, p2 };
		return points;
	}

	public float getMinZ() {
		return Math.min(p1.getZ(), p2.getZ());
	}

	public float getMaxZ() {
		return Math.max(p1.getZ(), p2.getZ());
	}
	
	public int getID(){
		return this.id;
	}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(p1.getX(), p1.getY(), p1.getZ());
		GL11.glVertex3f(p2.getX(), p2.getY(), p2.getZ());
		GL11.glEnd();
	}
}
