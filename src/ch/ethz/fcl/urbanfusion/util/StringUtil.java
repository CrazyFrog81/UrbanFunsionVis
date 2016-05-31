/*
 * Copyright (C) 2010 - 2015 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.util;

import java.text.DecimalFormat;
import java.text.MessageFormat;

public class StringUtil {
	public static final String SEPERATOR = "+";
	public static final String SEPERATOR_SPLITER = "\\+";

	// check if input string is numeric
	public static boolean isNumeric(String str) {
		// remove first 0s
		str = str.replaceFirst("^0+(?!$)", "");
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static String numToPercentage(float number) {
		return MessageFormat.format("{0,number,#.##%}", number);
	}

	public static String formatNum(int value) {
		DecimalFormat myFormatter = new DecimalFormat("###,###");
		String output = myFormatter.format(value);

		return output;
	}

	public static String formatNum(String format, float value) {
		DecimalFormat myFormatter = new DecimalFormat(format);
		String output = myFormatter.format(value);

		return output;
	}

	public static void debug(String s, boolean b) {
		if (b) {
			System.out.println("DEBUG: " + s);
		}
	}

	public static void print(float[] in, boolean print) {
		if (!print)
			return;
		float sum = 0;
		for (int i = 0; i < in.length; i++) {
			System.out.print(in[i] + " ");
			sum += in[i];
		}
		System.out.println("sum " + sum);
		System.out.println();
	}

	public static void print(int[] in, boolean print) {
		if (!print)
			return;
		int sum = 0;
		for (int i = 0; i < in.length; i++) {
			System.out.print(in[i] + " ");
			sum += in[i];
		}
		System.out.println("sum " + sum);
		System.out.println();
	}
}
