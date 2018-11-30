package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;

public class MemberEditAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// ** 로그인 유무 곰사 **
		MemberVO loginuser = super.getLoginUser(req);
		if(loginuser == null) return;
		String str_idx = req.getParameter("idx");
		
		int idx = 0;
		
		try {
			
			idx = Integer.parseInt(str_idx);
			
		} catch (NumberFormatException e){
			idx = -1;
		}
		
		if(idx != (loginuser.getIdx())) {
			// post방식으로  들어온 것이 아니라면
			req.setAttribute("msg", "잘못된 경로로 들어왔습니다!!!");
			req.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		
		MemberDAO memberdao = new MemberDAO();
		MemberVO loginuserInfo = memberdao.getMemberDetail(idx);
		
		
		req.setAttribute("idx", idx);
		req.setAttribute("loginuser", loginuserInfo);
		
		super.setRedirect(false); //기본값이 false이므로 굳이 줄 필요 없다.
		super.setViewPage("/WEB-INF/member/memberEdit.jsp"); // 보여줄 페이지 설정
		
		

		
	}		
}