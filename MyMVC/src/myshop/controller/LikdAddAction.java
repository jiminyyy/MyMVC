package myshop.controller;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;

public class LikdAddAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String userid = req.getParameter("userid");
		String pnum = req.getParameter("pnum");
		
		JSONObject jsonObj = new JSONObject();
		
		if(userid == null || "".equals(userid)) {
			/*
			String goBackURL = req.getParameter("goBackURL");
			
			// goBackURL을 받아서 세션에 넣어준다.
			HttpSession session = req.getSession();
			session.setAttribute("returnPage", goBackURL);
			*/
			jsonObj.put("msg", "로그인을 하신 후 \n좋아요를 클릭하세요.");
			
		}
		else {
			
			InterProductDAO pdao = new ProductDAO();
			
			try {
				
				int n = pdao.insertLike(userid, pnum);
				
				if(n==1) {
					jsonObj.put("msg", "좋아요를 클릭하셨습니다.");
				}
			
			} catch(SQLIntegrityConstraintViolationException e) {
				
				jsonObj.put("msg", "이미 좋아요를 클릭하셨습니다."); 
			}
		}
		
		String str_jsonObj = jsonObj.toString();
		req.setAttribute("str_json", str_jsonObj);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/likeAdd.jsp");
		
	}

}
