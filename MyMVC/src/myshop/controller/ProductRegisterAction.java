package myshop.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.ProductDAO;

public class ProductRegisterAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// 1. 반드시 관리자로 로그인 했을 경우에만 접근이 가능하도록 하자
		MemberVO loginuser = super.getLoginUser(req);
		
		if(loginuser == null) return;
		
		if(!"admin".equalsIgnoreCase(loginuser.getUserid())) {
			
			req.setAttribute("msg", "접근권한이 없습니다!");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		
		ProductDAO pdao = new ProductDAO();
		List<HashMap<String, String>> ctgList = pdao.getCategory();
		List<HashMap<String, String>> specList = pdao.getPspec();
		
		req.setAttribute("ctgList", ctgList);
		req.setAttribute("specList", specList);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/admin/productRegister.jsp");
		
	}

}
