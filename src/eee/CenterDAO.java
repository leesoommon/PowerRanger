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
	 * @return	������ ���� ��				***index page���� Ȱ��***
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int countCenter() throws ClassNotFoundException, SQLException {
		manager = new DBManager();
		conn = manager.getInstance();
		int cRow = 0;
		String sql = "SELECT COUNT(DISTINCT ����) FROM team";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		if(rs.next()) {cRow = rs.getInt(1);}
		manager.allClose(pstmt, stmt, rs, conn);
		return cRow;
	}

	/**
	 * @param t					*** energy.jsp���� Rank Table ������ ���ž� ***
	 * @return	t�� ���� office count �����´�.
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public int countOffice(String t) throws SQLException, ClassNotFoundException {
		manager = new DBManager();
		conn = manager.getInstance();
		int officeCount = 0;
		String sql = "SELECT COUNT(��) FROM team WHERE ����� = '" + t + "'";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		if(rs.next()) {	officeCount = rs.getInt(1);	}
		return officeCount;
	}
	
	
	
	/*** ���� ��� ����Ʈ 						***energy.jsp���� Select Box�� ���� ***
	 * @throws ClassNotFoundException ***/
	public List<String> selectCenter() throws SQLException, ClassNotFoundException {
		manager = new DBManager();
		conn = manager.getInstance();
		List<String> clist = new ArrayList<String>();
		String sql = "SELECT DISTINCT ���� FROM team ";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		while(rs.next()) {
			clist.add(rs.getString(1));
		}
		System.out.println(clist);
		manager.allClose(pstmt, stmt, rs, conn);
		return clist;
	}
	/*** ����� ��� ����Ʈ - ���� ���� 				***energy.jsp���� Select Box�� ���� ***
	 * @throws ClassNotFoundException ***/
	public List<String> selectTeam(String c) throws SQLException, ClassNotFoundException {
		manager = new DBManager();
		conn = manager.getInstance();
		List<String> tlist = new ArrayList<String>();
		
		String sql = "SELECT DISTINCT ����� FROM team WHERE ���� ='" + c +"'";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		while(rs.next()) {
			tlist.add(rs.getString(1));
		}
		manager.allClose(pstmt, stmt, rs, conn);
		return tlist;
	}
	/*** �� ��� ����Ʈ - ����� ���� 				***energy.jsp���� Select Box�� ���� ***
	 * @throws ClassNotFoundException ***/
	public List<String> selectOffice(String t) throws SQLException, ClassNotFoundException {
		manager = new DBManager();
		conn = manager.getInstance();
		List<String> olist = new ArrayList<String>();
		
		String sql = "SELECT DISTINCT �� FROM team WHERE ����� ='" + t +"'";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);;
		while(rs.next()) {
			olist.add(rs.getString(1));
		}
		manager.allClose(pstmt, stmt, rs, conn);
		return olist;
	}
	
	
	
	
	
	
	/*** ���� ��� ����Ʈ - �� ����, ���� ����(DB��)
	 * @param o
	 * @param y
	 * @return blist - ���� ����Ʈ
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public List<String> selectBranch(String o, String y) throws SQLException, ClassNotFoundException {
		manager = new DBManager();
		conn = manager.getInstance();
		List<String> blist = new ArrayList<String>();
		
		String sql = "SELECT DISTINCT ���� FROM kepco_energy_" + y + " WHERE �� = '" + o + "' AND �������� = '�ڱ�'";
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
	
	/** ����, ��, �� ����Ʈ JSON OBJECT ���� - TABLE ��
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public JSONObject centerTeamOfficeTable() throws SQLException, ClassNotFoundException {
		manager = new DBManager();
		conn = manager.getInstance();
		JSONObject obj = new JSONObject();			//�Һз�
		JSONObject oobj = new 	JSONObject();		//�ߺз�
		JSONArray array = new JSONArray();			//��з�
		
		String sql = "SELECT * FROM team WHERE ���� = '����'";
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
		
		String sql = "SELECT DISTINCT ����, ��������, ����ȣ, �ּ�, �������ø� FROM kepco_energy WHERE �� = '" + office + "'";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			infoObj.put("����", rs.getString(1));
			infoObj.put("��������", rs.getString(2));
			infoObj.put("����ȣ", rs.getString(3));
			infoObj.put("�ּ�", rs.getString(4));
			infoObj.put("�������ø�", rs.getString(5));
		}
		manager.allClose(pstmt, stmt, rs, conn);
		return infoObj;
	}
	
	
	/*�𱹺� ���� ����Ʈ �Լ� �ʿ� ��������='�ڱ�'*/

}
