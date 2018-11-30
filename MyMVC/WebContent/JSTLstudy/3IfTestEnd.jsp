<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>두개의 수를 입력받아서  더 큰 수 알아보기 결과물</title>
</head>
<body>
	더 큰 수 : 
	<c:if test="${param.num1 > param.num2}">
		${param.num1}
	</c:if>
	<c:if test="${param.num1 < param.num2}">
		${param.num2}
	</c:if>
	
	<c:if test="${param.num1 == param.num2}">
		${param.num1}와 ${param.num2}는 같습니다.
	</c:if>
</body>
</html>