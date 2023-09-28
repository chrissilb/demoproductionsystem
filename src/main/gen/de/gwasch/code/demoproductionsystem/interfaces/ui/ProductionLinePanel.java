package de.gwasch.code.demoproductionsystem.interfaces.ui;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLRole;
import de.gwasch.code.demoproductionsystem.ui.MainFrame;
import de.gwasch.code.demoproductionsystem.ui.ProductionLinePanelImpl;
import java.awt.Graphics;

public interface ProductionLinePanel extends ProductionLine {

    void init(MainFrame parent);

    ProductionLine getModel();

    void setName(String name);

    void setCapacity(int capacity);

    void setRole(Class<? extends PLRole> role);

    void doRepaint();

    void paintComponent(Graphics g);

    ProductionLinePanelImpl getProductionLinePanelImpl();
}
