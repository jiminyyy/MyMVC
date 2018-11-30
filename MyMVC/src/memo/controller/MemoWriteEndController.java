package memo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import memo.model.MemoDAO;
import memo.model.MemoVO;

public class MemoWriteEndController extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String method = req.getMethod();
		
		if(!"POST".equalsIgnoreCase(method)) {
			// post방식으로 들어온 것이 아니라면
			req.setAttribute("msg", "잘못된 경로로 들어왔습니다!!!");
			req.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {
			
			MemberVO loginuser = (MemberVO)req.getSession().getAttribute("loginuser");
			
			String fk_userid = loginuser.getUserid();
			String name = req.getParameter("name");
			String msg = req.getParameter("msg");
			
			// 클라이언트 아이피 알아오기
			String cip = req.getRemoteAddr();
			
			System.out.println("아이디 : "+fk_userid);
			System.out.println("성명 : "+name);
			System.out.println("메시지 : "+msg);
			System.out.println("IP주소 : "+cip);
			
			this.setRedirect(false);
			this.setViewPage("/WEB-INF/memo/memoWriteEnd.jsp"); // 없으면 nullexception이 뜬다.
			
			MemoVO memovo = new MemoVO();
			memovo.setFk_userid(fk_userid);
			memovo.setName(name);
			memovo.setMsg(msg);
			memovo.setCip(cip);
			
			MemoDAO memodao = new MemoDAO();
			int n = memodao.memoInsert(memovo);
			
			if(n == 1) { // 메모쓰기 성공
				req.setAttribute("msg", "메모 입력 성공!!!");
				req.setAttribute("loc", "memoList.do");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
			}
			else { // 메모쓰기 실패
				req.setAttribute("msg", "메모 입력 실패!!!");
				req.setAttribute("loc", "javascript:history.back()");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}
		}
	}

}
