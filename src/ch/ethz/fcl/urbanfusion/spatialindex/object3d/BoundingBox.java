/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.spatialindex.object3d;

import java.nio.FloatBuffer;

import ch.ethz.fcl.urbanfusion.spatialindex.object2d.Rectangle;

public class BoundingBox implements IObject3D {
	private float minX;
	private float minY;
	private float minZ;
	private float maxX;
	private float maxY;
	private float maxZ;

	public BoundingBox() {

	}

	public BoundingBox(float x, float y, float t) {
		minX = maxX = x;
		minY = maxY = y;
		minZ = maxZ = t;
	}

	public BoundingBox(Rectangle rec, float minZ, float maxZ) {
		minX = rec.getMinX();
		minY = rec.getMinY();
		maxX = rec.getMaxX();
		maxY = rec.getMaxY();
		this.minZ = minZ;
		this.maxZ = maxZ;
	}

	public BoundingBox(Point3D p) {
		minX = maxX = p.getX();
		minY = maxY = p.getY();
		minZ = maxZ = p.getZ();
	}

	public BoundingBox(float minX, float minY, float minZ, float maxX,
			float maxY, float maxZ) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;
	}

	public float[] getAttributes() {
		float[] attributes = { minX, maxX, minY, maxY, minZ, maxZ };
		return attributes;
	}

	public void setMinX(float minX) {
		this.minX = minX;
	}

	public void setMaxX(float maxX) {
		this.maxX = maxX;
	}

	public void setMinY(float minY) {
		this.minY = minY;
	}

	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}

	public void setMinZ(float minZ) {
		this.minZ = minZ;
	}

	public void setMaxZ(float maxZ) {
		this.maxZ = maxZ;
	}

	public void set(float minX, float minY, float minZ, float maxX, float maxY,
			float maxZ) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;
	}

	public void copy(BoundingBox another) {
		this.minX = another.minX;
		this.maxX = another.maxX;
		this.minY = another.minY;
		this.maxY = another.maxY;
		this.minZ = another.minZ;
		this.maxZ = another.maxZ;
	}

	public boolean contains(Point3D point) {
		if (point.getX() >= minX && point.getX() <= maxX
				&& point.getY() >= minY && point.getY() <= maxY
				&& point.getZ() >= minZ && point.getZ() <= maxZ)
			return true;
		return false;
	}

	@Override
	public boolean contains(BoundingBox bb) {
		if (maxX >= bb.maxX && maxY >= bb.maxY && maxZ >= bb.maxZ
				&& minX <= bb.minX && minY <= bb.minY && minZ <= bb.minZ)
			return true;
		return false;
	}

	public static boolean contains(float bb1MinX, float bb1MinY, float bb1MinZ,
			float bb1MaxX, float bb1MaxY, float bb1MaxZ, float bb2MinX,
			float bb2MinY, float bb2MinZ, float bb2MaxX, float bb2MaxY,
			float bb2MaxZ) {
		return bb1MinX < bb2MinX && bb1MinY < bb2MinY && bb1MinZ < bb2MinZ
				&& bb1MaxX > bb2MaxX && bb1MaxY > bb2MaxY && bb1MaxZ > bb2MaxZ;
	}

	public boolean containedBy(BoundingBox bb) {
		if (maxX <= bb.maxX && maxY <= bb.maxY && maxZ <= bb.maxZ
				&& minX >= bb.minX && minY >= bb.minY && minZ >= bb.minZ)
			return true;
		return false;
	}

	@Override
	public boolean intersects(BoundingBox bb) {
		if (contains(bb) || containedBy(bb))
			return false;
		return maxX >= bb.minX && maxY >= bb.minY && maxZ >= bb.minZ
				&& minX <= bb.maxX && minY <= bb.maxY && minZ <= bb.maxZ;
	}

	public boolean intersect(Cylinder cy) {
		return cy.intersects(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean equals = false;
		if (o instanceof BoundingBox) {
			BoundingBox bb = (BoundingBox) o;
			if (minX == bb.minX && minY == bb.minY && minZ == bb.minZ
					&& maxX == bb.maxX && maxY == bb.maxY && maxZ == bb.maxZ) {
				equals = true;
			}
		}
		return equals;
	}

	public void add(float x, float y, float z) {
		if (x < minX)
			minX = x;
		if (x > maxX)
			maxX = x;
		if (y < minY)
			minY = y;
		if (y > maxY)
			maxY = y;
		if (z < minZ)
			minZ = z;
		if (z > maxZ)
			maxZ = z;
	}

	public void add(Point3D p) {
		if (p.getX() < minX)
			minX = p.getX();
		if (p.getX() > maxX)
			maxX = p.getX();
		if (p.getY() < minY)
			minY = p.getY();
		if (p.getY() > maxY)
			maxY = p.getY();
		if (p.getZ() < minZ)
			minZ = p.getZ();
		if (p.getZ() > maxZ)
			maxZ = p.getZ();
	}

	public void add(BoundingBox bb) {
		if (bb.minX < minX)
			this.minX = bb.minX;
		if (bb.maxX > maxX)
			this.maxX = bb.maxX;
		if (bb.minY < minY)
			this.minY = bb.minY;
		if (bb.maxY > maxY)
			this.maxY = bb.maxY;
		if (bb.minZ < minZ)
			this.minZ = bb.minZ;
		if (bb.maxZ > maxZ)
			this.maxZ = bb.maxZ;
	}

	public static float enlargement(BoundingBox bb1, BoundingBox bb2) {
		return enlargement(bb1.minX, bb1.minY, bb1.minZ, bb1.maxX, bb1.maxY,
				bb1.maxZ, bb2.minX, bb2.minY, bb2.minZ, bb2.maxX, bb2.maxY,
				bb2.maxZ);
	}

	public static float enlargement(float bb1MinX, float bb1MinY,
			float bb1MinZ, float bb1MaxX, float bb1MaxY, float bb1MaxZ,
			float bb2MinX, float bb2MinY, float bb2MinZ, float bb2MaxX,
			float bb2MaxY, float bb2MaxZ) {
		float bb1Volume = (bb1MaxX - bb1MinX) * (bb1MaxY - bb1MinY)
				* (bb1MaxZ - bb1MinZ);

		if (bb1Volume == Float.POSITIVE_INFINITY)
			return 0;

		float bb1bb2UnionVolume = (Math.max(bb1MaxX, bb2MaxX) - Math.min(
				bb1MinX, bb2MinX))
				* (Math.max(bb1MaxY, bb2MaxY) - Math.min(bb1MinY, bb2MinY))
				* (Math.max(bb1MaxZ, bb2MaxZ) - Math.min(bb1MinZ, bb2MinZ));

		if (bb1bb2UnionVolume == Float.POSITIVE_INFINITY)
			return Float.POSITIVE_INFINITY;

		return bb1bb2UnionVolume - bb1Volume;
	}

	public static float volume(float bbMinX, float bbMinY, float bbMinZ,
			float bbMaxX, float bbMaxY, float bbMaxZ) {
		return (bbMaxX - bbMinX) * (bbMaxY - bbMinY) * (bbMaxZ - bbMinZ);
	}

	public float volume() {
		return getHeight() * getWidth() * getDepth();
	}

	public float getHeight() {
		return maxZ - minZ;
	}

	public float getWidth() {
		return maxX - minX;
	}

	public float getDepth() {
		return maxY - minY;
	}

	@Override
	public String toString() {
		return "(" + minX + ", " + minY + ", " + minZ + ", " + maxX + ", "
				+ maxY + ", " + maxZ + ")";
	}

	@Override
	public BoundingBox getBoundingBox() {
		return this;
	}

	public static void bufferBoundingBox(FloatBuffer buffer, float[] attributes) {
		buffer.put(attributes[0]);
		buffer.put(attributes[2]);
		buffer.put(attributes[4]);

		buffer.put(attributes[1]);
		buffer.put(attributes[2]);
		buffer.put(attributes[4]);

		buffer.put(attributes[1]);
		buffer.put(attributes[2]);
		buffer.put(attributes[4]);

		buffer.put(attributes[1]);
		buffer.put(attributes[2]);
		buffer.put(attributes[5]);

		buffer.put(attributes[1]);
		buffer.put(attributes[2]);
		buffer.put(attributes[5]);

		buffer.put(attributes[0]);
		buffer.put(attributes[2]);
		buffer.put(attributes[5]);

		buffer.put(attributes[0]);
		buffer.put(attributes[2]);
		buffer.put(attributes[4]);

		buffer.put(attributes[0]);
		buffer.put(attributes[2]);
		buffer.put(attributes[5]);

		buffer.put(attributes[0]);
		buffer.put(attributes[3]);
		buffer.put(attributes[5]);

		buffer.put(attributes[1]);
		buffer.put(attributes[3]);
		buffer.put(attributes[5]);

		buffer.put(attributes[1]);
		buffer.put(attributes[3]);
		buffer.put(attributes[4]);

		buffer.put(attributes[0]);
		buffer.put(attributes[3]);
		buffer.put(attributes[4]);

		buffer.put(attributes[1]);
		buffer.put(attributes[3]);
		buffer.put(attributes[4]);

		buffer.put(attributes[1]);
		buffer.put(attributes[3]);
		buffer.put(attributes[5]);

		buffer.put(attributes[0]);
		buffer.put(attributes[2]);
		buffer.put(attributes[5]);

		buffer.put(attributes[0]);
		buffer.put(attributes[3]);
		buffer.put(attributes[5]);

		buffer.put(attributes[0]);
		buffer.put(attributes[2]);
		buffer.put(attributes[4]);

		buffer.put(attributes[0]);
		buffer.put(attributes[3]);
		buffer.put(attributes[4]);

		buffer.put(attributes[0]);
		buffer.put(attributes[3]);
		buffer.put(attributes[5]);

		buffer.put(attributes[0]);
		buffer.put(attributes[3]);
		buffer.put(attributes[4]);

		buffer.put(attributes[1]);
		buffer.put(attributes[2]);
		buffer.put(attributes[5]);

		buffer.put(attributes[1]);
		buffer.put(attributes[3]);
		buffer.put(attributes[5]);

		buffer.put(attributes[1]);
		buffer.put(attributes[3]);
		buffer.put(attributes[4]);

		buffer.put(attributes[1]);
		buffer.put(attributes[2]);
		buffer.put(attributes[4]);
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

	public float getMinZ() {
		return minZ;
	}

	public float getMaxZ() {
		return maxZ;
	}

}
