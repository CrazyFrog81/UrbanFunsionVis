/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.model.road.basemap;

public class Node {
	protected String id;
	protected float x;
	protected float y;

	public Node(String id, float x, float y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float distance(Node node) {
		return (float) Math.sqrt(
				(x - node.x) * (x - node.x) + (y - node.y) * (y - node.y));
	}

	@Override
	public String toString() {
		return getId();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Node))
			return false;

		Node n = (Node) o;
		if (id.equals(n.id))
			return true;

		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public String getId() {
		return id;
	}
}
