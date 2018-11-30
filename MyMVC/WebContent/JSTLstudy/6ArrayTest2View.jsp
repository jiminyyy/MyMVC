<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>우리반 친구들 명단 출력하기</title>
</head>
<body>
	
	<h2>우리반 친구들</h2>
	<br/>
	<ol style= "list-style-type: decimal;" >
		<c:forEach var="val" items="${friend}">
			<li>${val}</li>
		</c:forEach>
	</ol>
	
</body>
</html>