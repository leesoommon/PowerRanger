package eee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class EnergyDAO {
	private Connection conn;
	private DBManager manager;
	private PreparedStatement pstmt;
	private Statement stmt;
	private ResultSet rs;
	
	/**	현재 입력된 데이터가 몇월까지 있는지 알아야겠징??
	 * @param year
	 * @return	현재 연월 개수
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int getPresentMonthCount(String year) throws ClassNotFoundException, SQLException {
		manager = new DBManager();
		conn = manager.getInstance();
		int monthCount = 0;
		String sql = "SELECT COUNT(DISTINCT 납기년월) FROM kepco_energy_" + year;
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		if(rs.next()) {monthCount = rs.getInt(1);}
		return monthCount;
	}

	/**
	 * @return 최근 2년 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public List<String> latestYear() throws ClassNotFoundException, SQLException {
		manager = new DBManager();
		conn = manager.getInstance();
		List<String> lateYearList = new ArrayList<String>();
		String sql = "SELECT SUBSTR(TABLE_NAME, 14, 4) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME LIKE 'kepco_energy_%' AND TABLE_SCHEMA = 'power' ORDER BY TABLE_NAME DESC LIMIT 0, 2";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		while(rs.next()) {	lateYearList.add(rs.getString(1)); }
		manager.allClose(pstmt, stmt, rs, conn);
		return lateYearList;
	}
	
	/** 본부그래프 ************************ 최근 2년 월별 데이터
	 * @param lateYlist
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public JSONArray headPowerRanger(List<String> lateYlist) throws ClassNotFoundException, SQLException {
		manager = new DBManager();
		conn = manager.getInstance();
		stmt = conn.createStatement();
		String presentYear = lateYlist.get(0);
		JSONObject pObj = null;
		JSONArray wrapperArr = new JSONArray();
		List<Double> pList = new ArrayList<Double>();				// Present Year 누적 energy List
		List<Double> bList = new ArrayList<Double>();				// Before Year 누적 energy List
		JSONArray jsonPrr = new JSONArray();							// Present Year Json Array
		JSONArray jsonBrr = new JSONArray();							// Before Year Json Array 
		
		String sql = "SELECT 본부, 납기년월, SUM(청구요금-부가세-TV수신료) AS 월금액, SUM(사용량) AS 월사용량 FROM kepco_energy_" + presentYear + " GROUP BY 본부, 납기년월";
		rs = stmt.executeQuery(sql);
		int index = 0;
		while(rs.next()) {
			pObj = new JSONObject();
			pObj.put("head", rs.getString(1));
			pObj.put("month", rs.getString(2));
			pObj.put("money", rs.getString(3));
			pObj.put("energy", rs.getString(4));
			if( index != 0 ) {	pList.add(pList.get(index-1) +  rs.getInt(4)); } 
			else { pList.add((double)rs.getInt(4)); }
			index++;
			jsonPrr.add(pObj);
		}
		wrapperArr.add(jsonPrr);
		if(lateYlist.size() == 2) {
			index = 0;
			String beforeYear = lateYlist.get(1);
			String before = "SELECT 본부, 납기년월, SUM(청구요금-부가세-TV수신료) AS 월금액, SUM(사용량) AS 월사용량 FROM kepco_energy_" + beforeYear + " GROUP BY 본부, 납기년월";
			rs = stmt.executeQuery(before);
			while(rs.next()) {
				pObj = new JSONObject();
				pObj.put("head", rs.getString(1));
				pObj.put("month", rs.getString(2));
				pObj.put("money", rs.getString(3));
				pObj.put("energy", rs.getString(4));
				if( index != 0 ) {	bList.add(bList.get(index-1) +  rs.getInt(4));  } 
				else { bList.add((double)rs.getInt(4)); }
				index++;
				jsonBrr.add(pObj);
			}
			wrapperArr.add(jsonBrr);
			double rate;
			JSONArray rateArr = new JSONArray();
			for(int i = 0; i < pList.size(); i++) {
				rate = ((double)(pList.get(i) / bList.get(i)) - 1) * 100;
				rateArr.add(rate);
			}
			wrapperArr.add(rateArr);
		}
		manager.allClose(pstmt, stmt, rs, conn);
		return wrapperArr;
	}
	
	/** 각 센터 그래프 ***********************  최근 2년 월별 데이터
	 * @param lateYlist
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public JSONArray centerPowerRanger(List<String> lateYlist) throws ClassNotFoundException, SQLException  {
		manager = new DBManager();
		conn = manager.getInstance();
		stmt = conn.createStatement();
		List<Double> pList = new ArrayList<Double>();					// 2017 energy 누적량 담을 List
		List<Double> bList = new ArrayList<Double>();					// 2016 energy 누적량 담을 List
		JSONObject jObj = null;													
		JSONArray jArr = null;
		JSONArray wrapperArr = new JSONArray();
		
		for(int i = 0; i < lateYlist.size(); i++) {									// 최근 연도 검색 값만큼 반복
			jArr = new JSONArray();
			String sql = "SELECT * FROM view_center_" + lateYlist.get(i);
			rs = stmt.executeQuery(sql);
			int index = 0;
			int preindex = 0;
			int rowcount = 0;
			rs.last();
			rowcount = rs.getRow();
			rs.beforeFirst();
			if(i == lateYlist.size()-1) {				
				index = 0;
				pList = bList;
				bList = new ArrayList<>();
			}
			while(rs.next()) {
				jObj = new JSONObject();
				jObj.put("center", rs.getString(1));
				jObj.put("month", rs.getString(2));
				jObj.put("money", rs.getString(3));
				jObj.put("energy", rs.getString(4));
				if (index != 0) {
					bList.add(bList.get(preindex + index - 1) + rs.getInt(4));
				} else
					bList.add((double) rs.getInt(4));
				index++;
				if (index % (rowcount / 3) == 0) {
					preindex = preindex + index;
					index = 0;
				}
				jArr.add(jObj);
			}
			wrapperArr.add(jArr);
		}
		JSONArray rateArr = new JSONArray();
		double rate;
		int lim = 1;
		int beforeIndex = 0;
		int blength = bList.size();    // 36
		for(int i = 0; i < pList.size(); i++) {							// pList.size/3 = 4
			rate = ((pList.get(i) / bList.get(beforeIndex)) - 1) * 100;
			rateArr.add(rate);
			beforeIndex++;
			if ( ((i+1) != pList.size()) && ((i + 1) % (pList.size() / 3) == 0)) {
				beforeIndex = (blength / 3) * lim++;
			}
		}
		wrapperArr.add(rateArr);
		manager.allClose(pstmt, stmt, rs, conn);
		return wrapperArr;
	}
	
	/** 운용팀 그래프 ***************************선택 센터 기준 해당 운용팀 에너지 그래프 
	 * @param lateYlist
	 * @param c
	 * @return	
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public JSONArray TeamEnergyByCenter(List<String> lateYlist, String center) throws ClassNotFoundException, SQLException {
		CenterDAO cDao = new CenterDAO(); 		
		List<String> tList = new ArrayList<>();				// team List 
		tList = cDao.selectTeam(center);					// team list for center
		int monthCount = getPresentMonthCount(lateYlist.get(0));			// 2017년 월 개수
		/******************************/
		manager = new DBManager();
		conn = manager.getInstance();
		stmt = conn.createStatement();
		/******************************/
		List<Double> bList = new ArrayList<Double>();					// 2016 energy 누적량 담을 List
		List<Double> pList = new ArrayList<Double>();					// 2017 energy 누적량 담을 List
		JSONArray wrapper = new JSONArray();  		// 최종 wrapper 여기다 다 담을거야
		JSONArray mediumWrapper = null;					// 중간 wrapper
		JSONArray jArr = null;										// jsonArray
		JSONObject jObj = null;									// jsonObject
		/******************************/								// 센터의 팀리스트 만큼 query 진행
		for(int i = 0; i < tList.size(); i++) {
			String sql = "SELECT 운용팀, 납기년월, SUM(청구요금-부가세-TV수신료) AS 월금액, SUM(사용량) AS 월사용량 FROM kepco_energy_" + lateYlist.get(1) 
					+ " WHERE 운용팀 = '" + tList.get(i).toString() + "' GROUP BY 운용팀, 납기년월 "
					+ "UNION"
					+ " SELECT 운용팀, 납기년월, SUM(청구요금-부가세-TV수신료) AS 월금액, SUM(사용량) AS 월사용량 FROM kepco_energy_" + lateYlist.get(0) 
					+ " WHERE 운용팀 = '" + tList.get(i).toString() +"' GROUP BY 운용팀, 납기년월";
			rs = stmt.executeQuery(sql);
			mediumWrapper = new JSONArray();
			bList = new ArrayList<>();
			pList = new ArrayList<>();
			
			int index = 0; 				// index for 누적 사용량!!!
			jArr = new JSONArray();
			for(int ii = 0; ii < 12; ii++) {
				rs.next();
				jObj = new JSONObject();
				jObj.put("team", rs.getString(1));
				jObj.put("month", rs.getString(2));
				jObj.put("money", rs.getString(3));
				jObj.put("energy", rs.getString(4));
				jArr.add(jObj);
				if (index != 0) {	bList.add(bList.get(index - 1) + rs.getInt(4));	}
				else {	bList.add((double) rs.getInt(4)); }
				index++;
				System.out.println(rs.getString(2));
			}
			mediumWrapper.add(jArr);
			
			index = 0;
			jArr = new JSONArray();
			for(int ii = 0; ii < monthCount; ii++) {
				rs.next();
				jObj = new JSONObject();
				jObj.put("team", rs.getString(1));
				jObj.put("month", rs.getString(2));
				jObj.put("money", rs.getString(3));
				jObj.put("energy", rs.getString(4));
				jArr.add(jObj);
				if (index != 0) {	pList.add(pList.get(index - 1) + rs.getInt(4));	}
				else {	pList.add((double) rs.getInt(4)); }
				index++;
				System.out.println(rs.getString(2));
			}
			mediumWrapper.add(jArr);
			
			JSONArray rateArr = new JSONArray();
			double rate;
			for(int ii = 0; ii < pList.size(); ii++) {							
				rate = ((pList.get(ii) / bList.get(ii)) - 1) * 100;
				rateArr.add(rate);
			}
			mediumWrapper.add(rateArr);
			wrapper.add(mediumWrapper);
		}
		manager.allClose(pstmt, stmt, rs, conn);
		return wrapper;
	}
	
	/** 운용팀 기준 모국 전력량 그래프
	 * @param lateYlist
	 * @param t
	 * @return 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public JSONArray OfficeEnergyByTeam(List<String> lateYlist, String t) throws SQLException, ClassNotFoundException {
		CenterDAO cDao = new CenterDAO(); 		
		List<String> oList = new ArrayList<>();				// team List 
		oList = cDao.selectOffice(t);					// team list for center
		int monthCount = getPresentMonthCount(lateYlist.get(0));			// 2017년 월 개수
		/******************************/
		manager = new DBManager();
		conn = manager.getInstance();
		stmt = conn.createStatement();
		List<Double> bList = new ArrayList<Double>();					// 2016 energy 누적량 담을 List
		List<Double> pList = new ArrayList<Double>();					// 2017 energy 누적량 담을 List
		JSONArray wrapper = new JSONArray();  		// 최종 wrapper 여기다 다 담을거야
		JSONArray mediumWrapper = null;					// 중간 wrapper
		JSONArray jArr = null;										// jsonArray
		JSONObject jObj = null;									// jsonObject
		/******************************/								// 팀의 모국개수 만큼 query 진행할거양 
		for(int i = 0; i < oList.size(); i++) {
			String sql = "SELECT 모국, 납기년월, SUM(청구요금-부가세-TV수신료) AS 월금액, SUM(사용량) AS 월사용량 FROM kepco_energy_" + lateYlist.get(1) 
					+ " WHERE 모국 = '" + oList.get(i).toString() + "' GROUP BY 모국, 납기년월 "
					+ "UNION"
					+ " SELECT 모국, 납기년월, SUM(청구요금-부가세-TV수신료) AS 월금액, SUM(사용량) AS 월사용량 FROM kepco_energy_" + lateYlist.get(0) 
					+ " WHERE 모국 = '" + oList.get(i).toString() +"' GROUP BY 모국, 납기년월";
			rs = stmt.executeQuery(sql);

			mediumWrapper = new JSONArray();
			bList = new ArrayList<>(); // 16년도 누적양 담을 리스트다잉
			pList = new ArrayList<>(); // 이건 17년도이지롱

			int index = 0; // index for 누적 사용량!!!
			jArr = new JSONArray();
			
			if(rs.next()) {
				for(int ii = 0; ii < 12; ii++) {
					jObj = new JSONObject();
					jObj.put("office", rs.getString(1));
					jObj.put("month", rs.getString(2));
					jObj.put("money", rs.getString(3));
					jObj.put("energy", rs.getString(4));
					jArr.add(jObj);
					if (index != 0) {
						bList.add(bList.get(index - 1) + rs.getInt(4));
					} else {
						bList.add((double) rs.getInt(4));
					}
					index++;
					rs.next();
				}
				mediumWrapper.add(jArr);
				index = 0;
				jArr = new JSONArray();
				for (int ii = 0; ii < monthCount; ii++) {
					jObj = new JSONObject();
					jObj.put("office", rs.getString(1));
					jObj.put("month", rs.getString(2));
					jObj.put("money", rs.getString(3));
					jObj.put("energy", rs.getString(4));
					jArr.add(jObj);
					if (index != 0) {	pList.add(pList.get(index - 1) + rs.getInt(4));	} 
					else {	pList.add((double) rs.getInt(4));	}
					index++;
					rs.next();
				}
				mediumWrapper.add(jArr);
				JSONArray rateArr = new JSONArray();
				double rate;
				for (int ii = 0; ii < pList.size(); ii++) {
					rate = ((pList.get(ii) / bList.get(ii)) - 1) * 100;
					rateArr.add(rate);
				}
				mediumWrapper.add(rateArr);
			} else {
				jObj = new JSONObject();
				jObj.put("office", oList.get(i).toString());
				jArr = new JSONArray();
				jArr.add(jObj);
				mediumWrapper.add(jArr);
			}
			wrapper.add(mediumWrapper);
		}
		manager.allClose(pstmt, stmt, rs, conn);
		return wrapper;
	}
	/**
	 * @param lateYlist
	 * @param o
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public JSONArray branchPowerRank(List<String> lateYlist, String o) throws SQLException, ClassNotFoundException {
		int monthCount = getPresentMonthCount(lateYlist.get(0));
		System.out.println(monthCount);
		String mm = "";
		if(monthCount < 10) { 
			mm = "0" + monthCount;
		} else {
			mm = mm + monthCount;
		}
		System.out.println("mm : " + mm);
		manager = new DBManager();
		conn = manager.getInstance();
		stmt = conn.createStatement();
		
		JSONObject jObj = null;
		JSONArray jArr = new JSONArray();
		
		String sql = "SELECT A.국사, A.국사종류, A.주소, A.납기년월, (((A.누적사용량 / B.누적사용량) - 1)*100) AS 누적증감율 "
				+ "FROM branch_sum_2017 A, branch_sum_2016 B "
				+ "WHERE A.모국 = '" + o +"' AND A.국사 = B.국사 AND A.납기년월 = '" + lateYlist.get(0) + mm 
				+ "' AND SUBSTRING(A.납기년월, 5, 2) = SUBSTRING(B.납기년월, 5, 2) ORDER BY 누적증감율";
 				
		rs = stmt.executeQuery(sql);
		while(rs.next()) {
			jObj = new JSONObject();
			jObj.put("branch", rs.getString(1));
			jObj.put("type", rs.getString(2));
			jObj.put("address", rs.getString(3));
			jObj.put("month", rs.getString(4));
			jObj.put("accumulateRate", rs.getString(5));
			jArr.add(jObj);
		}
		System.out.println(jArr);
		return jArr;
	}
	
	// 이건 쿼리 문 두개에 While로 짜봐야징
	public JSONArray BranchRanger(List<String> lateYlist, String branch) throws SQLException, ClassNotFoundException {
		int monthCount = getPresentMonthCount(lateYlist.get(0));			// 2017년 월 개수
		/******************************/
		manager = new DBManager();
		conn = manager.getInstance();
		stmt = conn.createStatement();
		/******************************/								// 팀의 모국개수 만큼 query 진행할거양
		JSONObject obj = null;
		JSONArray jrr = new JSONArray();
		JSONArray wrapper = new JSONArray();
		List<Double> bList = null;
		List<Double> pList = null;
		
		String sql = "SELECT 국사, 납기년월, SUM(청구요금-부가세-TV수신료) AS 월금액, 사용량 FROM kepco_energy_2016 WHERE 국사 = '" + branch + "' GROUP BY 납기년월";
		rs = stmt.executeQuery(sql);
		
		int index = 0;
		bList = new ArrayList<Double>();			// 최근 연도-1	Before Year

		while (rs.next()) {
			obj = new JSONObject();
			obj.put("branch", rs.getString(1).trim());
			obj.put("month", rs.getString(2));
			obj.put("money", rs.getString(3));
			obj.put("energy", rs.getString(4));
			jrr.add(obj);
			if (index != 0) {
				bList.add(bList.get(index - 1) + rs.getInt(4));
			} else {
				bList.add((double) rs.getInt(4));
			}
			index++;
		}		
		wrapper.add(jrr);

		// 여기서부터 변수 재사용을 위한 초기화
		jrr = new JSONArray();
		pList = new ArrayList<Double>();			// 최근 연도		Present Year
		index = 0;
		sql = "SELECT 국사, 납기년월, SUM(청구요금-부가세-TV수신료) AS 월금액, 사용량 FROM kepco_energy_2017 WHERE 국사 = '" + branch + "' GROUP BY 납기년월";
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			obj = new JSONObject();
			obj.put("branch", rs.getString(1).trim());
			obj.put("month", rs.getString(2));
			obj.put("money", rs.getString(3));
			obj.put("energy", rs.getString(4));
			jrr.add(obj);
			if (index != 0) {
				pList.add(pList.get(index - 1) + rs.getInt(4));
			} else {
				pList.add((double) rs.getInt(4));
			}
			index++;
		}		
		wrapper.add(jrr);
		
		JSONArray rateArr = new JSONArray();
		double rate = 0;
		for (int ii = 0; ii < pList.size(); ii++) {
			rate = ((pList.get(ii) / bList.get(ii)) - 1) * 100;
			rateArr.add(rate);
		}
		wrapper.add(rateArr);
		
		System.out.println(wrapper.toString());
		return wrapper;
	}
	

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		EnergyDAO e = new EnergyDAO();
		//e.BranchRanger(e.latestYear(), "북대전 자운2단지");
		//e.branchPowerRank(e.latestYear(), "금산");
		e.TeamEnergyByCenter(e.latestYear(), "대전");
	}

}
