package myshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.CartVO;
import myshop.model.ProductDAO;

public class CartListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		super.getCategoryList(req);
		
		MemberVO loginuser = super.getLoginUser(req); // 로그인안했을 때 msg.jsp로 보냄
		
		if( loginuser != null && "admin".equalsIgnoreCase(loginuser.getUserid())) { // 로그인하고 어드민일 경우
			
			req.setAttribute("msg", "관리자는 유저의 장바구니목록을 조회할 수 없습니다!!");
			req.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
			
		}
		
		else if(loginuser != null && !"admin".equalsIgnoreCase(loginuser.getUserid())) {
			
			ProductDAO pdao = new ProductDAO();
			
			// 페이징 처리 하기 전 장바구니 목록 보여주기
			List<CartVO> cartList = pdao.getCartList(loginuser.getUserid());
			
			// 페이징 처리 후 장바구니 목록 보여주기
			
			
			req.setAttribute("cartList", cartList);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/cartList.jsp");
			
		}
		
	}
}
