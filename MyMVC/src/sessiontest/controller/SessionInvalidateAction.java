package sessiontest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;

public class SessionInvalidateAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		HttpSession session = req.getSession();
		
		session.invalidate(); // 세션에 특정키값으로 매핑된 모든 객체를 세션에서 삭제한후 해당세션을 메모리에서 삭제시킨다.
		
		super.setRedirect(true);
		super.setViewPage("sessionTest.do");

	}

}
