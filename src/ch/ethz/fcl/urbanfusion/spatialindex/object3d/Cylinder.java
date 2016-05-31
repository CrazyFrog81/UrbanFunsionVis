/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex.object3d;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.GLU;

import ch.ethz.fcl.urbanfusion.spatialindex.object2d.Circle;
import ch.ethz.fcl.urbanfusion.spatialindex.object2d.Point2D;
import ch.ethz.fcl.urbanfusion.spatialindex.object2d.Rectangle;

public class Cylinder implements IObject3D {
	private Point3D center;
	private float radius;
	private float height;

	public Cylinder() {
		this.center = new Point3D(0.4f, 0.2f, 0);
		this.radius = 0.2f;
		this.height = 1.0f;
	}

	public Cylinder(Point3D center, float radius, float height) {
		this.center = new Point3D(center);
		this.radius = radius;
		this.height = height;
	}

	public boolean containPoint(Point3D p) {
		return p.getZ() >= center.getZ() && p.getZ() <= center.getZ() + height
				&& center.xydistance(p) <= radius;
	}


	@Override
	public boolean contains(BoundingBox bb) {
		Circle xy = new Circle(center.getX(), center.getY(), radius);
		Rectangle xz = new Rectangle(center.getX() - radius, center.getZ(),
				center.getX() + radius, center.getZ() + height);
		Rectangle yz = new Rectangle(center.getY() - radius, center.getZ(),
				center.getY() + radius, center.getZ() + height);

		return xy.contains(new Rectangle(bb.getMinX(), bb.getMinY(), bb
				.getMaxX(), bb.getMaxY()))
				&& xz.contains(new Rectangle(bb.getMinX(), bb.getMinZ(), bb
						.getMaxX(), bb.getMaxZ()))
				&& yz.contains(new Rectangle(bb.getMinY(), bb.getMinZ(), bb
						.getMaxY(), bb.getMaxZ()));
	}

	@Override
	public boolean intersects(BoundingBox bb) {
		Rectangle xy = new Rectangle(bb.getMinX(), bb.getMinY(), bb.getMaxX(),
				bb.getMaxY());
		Rectangle xz = new Rectangle(bb.getMinX(), bb.getMinZ(), bb.getMaxX(),
				bb.getMaxZ());
		Rectangle yz = new Rectangle(bb.getMinY(), bb.getMinZ(), bb.getMaxY(),
				bb.getMaxZ());
		Circle circle = new Circle(center.getX(), center.getY(), radius);

		return xy.intersects(circle)
				&& xz.intersects(new Rectangle(center.getX() - radius, center
						.getZ(), center.getX() + radius, center.getZ() + height))
				&& yz.intersects(new Rectangle(center.getY() - radius, center
						.getZ(), center.getY() + radius, center.getZ() + height));
	}

	public ArrayList<Point3D> intersectLineSegment(Point3D p1, Point3D p2) {
		Circle xy = new Circle(center.getX(), center.getY(), radius);
		Rectangle xz = new Rectangle(center.getX() - radius, center.getZ(),
				center.getX() + radius, center.getZ() + height);
		Rectangle yz = new Rectangle(center.getY() - radius, center.getZ(),
				center.getY() + radius, center.getZ() + height);

		ArrayList<Point3D> points = new ArrayList<Point3D>();

		if (xy.intersectLine(p1, p2) != null) {
			float[] t = xy.intersectLine(p1, p2);
			for (int i = 0; i < 2; i++)
				if (t[i] >= 0 && t[i] <= 1) {
					float intersectX = (p1.getX() - p2.getX()) * t[i]
							+ p2.getX();
					float intersectY = (p1.getY() - p2.getY()) * t[i]
							+ p2.getY();
					float intersectZ = (p1.getZ() - p2.getZ()) * t[i]
							+ p2.getZ();
					if (xz.contains(intersectX, intersectZ)
							&& yz.contains(intersectY, intersectZ))
						points.add(new Point3D(intersectX, intersectY,
								intersectZ));
				}
		}

		float[] t = xz.intersectLine(new Point2D(p1.getX(), p1.getZ()),
				new Point2D(p2.getX(), p2.getZ()));
		if (t != null) {
			for (int i = 0; i < 2; i++)
				if (t[i] >= 0 && t[i] <= 1) {
					float intersectX = (p1.getX() - p2.getX()) * t[i]
							+ p2.getX();
					float intersectY = (p1.getY() - p2.getY()) * t[i]
							+ p2.getY();
					float intersectZ = (p1.getZ() - p2.getZ()) * t[i]
							+ p2.getZ();
					if (xy.contains(intersectX, intersectY)
							&& yz.contains(intersectY, intersectZ))
						points.add(new Point3D(intersectX, intersectY,
								intersectZ));
				}
		}

		t = yz.intersectLine(new Point2D(p1.getY(), p1.getZ()),
				new Point2D(p2.getY(), p2.getZ()));
		if (t != null) {
			for (int i = 0; i < 2; i++)
				if (t[i] >= 0 && t[i] <= 1) {
					float intersectX = (p1.getX() - p2.getX()) * t[i]
							+ p2.getX();
					float intersectY = (p1.getY() - p2.getY()) * t[i]
							+ p2.getY();
					float intersectZ = (p1.getZ() - p2.getZ()) * t[i]
							+ p2.getZ();
					if (xy.contains(intersectX, intersectY)
							&& xz.contains(intersectX, intersectZ))
						points.add(new Point3D(intersectX, intersectY,
								intersectZ));
				}
		}
		return points;
	}

