package de.gwasch.code.demoproductionsystem.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;
import de.gwasch.code.demoproductionsystem.interfaces.ui.ProductionLinePanel;
import de.gwasch.code.demoproductionsystem.interfaces.ui.ProductionSystemPanel;
import de.gwasch.code.demoproductionsystem.models.Model;
import de.gwasch.code.demoproductionsystem.persist.FilePersistManager;
import de.gwasch.code.demoproductionsystem.persist.PersistManager;
import de.gwasch.code.demoproductionsystem.ui.ProportionalSplitPane.ComponentSelection;
import de.gwasch.code.escframework.components.utils.InstanceAllocator;
import de.gwasch.code.escframework.events.patterns.Rule;

//todo, undo funktionalität
//todo, logs optional speichern
public class MainFrame extends JFrame {

	class LogAdder implements Runnable {
		
		private String log;
		
		public LogAdder(String log) {
			this.log = log;
		}
		
		public void run() {
			logListModel.add(log);
			
			if (!chkScrollLock.isSelected()) {
				lstLogs.ensureIndexIsVisible(logListModel.getSize() - 1);
			}
		}
	}
	
	//todo, notwendig? wenn ja beschreiben warum
	class SystemOutStream extends OutputStream {

		private StringBuffer line; 
		
		public SystemOutStream() {
			line = new StringBuffer();
		}
		
		public void write(int b) throws IOException {
			
			if (b == '\n') {
				LogAdder la = new LogAdder(line.toString());
				SwingUtilities.invokeLater(la);
				line = new StringBuffer();
			}
			else {
				line.append((char)b);
			}			
		}
		
	}
	
	class ComponentHandler extends ComponentAdapter {

		public void componentResized(ComponentEvent e) {
//			MainFrame frame = (MainFrame)e.getComponent();
		}
	}
		
	enum FileState {
		New,
		Opened,
		Closed
	}
	
	enum SimulationState {
		Started,
		Paused,
		Stopped
	}
	
	private static final long serialVersionUID = -3207281575349722387L;
	
	private static final String TITLE_BASE = "Production Activity Monitoring";
		
	private SafeFileChooser fileChooser;
	private PersistManager persistManager;
	
	private Model persistModel;
	private Model workModel;
	
	private FileState fileState;
	private SimulationState simulationState;
	
	private JMenuItem miNew;
	private JMenuItem miOpen;
	private JMenuItem miClose;
	private JMenuItem miSave;
	private JMenuItem miSaveAs;
	private JMenuItem miExit;
	
	private JMenuItem miAddProductionLine;
	private JMenuItem miRemoveProductionLine;
	
	private JMenuItem miStartSimulation;
	private JMenuItem miPauseSimulation;
	private JMenuItem miStopSimulation;
	
	private JMenuItem miConfigurations;
	private JMenuItem miLogFilters;
	
	private ButtonGroupModel<ComponentSelection> showWindowModel;
	
	private ProportionalSplitPane splitPanel;

	private JPanel productionPanel;
	private JPanel productionSystemPanel;
	private JPanel productionLinesPanel;

