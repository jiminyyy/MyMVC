package sessiontest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;

public class SessionTestAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
	    /* http 프로토콜 및 https 프로토콜은 비연결지향성 이다.
		   http 프로토콜 및 https 프로토콜은 데이터를 요청하고 데이터의 결과 값을 받게 되면 바로 연결은 종료된다.
		     일반적으로 항상 연결된 상태에서 데이터를 주고 받는다고 생각할 수 있다. 
		     하지만 http 프로토콜 및 https 프로토콜은 데이터 송/수신을 하자마자 바로 연결이 끊기게 된다.
		     이것이 http 프로토콜 및 https 프로토콜의 기본적인 특성이다.
		     이렇게 해야만 클라이언트가 동시에 접속을 하더라도 서버측에서는 연결을 끊어버리므로 부하를 덜 받게 되기 때문이다. 
		*/
		
		HttpSession session = req.getSession();
		// session 객체는 웹브라우저(클라이언트)당 1개씩 생성되어 웹컨테이너의 메모리에 저장된다.
		// session 객체는 웹브라우저(클라이언트)의 요청시, 요청한 웹브라우저(클라이언트)에 관한 정보를 저장하고 관리하는 내장 객체이다.
		// WAS는 각각의 웹브라우저로 부터 발생한 요청에 대해서 특별한 식별자(JSESSIONID)를 부여한다.
		// 이후에 이 식별자(JSESSIONID)를 웹브라우저에서 발생한 요청들과 비교해서 같은 식별자 인지를 구별하게 된다.
		// 이 특별한 식별자(JSESSIONID)에 특정한 값을 넣을 수 있으며, 이것을 사용해서 세션을 유지하게 된다.
		
		String sessionId = session.getId();
		// 해당 세션의 세션ID를 문자열로 리턴함. 
		// 세션ID는 session 객체 생성시 웹컨테이너(톰캣)에 의해 자동으로 할당됨.
		
		// -- 웹컨테이너 ==> JSP 와 서블릿을 실행시킬수 있는 소프트웨어를 웹컨테이너(Web Container) 혹은 서블릿 컨테이너(Servlet Container)라고 한다.  
		// -- 아파치 웹서버로 JSP를 요청하면 자동적으로 웹컨테이너인 톰캣이 요청에 대한 처리를 해준다.
		//    웹컨테이너인 톰캣이 JSP 파일을 서블릿으로 변환하여 컴파일을 수행하고, 서블릿의 수행결과를 아파치 웹서버에게 전달해준다.
		//    아파치 웹서버는 수행결과를 클라이언트에게 전달해준다.
		
		// -- 아파치 웹서버로 .do를 요청하면 자동적으로 웹컨테이너인 톰캣이 요청에 대한 처리를 해준다.  
		//    1) 웹컨테이너인 톰캣은 제일먼저 배치(배포)서술자(web.xml)를 참조하여 해당 URL에 매핑된 서블릿에 대한 스레드를 생성하고,
		//       요청객체(HttpServletRequest) 및 응답객체(HttpServletResponse)를 생성하여 스레드에게 넘겨준다.
		//    2) 다음으로 웹컨테이너인 톰캣은 서블릿의 service() 메소드를 호출하여 JSP의 내장객체인 (request, response, session, application, exception out 등)을 사용할 수 있도록 한다. 
		//    3) 호출(service())된 서블릿의 작업처리를 위해 1)에서 생성된 스레드는 요청에 따라 doPost() 또는 doGet() 메소드를 호출한다.
		//    4) 호출된 doPost() 또는 doGet() 메소드는 생성된 동적페이지를 응답객체(HttpServletResponse)에 실어서 웹컨테이너(Web Container)에 전달한다. 
		//    5) 웹컨테이너(Web Container)는 웹서버로 동적페이지를 전달해준후 생성되었던 스레드를 종료하고 
		//       요청객체(HttpServletRequest) 및 응답객체(HttpServletResponse)를 소멸시킨다.
		//    6) 아파치 웹서버는 수행결과를 클라이언트에게 전달해준다. 
		//    자바 콘솔응용프로그램에서는 실행의 시작은 main()메소드에서 시작하는데 반해서 자바 웹프로그래밍에서는 main()메소드쯤에 
		//    해당되는 것을 웹컨테이너가 위와 같은 순서로 실행을 해주므로 개발자는 필요한 서블릿만 작성해주면 된다.
		
		//   == Servlet의 생명주기 순서(JSP 페이지도 결국은 Servlet이기 때문에 똑같이 아래의 생명 주기를 갖는다) ==  
		//   1) 요청이오면, 웹컨테이너는 Servlet 클래스를 로딩하여 요청에 대한 Servlet 객체를 생성한다.
		//   2) 웹컨테이너는 Servlet 객체의 init() 메소드를 호출해서 Servlet 객체의 초기화에 필요한 작업을 실행한다.
        //   3) 웹컨테이너는 service() 메소드를 호출해서 JSP의 내장객체인 (request, response, session, application, exception out 등)을 사용할 수 있도록 하여 Servlet이 브라우저의 요청을 처리하도록 한다. 
		//   4) service() 메소드를 통해 생성된 JSP의 내장객체를 사용하여 특정 HTTP 요청(GET, POST 등)을 처리하는 메서드 (doGet(), doPost() 등)를 호출한다.
        //	 5) 웹컨테이너는 Servlet 객체의 destroy() 메소드를 호출하여 Servlet을 제거한다.
		
		// == Scope ==
		// 1. page        --> 어떤 데이터는 해당 페이지내 에서만 사용가능함.
		// 2. request     --> 어떤 데이터는 form 전송을 통해 action 처리를 담당할 특정 페이지에서도 사용가능함.
		// 3. session     --> 어떤 데이터는 클라이언트가 연결하여 사용하는 웹브라우저 1개내 에서만 모두 사용가능함. (크롬과 익스플로어를 각각 띄워서 테스트 해보면 알 수 있다.)
		// 4. application --> 어떤 데이터는 특정 웹프로그램에 접속하여 사용하는 모든 클라이언트 웹브라우저에서 다같이 공용적으로 모두 사용가능함.
				
		
		long creationTime = session.getCreationTime();
		// 1970년 1월 1일 00시 00분 00초(epoch 이라고 부름) 부터 해당 세션이 생성된 시간까지의 경과 시간을 밀리초로 계산하여 long 타입으로 리턴해줌.
		
		long lastAccessedTime = session.getLastAccessedTime();
		// 1970년 1월 1일 00시 00분 00초(epoch 이라고 부름) 부터 해당 세션에 마지막으로 접근된 시간까지의 경과 시간을 밀리초로 계산하여 long 타입으로 리턴해줌. 
		
		int  maxInactiveInterval = session.getMaxInactiveInterval();
		// 클라이언트의 요청이 없을시 서버가 해당 세션을 유지하도록 지정된 시간을 초 단위의 정수로 리턴해줌.
		
	//   session.setMaxInactiveInterval(3600);
		// 클라이언트의 요청이 없더라도 세션을 유지할 시간을 초 단위의 정수값으로 설정해준다.
		// 음수로 설정할 경우 클라이언트의 요청이 없더라도 세션의 삭제명령 없는 경우이라면 세션을 계속해서 유지된다. 
		// 즉, 세션은 무효화(삭제)되지 않는다.
		
		/*
		    === 세션 타임아웃 적용 우선 순위 ===
		    
		    1. 프로그램에 코딩된  session.setMaxInactiveInterval(int 초);
		    2. 각 웹 어플리케이션의 WEB-INF/web.xml
		    3. [tomcat설치디렉토리]/conf/web.xml
		    		     
		    <!-- ==================== Default Session Configuration ================= -->
			<!-- You can set the default session timeout (in minutes) for all newly   -->
			<!-- created sessions by modifying the value below.                       -->
			
			   <session-config>
			       <session-timeout>30</session-timeout>
			   </session-config>
		 */
		
		session.setAttribute("sessionId", sessionId);
		session.setAttribute("creationTime", creationTime);
		session.setAttribute("lastAccessedTime", lastAccessedTime);
		session.setAttribute("maxInactiveInterval", maxInactiveInterval);
	
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/sessionTest/sessionTest.jsp");

	}

}
