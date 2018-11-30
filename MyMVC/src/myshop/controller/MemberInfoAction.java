package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.ProductDAO;

public class MemberInfoAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// ** 로그인 유무 곰사 **
		MemberVO loginuser = super.getLoginUser(req);
		if(loginuser == null) return;
		
		// 1. MemberDAO 객체 생성
		ProductDAO pdao = new ProductDAO();
		
		MemberVO memberInfo = null;
		String odrcode = req.getParameter("odrcode");
		String goBackURL = req.getParameter("goBackURL");

		memberInfo = pdao.getMemberDetailByOrdcode(odrcode);
		
			if(memberInfo == null) {
				
				req.setAttribute("msg", "회원 정보 불러오기 실패!!!");
				req.setAttribute("loc", goBackURL);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				return; // 위처럼 코딩해도 자바는 memberInfo를 실행하려고 하기 때문에 직접 멈춤명령을 내려줘야한다.
			}
			

		req.setAttribute("memberInfo", memberInfo);
		req.setAttribute("goBackURL", goBackURL);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/memberInfo.jsp");

		
	}

}
