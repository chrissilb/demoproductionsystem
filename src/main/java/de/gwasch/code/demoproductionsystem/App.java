package de.gwasch.code.demoproductionsystem;

import de.gwasch.code.demoproductionsystem.ui.MainFrame;
import de.gwasch.code.escframework.components.utils.InstanceAllocator;

public class App 
{
    public static void main( String[] args )
    {
		InstanceAllocator.collectTypes();	
		new MainFrame();
    }
}
