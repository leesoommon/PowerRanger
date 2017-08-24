package eee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class ModalServlet
 */
@WebServlet("/ModalServlet.do")
public class ModalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModalServlet() {
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
		
/*		 if(op.equals("InsertContents")) {
			ModalDAO mDao = new ModalDAO();
			String c = request.getParameter("center");
			String t = request.getParameter("team");
			String o = request.getParameter("otemp");
			if(o.contains("운용팀")) {
				t = o.substring(0, o.indexOf(" "));
				o = "-";
				System.out.println("t = " + t + ", o : " + o);
			} else if(o.contains("모국")) {
				o = o.substring(0, o.indexOf(" "));
			}
			String y = request.getParameter("index");
			String v = request.getParameter("value");
			String name = request.getParameter("name");
			String description = request.getParameter("description");
			try {
				pw.println(mDao.InsertModalContents(c, t, o, y, v, name, description));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else*/ 
/*		if (op.equals("ModalCheck")) {
			ModalDAO mDao = new ModalDAO();
			String c = request.getParameter("center");
			String t = request.getParameter("team");
			String o = request.getParameter("temp");
			String idx = request.getParameter("index");
			String val = request.getParameter("value");
			try {
				pw.println(mDao.ModalCheck(c, t, o, idx, val));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else */
		if(op.equals("largecategory")) {
			ModalDAO mDao = new ModalDAO();
			String f = request.getParameter("field");
			try {
				List<String> lg_category = mDao.showLargeCategory(f);
				JSONArray jsonArr = new JSONArray();
				JSONObject jObj = null;
				for (String l : lg_category) {
					jObj = new JSONObject();
					jObj.put("large", l);
					jsonArr.add(jObj);
				}
				pw.print(jsonArr);
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(op.equals("smallcategory")) {
			ModalDAO mDao = new ModalDAO();
			String f = request.getParameter("field");
			String l = request.getParameter("large");
			try {
				List<String> sm_category = mDao.showSmallCategory(f, l);
				JSONArray jsonArr = new JSONArray();
				JSONObject jObj = null;
				for(String s : sm_category) {
					jObj = new JSONObject();
					jObj.put("small", s);
					jsonArr.add(jObj);
				}
				System.out.println(sm_category);
				pw.print(jsonArr);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(op.equals("equipcategory")) {
			ModalDAO mDao = new ModalDAO();
			String f = request.getParameter("field");
			String l = request.getParameter("large");
			String s = request.getParameter("small");
			try {
				List<String> equipList = mDao.showEquipCategory(f, l, s);
				JSONArray jsonArr = new JSONArray();
				JSONObject jObj = null;
				for(String e : equipList) {
					jObj = new JSONObject();
					jObj.put("equip", e);
					jsonArr.add(jObj);
				}
				pw.print(jsonArr);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if(op.equals("equippower")) {
			ModalDAO mDao = new ModalDAO();
			String model = request.getParameter("model");
			String getEE;
			try {
				getEE = mDao.showEquipPower(model);
				System.out.println(getEE);
				pw.print(getEE);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(op.equals("analysis")) {
			ModalDAO mDao = new ModalDAO();
			String c = request.getParameter("center");
			String t = request.getParameter("team");
			String o = request.getParameter("temp");
			if(o.contains("운용팀")) {
				t = o.substring(0, o.indexOf(" "));
				o = "-";
				System.out.println("t = " + t + ", o : " + o);
			} else if(o.contains("모국")) {
				o = o.substring(0, o.indexOf(" "));
			}
			String d = request.getParameter("date");
			String w = request.getParameter("work");
			String f = request.getParameter("field");
			String b = request.getParameter("bigcat");
			String s = request.getParameter("smallcat");
			String e = request.getParameter("equip");
			String p = request.getParameter("power");
			String count = request.getParameter("count");
			String pp = request.getParameter("powerpower");
			String worker = request.getParameter("worker");
			String des = request.getParameter("description");
			
			try {
				if(mDao.InsertAnalysisContents(c, t, o, d, w, f, b, s, e, p, count, pp, worker, des)) {
					pw.print("성공");
				}
				else {
					pw.print("실패");
				}
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} else if(op.equals("contents")) {
			ModalDAO mDao = new ModalDAO();
			String c = request.getParameter("center");
			String t = request.getParameter("team");
			String o = request.getParameter("temp");
			String i = request.getParameter("index");
			try {
				pw.print(mDao.showAnalysis(c, t, o, i));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pw.flush();
		pw.close();
	}


	

}
