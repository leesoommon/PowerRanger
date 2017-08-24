package eee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class PowerServlet
 */
@WebServlet("/PowerServlet.do")
public class PowerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PowerServlet() {
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
		
		 if(op.equals("headQ")) {
			EnergyDAO eDao = new EnergyDAO();
			try {
				pw.print(eDao.headPowerRanger(eDao.latestYear()));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(op.equals("centerEnergy")) {
			EnergyDAO eDao = new EnergyDAO();
			try {
				pw.print(eDao.centerPowerRanger(eDao.latestYear()));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(op.equals("subEnergy")) {
			EnergyDAO eDao = new EnergyDAO();
			String c = request.getParameter("center");
			String t = request.getParameter("team");
			String o = request.getParameter("office");
			try {
				if (t.equals("no") && o.equals("no")) { pw.println(eDao.TeamEnergyByCenter(eDao.latestYear(), c));	 } 
				else if (o.equals("no")) {
					// 여기는 팀에 대한 국사 그래프가 나와야겟지
					pw.println(eDao.OfficeEnergyByTeam(eDao.latestYear(), t));
				} else {
					pw.println(eDao.branchPowerRank(eDao.latestYear(), o));
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(op.equals("branchEnergy")) {
			EnergyDAO eDao = new EnergyDAO();
			String b = request.getParameter("branch");
			System.out.println(b);
			try {
				pw.println(eDao.BranchRanger(eDao.latestYear(), b));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pw.flush();pw.close();
	}
}
