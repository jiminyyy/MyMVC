package test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class IndexController extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		req.setAttribute("result","<span style='color:darkblue; font-size:30pt; font-weight:bold;'>MVC패턴에 대해 공부를 잘해서 꼭 취업을 하도록 합시다!!</span>");

		super.setRedirect(false);
		super.setViewPage("/WEB-INF/index.jsp");
		/*
			확장자가 .java인 파일과 .xml인 파일에서 view단 페이지의 경로를 나타낼 때 맨 앞의 /는 http://localhost:9090/MyMVC/ 를 의미한다.
			.html, .jsp에서는 http://localhost:9090/까지를 의미한다.
			
		*/
	}

}
