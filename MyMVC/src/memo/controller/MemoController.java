package memo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;

public class MemoController extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// ** 로그인 유무 곰사 **
		MemberVO loginuser = super.getLoginUser(req);
		if(loginuser == null) return;

		// super.setRedirect(false); 기본값이 false이므로 굳이 줄 필요 없다.
		super.setViewPage("/WEB-INF/memo/memoForm.jsp"); // 보여줄 페이지 설정

	}
 
}
