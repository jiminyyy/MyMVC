<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String[] nameArr = {"배수미", "이지민", "최수욱", "김우철", "홍석화"};
	request.setAttribute("friend", nameArr);
	
	RequestDispatcher dispatcher =  request.getRequestDispatcher("6ArrayTest2View.jsp");
	dispatcher.forward(request, response);
%>