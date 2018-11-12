import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteCell {		
	private static final HorizontalAlignment DEFAULT_HORIZONTAL_ALIGNMENT = HorizontalAlignment.LEFT;
	private static final IndexedColors DEFAULT_INDEX_COLOR = IndexedColors.BLACK;
	private static final FillPatternType DEFAULT_FILL_PATTERN_TYPE = FillPatternType.NO_FILL;
	protected Sheet sheet;
	protected Cell cell;	

	public WriteCell() { }
	
	public WriteCell(WriteCellBuilder builder) {
		this.cell = builder.cell;
		this.sheet = builder.sheet;
	}
	
	public Cell getCell() {
		return cell;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public static final class WriteCellBuilder {
		private XSSFWorkbook workbook;
		private Sheet sheet;
		private String value;
		private XSSFFont font;
		private CellStyle style;
		private Cell cell;
		private HorizontalAlignment horizontalAlignment;
		private FillPatternType fillPatternType;
		private IndexedColors indexedColor;
		private Integer columnIndex, columnWidth;
		private Integer[] cellRangeIndices;
		
		public WriteCellBuilder(XSSFWorkbook workbook, Sheet sheet, Row row, int column) {
			this.workbook = workbook;
			this.sheet = sheet;
			this.style = workbook.createCellStyle();
			this.cell = row.createCell(column);
		}
		
		public WriteCellBuilder cellColumnWidth(Integer columnIndex, Integer columnWidth) {
			this.columnIndex = columnIndex;
			this.columnWidth = columnWidth;
			return this;
		}
		
		public WriteCellBuilder mergeCells(Integer ...cellRangeIndices) {
			this.cellRangeIndices = cellRangeIndices;
			return this;
		}
		
		public WriteCellBuilder value(String value) {
			this.value = value;
			return this;
		}
		
		public WriteCellBuilder cellStyleFont(XSSFFont font) {
			this.font = font;
			return this;
		}
		
		public WriteCellBuilder cellStyleAlignment(HorizontalAlignment horizontalAlignment) {
			this.horizontalAlignment = horizontalAlignment;
			return this;
		}
		
		public WriteCellBuilder cellStyleFillForegroundColor(IndexedColors indexedColor) {
			this.indexedColor = indexedColor;
			return this;
		}
		
		public WriteCellBuilder cellStyleFillPattern(FillPatternType fillPatternType) {
			this.fillPatternType = fillPatternType;
			return this;
		}			
		
		public WriteCell build() {
			if (this.font == null) {
				this.font = new SpreadsheetFont.SpreadsheetFontBuilder(workbook).build().getFont();
			}
			if (this.horizontalAlignment == null) {
				this.horizontalAlignment = DEFAULT_HORIZONTAL_ALIGNMENT;				
			}
			if (this.indexedColor == null) {
				this.indexedColor = DEFAULT_INDEX_COLOR;
			}
			if (this.fillPatternType == null) {
				this.fillPatternType = DEFAULT_FILL_PATTERN_TYPE;
			} 
			if (this.columnIndex != null && this.columnWidth != null) {
				sheet.setColumnWidth(this.columnIndex, this.columnWidth);
			}
			if (this.cellRangeIndices != null && this.cellRangeIndices.getClass().isArray() && this.cellRangeIndices.length == 4) {
				sheet.addMergedRegion(new CellRangeAddress(this.cellRangeIndices[0], this.cellRangeIndices[1], this.cellRangeIndices[2], this.cellRangeIndices[3]));
			}
			style.setFont(this.font);
			style.setAlignment(this.horizontalAlignment);
			style.setFillForegroundColor(this.indexedColor.getIndex());
			style.setFillPattern(this.fillPatternType);
			cell.setCellValue(this.value);
			cell.setCellStyle(this.style);
			return new WriteCell(this);
		}
	}
}
