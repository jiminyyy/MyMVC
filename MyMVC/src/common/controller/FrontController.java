package common.controller;

import java.io.*;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class FrontController
 */
@WebServlet(
		description = "사용자가 웹에서 *.do 설정 시 가장 먼저 응답하는 클래스", 
		urlPatterns = { "*.do" }, 
		initParams = { 
				@WebInitParam(name = "propertyConfig", 
							  value = "C:\\Git\\myjsp\\MyMVC\\WebContent\\WEB-INF\\Command.properties", // 역슬래쉬2개 혹은 슬래쉬 
							  description = "*.do 처리 시 매핑 파일")
		})
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	HashMap<String, Object> cmdMap = new HashMap<String, Object>();
	
	public void init(ServletConfig config) throws ServletException {

		// *** 확인용 ***
		System.out.println("확인!!FrontController서블릿 init메소드 실행!");
		/*
			*.do 실행 시 FrontController 서블릿이 받아 가장 먼저 실행되는 메소드가 init
			이 메소드는 WAS(톰캣서버) 구동 후 한번만 수행되고 이후에는 되지 않는다.
		*/
		
		String props = config.getInitParameter("propertyConfig");
		//propertyConfig을 부르면 props안에는 Command.properties파일 주소(value값)가 들어간다.
		// props : C:\myjsp\MyMVC\WebContent\WEB-INF\Command.properties
		
		// *** 확인2 ***
		System.out.println( "확인2!!초기화 파라미터 데이터값이 저장된 파일명 props : " + props );
		
		// Command.properties파일의 키값과 밸류값을 가져와서 설정
		
		Properties pr =  new Properties();
				
		FileInputStream fis = null;
		
		try {
		
			fis = new FileInputStream(props); // props에 담긴 주소의 파일을 fis에 넣는다.
			
			pr.load(fis);
			/*
				C:\myjsp\MyMVC\WebContent\WEB-INF\Command.properties 을 읽어
				Properties클래스의 객체 pr에 로드시킨다.(넣는다.)
				pr은 읽어온 파일(Command.properties)의 내용 =을 기준으로 왼쪽은 key, 오른쪽은 value로 인식한다.
			*/
			/*
			String str_className = pr.getProperty("/test1.do");
			
			// *** 확인3 ***
			System.out.println("key가 /test1.do인 value값 : "+ str_className);
			// key가 /test1.do인 value값 : test.controller.Test1Controller
			
			Class<?> cls = Class.forName(str_className);
			// ? 아무거나 하나를 의미한다.(클래스이름을 모르니까 ?로 한다.)
			// str_className의 이름을 가진 클래스를 cls에 넣는다.
			
			Object obj = cls.newInstance(); // 객체화

			cmdMap.put("/test1.do", obj);
			*/
			
			Enumeration<Object> en = pr.keys();
			/*
				pr.keys();은 pr안의 파일(Command.properties)의 모든 키값을 읽어온다.
			*/
			while(en.hasMoreElements()) { // en안에 키가 있나요? return은 boolean값
				
				String key_urlcmd = (String)en.nextElement(); // 실제로 키값을 불러오는 메소드
				String className = pr.getProperty(key_urlcmd);
				
				if(className != null) {
					className = className.trim();
					Class<?> cls = Class.forName(className);
					Object obj = cls.newInstance();
					cmdMap.put(key_urlcmd, obj);
				}
				
			}// end of while=========================================================
			
		} catch (ClassNotFoundException e) {
			System.out.println("존재하지 않는 클래스 입니다!!");
			e.printStackTrace();
		}catch (FileNotFoundException e) {
			System.out.println("C:\\myjsp\\MyMVC\\WebContent\\WEB-INF\\Command.properties 파일이 없습니다!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} // end of public void init(ServletConfig config) throws ServletException {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	// .do의 메소드 방식에 따라 doGet, doPost를 실행
		requestProcess(request, response);
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	// .do의 메소드 방식에 따라 doGet, doPost를 실행
		requestProcess(request, response);
		
	}

	private void requestProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//웹브라우저 상의 주소입력창 : http://localhost:9090/MyMVC/test2.do?name=홍길동&addr=서울
		
		// String url = request.getRequestURL().toString();
		// http://localhost:9090/MyMVC/test2.do
		
		String uri = request.getRequestURI();
		// /MyMVC/test2.do
		
		String ctxPath = request.getContextPath();
		// ctxPath는 /MyMVC 이다.
		
		String mapKey = uri = uri.substring(ctxPath.length());
		// /test2.do => 키값
		
		AbstractController action = (AbstractController)cmdMap.get(mapKey);
		
		if(action == null) {
			
			System.out.println(mapKey + "URL패턴에 매핑된 객체가 없습니다!");
			return;
		}
		else {
			try {
				action.execute(request, response);

				String viewPage = action.getViewPage();
				boolean bool = action.isRedirect();
				
				if(bool) { // VIEW단 페이지를 sendREdirect 방법으로 이동시킨다.
				
					response.sendRedirect(viewPage);
				
				}
				else { // VIEW단 페이지를 forward 방법으로 이동시킨다.
					
					RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
					dispatcher.forward(request, response);
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		
		
	}
}
