package ajax.controller.chap4;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import ajax.model.AjaxDAO;
import ajax.model.InterAjaxDAO;
import common.controller.AbstractController;

public class IdDuplicateCheckAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res)
		throws Exception {
		
		String userid = req.getParameter("userid");
		
		InterAjaxDAO adao = new AjaxDAO();
		
		int n = adao.idDuplicateCheck(userid);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		String str_json = jsonObj.toString();
		
		req.setAttribute("str_json", str_json);
		
		super.setRedirect(false);
		super.setViewPage("/ajaxstudy/chap4/3idDuplicateCheck.jsp");
		
	}

}
