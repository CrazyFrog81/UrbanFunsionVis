/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex.object2d;

import org.lwjgl.opengl.GL11;

public class Rectangle implements IObject2D {
	private float minX;
	private float minY;
	private float maxX;
	private float maxY;

	public Rectangle() {
	}

	public Rectangle(Point2D point1, Point2D point2) {
		this.minX = Math.min(point1.getX(), point2.getX());
		this.minY = Math.min(point1.getY(), point2.getY());
		this.maxX = Math.max(point1.getX(), point2.getX());
		this.maxY = Math.max(point1.getY(), point2.getY());
	}

	public Rectangle(float minX, float minY, float maxX, float maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public Rectangle zoom(float ratio) {
		float midX = (minX + maxX) / 2;
		float midY = (minY + maxY) / 2;

		Point2D mid = new Point2D(midX, midY);

		Point2D p1 = mid.getPointAt(new Point2D(minX, minY), ratio);
		Point2D p2 = mid.getPointAt(new Point2D(maxX, maxY), ratio);

		return new Rectangle(p1, p2);
	}

	public boolean contains(float x, float y) {
		return x >= this.minX && x <= this.maxX && y >= this.minY
				&& y <= this.maxY;
	}

	public Point2D[] getPoints() {
		Point2D[] points = { new Point2D(minX, minY), new Point2D(minX, maxY),
				new Point2D(maxX, minY), new Point2D(maxX, maxY) };

		return points;
	}

	public float getWidth() {
		return maxX - minX;
	}

	public float getHeight() {
		return maxY - minY;
	}

	@Override
	public boolean intersects(Rectangle r) {
		return this.maxX >= r.minX && this.maxY >= r.minY
				&& this.minX <= r.maxX && this.minY <= r.maxY;
	}

	@Override
	public boolean contains(Rectangle r) {
		return this.minX <= r.minX && this.minY <= r.minY
				&& this.maxX >= r.maxX && this.maxY >= r.maxY;
	}

	public boolean intersects(Circle circle) {
		float centerX = circle.getCenter().getX();
		float centerY = circle.getCenter().getY();
		float radius = circle.getRadius();

		return intersects(centerX, centerY, radius);
	}

	private boolean intersects(float centerX, float centerY, float radius) {
		float width = this.maxX - this.minX;
		float height = this.maxY - this.minY;
		double centerDistanceX = Math.abs(centerX - this.minX / 2 - this.maxX
				/ 2);
		double centerDistanceY = Math.abs(centerY - this.minY / 2 - this.maxY
				/ 2);

		if (centerDistanceX > width / 2 + radius)
			return false;
		if (centerDistanceY > height / 2 + radius)
			return false;

		if (centerDistanceX <= width / 2)
			return true;
		if (centerDistanceY <= height / 2)
			return true;

		double cornerDistance = Math.pow(centerDistanceX - width / 2, 2)
				+ Math.pow(centerDistanceY - height / 2, 2);

		return cornerDistance <= Math.pow(radius, 2);

	}

	public float[] intersectLine(Point2D p1, Point2D p2) {
		float[] results = new float[2];
		int assigned = 0;

		Point2D minXMinY = new Point2D(minX, minY);
		Point2D minXMaxY = new Point2D(minX, maxY);
		Point2D maxXMinY = new Point2D(maxX, minY);
		Point2D maxXMaxY = new Point2D(maxX, maxY);

		// check intersect with the (minX, minY) and (minX, maxY) segment
		float check = intersectLineSegement(p1, p2, minXMinY, minXMaxY);
		if (check != -1) {
			results[assigned] = check;
			assigned++;
		}

		// check intersect with the (maxX, minY) and (maxX, maxY) segment
		check = intersectLineSegement(p1, p2, maxXMinY, maxXMaxY);
		if (check != -1) {
			results[assigned] = check;
			assigned++;
		}

		// check intersect with the (minX, maxY) and (maxX, maxY) segment
		check = intersectLineSegement(p1, p2, minXMaxY, maxXMaxY);
		if (check != -1) {
			results[assigned] = check;
			assigned++;
		}

		// check intersect with the (minX, minY) and (maxX, maxY) segment
		check = intersectLineSegement(p1, p2, minXMinY, maxXMinY);
		if (check != -1) {
			results[assigned] = check;
			assigned++;
		}

		switch (assigned) {
		case 0:
			return null;
		case 2:
			return results;
		default:
			return results;
		}
	}

	private float intersectLineSegement(Point2D p1, Point2D p2, Point2D p3,
			Point2D p4) {
		LineSegment line1 = new LineSegment(p1, p2);
		LineSegment line2 = new LineSegment(p3, p4);

		Point2D intersect = line1.intersectAsLineSegment(line2);
		if (intersect != null)
			return (intersect.getX() - p1.getX()) / (p2.getX() - p1.getX());
		else
			return -1;
	}

	@Override
	public String toString() {
		return "(" + minX + ", " + minY + ", " + maxX + ", " + maxY + ")";
	}

	public float getMinX() {
		return minX;
	}

	public float getMaxX() {
		return maxX;
	}

	public float getMinY() {
		return minY;
	}

	public float getMaxY() {
		return maxY;
	}

	public void shift(float deltaX, float deltaY) {
		minX += deltaX;
		maxX += deltaX;
		minY += deltaY;
		maxY += deltaY;
	}

	public Point2D getCenter() {
		return new Point2D(minX / 2 + maxX / 2, minY / 2 + maxY / 2);
	}

	@Override
	public Rectangle getBound() {
		return this;
	}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_LINES);

		GL11.glVertex2f(minX, minY);
		GL11.glVertex2f(minX, maxY);

		GL11.glVertex2f(minX, minY);
		GL11.glVertex2f(maxX, minY);

		GL11.glVertex2f(maxX, maxY);
		GL11.glVertex2f(minX, maxY);

		GL11.glVertex2f(maxX, maxY);
		GL11.glVertex2f(maxX, minY);

		GL11.glEnd();
	}

	public void render(int type) {
		GL11.glBegin(type);
		GL11.glVertex2f(minX, minY);
		GL11.glVertex2f(minX, maxY);
		GL11.glVertex2f(maxX, maxY);
		GL11.glVertex2f(maxX, minY);
		GL11.glVertex2f(minX, minY);
		GL11.glEnd();
	}

	@Override
	public boolean contains(Point2D point) {
		return contains(point.getX(), point.getY());
	}
}
