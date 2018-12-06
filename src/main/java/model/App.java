package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class App {    
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("inputDir")
    @Expose
    private String inputDir;
    @SerializedName("outputDir")
    @Expose
    private String outputDir;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInputDir() {
		return inputDir;
	}

	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}

	public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }
}