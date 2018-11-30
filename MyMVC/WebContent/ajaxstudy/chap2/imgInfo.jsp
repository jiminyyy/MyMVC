<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    

<% String ctxPath = request.getContextPath(); %>  

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원사진정보</title>
</head>
<body>

<div id="container">
    <%-- 
    <c:set var="imgpath" value="<%=ctxPath %>/images" />
    =====> 오류!!!! <%=ctxPath %> 을 사용하면 오류임.
                   그냥 /MyMVC 이라고 사용해야함.!!! value 자체가 값이므로 value에 변수가 오면 안됨.
    --%>
	<c:set var="imgpath" value="/MyMVC/images" />
	<c:forEach var="map" items="${imgList}"> 
		<div style="margin-bottom: 20px;">
			<img src="${imgpath}/${map.img}" />
			<br/>${map.name}	
		</div>
	</c:forEach>
</div>

</body>
</html>