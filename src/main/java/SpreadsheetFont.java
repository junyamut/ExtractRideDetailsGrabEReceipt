import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SpreadsheetFont {
	public static final String DEFAULT_FONT_NAME = "Arial";
	public static final short DEFAULT_FONT_HEIGHT = 10;	
	private final XSSFFont font;		
	private final String name;
	private final short color;
	private final short height;
	private final boolean bold;
	private final boolean italic;

	public SpreadsheetFont(SpreadsheetFontBuilder builder) {
		this.font = builder.font;
		this.color = builder.color;
		this.name = builder.name;
		this.height = builder.height;
		this.bold = builder.bold;
		this.italic = builder.italic;
	}
	
	public XSSFFont getFont() {
		return font;
	}

	public short getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	public short getHeight() {
		return height;
	}

	public boolean isBold() {
		return bold;
	}

	public boolean isItalic() {
		return italic;
	}

	public static final class SpreadsheetFontBuilder {
		private XSSFFont font;			
		private String name;
		private short color;
		private short height;
		private boolean bold;
		private boolean italic;
		
		public SpreadsheetFontBuilder(XSSFWorkbook workbook) { 
			this.font = ((XSSFWorkbook) workbook).createFont();
		}
		
		public SpreadsheetFontBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public SpreadsheetFontBuilder height(int height) {
			this.height = (short) height;
			return this;
		}
		
		public SpreadsheetFontBuilder color(short color) {
			this.color = color;
			return this;
		}
		
		public SpreadsheetFontBuilder bold(boolean bold) {
			this.bold = bold;
			return this;
		}
		
		public SpreadsheetFontBuilder italic(boolean italic) {
			this.italic = italic;
			return this;
		}
		
		public SpreadsheetFont build() {
			if (name == null) {
				this.name = DEFAULT_FONT_NAME;
			}
			if (height < 6) {
				this.height = DEFAULT_FONT_HEIGHT;
			}
			this.font.setFontName(name);
			this.font.setFontHeightInPoints(height);
			this.font.setColor(color);		
			this.font.setBold(bold);
			this.font.setItalic(italic);
			return new SpreadsheetFont(this);
		}
	}

}
