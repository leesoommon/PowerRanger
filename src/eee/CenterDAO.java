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

public class CenterDAO {
	private Connection conn;
	private DBManager manager;
	private PreparedStatement pstmt;
	private Statement stmt;
	private ResultSet rs;
	
	
	/**
	 * @return	본부의 센터 수				***index page에서 활용***
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int countCenter() throws ClassNotFoundException, SQLException {
		manager = new DBManager();
		conn = manager.getInstance();
		int cRow = 0;
		String sql = "SELECT COUNT(DISTINCT 센터) FROM team";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		if(rs.next()) {cRow = rs.getInt(1);}
		manager.allClose(pstmt, stmt, rs, conn);
		return cRow;
	}

	/**
	 * @param t					*** energy.jsp에서 Rank Table 용으로 쓸거야 ***
	 * @return	t에 대한 office count 가져온다.
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public int countOffice(String t) throws SQLException, ClassNotFoundException {
		manager = new DBManager();
		conn = manager.getInstance();
		int officeCount = 0;
		String sql = "SELECT COUNT(모국) FROM team WHERE 운용팀 = '" + t + "'";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		if(rs.next()) {	officeCount = rs.getInt(1);	}
		return officeCount;
	}
	
	
	
	/*** 센터 출력 리스트 						***energy.jsp에서 Select Box를 위해 ***
	 * @throws ClassNotFoundException ***/
	public List<String> selectCenter() throws SQLException, ClassNotFoundException {
		manager = new DBManager();
		conn = manager.getInstance();
		List<String> clist = new ArrayList<String>();
		String sql = "SELECT DISTINCT 센터 FROM team ";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		while(rs.next()) {
			clist.add(rs.getString(1));
		}
		System.out.println(clist);
		manager.allClose(pstmt, stmt, rs, conn);
		return clist;
	}
	/*** 운용팀 출력 리스트 - 센터 기준 				***energy.jsp에서 Select Box를 위해 ***
	 * @throws ClassNotFoundException ***/
	public List<String> selectTeam(String c) throws SQLException, ClassNotFoundException {
		manager = new DBManager();
		conn = manager.getInstance();
		List<String> tlist = new ArrayList<String>();
		
		String sql = "SELECT DISTINCT 운용팀 FROM team WHERE 센터 ='" + c +"'";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		while(rs.next()) {
			tlist.add(rs.getString(1));
		}
		manager.allClose(pstmt, stmt, rs, conn);
		return tlist;
	}
	/*** 모국 출력 리스트 - 운용팀 기준 				***energy.jsp에서 Select Box를 위해 ***
	 * @throws ClassNotFoundException ***/
	public List<String> selectOffice(String t) throws SQLException, ClassNotFoundException {
		manager = new DBManager();
		conn = manager.getInstance();
		List<String> olist = new ArrayList<String>();
		
		String sql = "SELECT DISTINCT 모국 FROM team WHERE 운용팀 ='" + t +"'";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);;
		while(rs.next()) {
			olist.add(rs.getString(1));
		}
		manager.allClose(pstmt, stmt, rs, conn);
		return olist;
	}
	
	
	
	
	
	
	/*** 국사 출력 리스트 - 모국 기준, 연도 기준(DB명)
	 * @param o
	 * @param y
	 * @return blist - 국사 리스트
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public List<String> selectBranch(String o, String y) throws SQLException, ClassNotFoundException {
		manager = new DBManager();
		conn = manager.getInstance();
		List<String> blist = new ArrayList<String>();
		
		String sql = "SELECT DISTINCT 국사 FROM kepco_energy_" + y + " WHERE 모국 = '" + o + "' AND 국사종류 = '자국'";
		System.out.println(sql);
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		while(rs.next()) {
			blist.add(rs.getString(1));
			System.out.println(rs.getString(1));
		}
		System.out.println(blist);
		manager.allClose(pstmt, stmt, rs, conn);
		return blist;
	}
	
	/** 센터, 팀, 모국 리스트 JSON OBJECT 생성 - TABLE 용
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public JSONObject centerTeamOfficeTable() throws SQLException, ClassNotFoundException {
		manager = new DBManager();
		conn = manager.getInstance();
		JSONObject obj = new JSONObject();			//소분류
		JSONObject oobj = new 	JSONObject();		//중분류
		JSONArray array = new JSONArray();			//대분류
		
		String sql = "SELECT * FROM team WHERE 센터 = '대전'";
		stmt=conn.createStatement();
		rs=stmt.executeQuery(sql);
		
		while(rs.next()) {
			if(!oobj.containsKey(rs.getString(2))) {
				array = new JSONArray();
			}
			array.add(rs.getString(3));
			oobj.put(rs.getString(2), array);
			obj.put(rs.getString(1), oobj);
		}
		System.out.println(obj.toJSONString());
		manager.allClose(pstmt, stmt, rs, conn);
		return obj;
	}
	
	public JSONObject officeInformation(String office) throws SQLException, ClassNotFoundException {
		manager = new DBManager();
		conn = manager.getInstance();
		JSONObject infoObj = new JSONObject();
		
		String sql = "SELECT DISTINCT 국사, 국사종류, 고객번호, 주소, 공동주택명 FROM kepco_energy WHERE 모국 = '" + office + "'";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			infoObj.put("국사", rs.getString(1));
			infoObj.put("국사종류", rs.getString(2));
			infoObj.put("고객번호", rs.getString(3));
			infoObj.put("주소", rs.getString(4));
			infoObj.put("공동주택명", rs.getString(5));
		}
		manager.allClose(pstmt, stmt, rs, conn);
		return infoObj;
	}
	
	
	/*모국별 국사 리스트 함수 필요 국사종류='자국'*/

}
