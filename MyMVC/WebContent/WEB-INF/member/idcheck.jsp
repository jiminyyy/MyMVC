<%@ page language="java" contentType="text/html; charset=UTF-8"
    	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>중복 ID 검사하기</title>
<link rel= "stylesheet" type= "text/css" href= "${ctxPath}/css/style.css" />
<script type= "text/javascript" src= "${ctxPath}/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript">

	$(document).ready(function() {

		$("#error").hide();
		
		$("#userid").focus();
		
		$("#userid").keydown(function(event){
			if( event.keyCode == 13 ){ // 엔터를 했을 때
				goCheck();
			}
		});	
		
	}); // end of ready
		
	function goCheck() {
		
		var userid = $("#userid").val().trim();
		if(userid == "") {
			$("#error").show();
			$("#userid").val("");
			$("#userid").focus();
			return;
		}
		else {
			$("#error").hide();
			var frm = document.frmIdcheck;
			frm.method = "POST";
			frm.action = "idDuplicateCheck.do";
			frm.submit();
		}
		
	} // end of function goCheck() {
	
	/* 
	// *** !!!!! 팝업창에서 부모창으로 값 넘기기 일반적 방법
	function setUserID(userid) {
		
		var openerfrm = opener.document.registerFrm;
		// opener는 팝업창을 열게한 부모창을 말한다.
		// 여기서 부모창은 memberform.jsp 페이지이다.
		
		openerfrm.userid.value = userid; // openerfrm의 네임이 userid인 값에 userid를 넣어주세여
		openerfrm.pwd.focus();
		
		self.close(); // 팝업창 닫기
		
	} // end of function setUserID(userid) {
	*/	
	
	// *** !!!!! 팝업창에서 부모창으로 값 넘기기 jquery 방법
	function setUserID(userid) {
		
		// $("#userid", opener.document).val(userid); 또는
		$(opener.document).find("#userid").val(userid);
		
		// $("#pwd", opener.document).focus(); 또는
		$(opener.document).find("#pwd").focus();
		
		self.close();
		
	} // end of function setUserID(userid) {
		
</script>
</head>
<body style= "background-color: #fff0f5;">
	<span style= "font-size: 10pt; font-weight: bold;">${method}</span>
	<c:if test="${method == 'GET'}">
		<form name= "frmIdcheck">
			<table style= "width: 95%; height: 100px;">
				<tr>
					<td style= "text-align: center;">
						아이디를 입력하세요<br style= "line-height: 200%" />
						<input type= "text" id= "userid" name= "userid" size= "20" class= "box" /><br style= "line-height: 200%" />
						<span id= "error" style= "color: red; font-size: 12pt; font-weight: bold;">아이디를 입력하세요!!!</span><br/>
						<button type= "button"  class= "box" onClick="goCheck();">확인</button>
					</td>
				</tr>
			</table>
		</form>
	</c:if>	
	<c:if test="${method == 'POST'&& isUSEuserid}">
				<div align="center">
					<br style= "line-height: 400%" />
					ID[<span style= "color: red; font-weight: bold;">${userid}</span>]로 사용가능합니다.
					<br/><br/><br/>
					<button type="button"  class= "box" onClick="setUserID('${userid}');">닫기</button>
				</div>
							
		</c:if>
		<c:if test="${method == 'POST'&& !isUSEuserid}">
				<div align="center">
					[<span style= "color: red; font-weight: bold;">${userid}</span>]는 이미 사용중입니다.
					<br/>
					<form name= "frmIdcheck">
						<table style= "width: 95%; height: 100px;">
							<tr>
								<td style= "text-align: center;">
									아이디를 입력하세요<br style= "line-height: 200%" />
									<input type= "text" id= "userid" name= "userid" size= "20"  class= "box" /><br style= "line-height: 200%" />
									<span id= "error" style= "color: red; font-size: 12pt; font-weight: bold;">아이디를 입력하세요!!!</span><br/>
									<button type= "button"  class= "box" onClick="goCheck();">확인</button>
								</td>
							</tr>
						</table>
					</form>
				</div>
		</c:if>
</body>
</html>






