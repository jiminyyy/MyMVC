package myshop.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;

public class CoinAddUpdateLoginuserAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		MemberVO loginuser = super.getLoginUser(req);
		if (loginuser == null) return;
		// 로그인을 안했으면 코인충전액을 업뎃할 수 없어
		
		String str_idx = req.getParameter("idx");
		
		int idx = 0;
		
		try {
			
			idx = Integer.parseInt(str_idx);
			
		} catch (NumberFormatException e){
			idx = -1;
		}
		
		if(idx != (loginuser.getIdx())) {

			req.setAttribute("msg", "잘못된 경로로 들어왔습니다!!!");
			req.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		
		else {
			HttpSession session = req.getSession();
			HashMap<String,String> coinmap = (HashMap<String, String>)session.getAttribute("coinmap");
			
			String coinmap_idx = coinmap.get("idx");
			String coinmap_coinmoney = coinmap.get("coinmoney");
			
			int n = 0;
			
			if(coinmap_idx.equals(str_idx)) {
			
				MemberDAO memberdao = new MemberDAO();
				n = memberdao.coinAddUpdate(coinmap_idx, Integer.parseInt(coinmap_coinmoney));
			
			}
			
			String msg = "";
			
			if (n == 1) {
				
				msg = loginuser.getUserid() + "[" + loginuser.getName() + "]님의 코인" + coinmap_coinmoney
					+ "원 결제가 완료되었습니다.";
				
				loginuser.setCoin(loginuser.getCoin() + Integer.parseInt(coinmap_coinmoney));
				loginuser.setPoint(loginuser.getPoint() + Integer.parseInt(coinmap_coinmoney)/100 );				
				
				session.setAttribute("loginuser", loginuser);
				
			}
			else {
				msg = loginuser.getUserid() + "[" + loginuser.getName() + "]님의 코인" + coinmap_coinmoney
					+ "원 결제를 실패했습니다.";
			}
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", req.getContextPath()+"/index.do");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			session.removeAttribute("coinmap");
		}
		
		
	}

}
