<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String name = "이고은,이지예,이현재,김기복,백승범";
	request.setAttribute("friend", name);
%>

<jsp:forward page="7forTokensTestView.jsp" />