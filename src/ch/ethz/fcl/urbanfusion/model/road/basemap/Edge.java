/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.model.road.basemap;

public class Edge {
	private String id;
	private Node start;
	private Node end;

	// ---------------------------------------Constructor----------------------------------
	public Edge(String id, Node start, Node end) {
		this.id = id;

		this.start = start;
		this.end = end;
	}

	// ----------------------------------Function_Methods-------------------------------------
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Edge))
			return false;

		Edge e = (Edge) o;
		if (id.equals(e.id) && start.equals(e.start) && end.equals(e.end))
			return true;

		return false;
	}

	// ---------------------------------Output_Methods----------------------------------------
	public String getId() {
		return id;
	}

	public Node getStart() {
		return start;
	}

	public Node getEnd() {
		return end;
	}

	@Override
	public String toString() {
		return start.getId() + " -> " + end.getId();
	}

	@Override
	public int hashCode() {
		return new String(id + start.getId() + end.getId()).hashCode();
	}
}
