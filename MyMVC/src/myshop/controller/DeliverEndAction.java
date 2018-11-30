package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;

public class DeliverEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
String method = req.getMethod();
		
		if(!"POST".equalsIgnoreCase(method)) { // 포스트가 아니라면
			String msg =  "비정상적인 경로로 들어왔습니다!!";
			String loc = "javascript:history.back()";
						
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		
		MemberVO loginuser = super.getLoginUser(req);
		
		if(loginuser == null) return;
		if(!"admin".equalsIgnoreCase(loginuser.getUserid())) {
			String msg =  "접근권한이 없습니다!!";
			String loc = "javascript:history.back()";
						
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		
		// post면서 관리자로 로그인 한 경우
		// 체크박스의 value 값을 받아온다.
		String[] odrcodeArr = req.getParameterValues("odrcode");
		String[] pnumArr = req.getParameterValues("deliverEndPnum");
		
		InterProductDAO pdao = new ProductDAO();
		// 자식클래스 객체는 인터페이스 혹은 부모클래스로 받을 수 있다. 위 경우는 인터페이스에 구현된 메소드만 사용 가능.
		
		// 's20181122-3/1', 's20181122-4/3', 코드뒤에 바로 상품번호가 온다. 이런식으로 고고
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<odrcodeArr.length; i++) {
			
			sb.append("\'"+odrcodeArr[i]+"/"+pnumArr[i]+"\',");
		}
		
		String odrcodePnum = sb.toString().trim();
		odrcodePnum = odrcodePnum.substring(0, odrcodePnum.length()-1);
		
		// System.out.println("확인용 : " + odrcodePnum);
		
		int n = pdao.updateDeliverEnd(odrcodePnum, odrcodeArr.length);
		
		if(n == 1) {
			String msg =  "배송완료 변경완료!!";
			String loc = "orderList.do";
						
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
		else {
			
			String msg =  "배송처리 변경 실패!!";
			String loc = "javascript:history.back()";
						
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
		
		
	}

}
