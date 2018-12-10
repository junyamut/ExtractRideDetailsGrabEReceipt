import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import helper.FilesList;

public class EReciptsFilePaths {
	protected static Path sourceDir;
	protected static FilesList filesList;

	public EReciptsFilePaths() { }
	
	static {
		try {
			sourceDir = Paths.get(System.getProperty("user.dir"), Properties.getProperties().getApp().getInputDir());
			filesList = new FilesList(sourceDir);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static List<Path> getList() {
		return filesList.getList();
	}
}