	private JList<String> lstLogs;
	private LogListModel logListModel;
	private JCheckBox chkScrollLock;
	private JButton cmdClearConsole;
	private JButton cmdLogFilters;
	
	
	public MainFrame() {
				
		fileChooser = new SafeFileChooser(".", "pam", "PAM file");
		
		workModel = null;
		persistModel = null;
		
		setTitle(TITLE_BASE);
		setSize(1000, 600);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				doExit();
			}
		});
		
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);

		JMenu menu;
		JMenuItem mi;
		JRadioButtonMenuItem rmi;
		
		menubar.add(menu = new JMenu("File"));
		
		menu.add(miNew = new JMenuItem("New..."));
		miNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doNew();
			}
		});

		menu.add(miOpen = new JMenuItem("Open..."));
		miOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doOpen();
			}
		}); 
 
		menu.add(miClose = new JMenuItem("Close"));
		miClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doClose();
			}
		});

		menu.add(new JSeparator());

		menu.add(miSave = new JMenuItem("Save"));
		miSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		miSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSave();
			}
		});
		
		menu.add(miSaveAs = new JMenuItem("Save As..."));
		miSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSaveAs();
			}
		});
		
		menu.add(new JSeparator());
		
		menu.add(miExit = new JMenuItem("Exit"));
		miExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doExit();
			}
		});
		
		menubar.add(menu = new JMenu("Edit"));
		
		menu.add(miAddProductionLine = new JMenuItem("Add Production Line..."));
		miAddProductionLine.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.CTRL_DOWN_MASK));
		miAddProductionLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddProductionLine();
			}
		});
		
		menu.add(miRemoveProductionLine = new JMenuItem("Remove Last Production Line..."));
		miRemoveProductionLine.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK));
		miRemoveProductionLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doRemoveProductionLine();
			}
		});
		
		menubar.add(menu = new JMenu("Simulate"));
		
		menu.add(miStartSimulation = new JMenuItem("Start"));	
		miStartSimulation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));	
		miStartSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doStart();
			}
		});
		
		menu.add(miPauseSimulation = new JMenuItem("Pause"));
		miPauseSimulation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
		miPauseSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPause();
			}
		});
		
		menu.add(miStopSimulation = new JMenuItem("Stop"));
		miStopSimulation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
		miStopSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doStop();
			}
		});
				
		menubar.add(menu = new JMenu("Options"));
		
		menu.add(miConfigurations = new JMenuItem("Configurations..."));
		miConfigurations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doConfigurations();
			}
		});
		
		menu.add(miLogFilters = new JMenuItem("Log Filters..."));
		miLogFilters.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doLogFilters();
			}
		});
		
		menubar.add(menu = new JMenu("Window"));
		
		showWindowModel = new ButtonGroupModel<ComponentSelection>();
		
		menu.add(rmi = new JRadioButtonMenuItem("Show Dashboard"));		
		rmi.setModel(new ButtonGroupButtonModel<ComponentSelection>(ComponentSelection.Left));
		rmi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
		showWindowModel.add(rmi);
		rmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doShowDashboard();
			}
		});

		menu.add(rmi = new JRadioButtonMenuItem("Show Event Log"));
		rmi.setModel(new ButtonGroupButtonModel<ComponentSelection>(ComponentSelection.Right));
		rmi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
		showWindowModel.add(rmi);
		rmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doShowEventLog();
			}
		});
		
		menu.add(rmi = new JRadioButtonMenuItem("Split Screen"));
		rmi.setModel(new ButtonGroupButtonModel<ComponentSelection>(ComponentSelection.Both));
		rmi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
		showWindowModel.add(rmi);
		rmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSplitScreen();
			}
		});

		showWindowModel.setValue(ComponentSelection.Both);
		
		menu.add(new JSeparator());
		
		menu.add(mi = new JMenuItem("Toggle Split"));
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doToggleSplit();
			}
		});
		
		menubar.add(menu = new JMenu("Help"));
		
		menu.add(mi = new JMenuItem("About..."));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAbout();
			}
		});


		splitPanel = new ProportionalSplitPane();
		//NOTE: SplitPanel uses shortcut "F6". This is how to deactivate it. See https://stackoverflow.com/questions/8243720/is-there-a-swing-element-which-has-f6-as-a-default-accelerator
		splitPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "none");
		
		splitPanel.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (!evt.getPropertyName().equals(ProportionalSplitPane.DIVIDER_LOCATION_PROPERTY)) {
					return;
				}
				
				showWindowModel.setValue(splitPanel.getComponentSelection());
			}
		});

		productionPanel = new JPanel();
		productionPanel.setLayout(new BorderLayout());
		
		productionSystemPanel = new JPanel();
		productionSystemPanel.setLayout(new BorderLayout());
		productionPanel.add(productionSystemPanel, BorderLayout.NORTH);
				
		productionLinesPanel = new JPanel();
		productionLinesPanel.setLayout(new GridLayout(1,0));
		
		productionPanel.add(productionLinesPanel);
		splitPanel.add(JSplitPane.LEFT, productionPanel);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		logListModel = new LogListModel();
		logListModel.addListDataListener(new ListDataListener() {
			
			public void intervalRemoved(ListDataEvent e) {
				updateState(); //todo
			}
			
			public void intervalAdded(ListDataEvent e) {
				updateState(); //todo
			}
			
			public void contentsChanged(ListDataEvent e) {
				//todo
			}
		});
		
		lstLogs = new JList<String>(logListModel);
				
		JScrollPane scrollpane = new JScrollPane(lstLogs);
	    JPanel titlepane = new JPanel();
		titlepane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Event Log"));
		titlepane.setLayout(new BorderLayout());
		titlepane.add(scrollpane);
		
		panel.add(titlepane);
		
		JPanel menupanel = new JPanel();
		menupanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		chkScrollLock = new JCheckBox("Scroll Lock", false);
		menupanel.add(chkScrollLock);
		
		cmdClearConsole = new JButton("Clear");
		cmdClearConsole.setEnabled(false);
		cmdClearConsole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logListModel.clear();
			}		
		});
		menupanel.add(cmdClearConsole);
		
		cmdLogFilters = new JButton("Filters...");
		cmdLogFilters.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doLogFilters();
			}
		});
		
		menupanel.add(cmdLogFilters);
		
		panel.add(menupanel, BorderLayout.SOUTH);
		
		splitPanel.add(JSplitPane.RIGHT, panel);
		
		add(splitPanel);

		addComponentListener(new ComponentHandler());
		
		fileState = FileState.Closed;
		simulationState = SimulationState.Stopped;
		updateState();
