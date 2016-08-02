/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex.object2d;

import java.util.ArrayList;
import java.util.List;

import ch.ethz.fcl.urbanfusion.spatialindex.object3d.Point3D;
import ch.ethz.fcl.urbanfusion.util.StringUtil;

/*
 * Zeng Wei: a line segment contains two 2d points
 */
public class LineSegment implements IObject2D {
	private Point2D start;
	private Point2D end;

	private List<Point2D> subNodeList = null;
	private List<Point2D> tempSubNodeList = null;

	public List<LineSegment> intersect_line_list = null;
	public List<LineSegment> near_line_list = null;

	public LineSegment(Point2D point1, Point2D point2) {
		this.start = new Point2D(point1.getX(), point1.getY());
		this.end = new Point2D(point2.getX(), point2.getY());

		this.intersect_line_list = new ArrayList<LineSegment>();
		this.near_line_list = new ArrayList<LineSegment>();
	}

	public Point2D intersectAsLineSegment(LineSegment line) {
		return intersectAsLineSegment(this, line);
	}

	public float distance() {
		return start.distance(end);
	}

	// returns the intersection point of two line segments
	// reference http://paulbourke.net/geometry/lineline2d/
	public static Point2D intersectAsLineSegment(LineSegment line1,
			LineSegment line2) {
		Point2D p1 = line1.getPoints()[0];
		Point2D p2 = line1.getPoints()[1];
		Point2D p3 = line2.getPoints()[0];
		Point2D p4 = line2.getPoints()[1];
		float numerator12 = (p4.getX() - p3.getX()) * (p1.getY() - p3.getY())
				- (p4.getY() - p3.getY()) * (p1.getX() - p3.getX());
		float denominator12 = (p4.getY() - p3.getY()) * (p2.getX() - p1.getX())
				- (p4.getX() - p3.getX()) * (p2.getY() - p1.getY());

		float numerator34 = (p2.getX() - p1.getX()) * (p1.getY() - p3.getY())
				- (p2.getY() - p1.getY()) * (p1.getX() - p3.getX());
		float denominator34 = (p4.getY() - p3.getY()) * (p2.getX() - p1.getX())
				- (p4.getX() - p3.getX()) * (p2.getY() - p1.getY());

		if (denominator12 == 0 || denominator34 == 0)
			return null;
		else if (numerator12 / denominator12 >= 0
				&& numerator12 / denominator12 < 1
				&& numerator34 / denominator34 >= 0
				&& numerator34 / denominator34 < 1) {
			float u = numerator12 / denominator12;
			float x = p1.getX() + u * (p2.getX() - p1.getX());
			float y = p1.getY() + u * (p2.getY() - p1.getY());
			return new Point2D(x, y);
		}

		return null;
	}

	public Point2D intersectAsVector(LineSegment line) {
		return intersectAsVector(this, line);
	}

	public static Point2D intersectAsVector(LineSegment line1, LineSegment line2) {
		Point2D p1 = line1.getPoints()[0];
		Point2D p2 = line1.getPoints()[1];
		Point2D p3 = line2.getPoints()[0];
		Point2D p4 = line2.getPoints()[1];
		float numerator12 = (p4.getX() - p3.getX()) * (p1.getY() - p3.getY())
				- (p4.getY() - p3.getY()) * (p1.getX() - p3.getX());
		float denominator12 = (p4.getY() - p3.getY()) * (p2.getX() - p1.getX())
				- (p4.getX() - p3.getX()) * (p2.getY() - p1.getY());

		// float numerator34 = (p2.getX() - p1.getX()) * (p1.getY() - p3.getY())
		// - (p2.getY() - p1.getY()) * (p1.getX() - p3.getX());
		// float denominator34 = (p4.getY() - p3.getY()) * (p2.getX() -
		// p1.getX())
		// - (p4.getX() - p3.getX()) * (p2.getY() - p1.getY());

		if (denominator12 == 0)
			return null;

		float u = numerator12 / denominator12;
		float x = p1.getX() + u * (p2.getX() - p1.getX());
		float y = p1.getY() + u * (p2.getY() - p1.getY());
		return new Point2D(x, y);
	}

	public Point2D intersectAsLine(LineSegment line2) {
		Point2D p1 = start;
		Point2D p2 = end;
		Point2D p3 = line2.getPoints()[0];
		Point2D p4 = line2.getPoints()[1];
		float numerator12 = (p4.getX() - p3.getX()) * (p1.getY() - p3.getY())
				- (p4.getY() - p3.getY()) * (p1.getX() - p3.getX());
		float denominator12 = (p4.getY() - p3.getY()) * (p2.getX() - p1.getX())
				- (p4.getX() - p3.getX()) * (p2.getY() - p1.getY());

		float numerator34 = (p2.getX() - p1.getX()) * (p1.getY() - p3.getY())
				- (p2.getY() - p1.getY()) * (p1.getX() - p3.getX());
		float denominator34 = (p4.getY() - p3.getY()) * (p2.getX() - p1.getX())
				- (p4.getX() - p3.getX()) * (p2.getY() - p1.getY());

		if (denominator12 == 0 || denominator34 == 0)
			return null;

		else if (numerator12 / denominator12 >= -50
				&& numerator12 / denominator12 < 50
				&& numerator34 / denominator34 >= -50
				&& numerator34 / denominator34 < 50) {
			float u = numerator12 / denominator12;
			float x = p1.getX() + u * (p2.getX() - p1.getX());
			float y = p1.getY() + u * (p2.getY() - p1.getY());
			return new Point2D(x, y);
		}
		return null;
	}

