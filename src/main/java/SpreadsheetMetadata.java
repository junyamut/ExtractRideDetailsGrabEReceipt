import org.apache.poi.POIXMLProperties;
import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.POIXMLProperties.CustomProperties;
import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;

public class SpreadsheetMetadata {	
	public static final String META_DESCRIPTION = "Extract Ride Details from Grab E-Receipts";
	public static final String META_KEYWORDS = "Grab, E-Receipt, Ride-Summary, Ride-Details";
	public static final String META_CATEGORY = "Transportation Expense";
	public static final String META_DATA_BOUNDARY = "NO-DATA";	
	private POIXMLProperties properties;
	private POIXMLProperties.CoreProperties core;
	private POIXMLProperties.CustomProperties custom;
	private String title;
	private String dataBoundary;
	public Integer dataRows;
	
	public SpreadsheetMetadata(POIXMLProperties properties) {
		this.properties = properties;
		this.core = properties.getCoreProperties();
		this.custom = properties.getCustomProperties();
	}
	
	public SpreadsheetMetadata(SpreadsheetMetadataBuilder builder) {
		this.properties = builder.properties;
		this.core = builder.core;
		this.custom = builder.custom;
		this.title = builder.title;
		this.dataBoundary = builder.dataBoundary;
		this.dataRows = builder.dataRows;
	}	
	
	public void setProperties(POIXMLProperties properties) {
		this.properties = properties;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDataBoundary(String dataBoundary) {
		this.dataBoundary = dataBoundary;
	}

	public void setDataRows(Integer dataRows) {
		this.dataRows = dataRows;
	}

	public POIXMLProperties getProperties() {
		return properties;
	}
	
	public CoreProperties getCoreProperties() {
		return core;
	}
	
	public CustomProperties getCustomProperties() {
		return custom;
	}

	public String getTitle() {
		return title;
	}
	
	public String getDataBoundary() {
		return dataBoundary;
	}

	public Integer getDataRows() {
		return dataRows;
	}
	
	public Object getCustomProperty(String key) {
		try {
			CTProperty value = properties.getCustomProperties().getProperty(key);
			if (value.isSetLpwstr()) {
				return value.getLpwstr();
			} else if (value.isSetI4()) {
				return value.getI4();
			} else if (value.isSetBool()) {
				return value.getBool();
			} else if (value.isSetR8()) {
				return value.getR8();
			}
		} catch (NullPointerException e) {
			System.out.println("Cannot find property with key: " + key);
		}
		return null;
	}
	
	public static final class SpreadsheetMetadataBuilder {
		private POIXMLProperties properties;
		private POIXMLProperties.CoreProperties core;
		private POIXMLProperties.CustomProperties custom;
		private String title;
		private String dataBoundary;
		private Integer dataRows;
		private boolean modifyOnly;				

		public SpreadsheetMetadataBuilder() {
			this.dataBoundary = META_DATA_BOUNDARY;
			this.dataRows = 0;			
		}
		
		public SpreadsheetMetadataBuilder(POIXMLProperties properties) {
			this.properties = properties;
			this.core = properties.getCoreProperties();
			this.custom = properties.getCustomProperties();
			this.dataBoundary = META_DATA_BOUNDARY;
			this.dataRows = 0;
		}
		
		public SpreadsheetMetadataBuilder properties(POIXMLProperties properties) {
			this.properties = properties;
			this.core = properties.getCoreProperties();
			this.custom = properties.getCustomProperties();
			return this;
		}
		
		public SpreadsheetMetadataBuilder modifyOnly(boolean modifyOnly) {
			this.modifyOnly = modifyOnly;
			return this;
		}
		
		public SpreadsheetMetadataBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		public SpreadsheetMetadataBuilder dataBoundary(String dataBoundary) {
			this.dataBoundary = dataBoundary;
			return this;
		}
		
		public SpreadsheetMetadataBuilder dataRows(Integer dataRows) {
			this.dataRows = dataRows;
			return this;
		}
		
		public SpreadsheetMetadata build() {
			if (this.modifyOnly == true) {				
				if (this.dataBoundary == null || this.dataRows == null) {
					throw new NullPointerException();
				}
				if (this.title != null && !this.title.isEmpty()) {
					core.setTitle(this.title);
				}
				custom.addProperty("Data-boundary", this.dataBoundary);
				custom.addProperty("Data-rows", this.dataRows);
				return new SpreadsheetMetadata(this);
			} else {
				if (this.title == null) {
					throw new NullPointerException("Assign a valid spreadsheet title!");
				}
				core.setCreator("Apache POI on Java " + System.getProperty("java.version"));		
				core.setDescription(META_DESCRIPTION);		
				core.setKeywords(META_KEYWORDS);
				core.setCategory(META_CATEGORY);
				core.setTitle(this.title);				
				custom.addProperty("Data-boundary", this.dataBoundary);
				custom.addProperty("Data-rows", this.dataRows);		
				return new SpreadsheetMetadata(this);
			}
		}
	}

}
