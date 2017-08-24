package eee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ModalDAO {
	private Connection conn;
	private DBManager manager;
	private PreparedStatement pstmt;
	private Statement stmt;
	private ResultSet rs;
	
	/**		Modal에 값 넣기, DUPLICATE 되면 UPDATE
	 * @param c
	 * @param t
	 * @param o
	 * @param y
	 * @param v
	 * @param name
	 * @param description
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public String InsertModalContents(String center, String team, String office, String year, String v, String name, String description) throws ClassNotFoundException, SQLException {
		EnergyDAO eDao = new EnergyDAO();
		manager = new DBManager();
		conn = manager.getInstance();
		/*************************************************/
		String ly = eDao.latestYear().get(1);
		int inum = Integer.parseInt(year)+1;
		if(inum < 10) {
			year = ly + "0" + String.valueOf(inum);
		} else {
			year = ly + String.valueOf(inum);
		}
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy년 MM월 dd일 HH시 mm분", Locale.KOREA );
		Date date = new Date ( );
		String now = sdf.format ( date );
		/*************************************************/
		
		String sql = "INSERT INTO modal_contents VALUES(?, ?, ?, ?, ?, ?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE 이름 = '" + name + "', 작업내용 = '" + description + "', 갱신시간 = '" + now + "'";
		System.out.println(sql);
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, center);
		pstmt.setString(2, team);
		pstmt.setString(3, office);
		pstmt.setString(4, year);
		pstmt.setString(5, v);
		pstmt.setString(6, name);
		pstmt.setString(7, description);
		pstmt.setString(8,  now);
		if(pstmt.executeUpdate() != 0) {
			return "성공";
		} else { 
			return "실패";
		}
	}
	
	/**	Exist value Check in Modal
	 * @param c
	 * @param t
	 * @param temp
	 * @param idx
	 * @param val
	 * @return 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public JSONObject ModalCheck(String c, String t, String o, String y, String v) throws ClassNotFoundException, SQLException {
		EnergyDAO eDao = new EnergyDAO();
		manager = new DBManager();
		conn = manager.getInstance();
		stmt = conn.createStatement();
		String year = eDao.latestYear().get(1);
		int inum = Integer.parseInt(y)+1;
		if(inum < 10) {
			y = year + "0" + String.valueOf(inum);
		} else {
			y = year + String.valueOf(inum);
		}
		if(o.contains("운용팀")) {
			t = o.substring(0, o.indexOf(" "));
			o = "-";
			System.out.println("t = " + t + ", o : " + o);
		} else if(o.contains("모국")) {
			o = o.substring(0, o.indexOf(" "));
		}
		String sql = "SELECT * FROM modal_contents WHERE 센터 = '" + c + "' AND 운용팀 = '" + t + "' AND 모국 = '" + o + "' AND 기준월 = '" + y + "' AND 누적증감율 = '" + v + "'";
		rs = stmt.executeQuery(sql);
		JSONObject mobj = new JSONObject();
		while(rs.next()) {
			mobj.put("name", rs.getString(6));
			mobj.put("description", rs.getString(7));
			mobj.put("time", rs.getString(8));
		}
		System.out.println(mobj);
		return mobj;
	}
	
	
	// Equipment 관련 함수지만 Modal에 들어가는 내용이니 여기다 짜겠다. 잊어먹지 말자
	
	public List<String> showEquipField() throws ClassNotFoundException, SQLException {
		manager = new DBManager();
		conn = manager.getInstance();
		stmt = conn. createStatement();
		ArrayList<String> equipField = new ArrayList<String>();
		String sql = "SELECT 분야별 FROM equipment GROUP BY 분야별";
		rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			equipField.add(rs.getString(1));
		}
		System.out.println(equipField);
		return equipField;
	}
	
	public List<String> showLargeCategory(String field) throws ClassNotFoundException, SQLException {
		manager = new DBManager();
		conn = manager.getInstance();
		stmt = conn. createStatement();
		ArrayList<String> largeCate = new ArrayList<String>();
		String sql = "SELECT 대분류 FROM equipment WHERE 분야별 = '" + field + "' GROUP BY 대분류";
		rs = stmt.executeQuery(sql);
		while(rs.next()) {	largeCate.add(rs.getString(1));	}
		return largeCate;
	}
	
	public List<String> showSmallCategory(String field, String large) throws ClassNotFoundException, SQLException {
		manager = new DBManager();
		conn = manager.getInstance();
		stmt = conn. createStatement();
		ArrayList<String> smallCate = new ArrayList<String>();
		String sql = "SELECT 소분류 FROM equipment WHERE 분야별 = '" + field + "' AND 대분류 = '" + large + "' GROUP BY 소분류";
		rs = stmt.executeQuery(sql);
		while(rs.next()) {	smallCate.add(rs.getString(1));	}
		
		System.out.println(smallCate);
		return smallCate;
	}
	
	public List<String> showEquipCategory(String field, String large, String small) throws ClassNotFoundException, SQLException {
		manager = new DBManager();
		conn = manager.getInstance();
		stmt = conn. createStatement();
		ArrayList<String> equipCate = new ArrayList<String>();
		String sql = "SELECT 장비모델 FROM equipment WHERE 분야별 = '" + field + "' AND 대분류 = '" + large + "' AND 소분류 = '" + small + "' GROUP BY 장비모델";
		rs = stmt.executeQuery(sql);
		while(rs.next()) {	equipCate.add(rs.getString(1));	}
		
		System.out.println(equipCate);
		return equipCate;
	}
	
	
	public String showEquipPower(String model) throws ClassNotFoundException, SQLException {
		manager = new DBManager();
		conn = manager.getInstance();
		stmt = conn. createStatement();
		String ee;
		String sql = "SELECT 소모전력 FROM equipment WHERE 장비모델 = '" + model + "'";
		rs = stmt.executeQuery(sql);
		if(rs.next()) {
			ee = rs.getString(1);
		}
		else ee = "0";
		System.out.println(ee);
		return ee;
	}
	
	
	/**		Modal에 값 넣기, DUPLICATE 되면 UPDATE
	 * @param c
	 * @param t
	 * @param o
	 * @param y
	 * @param v
	 * @param name
	 * @param description
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean InsertAnalysisContents(String center, String team, String office, String date, String work, String field, String big, String small, String equip, String power, String count, String pp, String worker, String des) throws ClassNotFoundException, SQLException {
		manager = new DBManager();
		conn = manager.getInstance();
		/*************************************************/
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy년 MM월 dd일 HH시 mm분", Locale.KOREA );
		Date d = new Date ( );
		String now = sdf.format ( d );
		/*************************************************/
		
		String sql = "INSERT INTO energy_analysis VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
				//+ "ON DUPLICATE KEY UPDATE 작업자 = '" + worker + "', 작업내용 = '" + des + "', 갱신시간 = '" + now + "'";
		System.out.println(sql);
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, center);								// center
		pstmt.setString(2, team);								// team
		pstmt.setString(3, office);								// office
		pstmt.setString(4, date);								// date
		pstmt.setString(5, work);								// work
		pstmt.setString(6, field);								// field
		pstmt.setString(7, big);								// big category
		pstmt.setString(8,  small);								// small category
		pstmt.setString(9,  equip);								// equip
		pstmt.setString(10,  count);						// count
		pstmt.setString(11,  power);							// power
		pstmt.setString(12,  pp);
		pstmt.setString(13,  worker);
		pstmt.setString(14,  des);
		pstmt.setString(15, now);
		if(pstmt.executeUpdate() != 0) {
			return true;
		} else { 
			return false;
		}
	}
	
	/**
	 * @param c - 센터
	 * @param t - 운용팀
	 * @param o - 모국
	 * @param i - 기준월 Index
	 * @return 기준일, 작업, 분야, 장비, 장비전력, 작업자, 작업내용, 갱신시간
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParseException 
	 */
	public JSONArray showAnalysis(String center, String team, String office, String index) throws ClassNotFoundException, SQLException, ParseException {
		EnergyDAO eDao = new EnergyDAO();
		/***************************************************/
		manager = new DBManager();
		conn = manager.getInstance();
		stmt = conn.createStatement();
		/***************************************************/
		String year = eDao.latestYear().get(0);
		int inum = Integer.parseInt(index) + 1;
		if(inum < 10) {
			index = year + "-0" + String.valueOf(inum+1) + "-01";
			System.out.println(index);
		} else {
			index = year + "-" + String.valueOf(inum+1) + "-01";
		}
		if(office.contains("운용팀")) {
			team = office.substring(0, office.indexOf(" "));
			office = "-";
			System.out.println("t = " + team + ", o : " + office);
		} else if(office.contains("모국")) {
			office = office.substring(0, office.indexOf(" "));
		}
		System.out.println(inum);
		/*************************************************/
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), inum-1, 1);
		int end = cal.getActualMaximum(cal.DATE);
		String last = year + "-" + String.valueOf(inum) + "-" + end;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		/***************************************************/
		String sql = "SELECT 기준일, 작업, 분야, 장비, 장비전력, 시스템수, 작업자, 작업내용, 갱신시간 FROM energy_analysis WHERE 센터 = '" + center + "' AND 운용팀 = '" + team + "' AND 모국 = '" + office + "' AND 기준일 <= '" + index + "'";
		rs = stmt.executeQuery(sql);
		JSONArray jArr = new JSONArray();
		JSONObject obj = null;
		
		while(rs.next()) {
			obj = new JSONObject();
			obj.put("date", rs.getString(1));
			obj.put("type", rs.getString(2));
			obj.put("field", rs.getString(3));
			obj.put("equip", rs.getString(4));
			obj.put("power", rs.getString(5));
			obj.put("count", rs.getString(6) + "개");
			obj.put("worker", rs.getString(7));
			
			// 날짜 기준 - 기준일(Select로 가져온거 rs.1) 여기서 해줘야겟네 새로운 변수 총전력(rs.get5 * 24* day) 로 해서만들어서 해결해야돼.. obj에 put한다. 그리고 뿌려서 값은 알아서 가져 가는걸로
			Date startDate = sdf.parse(rs.getString(1));
			Date endDate = sdf.parse(last);
			
			long mil = startDate.getTime() - endDate.getTime();
			long diff = mil / (24*60*60*1000);
			
			diff = Math.abs(diff);
			if(diff == 0) {
				diff = 1;
			}
			double nowPower = Double.parseDouble(rs.getString(5)) * 24 * diff * Double.parseDouble(rs.getString(6)) / 1000;
			String allPower = null;
			if(rs.getString(2).equals("철거")) {
				allPower = String.valueOf(nowPower *= -1.0);
			} else  {
				allPower = "+"+nowPower;
			}
			obj.put("description", rs.getString(8));
			obj.put("timestamp", rs.getString(9));
			obj.put("monster", allPower + " kWh");
			jArr.add(obj);
		}
		System.out.println(jArr.toJSONString());
		return jArr;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		ModalDAO m = new ModalDAO();
		//m.showEquipPower("MAG-3500");
		m.showAnalysis("대전", "대전", "금산", "5");
	}
}
