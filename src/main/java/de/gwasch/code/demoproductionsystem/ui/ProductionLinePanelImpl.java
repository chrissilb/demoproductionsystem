package de.gwasch.code.demoproductionsystem.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLRole;
import de.gwasch.code.demoproductionsystem.interfaces.ui.ProductionLinePanel;
import de.gwasch.code.escframework.components.annotations.Core;
import de.gwasch.code.escframework.components.annotations.Extension;
import de.gwasch.code.escframework.components.annotations.Thiz;

@Extension(type=ProductionLinePanel.class, extendz=ProductionLine.class)
public class ProductionLinePanelImpl extends ProductionPanel {

	private static final long serialVersionUID = 2937229492056142878L;

	class MouseHandler extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			new ProductionLineDialog(parent, thiz);
		}
	}


	@Thiz
	private ProductionLinePanel thiz;
	
	@Core
	private ProductionLine model;
	
	private MainFrame parent;


	public void init(MainFrame parent) {
		this.parent = parent;
		addMouseListener(new MouseHandler());
	}
	
	public ProductionLine getModel() {
		return model;
	}
	
	//todo, autodelegate
	public void setName(String name) {
		model.setName(name);
		doRepaint();
		parent.updateState();
	}
	
	//todo, autodelegate
	public void setCapacity(int capacity) {
		model.setCapacity(capacity);
		doRepaint();
		parent.updateState();
	}
	
	//todo, autodelegate
	public void setRole(Class<? extends PLRole> role) {
		model.setRole(role);
		doRepaint();
	}
	
	public void doRepaint() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (getParent() != null) {
					getParent().repaint();
				}
			}
		});
	}
	
	public void paintComponent(Graphics g) {
				
		Color color;
		
		switch (model.getPLRoleEnum()) {
			case PLWorking: color = Color.BLUE; break;
			case PLIdle: color = Color.GREEN; break;
			case PLDefect: color = Color.RED; break;
			default /*case PLInactive */: color = Color.GRAY; break;
		}

		paintComponent(g, color, model.getName(), "(" + model.getCapacity() + ")");
	}
}
