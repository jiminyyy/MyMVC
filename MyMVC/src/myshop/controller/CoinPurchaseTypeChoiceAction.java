package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;

public class CoinPurchaseTypeChoiceAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		MemberVO loginuser = super.getLoginUser(req);
		if (loginuser == null) return;
		
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
		//////////////////////////////////////// 로그인 확인 및 idx가 본인 것인지 확인
		
		else {
			
			req.setAttribute("idx", idx);
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/coinPurchaseTypeChoice.jsp");
			
		}
		
		
	}

}
