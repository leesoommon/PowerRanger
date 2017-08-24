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
		
		KepcoVO kepcoObj = null;						// kepco ��ü ����
		String value;
		try {
			input = new FileInputStream(path);
			workbook = new XSSFWorkbook(input);
			System.out.println("kepco ������?");
		//for(int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {	// sheet 1�� �������ִ°� �� ������.
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
								case 0:		kepcoObj.setHead(value);		break;												// ����
								case 1:		kepcoObj.setCenter(value);		break;												// ����
								case 2:		kepcoObj.setOperation(value);		break;												// �����
								case 3:		kepcoObj.setOffice(value);		break;												// ��
								case 4: 	kepcoObj.setBranch(value);		break;												// ����
								case 5:		kepcoObj.setBranchType(value); 	break;										// ���� ����
								case 6:		kepcoObj.setStatementType(value);	break;									// ������ ����
								case 7:		kepcoObj.setChargeType(value);			break;									// û�� ����
								case 8:		kepcoObj.setCustomerNo(value);;		break;			// �� ��ȣ
								case 9:		kepcoObj.setChargeDate(value);		break;						// û�����
								case 10:	kepcoObj.setUsePeriod((value));		break;						// ��� �Ⱓ
								case 11: 	kepcoObj.setPaymentDate(value);	break;						// ������
								case 12:	kepcoObj.setPaymentDay(value);	break;						// ������
								case 13:	kepcoObj.setPaymentType(value);	break;						// ������ ����
								case 14:   kepcoObj.setAddress(value);			break;						// �ּ�
								case 15:	kepcoObj.setProductNo(value);		break;						// ����ȣ
								case 16:	kepcoObj.setManageField(value);	break;						// ���� �ʵ�
								case 17:	kepcoObj.setHousingName(value);	break;						// �������ø�
								case 18:	kepcoObj.setQuantity((int)Double.parseDouble(value));	break;			// ��뷮
								case 19:	kepcoObj.setChargeMoney((int)Double.parseDouble(value));		break;			// û�����
								case 20:	kepcoObj.setContractPower((int)Double.parseDouble(value));		break;			// ��� ����
								case 21:	kepcoObj.setContractType(value);									break;						// ��� ����
								case 22:	kepcoObj.setMaxPower((int)Double.parseDouble(value));				break;			// �ִ� ����
								case 23:	kepcoObj.setApplyPower((int)Double.parseDouble(value));			break;			// �����������
								case 24:	kepcoObj.setPowerFactor((int)Double.parseDouble(value));			break;			// ����
								case 25:	kepcoObj.setPowerFactorMoney((int)Double.parseDouble(value));	break;		// ���� ���
								case 26:	kepcoObj.setTv((int)Double.parseDouble(value));							break;			// TV
								case 27:	kepcoObj.setTvMoney((int)Double.parseDouble(value));				break;			// TV ���ŷ�
								case 28:	kepcoObj.setTax((int)Double.parseDouble(value));						break;			// �ΰ���
								case 29:	kepcoObj.setCustomerType(value);									break;			// �� ����
								case 30:	kepcoObj.setChargeID(value);											break;			// ���� ID
								case 31:	kepcoObj.setSelectionMoney(value);		break;			// ���� ���
								case 32:	kepcoObj.setKepcoNo(value);				break;			// ���� ���� ��ȣ														
									default:
										break;
								}
						} // cell �� �о���� for ���� ��.
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
