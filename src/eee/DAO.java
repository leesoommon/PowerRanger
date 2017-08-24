package eee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.util.SystemOutLogger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DAO {
	private static DAO dao;
	private static Connection conn = null;
	private static String driver = "com.mysql.jdbc.Driver";
	private static String dbname = "power";
	private static String url = "jdbc:mysql://localhost:3306/" + dbname;
	private static PreparedStatement pstmt = null;
	private static Statement stmt = null;
	private static ResultSet rs; 
	
	private DAO() throws ClassNotFoundException {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "root", "admin");
			System.out.println("DB 접속");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static DAO getInstance() throws ClassNotFoundException {
		if(dao == null) {		dao = new DAO();		}
		return dao;
	}
	
	
	/***************************************************INSERT TEAM***********************************************************
	 * @param list
	 * @throws SQLException
	 * 모국구분 excel sheet DB 입력
	 */
	public static void insertTeam(List<CenterVO> list) throws SQLException {
		String truncate = "TRUNCATE TABLE team";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(truncate);	
		String sql = "INSERT INTO team VALUES (?, ?, ?)";
		pstmt = conn.prepareStatement(sql);
		for(int i = 0 ; i < list.size(); i++) {
			pstmt.setString(1, list.get(i).getCenter());
			pstmt.setString(2, list.get(i).getOperation());
			pstmt.setString(3, list.get(i).getOffice());
			pstmt.addBatch();
			pstmt.clearParameters();
		}
		pstmt.executeBatch();
	}
	/****************************************INSERT KEPCO DATA **************************************************************
	 * @param list
	 * @throws SQLException
	 */
	public static void insertKepco(List<KepcoVO> list) throws SQLException {
		String sql = "INSERT INTO kepco_energy VALUES (?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?)";
		pstmt = conn.prepareStatement(sql);
		conn.setAutoCommit(false);
		for (int i = 0; i < list.size(); i++) {
			pstmt.setString(1, list.get(i).getHead());
			pstmt.setString(2, list.get(i).getCenter());
			pstmt.setString(3, list.get(i).getOperation());
			pstmt.setString(4, list.get(i).getOffice());
			pstmt.setString(5, list.get(i).getBranch());
			pstmt.setString(6, list.get(i).getBranchType());
			pstmt.setString(7, list.get(i).getStatementType());
			pstmt.setString(8, list.get(i).getChargeType());
			pstmt.setString(9, list.get(i).getCustomerNo());
			pstmt.setString(10, list.get(i).getChargeDate());
			pstmt.setString(11, list.get(i).getUsePeriod());
			pstmt.setString(12, list.get(i).getPaymentDate());
			pstmt.setString(13, list.get(i).getPaymentDay());
			pstmt.setString(14, list.get(i).getPaymentType());
			pstmt.setString(15, list.get(i).getAddress());
			pstmt.setString(16, list.get(i).getProductNo());
			pstmt.setString(17, list.get(i).getManageField());
			pstmt.setString(18, list.get(i).getHousingName());
			pstmt.setInt(19, list.get(i).getQuantity());
			pstmt.setInt(20, list.get(i).getChargeMoney());
			pstmt.setInt(21, list.get(i).getContractPower());
			pstmt.setString(22, list.get(i).getContractType());
			pstmt.setInt(23, list.get(i).getMaxPower());
			pstmt.setInt(24, list.get(i).getApplyPower());
			pstmt.setInt(25, list.get(i).getPowerFactor());
			pstmt.setInt(26, list.get(i).getPowerFactorMoney());
			pstmt.setInt(27, list.get(i).getTv());
			pstmt.setInt(28, list.get(i).getTvMoney());
			pstmt.setInt(29, list.get(i).getTax());
			pstmt.setString(30, list.get(i).getCustomerType());
			pstmt.setString(31, list.get(i).getChargeID());
			pstmt.setString(32, list.get(i).getSelectionMoney());
			pstmt.setString(33, list.get(i).getKepcoNo());
			pstmt.addBatch();
			pstmt.clearParameters();
		}
		pstmt.executeBatch();
		conn.commit();
	}
	/*******************************************************************************************************************/
	


	/**
	 * @throws SQLException **************************************************************************************/
	
	
	public static void selectOperationTeam() {
		List<String> slist = new ArrayList<String>();
		try {
			getInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql = "SELECT * FROM team";
		try {
			stmt=conn.createStatement();
			rs = stmt.executeQuery(sql);

			while(rs.next()) {
				slist.add(rs.getString(1));
				slist.add(rs.getString(2));
				slist.add(rs.getString(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(slist);
	}

	public static List<String> aTeam() throws SQLException {
		List<String> list = new ArrayList<String>();								
		String cQuery = "SELECT DISTINCT 센터 FROM team";			
		stmt = conn.createStatement();													
		rs = stmt.executeQuery(cQuery);												
		int iend = getRows(rs);
		System.out.println(iend);	
		while(rs.next()) {
			list.add(rs.getString(1));
			for(int i = 1; i <= iend; i++) {
				String oQuery = "SELECT DISTINCT 운용팀 FROM team WHERE 센터 = '" + rs.getString(1) + "'";
				ResultSet rss;
				stmt = conn.createStatement();
				rss = stmt.executeQuery(oQuery);
				int jend = getRows(rss);
				while(rss.next()) {
					list.add(rss.getString(1));
					for(int j = 1; j<=jend; j++) {
						String tQuery = "SELECT DISTINCT 모국 From team WHERE 운용팀 = '" + rss.getString(1) + "'";
						ResultSet rsss;
						stmt = conn.createStatement();
						rsss = stmt.executeQuery(tQuery);
						while(rsss.next()) {
							//list.add(rs.getString(1));
							//list.add(rss.getString(1));
							list.add(rsss.getString(1));
						}
					}
				}
			}			
		}
		System.out.println("ateam : " +  list);
		return list;
	}	
	public static int getRows(ResultSet rs) throws SQLException {
		int total = 0;
		if(rs.next()) {
			rs.last();
			total = rs.getRow();
			rs.beforeFirst();
		}
		return total;
	}
	
	public static List<String> cTeam() throws SQLException {
		List<String> list = new ArrayList<String>();								
		String cQuery = "SELECT * FROM team";			
		stmt = conn.createStatement();													
		rs = stmt.executeQuery(cQuery);			
		while(rs.next()) {
			list.add(rs.getString(1));
			list.add(rs.getString(2));
			list.add(rs.getString(3));
		}
		System.out.println("bteam : " + list);
		return list;
	}

	

//	
//	public static ArrayLIst<> getPeriod() throws SQLException {
//		String mQuery = "SELECT 청구년월 AS startMonth FROM kepco_energy LIMIT 0,1";			
//		stmt = conn.createStatement();													
//		rs = stmt.executeQuery(mQuery);			
//		String returnData = "";
//		if(rs.next()) {
//			returnData = rs.getString(1);
//		}
//		
//		mQuery = "SELECT 청구년월 AS startMonth FROM kepco_energy ORDER BY 청구년월 DESC LIMIT 0,1";			
//		stmt = conn.createStatement();													
//		rs = stmt.executeQuery(mQuery);			
//		if(rs.next()) {
//			returnData = returnData + "_" + rs.getString(1);
//		}
//		return returnData;
//	}
}
