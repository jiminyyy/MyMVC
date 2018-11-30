package myshop.controller;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import common.controller.AbstractController;
import member.model.MemberVO;

public class CoinPurchaseEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		MemberVO loginuser = super.getLoginUser(req);
		if (loginuser == null) return;
		
		String str_idx = req.getParameter("idx");
		String coinmoney = req.getParameter("coinmoney");
		
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
		else {
			
			req.setAttribute("idx", idx);
			req.setAttribute("coinmoney", coinmoney);
			req.setAttribute("name", loginuser.getName());
			
			HashMap<String, String> coinmap = new HashMap<String, String>();
			coinmap.put("idx", str_idx);
			coinmap.put("coinmoney", coinmoney);
			
			HttpSession session = req.getSession();
			
			session.setAttribute("coinmap", coinmap);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/paymentGateway.jsp");
			
		}
		
	}

}
