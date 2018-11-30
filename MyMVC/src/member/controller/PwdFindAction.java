package member.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberDAO;

public class PwdFindAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		
		String method = req.getMethod();
		
		if("POST".equalsIgnoreCase(method)) { // 찾기버튼을 누르고 다시 들어온 경우!
			
			String userid = req.getParameter("userid");
			String email = req.getParameter("email");
			
			MemberDAO memberdao = new MemberDAO();
			
			int n = memberdao.isUserExists(userid, email);
			
			if(n == 1) { // 입력한 정보와 일치하는 회원이 존재하는 경우
				
				GoogleMail mail = new GoogleMail();
				
				// 인증키 랜덤 형성 하기
				 Random rnd = new Random();
				  
				 String certificationCode = "";
				  
				 char randchar = ' ';
				 for(int i=0; i<5; i++) {
					 
					 //min부터 max 사이의 랜덤한 정수를 얻으려면 int rndnum = rnd.nextInt(max-min+1)+min;
					 randchar = (char)(rnd.nextInt('z'-'a'+1)+'a');
					 
					 certificationCode += randchar;
				 }
				 
				 int randint = 0;
				 for(int i=0; i<7; i++) {
				 
					 randint = rnd.nextInt(10);
					 
					 certificationCode += randint;
				 }
				 
				 try {
					 
					 // 메일 보내기
					 mail.sendmail(email, certificationCode);
					 req.setAttribute("certificationCode", certificationCode);
					 
				 } catch (Exception e) {
					 e.printStackTrace();
					 
					 n = -1;
					 req.setAttribute("sendFailmsg", "메일발송에 실패했습니다!!");
				 }
			}
			
			req.setAttribute("n", n);
			
			req.setAttribute("userid", userid);
			req.setAttribute("email", email);
			
		}
		
		req.setAttribute("method", method);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/login/pwdFind.jsp");
		
		
	}

}
