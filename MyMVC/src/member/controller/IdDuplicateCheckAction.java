package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;

public class IdDuplicateCheckAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		String method = req.getMethod();
		String ctxPath = req.getContextPath();
		
		if("POST".equalsIgnoreCase(method)) {
		
		String userid = req.getParameter("userid");
		
		MemberDAO memberdao = new MemberDAO();
		boolean isUSEuserid = memberdao.idDuplicateCheck(userid);
		
		req.setAttribute("userid", userid);
		req.setAttribute("isUSEuserid", isUSEuserid);

		}
		// Attribute는 set, get이 필요  // Parameter는 폼의 name값을 가져올 수 있다.

		req.setAttribute("method", method);
		req.setAttribute("ctxPath", ctxPath);
		
		super.setRedirect(false); //기본값이 false이므로 굳이 줄 필요 없다.
		super.setViewPage("/WEB-INF/member/idcheck.jsp"); // 보여줄 페이지 설정

	}

}
