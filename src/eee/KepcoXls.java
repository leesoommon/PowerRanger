package eee;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class KepcoXls {
public List<KepcoVO> xlsToKepco(String path) {
		
		List<KepcoVO> list = new ArrayList<KepcoVO>();
		
		FileInputStream input = null;

		XSSFWorkbook workbook = null;
		XSSFSheet sheet;
		XSSFRow row;
		XSSFCell cell;
		
		KepcoVO kepcoObj = null;						// kepco 객체 선언
		String value;
		try {
			input = new FileInputStream(path);
			workbook = new XSSFWorkbook(input);
			System.out.println("kepco 들어오냐?");
		//for(int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {	// sheet 1로 지정해주는게 더 빠르다.
				sheet = workbook.getSheetAt(0);
				for(int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
					row = sheet.getRow(rowIndex);
					kepcoObj = new KepcoVO();
					if(row != null) {
						for(int cellIndex = 0; cellIndex < 33; cellIndex++) {
							
							cell = row.getCell(cellIndex);
							value = "";
						
							if(cell != null) {
								switch(cell.getCellType()) {
								case XSSFCell.CELL_TYPE_FORMULA:	value = cell.getCellFormula();
									break;
								case XSSFCell.CELL_TYPE_NUMERIC:	cell.setCellType(cell.CELL_TYPE_STRING);
									value = cell.getStringCellValue() + "";
									break;
								case XSSFCell.CELL_TYPE_STRING:		value = cell.getStringCellValue() + "";
									break;
								case XSSFCell.CELL_TYPE_BLANK:		value = cell.getBooleanCellValue() + "";
									break;
								case XSSFCell.CELL_TYPE_ERROR:		value = cell.getErrorCellValue() + "";
									break;
									default:
										value = "";
										break;
								}
							} else {
								value = "";
							}
								switch (cellIndex) {
								case 0:		kepcoObj.setHead(value);		break;												// 본부
								case 1:		kepcoObj.setCenter(value);		break;												// 센터
								case 2:		kepcoObj.setOperation(value);		break;												// 운용팀
								case 3:		kepcoObj.setOffice(value);		break;												// 모국
								case 4: 	kepcoObj.setBranch(value);		break;												// 국사
								case 5:		kepcoObj.setBranchType(value); 	break;										// 국사 종류
								case 6:		kepcoObj.setStatementType(value);	break;									// 내역서 구분
								case 7:		kepcoObj.setChargeType(value);			break;									// 청구 구분
								case 8:		kepcoObj.setCustomerNo(value);;		break;			// 고객 번호
								case 9:		kepcoObj.setChargeDate(value);		break;						// 청구년월
								case 10:	kepcoObj.setUsePeriod((value));		break;						// 사용 기간
								case 11: 	kepcoObj.setPaymentDate(value);	break;						// 납기년월
								case 12:	kepcoObj.setPaymentDay(value);	break;						// 납기일
								case 13:	kepcoObj.setPaymentType(value);	break;						// 납기일 구분
								case 14:   kepcoObj.setAddress(value);			break;						// 주소
								case 15:	kepcoObj.setProductNo(value);		break;						// 계기번호
								case 16:	kepcoObj.setManageField(value);	break;						// 관리 필드
								case 17:	kepcoObj.setHousingName(value);	break;						// 공동주택명
								case 18:	kepcoObj.setQuantity((int)Double.parseDouble(value));	break;			// 사용량
								case 19:	kepcoObj.setChargeMoney((int)Double.parseDouble(value));		break;			// 청구요금
								case 20:	kepcoObj.setContractPower((int)Double.parseDouble(value));		break;			// 계약 전력
								case 21:	kepcoObj.setContractType(value);									break;						// 계약 종별
								case 22:	kepcoObj.setMaxPower((int)Double.parseDouble(value));				break;			// 최대 전력
								case 23:	kepcoObj.setApplyPower((int)Double.parseDouble(value));			break;			// 요금적용전력
								case 24:	kepcoObj.setPowerFactor((int)Double.parseDouble(value));			break;			// 역율
								case 25:	kepcoObj.setPowerFactorMoney((int)Double.parseDouble(value));	break;		// 역율 요금
								case 26:	kepcoObj.setTv((int)Double.parseDouble(value));							break;			// TV
								case 27:	kepcoObj.setTvMoney((int)Double.parseDouble(value));				break;			// TV 수신료
								case 28:	kepcoObj.setTax((int)Double.parseDouble(value));						break;			// 부가세
								case 29:	kepcoObj.setCustomerType(value);									break;			// 고객 구분
								case 30:	kepcoObj.setChargeID(value);											break;			// 빌링 ID
								case 31:	kepcoObj.setSelectionMoney(value);		break;			// 선택 요금
								case 32:	kepcoObj.setKepcoNo(value);				break;			// 한전 관리 번호														
									default:
										break;
								}
						} // cell 값 읽어오는 for 구문 끝.
						list.add(kepcoObj);
					}	
				}
			//}
		}
		catch (IOException e)
		{ e.printStackTrace();}
		finally {
				if(workbook != null) { try {
					workbook.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
				if(input != null) { try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} }
		}
		return list;
	}
}
