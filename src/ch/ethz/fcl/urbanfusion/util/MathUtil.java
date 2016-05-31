/*
 * Copyright (C) 2010 - 2015 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.util;

public class MathUtil {
	static public final float map(float value, float istart, float istop,
			float ostart, float ostop) {
		return ostart + (ostop - ostart)
				* ((value - istart) / (istop - istart));
	}

	public static final float[] map(float value, float istart, float istop,
			float[] ostart, float[] ostop) {
		float[] output = new float[ostart.length];
		for (int i = 0; i < ostart.length; i++)
			output[i] = map(value, istart, istop, ostart[i], ostop[i]);
		return output;
	}

	/**
	 * 
	 * @param value
	 * @param low_bound
	 * @param up_bound
	 * @param choice
	 *            0 for linear, 1 for sqrt, 2 for log
	 * @return
	 */
	public static final float ratio(float value, float low_bound,
			float up_bound, int choice) {
		if (low_bound == up_bound)
			return 1;

		if (choice == 0)
			return (value - low_bound) / (up_bound - low_bound);
		else if (choice == 1)
			return (float) ((Math.sqrt(value) - Math.sqrt(low_bound)) / (Math
					.sqrt(up_bound) - Math.sqrt(low_bound)));
		else if (choice == 2)
			return (float) ((Math.log(value) - Math.log(low_bound)) / (Math
					.log(up_bound) - Math.log(low_bound)));
		else
			return (value - low_bound) / (up_bound - low_bound);
	}

	public static int get2Fold(int input) {
		int min_power_2 = 1;

		while (min_power_2 < input) {
			min_power_2 *= 2;
		}

		return min_power_2;
	}
	
	public static int binlog(int bits)
	{
		int log = 0;
		if ((bits & 0xffff0000) != 0) {
			bits >>>= 16;
			log = 16;
		}
		if (bits >= 256) {
			bits >>>= 8;
			log += 8;
		}
		if (bits >= 16) {
			bits >>>= 4;
			log += 4;
		}
		if (bits >= 4) {
			bits >>>= 2;
			log += 2;
		}
		return log + (bits >>> 1);
	}

	public static float sum(float[] input) {
		float sum = 0;
		for (int i = 0; i < input.length; i++)
			sum += input[i];

		return sum;
	}

	public static int sum(int[] input) {
		int sum = 0;
		for (int i = 0; i < input.length; i++)
			sum += input[i];

		return sum;
	}

	public static float log_ceiling(int input) {
		int two_pow = 0;
		float i = 0;
		if (input < 1)
			return 0;
		while (two_pow < input) {
			two_pow = (int) Math.pow(2, i);
			i++;
		}
		return i;
	}

	public static int closestInteger(double in) {
		double diff = in - (int) in;
		if (diff > 0.5)
			return (int) in + 1;
		else
			return (int) in;
	}

	public static float distance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
}
