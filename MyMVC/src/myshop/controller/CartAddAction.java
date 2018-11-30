package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.ProductDAO;

public class CartAddAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String method = req.getMethod();
		
		if (!"POST".equalsIgnoreCase(method)) {
			
			req.setAttribute("msg", "잘못된 경로로 들어왔습니다!!!");
			req.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {
			
			MemberVO loginuser = super.getLoginUser(req);
			
			if (loginuser == null) { // 로그인하지 않은 상태에서 장바구니 담기 시도, 로그인 시도시 유저가 보던 제품 페이지로 돌아가야한다.
				
				String goBackURL = req.getParameter("goBackURL");
				// goBackURL을 받아서 세션에 넣어준다.
				HttpSession session = req.getSession();
				session.setAttribute("returnPage", goBackURL);
				
				return;
			
			}
			
			else { // 로그인 한 경우 장바구니 테이블에 제품을 담아야 한다. 장바구니에 해당 제품이 없으면 insert 있어도 동일제품 추가 가능(update)
				
				// 장바구니에 담고자하는 제품의 상품번호, 주문량을 받아온다.
				String pnum = req.getParameter("pnum");
				String oqty = req.getParameter("oqty");
				
				ProductDAO pdao = new ProductDAO();
				// 로그인한 유저아이디를 넘겨서 해당 유저의 장바구니에 선택한 번호의 상품을, 주문량만큼 담는다.
				int n = pdao.addCart(loginuser.getUserid(), pnum, oqty);
				
				if(n == 1) {
					
					String msg = "장바구니 담기 성공!!";
					String loc = "cartList.do";
					
					req.setAttribute("msg", msg);
					req.setAttribute("loc", loc);
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
				}
				else {

					String msg = "장바구니 담기 실패!!";
					String loc = "javascript:history.back()";
					

					req.setAttribute("msg", msg);
					req.setAttribute("loc", loc);
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
				}
				
			}// end of else (로그인 상태에서 장바구니 담기를 클릭한 경우
			
		}
	
		
	}

}
