package memo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import memo.model.MemoDAO;

public class MemoBlindOpenAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String[] delCheckArr = req.getParameterValues("delCheck");
		
		int status = Integer.parseInt(req.getParameter("status"));
		
		MemoDAO memodao = new MemoDAO();
		
		memodao.memoBlindOpen(delCheckArr, status);
		
		super.setRedirect(true);
		super.setViewPage("myMemoList.do"); 
		
	}// end of public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

}
