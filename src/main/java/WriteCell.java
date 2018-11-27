import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
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
	public static enum DataType {Text, NumericDecimal, DateTime, Empty};
	protected Sheet sheet;
	protected Cell cell;	

	public WriteCell() { }
	
	public WriteCell(WriteCellBuilder builder) {
		this.cell = builder.cell;
		this.sheet = builder.sheet;
	}
	
	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public Cell getCell() {
		return cell;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public static final class WriteCellBuilder {
		private Sheet sheet;
		private Object value;
		private XSSFFont font;		
		private CellType type;
		private CellStyle style;
		private Cell cell;
		private HorizontalAlignment horizontalAlignment;
		private FillPatternType fillPatternType;
		private IndexedColors indexedColor;		
		private Integer columnIndex, columnWidth;
		private Integer[] cellRangeIndices;
		private short dataFormat;
		private CreationHelper creationHelper;
		
		public WriteCellBuilder(XSSFWorkbook workbook, Sheet sheet, Row row, int column) {
			this.creationHelper = workbook.getCreationHelper();
			this.sheet = sheet;
			this.style = workbook.createCellStyle();
			this.cell = row.createCell(column);
			this.dataFormat = creationHelper.createDataFormat().getFormat("General");
			this.type = CellType.STRING;
			this.font = new SpreadsheetFont.SpreadsheetFontBuilder(workbook).build().getFont();
			this.horizontalAlignment = DEFAULT_HORIZONTAL_ALIGNMENT;
			this.indexedColor = DEFAULT_INDEX_COLOR;
			this.fillPatternType = DEFAULT_FILL_PATTERN_TYPE;
		}
		
		public WriteCellBuilder cellStyleDataType(DataType dataType) {
			switch (dataType) {
				case NumericDecimal:
					this.type = CellType.NUMERIC;
					this.dataFormat = creationHelper.createDataFormat().getFormat("#,##0.00");
					break;
				case DateTime:
					this.type = CellType.NUMERIC;
					this.dataFormat = creationHelper.createDataFormat().getFormat("yyyy-mm-dd HH:mm");
					break;
				case Empty:
					this.type = CellType.BLANK;
					break;
				case Text:
				default:
					this.type = CellType.STRING;
					
			}
			return this;
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
		
		public WriteCellBuilder value(Object object) {
			this.value = object;
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
			if (this.columnIndex != null && this.columnWidth != null) {
				sheet.setColumnWidth(this.columnIndex, this.columnWidth);
			}
			if (this.cellRangeIndices != null && this.cellRangeIndices.getClass().isArray() && this.cellRangeIndices.length == 4) {
				sheet.addMergedRegion(new CellRangeAddress(this.cellRangeIndices[0], this.cellRangeIndices[1], this.cellRangeIndices[2], this.cellRangeIndices[3]));
			}
			if (this.horizontalAlignment == null) {
				this.horizontalAlignment = DEFAULT_HORIZONTAL_ALIGNMENT;
			}
			if (this.value instanceof String) {
				cell.setCellValue(this.value.toString());
			} else if (this.value instanceof Double) {
				Double value = (double) this.value;
				cell.setCellValue(value.doubleValue()); 
			} else if (this.value instanceof Date) {
				Date value = (Date) this.value;
				cell.setCellValue(value);
			}
			style.setFont(this.font);
			style.setAlignment(this.horizontalAlignment);
			style.setFillForegroundColor(this.indexedColor.getIndex());
			style.setFillPattern(this.fillPatternType);
			style.setDataFormat(this.dataFormat);
			cell.setCellStyle(this.style);
			cell.setCellType(this.type);						
			return new WriteCell(this);
		}
	}
}
