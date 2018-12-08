import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import helper.FilesList;

public class EReciptsFilePaths {
	protected static Path sourcePath;
	protected static FilesList filesList;

	public EReciptsFilePaths() { }	
	
	public static List<Path> getPaths() {
		try {
			sourcePath = Paths.get(System.getProperty("user.dir"), Properties.getProperties().getApp().getInputDir());
			filesList = new FilesList(sourcePath);
		} catch (IOException e) {
			System.out.println("Error reading files from source directory: " + e.getMessage());
		}
		return filesList.getList();
	}

}
