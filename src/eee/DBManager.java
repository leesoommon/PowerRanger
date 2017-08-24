package eee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	private Connection conn = null;
	private String driver = "com.mysql.jdbc.Driver";
	private String dbname = "power";
	private String url = "jdbc:mysql://localhost:3306/" + dbname;
	
	public DBManager() throws ClassNotFoundException {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "root", "admin");
			System.out.println("DB Á¢¼Ó");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Connection getInstance() throws ClassNotFoundException {
		return conn;
	}
	
	public void allClose(PreparedStatement p, Statement s, ResultSet rs, Connection c ) throws SQLException {
		if(p!= null) { 
			p.close();
		}
		if(s!=null) {
			s.close();
		}
		if(c!=null) {
			c.close();
		}
		if(rs!=null) {
			rs.close();
		}
	}
}
