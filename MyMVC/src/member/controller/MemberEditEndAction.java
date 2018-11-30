package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;

public class MemberEditEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// *** POST 방식으로 넘어온 것이 아니라면 *** //
		String method = req.getMethod();
		
		if(!"POST".equalsIgnoreCase(method)) {
			String msg = "비정상적인 경로로 들어왔습니다.";
		    String loc = "javascript:history.back();";
		    
		    req.setAttribute("msg", msg);
		    req.setAttribute("loc", loc);
		    
		    super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		    return;
		}
		
		
		// *** 로그인 유무 검사 *** //
		MemberVO loginuser = super.getLoginUser(req);
		
		if(loginuser == null) 
			return;
		
		
		MemberVO membervo = new MemberVO();
		
		membervo.setName(req.getParameter("name"));
		membervo.setPwd(req.getParameter("pwd"));
		membervo.setEmail(req.getParameter("email"));
		membervo.setHp1(req.getParameter("hp1"));
		membervo.setHp2(req.getParameter("hp2"));
		membervo.setHp3(req.getParameter("hp3"));
		membervo.setPost1(req.getParameter("post1"));
		membervo.setPost2(req.getParameter("post2"));
		membervo.setAddr1(req.getParameter("addr1"));
		membervo.setAddr2(req.getParameter("addr2"));
		membervo.setIdx(Integer.parseInt(req.getParameter("idx")));
	
		MemberDAO memberdao = new MemberDAO();
		int n = memberdao.updateMember(membervo);
		
		if(n == 1) {
			
			HttpSession session = req.getSession();
			
			loginuser.setName(req.getParameter("name"));
			
			// session.removeAttribute("loginuser");
			session.setAttribute("loginuser", loginuser);
			
			req.setAttribute("msg", "회원정보 수정 성공!!!");
			req.setAttribute("loc", "index.do");
			req.setAttribute("loc2", "opener.location.reload();" + "window.close();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
		}
		else {
			req.setAttribute("msg", "회원정보 수정 실패!!!");
			req.setAttribute("loc", "memberEdit.do");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
		
		
	}

	
	
}
