package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberDAO;

public class PwdConfirmAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
	
		String method = req.getMethod();
		String userid = req.getParameter("userid");
		
		if("POST".equalsIgnoreCase(method)) {
		
			String pwd = req.getParameter("pwd");
			
			MemberDAO memberdao = new MemberDAO();
			int n = memberdao.updatePwdUser(userid, pwd);
			
			req.setAttribute("n", n);
			
		}
		req.setAttribute("method", method);
		req.setAttribute("userid", userid);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/login/pwdConfirm.jsp");
		
	} // end of execute

}
