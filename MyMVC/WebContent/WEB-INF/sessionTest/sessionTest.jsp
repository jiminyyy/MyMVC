<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<jsp:include page="../header.jsp" />

    <div align="left" style="margin-top: 50px; margin-bottom: 20px;">
		<ul style="list-style-type: square;">
			<li>sessionId : ${sessionScope.sessionId}</li>
			<li>creationTime : ${sessionScope.creationTime} 밀리초</li>
			<li>lastAccessedTime : ${sessionScope.lastAccessedTime} 밀리초</li>
			<li>(lastAccessedTime - creationTime)/1000 : ${(sessionScope.lastAccessedTime - sessionScope.creationTime)/1000} 초</li>
			<li>maxInactiveInterval : ${sessionScope.maxInactiveInterval} 초</li>
		</ul>
	</div>
	<div>
		<button type="button" onClick="javascript:location.href='sessionInvalidate.do'">세션삭제</button>
	</div>

<jsp:include page="../footer.jsp" />	