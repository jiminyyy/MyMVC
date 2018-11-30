<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String ctxPath = request.getContextPath();
	// /MyWeb
%>

<!DOCTYPE html>
<html>
<head>

<title>:::HOMEPAGE:::</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%= ctxPath %>/css/style.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=ctxPath%>/jquery-ui-1.11.4.custom/jquery-ui.min.css" /> 
<script type="text/javascript" src="<%=ctxPath%>/jquery-ui-1.11.4.custom/jquery-ui.min.js"></script> 

<script type="text/javascript">
	
	$(document).ready(function(){
		
		var vhtml = "";
		for(var i=0; i<15; i++) {
			vhtml += (i+1)+".내용물<br/>";
		}
		
		
	$("#sideinfo").css('height',$("#content").height());	

	});
</script>

</head>
<body>

<div id="mycontainer">

	<div id="headerImg">
		<div class="row">
			<div class="col-md-2 headerImg2">1. 로고이미지/네비게이터</div>
			<div class="col-md-2 headerImg2"><a href="http://www.samsung.com"><img src="<%= ctxPath %>/images/logo1.png"/></a></div>
			<div class="col-md-2 headerImg2"><img src="<%= ctxPath %>/images/logo2.png"/></div>
		</div>
	</div>
	
	<div id="headerLink">
		<div class="row">
			<div class="col-md-1 menu">
				<a href="<%= ctxPath %>/index.do">HOME</a>
			</div>
			
			<div class="col-md-1 menu">
				<a href="<%= ctxPath %>/memberRegister.do">회원가입</a>
			</div>
			<c:if test="${sessionScope.loginuser != null}"> <%-- session에 저장되어진 것.키값 --%>
				<div class="col-md-1 menu">
					<a href="<%= ctxPath %>/memberList.do">회원목록</a>
				</div>
			</c:if>
			<c:if test="${sessionScope.loginuser != null}"> <%-- session에 저장되어진 것.키값 --%>
				<div class="col-md-2 menu">
					<a href="<%= ctxPath %>/memo.do">한줄메모쓰기</a>
				</div>
				<div class="col-md-2 menu">
					<a href="<%= ctxPath %>/memoList.do">메모조회(HashMap)</a>
				</div>
				<div class="col-md-2 menu">
					<a href="<%= ctxPath %>/myMemoList.do">내 메모조회(VO)</a>
				</div>
			</c:if>
			<div class="col-md-1 menu">
				<a href="<%= ctxPath %>/mallHome.do">쇼핑몰 홈</a>
			</div>
			<c:if test="${sessionScope.loginuser != null && sessionScope.loginuser.userid eq 'admin'}">
				<div class="col-md-1 menu">
					<a href="<%= ctxPath %>/admin/productRegister.do">제품등록</a>
				</div>
			</c:if>
			<c:if test="${sessionScope.loginuser != null && sessionScope.loginuser.userid ne 'admin'}">
			<div class="col-md-1 menu">
				<a href="<%= ctxPath %>/cartList.do">장바구니</a>
			</div>
			</c:if>
			<c:if test="${sessionScope.loginuser != null}">
			<div class="col-md-1 menu">
				<a href="<%= ctxPath %>/orderList.do">주문내역</a>
			</div>
			</c:if>
			<div class="col-md-1 menu">
				<a data-toggle="modal" data-target="#searchStore" data-dismiss="modal" >매장찾기</a>
				<%-- #searchStore는 footer에 있음 --%>
			</div>
		</div>
	</div>
	
	<div id="sideinfo" >
		<div class="row">
			<div class="col-md-12" style="height: 50px; text-align: left; padding: 20px;">
				2. 로그인<br/><br/>
				<%@ include file="/WEB-INF/login/login.jsp" %>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12" id="sideconent" style="text-align: left; padding: 20px;">
			</div>
		</div>	
		<div class="row">
			<div class="col-md-12" style="height: 50px; text-align: left; padding: 20px; margin-top: 80%;">
				<%@ include file="/WEB-INF/myshop/categoryList.jsp" %>
			</div>
		</div>
	</div>
	
	<div id="content" align="center">