<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<start>
	
	<c:forEach var="prodvo" items="${productList}">
		<product>
			<pnum>${prodvo.pnum}</pnum>
			<pimage1>${prodvo.pimage1}</pimage1>
			<pname>${prodvo.pname}</pname>
			<price><fmt:formatNumber value="${prodvo.price}" pattern="###,###"></fmt:formatNumber></price>
			<%-- 
			<price>${prodvo.price}</price> 
			--%>
			<saleprice><fmt:formatNumber value="${prodvo.saleprice}" pattern="###,###"></fmt:formatNumber></saleprice>
			<%-- 
			<saleprice>${prodvo.saleprice}</saleprice> 
			--%>
			<percent>${prodvo.percent}</percent>
			<point>${prodvo.point}</point>			
		</product>
	</c:forEach>

</start>