package member.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;

public class MemberEnableAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// ** 로그인 유무 곰사 **
		MemberVO loginuser = super.getLoginUser(req);
		if(loginuser == null || !"admin".equalsIgnoreCase(loginuser.getUserid())) return;
		
		String method = req.getMethod();
		
		if(!"POST".equalsIgnoreCase(method)) {
			// post방식으로 들어온 것이 아니라면
			req.setAttribute("msg", "잘못된 경로로 들어왔습니다!!!");
			req.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		
		MemberDAO memberdao = new MemberDAO();
		
		String str_idx = req.getParameter("idx");
		String goBackURL = req.getParameter("goBackURL");

		int idx = Integer.parseInt(str_idx);
		
		int result = memberdao.getMemberEnable(idx);
		
		if(result == 1) { // 활성화 성공
		
			req.setAttribute("msg", "휴면계정 활성화 성공!!!");
			req.setAttribute("loc", goBackURL);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
		}
		else { // 활성화 실패
			
			req.setAttribute("msg", "휴면계정 활성화 실패!!!");
			req.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
			
		}
		
	}
}
