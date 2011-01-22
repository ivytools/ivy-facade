package com.procippus.ivy.util;
/*
 * 
 * Copyright 2011 Procippus, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.procippus.ivy.model.Dependency;
import com.procippus.ivy.model.Module;
import com.procippus.ivy.model.PieValue;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness
 */
public class GraphicUtil {
	static final int BASE_SIXTEEN = 16;
	static final int MARGIN = 10;
	static final int NODE_RADIUS = 10;
	
	public static final Color RED = new Color(parseInt("AE"), parseInt("1E"), parseInt("1E"));
	public static final Color BLUE_GRAY = new Color(parseInt("C3"), parseInt("D5"), parseInt("CB"));
	public static final Color DARK_BLUE = new Color(parseInt("4A"), parseInt("6B"), parseInt("6E"));
	public static final Color BAIGE = new Color(parseInt("FF"), parseInt("FF"), parseInt("CC"));
	public static final Color GREEN = new Color(parseInt("81"), parseInt("A6"), parseInt("1A"));
	
	static final float F_TEN = 10.0f;
	static final float F_ONE = 1.0f;
	static final float F_ZERO = 0.0f;
	
	static final float dash1[] = {10.0f};
	static final BasicStroke DASHED = new BasicStroke(F_ONE, 
	                                          BasicStroke.CAP_BUTT, 
	                                          BasicStroke.JOIN_MITER, 
	                                          F_TEN, dash1, F_ZERO);
	
	
	static final BasicStroke SOLID = new BasicStroke(F_ONE, 
                BasicStroke.CAP_BUTT, 
                BasicStroke.JOIN_MITER, 
                F_TEN);
	
	static final String FONT_NAME = "Dialog";
	
	static final Font FONT_NORMAL = new Font(FONT_NAME, Font.PLAIN, 12);
	static final Font FONT_SMALL = new Font(FONT_NAME, Font.PLAIN, 8);
	
	private static int parseInt(String s) {
		return Integer.parseInt(s, BASE_SIXTEEN);
	}
	
	public static void addCenterTextToPieChart(BufferedImage bi, String text, Color c, int w, int h) {
		Graphics2D g2 = bi.createGraphics();
		g2.setFont(new Font(FONT_NAME, Font.BOLD, 12));
		g2.setColor(c);
		FontMetrics m = g2.getFontMetrics();
		

		int left = (int)((w/2) - (m.stringWidth(text)/2));
		int top = (int)(h/2)+12; // - (m.getHeight()/2));
		g2.drawString(text, left, top);
	}
	
	public static BufferedImage drawPie(PieValue[] slices, int w, int h) {
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bi.createGraphics();
		g2.setColor(Color.WHITE);
		
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Rectangle area = new Rectangle(w, h);
		
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
	    return bi;
	}
	
