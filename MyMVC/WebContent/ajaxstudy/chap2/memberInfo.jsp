<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보</title>

<style type="text/css">
	table, th, td { border: solid gray 1px;
	                border-collapse: collapse;}
</style>

</head>
<body>
	<table>
	<thead>
		<tr>
			<th>회원명</th>
			<th>이메일</th>
			<th>주소</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${empty memberList}">
	 		<tr>
       			<td colspan="3">데이터가 없습니다.</td>
        	</tr>
		</c:if>
        <c:if test="${not empty memberList}">
        <c:forEach var="mvo" items="${memberList}" varStatus="status">
        	<tr>
       			<td>${mvo.name}</td>
       			<td>${mvo.email}</td>
       			<td>${mvo.addr1}&nbsp;${mvo.addr2}</td>
        	</tr>
        </c:forEach>
        </c:if>
	</tbody>	
	</table>
</body>
</html>


