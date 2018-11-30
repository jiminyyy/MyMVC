<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String[] nameArr = {"양정구", "정구현", "차지석", "최현영", "김보현"};
	request.setAttribute("friend", nameArr);
	
	RequestDispatcher dispatcher =  request.getRequestDispatcher("6ArrayTest1View.jsp");
	dispatcher.forward(request, response);
%>