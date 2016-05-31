/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.util;

public class Changeable<T> {
	public T value;

	public Changeable(T value) {
		this.value = value;
	}

	public String toString() {
		return value.toString();
	}

	public boolean equals(Object other) {
		if (other instanceof Changeable) {
			return value.equals(((Changeable<?>) other).value);
		} else {
			return value.equals(other);
		}
	}

	public int hashCode() {
		return value.hashCode();
	}
}
