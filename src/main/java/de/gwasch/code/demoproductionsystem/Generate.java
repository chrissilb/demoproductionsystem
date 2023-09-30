package de.gwasch.code.demoproductionsystem;

import de.gwasch.code.escframework.components.utils.CodeGenerator;

public class Generate {
	public static void main(String[] args) throws Exception {		
		
		CodeGenerator generator = new CodeGenerator("src/main/java", Generate.class.getPackageName(), "target/generated-sources");
		generator.generateInterfaces();
	}
}
