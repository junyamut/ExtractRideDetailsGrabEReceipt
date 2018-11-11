package helper;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FilesList {
	private List<Path> filesList = new ArrayList<Path>();
	
	public FilesList(Path sourceDir) {		
		 try {
			 Files.walkFileTree(sourceDir, fileVisitor);
		 } catch (IOException e) {
			 System.out.println("Error encountered: " + e);
		 }
	}
		
	FileVisitor<Path> fileVisitor = new SimpleFileVisitor<Path>() {
		public FileVisitResult visitFile(Path file, BasicFileAttributes attribute) throws IOException {
			if (attribute.isRegularFile()) {
				filesList.add(file.toAbsolutePath());
			}
			return FileVisitResult.CONTINUE;
		}
		
		public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
			return FileVisitResult.CONTINUE;
		}
		
		public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
			System.out.println("Cannot read directory: " + e);
			return FileVisitResult.CONTINUE;
		}
	};
	
	public List<Path> getList() {
		if (filesList.size() == 0) {
			throw new IllegalArgumentException("Files list is empty. Nothing to work on.");
		}
		return filesList;
	}
}
