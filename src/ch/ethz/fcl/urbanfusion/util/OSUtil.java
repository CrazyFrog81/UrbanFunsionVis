/*
 * Copyright (C) 2010 - 2015 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.util;

public class OSUtil {
	private static String OS;

	public static boolean isWindows() {
		OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("mac") >= 0);
	}

	public static void memoryUsage() {
		System.out.println(Runtime.getRuntime().totalMemory() / 1024 / 1024
				+ " MB total, " + Runtime.getRuntime().freeMemory() / 1024
				/ 1024 + " MB free");
	}

}
