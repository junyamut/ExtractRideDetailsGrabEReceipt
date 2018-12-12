import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WorkbookOutputPath {
	private static Path outputDirPath;

	public WorkbookOutputPath() { }
	
	static {
		try {
			outputDirPath = Paths.get(Properties.getProperties().getApp().getOutputDir());
			if (!Files.isDirectory(outputDirPath)) Files.createDirectory(outputDirPath);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}		
	}

	public static Path getPath() throws IOException {
		return Paths.get(outputDirPath.getFileName().toString(), Properties.getProperties().getWorkbook().getName());
	}
}