	public static String writeImageToBase64(BufferedImage bi) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, PropertiesUtil.getValue("graphics.type"), baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bytes = baos.toByteArray();
		return Base64.encodeBase64String(bytes);
	}
	
	public static void writeImage(File directory, String name, BufferedImage bi) {
		try {
			File outputfile = new File(directory.getPath() + File.separatorChar + name + PropertiesUtil.getValue("graphics.extension"));
		    ImageIO.write(bi, PropertiesUtil.getValue("graphics.type"), outputfile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int drawNode(Graphics2D g2, String org, String name, int offset, int row, int centerX, int centerY, int totalHeight, boolean bottom, boolean isMissing) {
		FontMetrics m = g2.getFontMetrics();
		
		int fontHeight = m.getHeight();
		int labelWidth = m.stringWidth(name);
		
		int textX = offset+5;
		int textY = row*(fontHeight+NODE_RADIUS);
		if (row > 1) textY+=30;
		
		if (bottom) textY = totalHeight - textY;
		
		int nodeX = textX + labelWidth/2;
		int nodeY = textY + 4;

		if (bottom) {
			g2.setColor(DARK_BLUE);
			g2.setStroke(SOLID);
		} else {
			g2.setStroke(DASHED);
			g2.setColor(BLUE_GRAY);
		}
		g2.drawLine(centerX, centerY, nodeX + (NODE_RADIUS / 2), nodeY + (NODE_RADIUS / 2));
		
		//g2.setColor(Color.DARK_GRAY);
		g2.fillOval(nodeX, nodeY, NODE_RADIUS, NODE_RADIUS);
		g2.setColor(RED);
		g2.drawOval(nodeX-2, nodeY-2, NODE_RADIUS+3, NODE_RADIUS+3);
		
		
		g2.setColor(Color.BLACK);
		g2.setFont(FONT_SMALL);
		g2.drawString(org,textX, textY-10);
		
		g2.setFont(FONT_NORMAL);
		if (isMissing) {
			g2.setColor(RED);
		}
		g2.drawString(name,textX, textY);
		g2.setColor(Color.BLACK);
		
		return labelWidth + offset + MARGIN;
	}
	
	private static String buildDisplayText(Module module) {
		StringBuilder builder = new StringBuilder();
		builder.append(module.getInfo().getModule())
		       .append(" (")
		       .append(module.getInfo().getRevision()).append(")");
		return builder.toString();
	}
	
	private static String buildDisplayText(Dependency dependency) {
		StringBuilder builder = new StringBuilder();
		builder.append(dependency.getName())
		       .append(" (")
		       .append(dependency.getRev()).append(")");
		return builder.toString();
	}
	
	public static BufferedImage draw(int w, int h, Module module) {
		List<Dependency> dependents = module.getDependentList().getDependencies();
		List<Dependency> dependencies = module.getDependencyList().getDependencies();
		int centerX = w/2;
		int centerY = h/2;
		
		Collections.sort(dependents);
		
		int totalDependents = dependents.size();
		int totalDependencies = dependencies.size();
		
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bi.createGraphics();
		
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, w, h);
		
		g2.setFont(FONT_NORMAL);
		
		int offset = MARGIN;
		int row = 1;
		if (totalDependents > 0) {
			for (int i=0;i<totalDependents;i++) {
				String name = buildDisplayText(dependents.get(i));
				offset = drawNode(g2, dependents.get(i).getOrg(), name, offset, row, centerX, centerY, h, false, dependents.get(i).getMissing());
				if (i<totalDependents-2) {
					int x = offset + g2.getFontMetrics().stringWidth(buildDisplayText(dependents.get(i+1)));
					if (x>w) {
						offset = MARGIN;
						++row;
					}
				}
			}
		} else {
			offset = g2.getFontMetrics().stringWidth("No dependents");
			drawNode(g2, "", "No dependents", ((centerX)-offset/2)-5, row, centerX, centerY, h, false, false);
		}
		
		offset = MARGIN;
		row = 1;
		if (totalDependencies > 0) {
			for (int i=0; i<totalDependencies;i++) {
				String name = buildDisplayText(dependencies.get(i));
				offset = drawNode(g2, dependencies.get(i).getOrg(), name, offset, row, centerX, centerY, h, true, dependencies.get(i).getMissing());
				if (i<totalDependencies-2) {
					int x = offset + g2.getFontMetrics().stringWidth(buildDisplayText(dependencies.get(i+1)));
					if (x>w) {
						offset = MARGIN;
						++row;
					}
				}
			}
		} else {
			offset = g2.getFontMetrics().stringWidth("No dependencies");
			drawNode(g2, "", "No dependencies", ((centerX)-offset/2)-5, row, centerX, centerY, h, true, false);
		}
		
		int titleWidth = g2.getFontMetrics().stringWidth(buildDisplayText(module));
		int fontHeight = g2.getFontMetrics().getHeight();
		
		int ovalWidth = 150;
		int ovalHeight = 45;
		int ovalCenterX =  centerX - ovalWidth/2;
		int ovalCenterY = centerY - ovalHeight/2;
		int variant = 2;
		int ovalOffset = 4;
		
		g2.setColor(DARK_BLUE);
		
		
		g2.fillOval(ovalCenterX-variant, ovalCenterY-variant , ovalWidth+ovalOffset, ovalHeight+ovalOffset);
		g2.setColor(BAIGE);
		g2.fillOval(ovalCenterX, ovalCenterY , ovalWidth, ovalHeight);
		g2.setColor(Color.BLACK);
		g2.drawString(buildDisplayText(module), centerX-titleWidth/2, (centerY-fontHeight/2) + 12);
		
		return bi;
	}
}