	// find the 3d pointof p in line p1, p2
	public static Point3D pointIn3D(Point2D p2d, Point3D p1, Point3D p2) {
		if (p2d == null)
			return null;

		float ux = (p2d.getX() - p1.getX()) / (p2.getX() - p1.getX());
		float uy = (p2d.getY() - p1.getY()) / (p2.getY() - p1.getY());

		StringUtil.debug("ux " + ux + " uy " + uy + " difference "
				+ (ux - uy) / ux, false);
		// ux, uy difference is too big, consider not
		// on the same line
		if ((ux - uy) / ux > 0.01)
			return null;

		float z = p1.getZ() + ux * (p2.getZ() - p1.getZ());
		StringUtil.debug(z + "z", false);
		return new Point3D(p2d.getX(), p2d.getY(), z);
	}

	public float distance(Point2D p) {
		float dx = start.getX() - end.getX();
		float dy = start.getY() - end.getY();

		double denominator = Math.sqrt(dx * dx + dy * dy);
		double numerator = Math.abs(dy * p.getX() - dx * p.getY()
				+ start.getX() * end.getY() - start.getY() * end.getX());

		return (float) (numerator / denominator);
	}

	public Point2D[] getPoints() {
		Point2D[] points = { start, end };
		return points;
	}

	public void increaseSubNodes() {
		if (subNodeList == null) {
			subNodeList = new ArrayList<Point2D>();

			subNodeList.add(start);
			subNodeList.add(start.getPointAt(end, 1.0f / 3));
			subNodeList.add(start.getPointAt(end, 2.0f / 3));
			subNodeList.add(end);
		} else {
			List<Point2D> tempNodeList = new ArrayList<Point2D>();
			for (int i = 0; i < subNodeList.size() - 1; i++) {
				tempNodeList.add(subNodeList.get(i));

				Point2D mid = subNodeList.get(i).getPointAt(
						subNodeList.get(i + 1), 0.5f);
				tempNodeList.add(mid);
			}

			tempNodeList.add(end);
			subNodeList = tempNodeList;
		}
	}

	public List<Point2D> getSubNodes() {
		return this.subNodeList;
	}

	public float magnitude() {
		return (float) Math.sqrt(dx() * dx() + dy() * dy());
	}

	public void setTempSubNodes(List<Point2D> subNodes) {
		tempSubNodeList = subNodes;
	}

	public void finalizeSubNodes() {
		subNodeList = tempSubNodeList;
		tempSubNodeList = null;
	}

	public double compatibility(LineSegment other) {
		return angleComp(other) * posComp(other) * scaleComp(other)
				* visComp(other);
	}

	private double angleComp(LineSegment other) {
		double angle = Math.cos(Math.acos(Math.abs((dx() * other.dx() + dy()
				* other.dy())
				/ (magnitude() * other.magnitude()))));
		return angle;
	}

	private double posComp(LineSegment other) {
		LineSegment bridge = new LineSegment(midPoint(), other.midPoint());
		double lavg = (magnitude() + other.magnitude()) / 2;
		double pos = lavg / (lavg + bridge.magnitude());

		return pos;
	}

	private double scaleComp(LineSegment other) {
		double lavg = (magnitude() + other.magnitude()) / 2;
		double scale = 2 / (lavg / Math.min(magnitude(), other.magnitude()) + Math
				.max(magnitude(), other.magnitude()) / lavg);
		return scale;
	}

	private double visComp(LineSegment other) {
		return Math.min(visibility(other), other.visibility(this));
	}

	public double visibility(LineSegment other) {
		Point2D ds = new Point2D(other.getStart().getX() + dy(), other
				.getStart().getY() - dx());
		Point2D de = new Point2D(other.getEnd().getX() + dy(), other.getEnd()
				.getY() - dx());

		Point2D is = this.intersectAsVector(new LineSegment(other.getStart(),
				ds));
		Point2D ie = this
				.intersectAsVector(new LineSegment(other.getEnd(), de));

		if (is == null || ie == null)
			return 0;

		Point2D im = is.getPointAt(ie, 0.5f);
		if (im == null)
			return 0;

		return Math.max(0, 1 - (2 * midPoint().distance(im)) / is.distance(ie));
	}

	public float dy() {
		return end.getY() - start.getY();
	}

	public float dx() {
		return end.getX() - start.getX();
	}

	public Point2D getStart() {
		return start;
	}

	public Point2D getEnd() {
		return end;
	}

	public Point2D midPoint() {
		return start.getPointAt(end, 0.5f);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof LineSegment))
			return false;

		LineSegment other = (LineSegment) o;
		return other.getStart().equals(start) && other.getEnd().equals(end);
	}

	@Override
	public boolean intersects(Rectangle rec) {
		if (!getBound().intersects(rec))
			return false;
		else {
			Point2D p1 = new Point2D(rec.getMinX(), rec.getMinY());
			Point2D p2 = new Point2D(rec.getMinX(), rec.getMaxY());
			Point2D p3 = new Point2D(rec.getMaxX(), rec.getMinY());
			Point2D p4 = new Point2D(rec.getMaxX(), rec.getMaxY());

			return intersectAsLineSegment(new LineSegment(p1, p2)) != null
					|| intersectAsLineSegment(new LineSegment(p1, p3)) != null
					|| intersectAsLineSegment(new LineSegment(p2, p4)) != null
					|| intersectAsLineSegment(new LineSegment(p3, p4)) != null;
		}
	}

	@Override
	public boolean contains(Rectangle rec) {
		return false;
	}

	@Override
	public Rectangle getBound() {
		return new Rectangle(start, end);
	}

	@Override
	public boolean contains(Point2D point) {
		return false;
	}
}