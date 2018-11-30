<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 변수의 선언 방법 --%>
<c:set var="no1" value="${param.num1}" scope="request" />
<c:set var="no2" value="${param.num2}" scope="request" />
<c:set var="result" value="${no1*no2}" scope="request" />

<jsp:forward page="2MultiplyView.jsp" />