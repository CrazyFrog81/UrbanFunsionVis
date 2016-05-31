/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class ImageUtil extends Component {
	private static final long serialVersionUID = 7734329719925138443L;
	
	RescaleOp rop;
	float[] colors = { 1f, 1f, 1f, 1f };
	
	BufferedImage image;
	Dimension size = new Dimension();

	public ImageUtil(BufferedImage image) {
		this.image = image;
		size.setSize(image.getWidth(), image.getHeight());
	}

	/**
	 * Drawing an image can allow for more flexibility in processing/editing.
	 */
	protected void paintComponent(Graphics g) {
		// Center image in this component.
		int x = (getWidth() - size.width) / 2;
		int y = (getHeight() - size.height) / 2;
		g.drawImage(image, x, y, this);
	}

	public Dimension getPreferredSize() {
		return size;
	}

	public static void main(String[] args) throws IOException {
		String path = "images/hawk.jpg";
		BufferedImage image = ImageIO.read(new File(path));
		ImageUtil test = new ImageUtil(image);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new JScrollPane(test));
		f.setSize(2000, 200);
		f.setLocation(1000, 1000);
		f.setVisible(true);
		// showIcon(image);
	}

	/**
	 * Easy way to show an image: load it into a JLabel and add the label to a
	 * container in your gui.
	 */
	public static void showIcon(BufferedImage image) {
		ImageIcon icon = new ImageIcon(image);
		JLabel label = new JLabel(icon, JLabel.CENTER);
		JOptionPane.showMessageDialog(null, label, "icon", -1);
	}
	
	public static BufferedImage clipImage(String fileName,
			BufferedImage inputImage, Color backgroundColor) {
		// long time = System.currentTimeMillis();

		int width = inputImage.getWidth();
		int height = inputImage.getHeight();

		int backR = backgroundColor.getRed();
		int backG = backgroundColor.getGreen();
		int backB = backgroundColor.getBlue();

		int[] colors = inputImage.getRGB(0, 0, width, height, null, 0, width);

		int minX = width, maxX = 0, minY = height, maxY = 0;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int color = colors[x + y * width];

				int r = (color >> 16) & 0xFF;
				int g = (color >> 8) & 0xFF;
				int b = (color) & 0xFF;

				if (r != backR || g != backG || b != backB) {
					minX = Math.min(minX, x);
					maxX = Math.max(maxX, x);
					minY = Math.min(minY, y);
					maxY = Math.max(maxY, y);
				}
			}
		}

		// System.out.println(fileName + " " + width + ", " + height + ", " +
		// minX
		// + ", " + minY + ", " + maxX + ", " + maxY);

		int clipLength = Math.max(maxX - minX, maxY - minY);
		int clipMinX, clipMinY, clipMaxX, clipMaxY;

		// X dimension is longer
		if (clipLength == (maxX - minX)) {
			clipMinX = minX;
			clipMaxX = maxX;

			float midY = minY / 2 + maxY / 2;
			clipMinY = (int) Math.max(0, midY - clipLength / 2.0f);
			clipMaxY = (int) Math.min(height - 1, midY + clipLength / 2.0f);
		} else {
			clipMinY = minY;
			clipMaxY = maxY;

			float midX = minX / 2 + maxX / 2;
			clipMinX = (int) Math.max(0, midX - clipLength / 2.0f);
			clipMaxX = (int) Math.min(width - 1, midX + clipLength / 2.0f);
		}

		// System.out.println(width + ", " + height + ", " + clipMinX + ", "
		// + clipMinY + ", " + clipMaxX + ", " + clipMaxY);

		BufferedImage clipImage = inputImage.getSubimage(clipMinX, clipMinY,
				clipMaxX - clipMinX - 1, clipMaxY - clipMinY - 1);

		// long timeDelta = System.currentTimeMillis() - time;
		// System.out
		// .println((timeDelta / 1000.0f) + " seconds for saving screen");
		//
		// LoadAndShow.showIcon(clipImage);

		return clipImage;
	}
}
