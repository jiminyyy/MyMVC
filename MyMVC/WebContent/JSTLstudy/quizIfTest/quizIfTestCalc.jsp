<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	String str_num1 = request.getParameter("num1");
	String str_num2 = request.getParameter("num2");

	try {
		int num1 = Integer.parseInt(str_num1);
		int num2 = Integer.parseInt(str_num2);
		
		request.setAttribute("num1", num1);
		request.setAttribute("num2", num2);
		RequestDispatcher dispatcher = request.getRequestDispatcher("quizIfTestView.jsp");
		dispatcher.forward(request, response);
		
	} catch (NumberFormatException e) {
		request.setAttribute("msg", "숫자만 입력하세요!!");
		request.setAttribute("loc", "javascript:history.back();");
		RequestDispatcher dispatcher = request.getRequestDispatcher("quizIfTestError.jsp");
		dispatcher.forward(request, response);
	}
	

%>
