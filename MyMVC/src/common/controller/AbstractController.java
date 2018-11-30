package common.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import member.model.MemberVO;
import myshop.model.CategoryVO;
import myshop.model.ProductDAO;
import myshop.model.ProductVO;

public abstract class AbstractController
	   implements Command {
	
	private boolean isRedirect = false;
	/*
		** 우리가 하고자하는 규칙 설정
		VIEW단 페이지(.jsp 페이지)로 이동 시
	 	sendRedirect 방법으로 이동 시키고자 한다면  isRedirect 변수 값을 true로 한다.
	 	VIEW단 페이지(.jsp 페이지)로 이동 시
	 	forward 방법으로 이동 시키고자 한다면  isRedirect 변수 값을 false로 한다.
	*/

	private String viewPage;
	// VIEW단 페이지(.jsp 페이지)의 경로명으로 사용되는 변수

	public boolean isRedirect() {
		return isRedirect;
	} // 리턴타입이 boolean이라면 get이 아니라 is로 나온다.

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

	public String getViewPage() {
		return viewPage;
	}

	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}
	
	// *** 로그인 유무를 검사해서 로그인했으면 로그인 한 사용자의 정보를 return 비로그인 시 null return
	public MemberVO getLoginUser(HttpServletRequest req) {
		
		MemberVO loginuser = null;
		
		HttpSession session = req.getSession();
		
		loginuser = (MemberVO)session.getAttribute("loginuser");
		
		if(loginuser == null) { //로그인 안했으면 msg페이지로 이동
			
			String msg =  "먼저 로그인하세요!!!";
			String loc = "javascript:history.back()";
						
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			isRedirect = false;
			viewPage = "/WEB-INF/msg.jsp";	
		}	
		
		return loginuser;
		
	} // end of public static HashMap<String, Object> checkLoginUser(HttpServletRequest req) {

	public void getCategoryList(HttpServletRequest req) throws SQLException {
		
		// jsp_category 테이블에서 카테고리코드 (code)와 카테고리명(cname)을 가져와서 request 영역에 저장시킨다.
		
		ProductDAO pdao = new ProductDAO();
		List<CategoryVO> categoryList = pdao.getCategoryList();
		
		req.setAttribute("categoryList", categoryList);
		
	}
	
}