	@Override
	public String toString() {
		return "Center Point (" + center.getX() + ", " + center.getY() + ", "
				+ center.getZ() + ") Radius" + radius + " Height" + height;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(center.getX() - radius, center.getY() - radius,
				center.getZ() - height, center.getX() + radius, center.getY()
						+ radius, center.getZ() + height);
	}

	public float getRadius() {
		return this.radius;
	}

	public float getCenterX() {
		return center.getX();
	}

	public float getCenterY() {
		return center.getY();
	}

	public float getHeight() {
		return this.height;
	}

	public float getCenterZ() {
		return center.getZ();
	}

	public void set(float x, float y) {
		center.set(x, y, center.getZ());
	}

	public void setRadius(float r) {
		this.radius = r;
	}

	public void set(float x, float y, float z) {
		center.set(x, y, z);
	}

	public void setHeight(float h) {
		this.height = h;
	}

	public boolean inHeightDomain(float start, float end) {
		if (getCenterZ() < end && (getCenterZ() + height) >= start)
			return true;
		return false;
	}

	@Override
	public void render() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		Disk disk = new Disk();
		Disk disk2 = new Disk();
		GL11.glPushMatrix();
		
		int slices = 64;
		int stacks = 32;
		int loops = 32;
		float r = 0.5f;
		float g = 0.75f;
		float b = 1.0f;
		float r2 = 0.5f * 0.5f;
		float g2 = 0.5f * 0.75f;
		float b2 = 0.5f * 1.0f;

		GL11.glLineWidth(1.2f);
		GL11.glPushMatrix();
		GL11.glTranslatef(getCenterX(), getCenterY(), getCenterZ());
		// GL11.glRotatef(-90, 1, 0, 0);

		GL11.glColor4f(r, g, b, 0.2f);
		disk.setDrawStyle(GLU.GLU_FILL);
		disk.draw(0, getRadius(), slices, loops);
		GL11.glColor4f(r2, g2, b2, 1f);
		disk.setDrawStyle(GLU.GLU_SILHOUETTE);
		disk.draw(0, getRadius(), slices, loops);

		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, getHeight());
		GL11.glColor4f(r, g, b, 0.2f);
		disk2.setDrawStyle(GLU.GLU_FILL);
		disk2.draw(0, getRadius(), slices, loops);
		GL11.glColor4f(r2, g2, b2, 1f);
		disk2.setDrawStyle(GLU.GLU_SILHOUETTE);
		disk2.draw(0, getRadius(), slices, loops);
		GL11.glPopMatrix();

		if (true) {
			org.lwjgl.util.glu.Cylinder cylinder = new org.lwjgl.util.glu.Cylinder();
			GL11.glColor4f(r, g, b, 0.1f);
			
			cylinder.setDrawStyle(GLU.GLU_FILL);
			cylinder.draw(getRadius(), getRadius(), getHeight(),
					slices, stacks);
		}

		GL11.glPopMatrix();
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
	}
}
