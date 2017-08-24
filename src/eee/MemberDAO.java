package eee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
	private Connection conn;
	private DBManager manager;
	private PreparedStatement pstmt;
	private Statement stmt;
	private ResultSet rs;
	
	public boolean checkMember(String user, String password) throws ClassNotFoundException, SQLException {
		manager = new DBManager();
		conn = manager.getInstance();
		boolean result = false;
		String sql = "SELECT * FROM member WHERE 아이디 = '" + user + "' AND 비밀번호 = '" + password + "';";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		if(rs.next()) {
			String pass = password;
			if(pass.equals(password)) {
				result = true;
			}
		} else {
			result = false;
		}
		return result;
	}
	
	public String checkcheck(String user, String password) throws ClassNotFoundException, SQLException {
		manager = new DBManager();
		conn = manager.getInstance();
		//boolean result = false;
		String result = null;
		String sql = "SELECT 이름 FROM member WHERE 아이디 = '" + user + "' AND 비밀번호 = '" + password + "';";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		if(rs.next()) {
			String pass = password;
			if(pass.equals(password)) {
				System.out.println(rs.getString(1));
				return rs.getString(1);
			}
		} else {
			result = null;
		}
		return result;
	}
	
}
