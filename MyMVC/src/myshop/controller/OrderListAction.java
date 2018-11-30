package myshop.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.ProductDAO;

public class OrderListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		MemberVO loginuser = super.getLoginUser(req);
		
		// 로그인 유무 검사
		if (loginuser == null) return;
		
		// 관리자가 아닌 일반사용자 로그인 했을 경우 자신이 주문한 내역만 열람할 권한 부여
		// 관리자의 경우 모든 사용자의 주문내역 열람 권한 부여
		ProductDAO pdao = new ProductDAO();
		List<HashMap<String, String>> orderList = pdao.getOrderList(loginuser.getUserid());
		
		req.setAttribute("orderList", orderList);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/orderList.jsp");
	
		
		
	} // public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

}
