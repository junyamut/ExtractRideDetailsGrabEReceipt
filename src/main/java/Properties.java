import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Settings;

public class Properties {
	private final static String PROPERTIES_FILE_NAME = "app.json";
	private final static String PROPERTIES_FOLDER = "settings";
	private final static String PROPERTIES_FILE_PATH = System.getProperty("user.dir") + File.separator + PROPERTIES_FOLDER + File.separator + PROPERTIES_FILE_NAME;
	
	public Properties() { }
	
	public static Settings getProperties() throws IOException {
		InputStream inputstream = new FileInputStream(PROPERTIES_FILE_PATH);
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(new InputStreamReader(inputstream, "UTF-8"), Settings.class);
	}
}