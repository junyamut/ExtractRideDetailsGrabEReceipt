package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Settings {
    @SerializedName("app")
    @Expose
    private App app;
    @SerializedName("workbook")
    @Expose
    private Workbook workbook;

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

	public Workbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}
}