package member.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;

public class LoginEndAction extends AbstractController {

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
			
			String userid = req.getParameter("userid");
			String pwd = req.getParameter("pwd");
			
			String saveid = req.getParameter("saveid");
			// System.out.println("==> 확인용! saveid : " + saveid); 체크박스에 value값이 없을 떄 체크하면 on, 아니면 null
			
			MemberDAO memberdao = new MemberDAO();
			
			MemberVO loginUser = memberdao.loginOKmemberInfo(userid, pwd);
			
			if(loginUser == null) { // 로그인 실패
				
				req.setAttribute("msg", "아이디 또는 비밀번호를 확인해주세요!!!");
				req.setAttribute("loc", "index.do");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				return;				

			} 
			
			else if(loginUser.isDormant()) {
					
					req.setAttribute("msg", "로그인 하신지 1년이 경과하여 휴면회원이 되셨습니다. 관리자에게 문의해주세요!!!");
					req.setAttribute("loc", "index.do");
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
					
					return;		
			}
			
			else { // 로그인 성공!

				/*	로그인 된 사용자 정보(loginUser)를 세션(session)에 저장하도록 한다.
				 	** 세션(session) **
					세션은 WAS(톰캣서버)의 일부분을 사용하는 저장공간이다.
					세션에 저장된 데이터는 없애기 전에는 모든 파일(*.do, *.jsp)에서 사용할 수 있다.(접근가능)
				*/
				// 세션(session) 객체 생성
				HttpSession session = req.getSession();
				
				session.setAttribute("loginuser", loginUser);
				
				// 사용자가 로그인 시 아이디 저장 체크박스에 체크했을 경우 => 쿠키저장 / 체크안했을 경우 => 저장된 쿠키 삭제
				/*	*** 쿠키란 
					was컴퓨터가 아닌 클라이언트 사용자 컴퓨터의 디스크공간을 저장공간으로 사용하는 기법.
					쿠키에 저장되는 정보는 보안성이 낮은 데이터만 가능하다.
				*/
				
				Cookie cookie = new Cookie("saveid", loginUser.getUserid());
				
				if(saveid != null) { // 위에서 on을 받았으면 쿠키저장
					cookie.setMaxAge(7*24*60*60); // 일주일간 저장 (일*시*분*초)
				}
				else { // 아니면 삭제 
					cookie.setMaxAge(0);
				}
				
				cookie.setPath("/");
				/*
				   쿠키가 사용되어질 디렉토리 정보 설정.
				  cookie.setPath("사용되어질 경로"); 
				   만일 "사용되어질 경로" 가 "/" 일 경우
				   해당 도메인(예 iei.or.kr)의 모든 페이지에서 사용가능하다. 
				 */
				
				res.addCookie(cookie);
				/*
				     사용자 컴퓨터의 웹브라우저로 쿠키를 전송시킨다.
				   7일간 사용가능한 쿠키를 전송하든지
				     아니면 0초 짜리 사용가능한 쿠키(쿠키삭제)를     
				     사용자 컴퓨터의 웹브라우저로 전송시킨다.    
				*/
				String returnPage = (String)session.getAttribute("returnPage");
				
				if(loginUser.isRequirePwdChange()) {
					
					req.setAttribute("msg", "비밀번호를 변경하신지 6개월이 지났습니다 비밀번호를 변경해주세요!!!");
					req.setAttribute("loc", "index.do");
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
					
					return;		
				}
				// 세션에 goBackURL이 있다면 장바구니를 통해 들어온것이므로 로그인완료 후 제품 페이지로 돌아가야한다.
				else if(returnPage == null) {
					super.setRedirect(true);
					super.setViewPage("index.do");
				}
				
				else {
					super.setRedirect(true);
					super.setViewPage(returnPage);
					
					session.removeAttribute("returnPage");
				}
			}
		}
	}
}
