package myshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.CartVO;
import myshop.model.ProductDAO;

public class CartEditAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String method = req.getMethod();
		
		if(!"POST".equalsIgnoreCase(method)) {
			
			req.setAttribute("msg", "잘못된 경로입니다!!");
			req.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
			
		}
		
		MemberVO loginuser = super.getLoginUser(req); // 로그인안했을 때 msg.jsp로 보냄
		
		if( loginuser != null && "admin".equalsIgnoreCase(loginuser.getUserid())) { // 로그인하고 어드민일 경우
			
			req.setAttribute("msg", "관리자는 유저의 장바구니목록을 변경할 수 없습니다!!");
			req.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
			
		}
		
		else if(loginuser != null && !"admin".equalsIgnoreCase(loginuser.getUserid())) {
			
			String cartno = req.getParameter("cartno");
			String oqty = req.getParameter("oqty");
			
			ProductDAO pdao = new ProductDAO();
			
			int n = pdao.updateDeleteCart(cartno, oqty);
			
			String msg = (n>0)?"장바구니 제품수량 변경 성공!":"장바구니 제품수량 변경 실패!";
			String loc = (n>0)?"cartList.do":"javascript:history.back()";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			
		}

		
	}

}
