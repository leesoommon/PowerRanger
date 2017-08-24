package eee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class TeamServlet
 */
@WebServlet("/TeamServlet.do")
public class TeamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TeamServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=euc-kr");
		String op = request.getParameter("op");
		PrintWriter pw = response.getWriter();
		if(op.equals("team_List")) {
			CenterDAO cDao = new CenterDAO();
			String c = request.getParameter("param");
			try{
				List<String> tlist = cDao.selectTeam(c);
				JSONArray jsonArr = new JSONArray();
				JSONObject jObj = null;
				for (String t : tlist) {
					jObj = new JSONObject();
					jObj.put("team", t);
					jsonArr.add(jObj);
				}
				pw.print(jsonArr);
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(op.equals("office_List")) {
			CenterDAO cDao = new CenterDAO();
			String t = request.getParameter("param");
			try{
				List<String> olist = cDao.selectOffice(t);
				JSONArray jsonArr = new JSONArray();
				JSONObject jObj = null;
				for (String o : olist) {
					jObj = new JSONObject();
					jObj.put("office", o);
					jsonArr.add(jObj);
				}
				pw.print(jsonArr);
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} else if(op.equals("branchSL")) {
			CenterDAO cDao = new CenterDAO();
			String o = request.getParameter("param");
			String y = request.getParameter("paramsecond");
			List<String> blist;
			try {
				blist = cDao.selectBranch(o, y);
				JSONArray jsonArr = new JSONArray();
				JSONObject jObj = null;
				for(String b : blist) {
					jObj = new JSONObject();
					jObj.put("branch", b);
					jsonArr.add(jObj);
				}
				pw.print(jsonArr);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		

		
		
		
		
		

		pw.flush();pw.close();
	}

}
