import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Settings;

public class Properties {
	private final static String PROPERTIES_FILE_PATH = "/settings.json";
	
	public Properties() { }
	
	public static Settings getProperties() throws IOException {
		InputStream inputstream = Properties.class.getResourceAsStream(PROPERTIES_FILE_PATH);
		if (inputstream == null) {
			throw new IOException("Properties file not found: " + PROPERTIES_FILE_PATH);
		}
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(new InputStreamReader(inputstream, "UTF-8"), Settings.class);
	}
}