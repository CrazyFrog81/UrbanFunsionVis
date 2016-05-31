/*
* Copyright (C) 2010 - 2016 ETH Zurich
* Unauthorized copying of this file, via any medium is strictly prohibited
* Proprietary and confidential
* Written by Zeng Wei (zeng@arch.ethz.ch)
*/

package ch.ethz.fcl.urbanfusion.config;

import ch.fhnw.util.color.RGB;

public interface IColors {
	// MRT and LRT colors
	RGB MRT_NS = new RGB(217, 29, 7);
	RGB MRT_NE = new RGB(143, 17, 162);
	RGB MRT_EW = new RGB(5, 157, 77);
	RGB MRT_CC = new RGB(250, 155, 16);
	RGB LRT = new RGB(115, 133, 112);

	// bus colors
	RGB SBS_RED = new RGB(209, 46, 27);
	RGB SBS_PURPLE = new RGB(92, 3, 102);
}
