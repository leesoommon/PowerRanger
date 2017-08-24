package eee;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CenterXls {

	public List<CenterVO> xlsToCenterList(String path) {
		List<CenterVO> list = new ArrayList<CenterVO>();
		
		FileInputStream input = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet;
		XSSFRow row;
		XSSFCell cell;
		
		CenterVO centerObj = null;
		String value;
		
		try {
			input = new FileInputStream(path);
			workbook = new XSSFWorkbook(input);
		
			for(int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
				sheet = workbook.getSheetAt(sheetIndex);
				for(int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
					row = sheet.getRow(rowIndex);
					centerObj = new CenterVO();

					if(row != null) {
						for(int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) {
							cell = row.getCell(cellIndex);
							value = "";
							
							if(cell != null) {
								switch(cell.getCellType()) {
								case XSSFCell.CELL_TYPE_FORMULA:	value = cell.getCellFormula();
								break;
								case XSSFCell.CELL_TYPE_NUMERIC:	value = cell.getNumericCellValue() + "";
								break;
								case XSSFCell.CELL_TYPE_STRING:	value = cell.getStringCellValue() + "";
								break;
								case XSSFCell.CELL_TYPE_BLANK:	value = cell.getBooleanCellValue() + "";
								break;
								case XSSFCell.CELL_TYPE_ERROR:	value = cell.getErrorCellValue() + "";
								break;
								default: value = new String();
								break;
								}
							}
							
							if(cell != null) {
								switch(cellIndex) {
								case 0: centerObj.setCenter(value);
								break;
								case 1: centerObj.setOperation(value);
								break;
								case 2: centerObj.setOffice(value);
								break;
								default:
									break;
								}
							}
						}
					}
					list.add(centerObj);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if(workbook != null) {try {
				workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} }
			if(input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;
	}	
}
