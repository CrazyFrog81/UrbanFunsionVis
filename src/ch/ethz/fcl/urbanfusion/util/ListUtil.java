/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ListUtil {
	public static <T> Map<T, ? extends Number> sortByComparator(
			Map<T, ? extends Number> unsortMap, final boolean order) {
		List<Entry<T, ? extends Number>> list = new LinkedList<Entry<T, ? extends Number>>(
				unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<T, ? extends Number>>() {
			public int compare(Entry<T, ? extends Number> o1,
					Entry<T, ? extends Number> o2) {
				if (o1.getValue() instanceof Float) {
					if (order) {
						return ((Float) o1.getValue()).compareTo((Float) o2
								.getValue());
					} else
						return ((Float) o2.getValue()).compareTo((Float) o1
								.getValue());
				} else if (o1.getValue() instanceof Integer) {
					if (order) {
						return ((Integer) o1.getValue()).compareTo((Integer) o2
								.getValue());
					} else
						return ((Integer) o2.getValue()).compareTo((Integer) o1
								.getValue());
				} else {
					if (order) {
						return ((Double) o1.getValue()).compareTo((Double) o2
								.getValue());
					} else {
						return ((Double) o2.getValue()).compareTo((Double) o1
								.getValue());

					}
				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<T, Number> sortedMap = new LinkedHashMap<T, Number>();
		for (Entry<T, ? extends Number> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	public static <T> int[] getIndices(Map<T, Integer> countMap,
			Map<String, Integer> indexMap, T start, String startId, T end,
			String endId) {
		int[] indices = { -1, -1 };

		Integer startCount = countMap.get(start);
		Integer endCount = countMap.get(end);

		if (startCount != null && endCount != null) {
			if (startCount == 1 && endCount == 1) {
				indices[0] = indexMap.get(startId);
				indices[1] = indexMap.get(endId);
			} else {
				int[] startIndices = new int[startCount];
				for (int i = 0; i < startCount; i++) {
					if (i == 0)
						startIndices[i] = indexMap.get(startId);
					else
						startIndices[i] = indexMap.get(startId
								+ StringUtil.SEPERATOR + i);
				}

				int[] endIndices = new int[endCount];
				for (int i = 0; i < endCount; i++) {
					if (i == 0)
						endIndices[i] = indexMap.get(endId);
					else
						endIndices[i] = indexMap.get(endId
								+ StringUtil.SEPERATOR + i);
				}

				for (int i = 0; i < endIndices.length; i++) {
					indices[1] = endIndices[i];
					boolean success = false;
					for (int j = startIndices.length - 1; j >= 0; j--) {
						indices[0] = startIndices[j];
						if (indices[0] < indices[1]) {
							success = true;
							break;
						}
					}
					if (success)
						break;
				}
			}
		}

		return indices;
	}

	/**
	 * 
	 * @param values
	 *            input values
	 * @param gapSize
	 *            minimum gap between local maximums
	 * @return a list of the indices of local maximums
	 */
	public static List<Integer> getLocalMaximum(float[] values, int gapSize) {
		float local_max = -Float.MAX_VALUE;
		List<Integer> local_max_index = new ArrayList<Integer>();

		float global_max = -Float.MAX_VALUE;
		for (int i = 0; i < values.length; i++)
			global_max = Math.max(global_max, values[i]);

		int cur_max_index = 0;

		while (cur_max_index + gapSize < values.length) {
			int i = cur_max_index;
			for (; i <= cur_max_index + gapSize; i++) {
				if (i >= values.length)
					break;

				float cur_value = values[i];

				if (cur_value > local_max) {
					local_max = cur_value;
					cur_max_index = i;
				}
			}

			boolean is_local_max = true;
			for (int j = cur_max_index - gapSize / 2; j <= cur_max_index
					+ gapSize / 2; j++) {
				if (j < 0 || j >= values.length)
					continue;

				if (values[j] > local_max && j != cur_max_index) {
					is_local_max = false;
					break;
				}
			}

			if (is_local_max && local_max >= global_max / 8) {
				int same_local_max_num = 0;
				float cur_max_value = values[cur_max_index];

				while (cur_max_value == values[cur_max_index]
						&& (cur_max_index + same_local_max_num) < (values.length - 1)) {
					same_local_max_num++;

					cur_max_value = values[cur_max_index + same_local_max_num];
				}

				cur_max_index = cur_max_index + same_local_max_num / 2;
				local_max_index.add(cur_max_index);
			}

			local_max = -Float.MAX_VALUE;
			cur_max_index += gapSize;
		}

		return local_max_index;
	}
}