//		splitPanel.setDividerLocation(0.5);
		
		PrintStream ps = new PrintStream(new SystemOutStream());
		System.setOut(ps);
		
		setVisible(true);

		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				splitPanel.setDividerLocation(0.5);
			}
		});		
	}
	
	private boolean changeResource() {
		if (persistManager != null && workModel.hasChanged(persistModel)) {
			int result = JOptionPane.showConfirmDialog(MainFrame.this, "Save Changes?", 
				"Save Resource", JOptionPane.YES_NO_CANCEL_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				doSave();
			}
			else if (result == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}
		
		return true;
	}
	
	private void doNew() {
		
		if (!changeResource()) {
			return;
		}
		
		doClose();

		workModel = new Model();
		ProductionSystem ps = InstanceAllocator.create(ProductionSystem.class);
		ps.init("PS", 100);
		ps.setConfig(workModel.config);
		workModel.productionSystem = ps;
		
		setProductionSystem(workModel.productionSystem);
		fileState = FileState.New;
		updateTicks();
		updateState();
		updateFilters();
	}
	
	private void doOpen() {
		
		if (!changeResource()) {
			return;
		}
								
	    int ret = fileChooser.showOpenDialog(this);
	    if (ret != JFileChooser.APPROVE_OPTION) {
	    	return;
	    }
    	
    	PersistManager pm = null;
    	Model m = null;
    	
    	try {
	    	pm = new FilePersistManager(fileChooser.getSelectedFile());
	    	m = pm.load();
    	}
    	catch(IOException ex) {
    		JOptionPane.showMessageDialog(MainFrame.this, "Invalid PAM file!", "Error", JOptionPane.ERROR_MESSAGE);
    		return;
    	}
    	
    	persistModel = workModel;
    	doClose();
    	
    		
    	workModel = m;
    	persistModel = m.clone();
    	persistManager = pm;
    	
    	setProductionSystem(workModel.productionSystem);
		
		for (ProductionLine pl : workModel.productionLines) {
			addProductionLine(pl);
		}
    	
		fileState = FileState.Opened;
		updateTicks();
		updateState();
		updateFilters();
	}
	
	private void doClose() {
		
		if (fileState == FileState.Closed || !changeResource()) {
			return;
		}
		
		doStop();
		
		removeProductionSystem();
		
		for (ProductionLine pl : workModel.productionLines) {
			removeProductionLine(pl);
		}
		
		workModel.productionLines.clear();
		persistManager = null;
		
		fileState = FileState.Closed;
		updateState();
	}
	
	private void doSave() {
		
		if (fileState == FileState.Closed) {
			return;
		}
		if (fileState == FileState.New) {
			doSaveAs();
			return;
		}
		
		
		try {
			persistManager.save(workModel);
			persistModel = workModel.clone();
			updateState();
		}
		catch(IOException ex) {
    		JOptionPane.showMessageDialog(this, "Save error!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void doSaveAs() {
		
	    int ret = fileChooser.showSaveDialog(this);
	    if (ret != JFileChooser.APPROVE_OPTION) {
	    	return;
	    }
	    		    	
    	try {
	    	PersistManager pm = new FilePersistManager(fileChooser.getSelectedFile());
	    	pm.save(workModel);
	    	persistModel = workModel.clone();
	    	persistManager = pm;
	    	updateState();
	    }
    	catch(IOException ex) {
			JOptionPane.showMessageDialog(this, "Save error!", "Error", JOptionPane.ERROR_MESSAGE);
    	}
	}

	
	private void doExit() {
	
		if (fileState == FileState.Opened) {
			if (!changeResource()) {
				return;
			}
			
			persistModel = workModel.clone();
			doClose();
		}
		
		InstanceAllocator.getTimerPN().deactivate();
		
		dispose();
	}
	
	private void doAddProductionLine() {
		
		String name = "PL" + (workModel.productionLines.size() + 1);
		int capacity = 10;
		
		ProductionLine pl = InstanceAllocator.create(ProductionLine.class);
		pl.init(name, capacity);
		pl.setProductionSystem(workModel.productionSystem);
		new ProductionLineDialog(MainFrame.this, pl, DialogMode.Create);
		updateState();
	}
	
	private void doRemoveProductionLine() {

		removeProductionLine(workModel.productionLines.get(workModel.productionLines.size() - 1));
		workModel.productionLines.remove(workModel.productionLines.size() - 1);
		updateState();
	}
	
	private void doStart() {
		
		
		if (simulationState != SimulationState.Paused) {
			
			workModel.productionSystem.startTick();
			
			for (ProductionLine pl : workModel.productionLines) {
				pl.startTick();
			}
			
			workModel.productionSystem.start();
			
			for (ProductionLine pl : workModel.productionLines) {
				pl.start();
			}
		}
		else {
			workModel.productionSystem.resumeTick();
			
			for (ProductionLine pl : workModel.productionLines) {
				pl.resumeTick();
			}
		}
		
		simulationState = SimulationState.Started;
		updateState();
	}
	
	private void doPause() {
		
		workModel.productionSystem.suspendTick();
		
		for (ProductionLine pl : workModel.productionLines) {
			pl.suspendTick();
		}
				
		simulationState = SimulationState.Paused;
		updateState();
	}
	
	private void doStop() {
		
		workModel.productionSystem.stop();
		
		for (ProductionLine pl : workModel.productionLines) {
			pl.stop();
		}
		
		workModel.productionSystem.stopTick();
		
		for (ProductionLine pl : workModel.productionLines) {
			pl.stopTick();
		}
										
		simulationState = SimulationState.Stopped;
		updateState();
	}
	
	private void doConfigurations() {
		new ConfigurationDialog(MainFrame.this, workModel.config);
	}

	private void doLogFilters() {
		new LogFilterDialog(MainFrame.this, workModel);
	}
	
	private void doShowDashboard() {
		splitPanel.showComponent(ComponentSelection.Left);
	}

	private void doShowEventLog() {
		splitPanel.showComponent(ComponentSelection.Right);
	}
	
	private void doSplitScreen() {
		splitPanel.showComponent(ComponentSelection.Both);
	}
	
	private void doToggleSplit() {
		splitPanel.toggleSplit();
	}

	private void doAbout() {
		
	}

	
	public Model getWorkModel() {
		return workModel;
	}
	
	public void updateTicks() {
		List<Rule> tickRules = InstanceAllocator.getPatternMatcher().getRulesByName("tick");

		for (Rule tickRule : tickRules) {
			tickRule.setActionInterval(workModel.config.getAvgTickPause());
			tickRule.setMaxActionDeviationFactor(workModel.config.getMaxDeviationFactor());
		}
	}
	
	public void updateState() {

		if (fileState == FileState.Closed) {
			miNew.setEnabled(true);
			miOpen.setEnabled(true);
			miClose.setEnabled(false);
			miSave.setEnabled(false);
			miSaveAs.setEnabled(false);
			miExit.setEnabled(true);
			miAddProductionLine.setEnabled(false);
			miRemoveProductionLine.setEnabled(false);
			miStartSimulation.setEnabled(false);
			miPauseSimulation.setEnabled(false);
			miStopSimulation.setEnabled(false);
			miConfigurations.setEnabled(false);
			miLogFilters.setEnabled(false);
			cmdLogFilters.setEnabled(false);
		}
		else {
			miNew.setEnabled(true);
			miOpen.setEnabled(true);
			miClose.setEnabled(true);
			miSave.setEnabled(fileState == FileState.New || workModel.hasChanged(persistModel));
			miSaveAs.setEnabled(true);
			miExit.setEnabled(true);
			miAddProductionLine.setEnabled(true);
			miRemoveProductionLine.setEnabled(workModel.productionLines.size() > 0);
			miConfigurations.setEnabled(true);
			miLogFilters.setEnabled(true);
			cmdLogFilters.setEnabled(true);
			
			switch(simulationState) {
			case Started:
				miStartSimulation.setEnabled(false);
				miPauseSimulation.setEnabled(true);
				miStopSimulation.setEnabled(true);
				break;
			case Paused:
				miStartSimulation.setEnabled(true);
				miPauseSimulation.setEnabled(false);
				miStopSimulation.setEnabled(true);
				break;
			case Stopped:
				miStartSimulation.setEnabled(true);
				miPauseSimulation.setEnabled(false);
				miStopSimulation.setEnabled(false);
				break;
			}
		}
		
		cmdClearConsole.setEnabled(logListModel.getSize() > 0);
		//todo, switch über file state
		
		if (persistManager != null) { //todo, warum die prüfung?
			if (workModel.hasChanged(persistModel)) {
				setTitle(persistManager.getResourceName() + "* - " + TITLE_BASE);
			}
			else {
				setTitle(persistManager.getResourceName() + " - " + TITLE_BASE);
			}
		}
		else if (fileState == FileState.New){
			setTitle("<<new>>.pam* - " + TITLE_BASE);
		}
		else {
			setTitle(TITLE_BASE);
		}
	}
	
	public void updateFilters() {
						
		int vindex = lstLogs.getMinSelectionIndex();
		int index = -1;
		
		if (vindex != -1) {
			index = logListModel.getUnfilteredIndex(vindex);
		}
		
		logListModel.installFilters(workModel.logFilters);
		
		if (index != -1) {
			vindex = logListModel.getFilteredIndex(index);
			lstLogs.setSelectedIndex(vindex);
			lstLogs.ensureIndexIsVisible(vindex);
		}
	}

	public void setProductionSystem(ProductionSystem ps) {
		
		ProductionSystemPanel psPanel = (ProductionSystemPanel)ps;
		psPanel.init(this);
		productionSystemPanel.add(psPanel.getProductionSystemPanelImpl());	
		
		productionPanel.revalidate();
		productionPanel.repaint();
	}
	
	public void removeProductionSystem() {
		
		//todo, instanzen im InstanceAllocator entfernen
		productionSystemPanel.removeAll();

		productionSystemPanel.revalidate();
		productionSystemPanel.repaint();
	}
	
	public void addProductionLine(ProductionLine pl) {
		
		ProductionLinePanel plPanel = (ProductionLinePanel)pl;
		plPanel.init(this);
		productionLinesPanel.add(plPanel.getProductionLinePanelImpl());
		
		productionPanel.revalidate();
		productionPanel.repaint();

		if (simulationState == SimulationState.Started) {
			pl.start();
			pl.startTick();
		}
		
		updateState();
	}
	
	
	public void removeProductionLine(ProductionLine pl) {

		pl.stop();
		
		if (simulationState == SimulationState.Started) {
			pl.stopTick();

		}
				
		int i = 0;
		for (Component c : productionLinesPanel.getComponents()) {
			ProductionLinePanelImpl panel = (ProductionLinePanelImpl)c;
			if (panel.getModel().equals(pl)) { 
				break;
			}
			i++;
		}
		
		productionLinesPanel.remove(i);

		productionLinesPanel.revalidate();
		productionLinesPanel.repaint();

		updateState();
	}
	
	public int getCountProductionLines() {
		return workModel.productionLines.size();
	}
}
