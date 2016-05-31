/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex.object2d;

import javax.vecmath.Vector2d;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.GLU;

import ch.ethz.fcl.urbanfusion.spatialindex.object3d.Point3D;

public class Circle implements IObject2D {
	private Point2D center;
	private float radius;

	public Circle(Point2D center, float radius) {
		this.center = center;
		this.radius = radius;
	}

	public Circle(float centerX, float centerY, float radius) {
		center = new Point2D(centerX, centerY);
		this.radius = radius;
	}

	public boolean contains(Point2D p) {
		double centerDistance = Math.pow(center.getX() - p.getX(), 2)
				+ Math.pow(center.getY() - p.getY(), 2);
		return centerDistance < Math.pow(radius, 2);
	}

	public boolean contains(float x, float y) {
		double centerDistance = Math.pow(center.getX() - x, 2)
				+ Math.pow(center.getY() - y, 2);
		return centerDistance < Math.pow(radius, 2);
	}

	public boolean contains(Rectangle r) {
		return contains(r.getMinX(), r.getMinY())
				&& contains(r.getMinX(), r.getMaxY())
				&& contains(r.getMaxX(), r.getMinY())
				&& contains(r.getMaxX(), r.getMaxY());
	}

	public Point2D intersect(LineSegment line) {
		Point2D start = line.getPoints()[0];
		Point2D end = line.getPoints()[1];
		Vector2d d = new Vector2d(end.getX() - start.getX(), end.getY()
				- start.getY());
		Vector2d f = new Vector2d(start.getX() - center.getX(), start.getY()
				- center.getY());

		// t2 * (d DOT d) + 2t*( f DOT d ) + ( f DOT f - r2 ) = 0

		float a = (float) d.dot(d);
		float b = (float) f.dot(d) * 2;
		float c = (float) f.dot(f) - radius * radius;

		float discriminant = b * b - 4 * a * c;
		if (discriminant < 0)
			return null;
		else {
			// ray didn't totally miss sphere,
			// so there is a solution to
			// the equation.

			discriminant = (float) Math.sqrt(discriminant);
			// either solution may be on or off the ray so need to test both
			float t1 = (-b + discriminant) / (2 * a);
			float t2 = (-b - discriminant) / (2 * a);

			if (t1 >= 0 && t1 <= 1) { // t1 in range
				float t = t1;
				if (t2 >= 0 && t2 <= 1) { // t2 in range
					if (t2 > t1)
						t = t2;
				}

				float x = start.getX() + (float) d.x * t;
				float y = start.getY() + (float) d.y * t;

				return new Point2D(x, y);
			} else { // t1 "out of range"
				if (t2 >= 0 && t2 <= 1) {
					float x = start.getX() + (float) d.x * t2;
					float y = start.getY() + (float) d.y * t2;

					return new Point2D(x, y);
				}
			}

			return null;
		}
	}

	public void shift(float x, float y) {
		center = new Point2D(center.getX() + x, center.getY() + y);
	}

	public float[] intersectLine(Point3D p1, Point3D p2) {
		double a = Math.pow(p1.getX() - p2.getX(), 2)
				+ Math.pow(p1.getY() - p2.getY(), 2);
		double b = 2 * (p1.getX() - p2.getX())
				* (p2.getX() - this.center.getX()) + 2
				* (p1.getY() - p2.getY()) * (p2.getY() - this.center.getY());
		double c = Math.pow(p2.getX() - this.center.getX(), 2)
				+ Math.pow(p2.getY() - this.center.getY(), 2)
				- Math.pow(radius, 2);

		double discriminant = Math.pow(b, 2) - 4 * a * c;

		if (discriminant > 0) {
			float[] results = {
					(float) ((-b - Math.sqrt(discriminant)) / (2 * a)),
					(float) ((-b + Math.sqrt(discriminant)) / (2 * a)) };
			return results;
		}

		else
			return null;
	}

	@Override
	public Rectangle getBound() {
		Rectangle rec = new Rectangle(center.getX() - radius, center.getY()
				- radius, center.getX() + radius, center.getY() + radius);
		return rec;
	}

	@Override
	public boolean intersects(Rectangle object) {
		return object.intersects(this);
	}

	public Point2D getCenter() {
		return center;
	}

	public float getRadius() {
		return this.radius;
	}

	@Override
	public void render() {
		Disk disk = new Disk();

		int slices = 64;
		int loops = 32;

		GL11.glLineWidth(1.2f);
		GL11.glPushMatrix();
		GL11.glTranslatef(getCenter().getX(), getCenter().getY(), 0);

		disk.setDrawStyle(GLU.GLU_SILHOUETTE);
		disk.draw(0, getRadius(), slices, loops);
		disk.setDrawStyle(GLU.GLU_FILL);
		disk.draw(0, getRadius(), slices, loops);

		GL11.glPopMatrix();
	}

	public void rendInter(Circle other, float width) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

		float y_diff = other.center.getY() - this.center.getY();
		float x_diff = other.center.getX() - this.center.getX();

		float angle = (float) Math.atan(y_diff / x_diff);

		float this_x = 0, this_y = 0, other_x = 0, other_y = 0;
		if (this.radius >= other.radius) {
			this_x = this.center.getX() - this.radius / 2
					* (float) Math.sin(angle);
			this_y = this.center.getY() + this.radius / 2
					* (float) Math.cos(angle);
			other_x = other.center.getX() - other.radius / 2
					* (float) Math.sin(angle);
			other_y = other.center.getY() + other.radius / 2
					* (float) Math.cos(angle);
		} else {
			this_x = this.center.getX() + this.radius / 2
					* (float) Math.sin(angle);
			this_y = this.center.getY() - this.radius / 2
					* (float) Math.cos(angle);
			other_x = other.center.getX() + other.radius / 2
					* (float) Math.sin(angle);
			other_y = other.center.getY() - other.radius / 2
					* (float) Math.cos(angle);
		}

		// GL11.glBegin(GL11.GL_POLYGON);
		// GL11.glVertex3f(this.center.getX(), this.center.getY(), 0);
		// GL11.glVertex3f(this_x, this_y, 0);
		// GL11.glVertex3f(other_x, other_y, 0);
		// GL11.glVertex3f(other.center.getX(), other.center.getY(), 0);
		// GL11.glEnd();

		GL11.glLineWidth(width);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(this_x, this_y, 0);
		GL11.glVertex3f(other_x, other_y, 0);

		GL11.glEnd();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
}
