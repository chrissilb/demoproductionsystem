package de.gwasch.code.demoproductionsystem.interfaces.ui;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSRole;
import de.gwasch.code.demoproductionsystem.ui.MainFrame;
import de.gwasch.code.demoproductionsystem.ui.ProductionSystemPanelImpl;
import java.awt.Dimension;
import java.awt.Graphics;

public interface ProductionSystemPanel extends ProductionSystem {

    void init(MainFrame parent);

    void setName(String name);

    void setVolume(int volume);

    void setRole(Class<? extends PSRole> role);

    void doRepaint();

    Dimension getPreferredSize();

    void paintComponent(Graphics g);

    ProductionSystemPanelImpl getProductionSystemPanelImpl();
}
