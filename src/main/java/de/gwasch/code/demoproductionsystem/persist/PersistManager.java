package de.gwasch.code.demoproductionsystem.persist;

import java.io.IOException;

import de.gwasch.code.demoproductionsystem.models.Model;

public interface PersistManager {
	String getResourceName();
	Model load() throws IOException;
	void save(Model model) throws IOException;
}
