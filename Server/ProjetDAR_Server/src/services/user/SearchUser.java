package services.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import annotations.AuthenticationRequiried;
import services.ServicesTools;
import services.errors.ServerErrors;

@AuthenticationRequiried
@WebServlet("/user/search")
public class SearchUser extends HttpServlet {
	private static final long serialVersionUID = 4876627363120277958L;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject answer = new JSONObject();

		String query = req.getParameter(ServicesTools.QUERY_ARG);
		try {
			int page = Integer.parseInt(req.getParameter(ServicesTools.PAGE_ARG));
			int pageSize = Integer.parseInt(req.getParameter(ServicesTools.SIZE_ARG));
			
			if (page < 0 || pageSize <= 0) {
				answer = ServicesTools.createJSONError(ServerErrors.BAD_ARGUMENT);
			} else {
				if (!ServicesTools.nullChecker(query, page, pageSize)) {
					answer = UserUtils.search(query, page, pageSize);
				} else {
					answer = ServicesTools.createJSONError(ServerErrors.MISSING_ARGUMENT);
				}
			}
		} catch (NumberFormatException e) {
			answer = ServicesTools.createJSONError(ServerErrors.BAD_ARGUMENT);
		}
		PrintWriter out = resp.getWriter();
		out.write(answer.toString());
		resp.setContentType("text/plain");
	}
}
