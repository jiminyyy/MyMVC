package test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class Test1Controller extends AbstractController {

	public void testPrint() {
		System.out.println("여기는 Test1Controller의 testPrint()메소드 입니다.");
	}

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		req.setAttribute("result", "Test1Controller에서 넘겨준 값은 <span style='color: red;'>\"임진왜란의 구국영웅\"</span>입니다");
		req.setAttribute("name", "이순신");
		
		super.setRedirect(false);
		super.setViewPage("/test/test1.jsp");
		/*
			확장자가 .java인 파일과 .xml인 파일에서 view단 페이지의 경로를 나타낼 때 맨 앞의 /는 http://localhost:9090/MyMVC/ 를 의미한다.
			.html, .jsp에서는 http://localhost:9090/까지를 의미한다.
			
			
			
		*/
		
	}
	
	
}
