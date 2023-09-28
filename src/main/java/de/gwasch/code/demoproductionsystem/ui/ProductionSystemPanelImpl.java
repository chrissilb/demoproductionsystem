package de.gwasch.code.demoproductionsystem.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSRole;
import de.gwasch.code.demoproductionsystem.interfaces.ui.ProductionSystemPanel;
import de.gwasch.code.escframework.components.annotations.Core;
import de.gwasch.code.escframework.components.annotations.Extension;
import de.gwasch.code.escframework.components.annotations.Thiz;

@Extension(type=ProductionSystemPanel.class, extendz=ProductionSystem.class)
public class ProductionSystemPanelImpl extends ProductionPanel {

	private static final long serialVersionUID = -4863245046885170067L;

	class MouseHandler extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			new ProductionSystemDialog(parent, thiz);
		}
	}

	
	@Thiz
	private ProductionSystemPanel thiz;

	@Core
	private ProductionSystem model;

	private Dimension preferredDimension;
	private MainFrame parent;


	public void init(MainFrame parent) {
		this.parent = parent;
		addMouseListener(new MouseHandler());
		preferredDimension = new Dimension(-1, -1);
	}
	
	//todo, autodelegate
	public void setName(String name) {
		model.setName(name);
		doRepaint();
	}
	
	//todo, autodelegate
	public void setVolume(int volume) {
		model.setVolume(volume);
		doRepaint();
	}
	
	//todo, autodelegate
	public void setRole(Class<? extends PSRole> role) {
		model.setRole(role);
		doRepaint();
	}
	
	public void doRepaint() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				parent.repaint();
			}
		});
	}
	
	public Dimension getPreferredSize() {
		if (   parent.getCountProductionLines() == 0
			|| getParent().getParent().getHeight() / 2 < getParent().getWidth() / parent.getCountProductionLines()) {
			
			preferredDimension.height = getParent().getParent().getHeight() / 2;
		}
		else {
			preferredDimension.height = getParent().getWidth() / parent.getCountProductionLines();
		}

		return preferredDimension;
	}
	
	public void paintComponent(Graphics g) {
		
		Color color;

		switch (model.getPSRoleEnum()) {
			case PSDistribute: color = Color.BLUE; break;
			case PSDefect: color = Color.RED; break;
			default /*case PSInactive*/: color = Color.GRAY; break;
		}

		paintComponent(g, color, model.getName() + " (" + model.getVolume() + ")", null);
	}
}
