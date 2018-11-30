<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>

<jsp:include page="header.jsp" />

<%-- 내용물 시작 --%>

	<div>
	${result}
	</div>
	<div class="row">
		<div class="col-md-12" style="border: 0px solid gray; height: 150px; 
		padding-top : 40px; padding-bottom: 20px; padding-left: 20px; padding-right: 20px;">
			Content1 입니다.
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-12" style="border: 0px solid gray; height: 150px; padding: 20px;">
			Content2 입니다.
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-12" style="border: 0px solid gray; height: 150px; padding: 20px;">
			Content3 입니다.
		</div>
	</div>
<%-- 내용물 끝 --%>

<jsp:include page="footer.jsp" />