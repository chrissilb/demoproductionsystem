package de.gwasch.code.demoproductionsystem.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

// Note: Class breaks Liskov. It accepts only one FileNameExtensionFilter and expects setAcceptAllFileFilterUsed(false).
//       Both is configured by its constructor.

public class SafeFileChooser extends JFileChooser {

	private static final long serialVersionUID = -2456562687085143372L;
	
	public SafeFileChooser(String currentDirectoryPath, String fileExtension, String fileDescription) {
		super(currentDirectoryPath);
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(fileDescription, fileExtension);
		setFileFilter(filter);
		setAcceptAllFileFilterUsed(false);
	}
	
	public void approveSelection() {
		
		FileNameExtensionFilter filter = (FileNameExtensionFilter)getFileFilter();
		String fileExtension = filter.getExtensions()[0];
		
		if (getDialogType() == SAVE_DIALOG) {
			
			File f = getSelectedFile();
			String name = f.getAbsolutePath();
			
			String prefix = FilenameUtils.getPrefix(name);
			String path = FilenameUtils.getPath(name);
			String base = FilenameUtils.getBaseName(name);
			String ext = FilenameUtils.getExtension(name);
			
			if (ext.equals("")) {
				f = new File(prefix + path + base + "." + fileExtension);
				setSelectedFile(f);
			}
			else if (!ext.equalsIgnoreCase(fileExtension)) {
				JOptionPane.showMessageDialog(getParent(), 
						"File extension must be 'pam'", 
						"Invalid file extension", 
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			
			if (f.exists()) {
				int res = JOptionPane.showConfirmDialog(getParent(), 
						"The file '" + base + "." + ext + "' already exists. Overwrite?", 
						"Existing file", 
						JOptionPane.YES_NO_OPTION);
				
				if (res != JOptionPane.YES_OPTION) {
					return;
				}
			}	
		}
		
		super.approveSelection();
	}
}
