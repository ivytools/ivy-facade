package com.procippus.ivy.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class PieChart {
	Font font = new Font("Dialog", Font.BOLD, 12);
	Color color = Color.BLACK;
	String text = null;
	
	Color backgroundColor = Color.WHITE;
	PieValue[] slices = null;
	int width = 150;
	int height = 150;
	BufferedImage bi = null;
	Graphics2D g2 = null;
	
	public void init() {
		bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2 = bi.createGraphics();
		g2.setColor(Color.WHITE);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	public void renderText() {
		if (font != null)
			g2.setFont(font);
		g2.setColor(color);
		FontMetrics m = g2.getFontMetrics();
		int left = ((width/2) - (m.stringWidth(text)/2));
		int top = (height/2)+12;
		g2.drawString(text, left, top);
	}
	
	public void render() {
		Rectangle area = new Rectangle(width, height);

	    // Get total value of all slices
	    double total = 0.0D;
	    for (int i=0; i<slices.length; i++) {
	        total += slices[i].getValue();
	    }

	    // Draw each pie slice
	    double curValue = 0.0D;
	    int startAngle = 0;
	    for (int i=0; i<slices.length; i++) {
	        // Compute the start and stop angles
	        startAngle = (int)(curValue * 360 / total);
	        int arcAngle = (int)(slices[i].getValue() * 360 / total);

	        // Ensure that rounding errors do not leave a gap between the first and last slice
	        if (i == slices.length-1) {
	            arcAngle = 360 - startAngle;
	        }

	        // Set the color and draw a filled arc
	        g2.setColor(slices[i].getColor());
	        g2.fillArc(area.x, area.y, area.width, area.height, startAngle, arcAngle);

	        curValue += slices[i].getValue();
	    }
	}
}
