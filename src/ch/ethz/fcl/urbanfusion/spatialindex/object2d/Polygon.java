/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex.object2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon implements IObject2D {
	private List<Point2D> points;
	private Point2D centroid = null;
	private float minX = Float.MAX_VALUE;
	private float maxX = -Float.MAX_VALUE;
	private float minY = Float.MAX_VALUE;
	private float maxY = -Float.MAX_VALUE;

	private double[][] vertex;

	private double[][] vertex3D;

	public Polygon() {
		points = new ArrayList<Point2D>();
		calVertex();
	}

	public Polygon(Point2D... points) {
		this.points = Arrays.asList(points);
		calVertex();
	}

	public Polygon(List<Point2D> points) {
		this.points = points;
		calVertex();
	}

	public void addPoint(Point2D p) {
		points.add(p);

		calVertex();
		calBounds();
	}

	public void addPoint(float x, float y) {
		Point2D point = new Point2D(x, y);
		points.add(point);

		calVertex();
		calBounds();
	}

	public void shift(float x, float y) {
		for (int i = 0; i < points.size(); i++) {
			updateX(i, points.get(i).getX() + x);
			updateY(i, points.get(i).getY() + y);
		}
		calBounds();
		calCentroid();
		calVertex();
	}

	private void calBounds() {
		for (int i = 0; i < points.size(); i++) {
			Point2D p = points.get(i);

			if (p.getX() > maxX)
				maxX = p.getX();
			if (p.getX() < minX)
				minX = p.getX();

			if (p.getY() > maxY)
				maxY = p.getY();
			if (p.getY() < minY)
				minY = p.getY();
		}
	}

	private void calVertex() {
		vertex = new double[points.size()][2];
		vertex3D = new double[points.size()][3];
		for (int i = 0; i < points.size(); i++) {
			vertex[i][0] = points.get(i).getX();
			vertex[i][1] = points.get(i).getY();

			vertex3D[i][0] = points.get(i).getX();
			vertex3D[i][1] = points.get(i).getY();
			vertex3D[i][2] = 0;
		}
	}

	public Point2D getCentroid() {
		if (centroid == null)
			calCentroid();
		return centroid;
	}

	/**
	 * @see http://en.wikipedia.org/wiki/Centroid#Centroid_of_polygon
	 */
	private void calCentroid() {
		float signedArea = 0.0f;
		float cx = 0.0f;
		float cy = 0.0f;

		for (int i = 0; i < points.size() - 1; i++) {
			float ix = points.get(i).getX();
			float iy = points.get(i).getY();
			float i1x = points.get((i + 1) % points.size()).getX();
			float i1y = points.get((i + 1) % points.size()).getY();
			float a = (ix * i1y - i1x * iy);

			cx += (ix + i1x) * a;
			cy += (iy + i1y) * a;
			signedArea += a / 2;
		}
		cx /= (6 * signedArea);
		cy /= (6 * signedArea);

		centroid = new Point2D(cx, cy);
	}

	public double[][] getVertex() {
		if (vertex == null)
			calVertex();

		return vertex;
	}

	// Reference:
	// http://introcs.cs.princeton.edu/java/35purple/Polygon.java.html
	public boolean contains(float x, float y) {
		int crossings = 0;
		for (int i = 0; i < points.size() - 1; i++) {
			double slope = (points.get(i + 1).getX() - points.get(i).getX())
					/ (points.get(i + 1).getY() - points.get(i).getY());
			boolean cond1 = (points.get(i).getY() <= y)
					&& (y < points.get(i + 1).getY());
			boolean cond2 = (points.get(i + 1).getY() <= y)
					&& (y < points.get(i).getY());
			boolean cond3 = x < slope * (y - points.get(i).getY())
					+ points.get(i).getX();
			if ((cond1 || cond2) && cond3)
				crossings++;
		}

		double slope = (points.get(0).getX()
				- points.get(points.size() - 1).getX())
				/ (points.get(0).getY() - points.get(points.size() - 1).getY());
		boolean cond1 = (points.get(points.size() - 1).getY() <= y)
				&& (y < points.get(0).getY());
		boolean cond2 = (points.get(0).getY() <= y)
				&& (y < points.get(points.size() - 1).getY());
		boolean cond3 = x < slope * (y - points.get(points.size() - 1).getY())
				+ points.get(points.size() - 1).getX();
		if ((cond1 || cond2) && cond3)
			crossings++;

		return (crossings % 2 != 0);
	}

	public boolean contains(Point2D point) {
		return contains(point.getX(), point.getY());
	}

	@Override
	public boolean contains(Rectangle rec) {
		return contains(new Point2D(rec.getMinX(), rec.getMinY()))
				&& contains(new Point2D(rec.getMinX(), rec.getMaxY()))
				&& contains(new Point2D(rec.getMaxX(), rec.getMinY()))
				&& contains(new Point2D(rec.getMaxX(), rec.getMaxY()));
	}

	public boolean containsBy(Rectangle rec) {
		return rec.contains(new Point2D(minX, minY))
				&& rec.contains(new Point2D(maxX, maxY));
	}

	public boolean contains(Polygon pol) {
		for (int i = 0; i < pol.points.size(); i++)
			if (!contains(pol.points.get(i).getX(), pol.points.get(i).getY()))
				return false;
		return true;
	}

	private void updateX(int index, float x) {
		if (index >= points.size())
			return;
		Point2D p = new Point2D(x, points.get(index).getY());
		points.add(index + 1, p);
		points.remove(index);
	}

	private void updateY(int index, float y) {
		if (index >= points.size())
			return;
		Point2D p = new Point2D(points.get(index).getX(), y);
		points.add(index + 1, p);
		points.remove(index);
	}

	public int getNumPoints() {
		return points.size();
	}

	public ArrayList<Point2D> getIntersectPoints(Rectangle rec) {
		ArrayList<Point2D> intersect_points = new ArrayList<Point2D>();
		Point2D[] points = rec.getPoints();
		LineSegment line1 = new LineSegment(points[0], points[1]);
		LineSegment line2 = new LineSegment(points[0], points[2]);
		LineSegment line3 = new LineSegment(points[1], points[3]);
		LineSegment line4 = new LineSegment(points[2], points[3]);

		intersect_points.addAll(intersects(line1));
		intersect_points.addAll(intersects(line2));
		intersect_points.addAll(intersects(line3));
		intersect_points.addAll(intersects(line4));

		return intersect_points;
	}

	@Override
	public boolean intersects(Rectangle rec) {
		if (getIntersectPoints(rec).size() != 0)
			return true;
		if (contains(rec))
			return true;
		if (containsBy(rec))
			return true;
		return false;
	}

	@Override
	public Rectangle getBound() {
		return new Rectangle(minX, minY, maxX, maxY);
	}

	public ArrayList<Point2D> adjacent(Polygon poly) {
		ArrayList<Point2D> adjacent = new ArrayList<Point2D>();
		int startIndex = 0;
		Point2D pp = poly.getPoints().get(startIndex);
		while (has(points, pp)) {
			startIndex = (startIndex - 1 + poly.getPoints().size())
					% poly.getPoints().size();
			pp = poly.getPoints().get(startIndex);
		}

		for (int i = 0; i < poly.getPoints().size(); i++) {
			int index = (i + startIndex + poly.getPoints().size())
					% poly.getPoints().size();
			Point2D p = poly.getPoints().get(index);
			if (has(points, p) && !has(adjacent, p)) {
				adjacent.add(p);
			}
		}
		return adjacent;
	}

	private boolean has(List<Point2D> points, Point2D p) {
		for (Point2D thisP : points)
			if (thisP.getX() == p.getX() && thisP.getY() == p.getY())
				return true;

		return false;
	}

	public ArrayList<Point2D> intersects(LineSegment line) {
		ArrayList<Point2D> intersectPoints = new ArrayList<Point2D>();
		for (int i = 0; i < points.size(); i++) {
			Point2D p1 = points.get(i);
			Point2D p2;
			if (i != points.size() - 1)
				p2 = points.get(i + 1);
			else
				p2 = points.get(0);
			Point2D p = line.intersectAsLineSegment(new LineSegment(p1, p2));

			if (p != null)
				intersectPoints.add(p);
		}

		return intersectPoints;
	}

	@Override
	public void render() {
	}

	public List<Point2D> getPoints() {
		return points;
	}

	public String toString() {
		String x_info = "x points:";
		String y_info = "y points:";
		for (int i = 0; i < points.size(); i++) {
			x_info += " " + points.get(i).getX();
			y_info += " " + points.get(i).getY();
		}
		return x_info + "\n" + y_info;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Polygon))
			return false;

		Polygon p = (Polygon) o;

		return p.toString().equals(toString());
	}
}
