/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex.object3d;

import org.lwjgl.opengl.GL11;

public class Point3D implements IObject3D {
	private float x;
	private float y;
	private float z;

	public Point3D() {
	}

	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D(Point3D p) {
		this.x = p.getX();
		this.y = p.getY();
		this.z = p.getZ();

	}

	public float distance(Point3D p) {
		double square = Math.pow(this.x - p.getX(), 2)
				+ Math.pow(this.y - p.getY(), 2)
				+ Math.pow(this.z - p.getZ(), 2);
		return (float) Math.sqrt(square);
	}

	public float xydistance(Point3D p) {
		double square = Math.pow(this.x - p.getX(), 2)
				+ Math.pow(this.y - p.getY(), 2);
		return (float) Math.sqrt(square);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setZ(float z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public void set(float a, float b, float c) {
		this.x = a;
		this.y = b;
		this.z = c;

	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(x, y, z);
	}

	@Override
	public boolean intersects(BoundingBox bb) {
		return false;
	}

	@Override
	public boolean contains(BoundingBox bb) {
		return false;
	}

	@Override
	public void render() {
		GL11.glVertex3f(x, y, z);
	}
}
