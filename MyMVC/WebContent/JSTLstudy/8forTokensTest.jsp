<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String name = "유민후,김상원.박영민,홍윤성.장동수";
	request.setAttribute("friend", name);
%>

<jsp:forward page="8forTokensTestView.jsp" />