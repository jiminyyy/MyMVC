package myshop.controller;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;

public class dislikeAddAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		
		String userid = req.getParameter("userid");
		String pnum = req.getParameter("pnum");
		
		JSONObject jsonObj = new JSONObject();
		
		if(userid == null || "".equals(userid)) {
			
			jsonObj.put("msg", "로그인 후\n싫어요를 클릭하세요.");
			
		}
		else {
			
			InterProductDAO pdao = new ProductDAO();
			
			try {
				
				int n = pdao.insertDislike(userid, pnum);
				
				if(n==1) {
					jsonObj.put("msg", "싫어요를 클릭하셨습니다.");
				}
			
			} catch(SQLIntegrityConstraintViolationException e) {
				
				jsonObj.put("msg", "이미 싫어요를 클릭하셨습니다."); 
			}
		}
		
		String str_jsonObj = jsonObj.toString();
		req.setAttribute("str_json", str_jsonObj);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/dislikeAdd.jsp");
		
		
		
	}

}
