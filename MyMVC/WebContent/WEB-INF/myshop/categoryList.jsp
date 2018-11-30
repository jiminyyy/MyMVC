<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${categoryList != null}" >
	<div style="width: 90%; 
				border: 1px solid gray;
				margin-left: 15px;
				padding-top: 5px;
				padding-bottom: 5px;
				text-align: center;">
		<span style="color: navy; font-size: 14pt; font-weight: bold;">
			CATEGORY LIST
		</span>
	</div>
	<div style="width: 90%;
				border: 1px solid gray; 
				border-top: hidden;
				margin-left: 15px;
				padding-top: 5px;
				padding-bottom: 5px;
				text-align: center;">
		<c:forEach var="ctgvo" items="${categoryList}">
			<a href="<%=request.getContextPath()%>/mallByCategory.do?code=${ctgvo.code}">${ctgvo.cname}</a>&nbsp;
		</c:forEach>
	</div>
</c:if>