package memo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import memo.model.MemoDAO;

public class MemoDeleteAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String[] delCheckArr = req.getParameterValues("delCheck");
		
		MemoDAO memodao = new MemoDAO();
		
		memodao.memoDelete(delCheckArr, 0);
		
		super.setRedirect(true);
		
		MemberVO loginuser = super.getLoginUser(req);
		
		if("admin".equals(loginuser.getUserid()))
			super.setViewPage("memoList.do"); 
		
	}// end of public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

}
