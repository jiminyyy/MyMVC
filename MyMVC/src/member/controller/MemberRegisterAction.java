package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class MemberRegisterAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

			super.setRedirect(false); //기본값이 false이므로 굳이 줄 필요 없다.
			super.setViewPage("/WEB-INF/member/memberForm.jsp"); // 보여줄 페이지 설정
		
	}

}
