package de.gwasch.code.demoproductionsystem.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class ProductionPanel extends JPanel {
	
	private static final long serialVersionUID = 2775718794356315714L;
	private static final double MARGIN_FACTOR = 0.15;
	private static final double BORDER_FACTOR = 0.02;
	private static final double PADDING_FACTOR = 0.1;
	
	
	protected static Font deriveFont(Graphics g, Font font, String text, int width, int height) {
		
		float size = 0.0f;
		FontMetrics metrics = g.getFontMetrics();
		
		do {
			size++;
			metrics = g.getFontMetrics(font.deriveFont(size));
		}
		while ((metrics.stringWidth(text) <= width) && metrics.getHeight() <= height);
		
		return font.deriveFont(size - 1.0f);
	}
	
	protected void paintComponent(Graphics g, Color color, String text1, String text2) {
				
		int offx = (int)(Math.min(getWidth(), getHeight()) * MARGIN_FACTOR);

		g.setColor(Color.BLACK);
		g.fillRect(offx, offx, getWidth() - offx * 2, getHeight() - offx * 2);
	
		offx = offx + Math.max((int)(Math.min(getWidth(), getHeight()) * BORDER_FACTOR), 2);
		
		if (color == null) {
			color = Color.GRAY;
		}
		
		g.setColor(color);
		g.fillRect(offx, offx, getWidth() - offx * 2, getHeight() - offx * 2);
		
		
		offx = offx + Math.max((int)(Math.min(getWidth(), getHeight()) * PADDING_FACTOR), 2);
		int offy;
		int height = getHeight() - offx * 2;
		if (text2 != null) {
			height /= 2;
		}
		
		g.setColor(Color.BLACK);
		Font font = new Font("Arial", 0, 0);
		Font font1 = deriveFont(g, font, text1, getWidth() - offx * 2, height);
		g.setFont(font1);
		
		if (text2 != null) {
			offy = getHeight() / 2 - g.getFontMetrics(font1).getDescent();
		}
		else {
			offy =  getHeight() / 2 + g.getFontMetrics(font1).getAscent() -
			(g.getFontMetrics(font1).getAscent() + g.getFontMetrics(font1).getDescent()) / 2;
		}
		
		g.drawString(text1, offx, offy);
		
		//g.drawLine(offx, getHeight() / 2, offx + 100, getHeight() / 2);

		if (text2 != null) {
			font = deriveFont(g, font, text2, getWidth() - offx * 2, (getHeight() - offx * 2) / 2);
			g.setFont(font);
			g.drawString(text2, offx, getHeight() / 2 + g.getFontMetrics(font).getAscent());
		}
	}
}
