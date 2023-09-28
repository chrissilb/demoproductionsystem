package de.gwasch.code.demoproductionsystem.persist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;
import de.gwasch.code.demoproductionsystem.models.Configuration;
import de.gwasch.code.demoproductionsystem.models.Model;
import de.gwasch.code.escframework.components.utils.InstanceAllocator;
import de.gwasch.code.escframework.utils.logging.Filters;

public class FilePersistManager2 implements PersistManager {
	private File file;
	
	public FilePersistManager2(File file) {
		this.file = file;
	}
	
	public String getResourceName() {
		return file.getName();
	}
	
	public Model load() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		StreamTokenizer st = new StreamTokenizer(br);     
		st.whitespaceChars(',', ',');   

		Model model = new Model();
		
		Configuration config = new Configuration();
		st.nextToken();
		config.setAvgTickPause((int)st.nval);
		st.nextToken();
		config.setMaxDeviationFactor(st.nval);
		st.nextToken();
		config.setTimeoutFactor(st.nval);
		st.nextToken();
		model.config = config;
		
		//todo, filter von datei laden
		Filters filters = new Filters();
		filters.add(".*", true);		
		filters.add("Initializer.finish.*Event", true);		
		model.logFilters = filters;
		
		String name = st.sval;
		st.nextToken();
		int volume = (int)st.nval;
		st.nextToken();
		model.productionSystem = InstanceAllocator.create(ProductionSystem.class);
		model.productionSystem.init(name, volume);
		model.productionSystem.setConfig(config);
		
		model.productionLines = new ArrayList<ProductionLine>();
		
		while (st.nextToken() != StreamTokenizer.TT_EOF) {
			name = st.sval;
			st.nextToken();
			int capacity = (int)st.nval;
			ProductionLine pl = InstanceAllocator.create(ProductionLine.class);
			pl.init(name, capacity);
			pl.setProductionSystem(model.productionSystem);

			model.productionLines.add(pl);
		}
		
		br.close();
		
		return model;
	}
	
	public void save(Model model) throws IOException {
		
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw); 
        PrintWriter pw = new PrintWriter(bw);       
        
        pw.print(model.config.getAvgTickPause());
        pw.print(',');
        pw.print(model.config.getMaxDeviationFactor());
        pw.print(',');
        pw.println(model.config.getTimeoutFactor());

		
        pw.print(model.productionSystem.getName());
        pw.print(',');
        pw.println(model.productionSystem.getVolume());
		
        for(ProductionLine pl : model.productionLines) {
        	pw.print(pl.getName());
        	pw.print(',');
        	pw.println(pl.getCapacity());
        }

        pw.close();       
	}
}
