<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@	page import="member.model.*" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    

<%-- ===== 이 페이지는 header.jsp에 포함되어있다. ===== --%>

<style>

table#loginTbl		{width: 90%;
					 border: 1px solid gray;
					 border-collapse: collapse;
					 margin-left: 5%;}
					 
#th					{background-color: white;
					 font-size: 14pt;}
					 
table#loginTbl td	{border: 1px solid gray;}
					 
</style>

<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-2.0.0.js"></script> 
이미 header에 있는데 또 만들면 스피너가 죽어 --%>

<script type="text/javascript" >

	$(document).ready(function(){
		
		$("#loginUserid").focus();
		
		$("#btnSubmit").click(function(){
			
 			goLogin();
			
		}); // end of $("#btnSubmit").click(function(){
		
		$("#loginPwd").keydown(function(event){
			
			if(event.keyCode == 13) {
				goLogin();
			}
			
		}); // end of $("#loginPwd").keydown(function(event){
			
		
		$(".myclose").click(function(){
			// alert("닫는다.");
			
			javascript:history.go(0);
			
			// 현재 페이지를 새로고침을 함으로써 모달창에 입력한 성명과 휴대폰의 값이 텍스트박스에 남겨있지 않고 삭제하는 효과를 누린다. 
			
			/* === 새로고침(다시읽기) 방법 3가지 차이점 ===
			   >>> 1. 일반적인 다시읽기 <<<
			   window.location.reload();
			   ==> 이렇게 하면 컴퓨터의 캐쉬에서 우선 파일을 찾아본다.
			            없으면 서버에서 받아온다.  
			   
			   >>> 2. 강력하고 강제적인 다시읽기 <<<
			   window.location.reload(true);
			   ==> true 라는 파라미터를 입력하면, 무조건 서버에서 직접 파일을 가져오게 된다.
			            캐쉬는 완전히 무시된다.
			   
			   >>> 3. 부드럽고 소극적인 다시읽기 <<<
			   history.go(0);
			   ==> 이렇게 하면 캐쉬에서 현재 페이지의 파일들을 항상 우선적으로 찾는다.
			*/	
			
		});
			
		
	}); // end of $(document).ready(function(){
		
		
	function goLogin() {
		
		var loginUserid = $("#loginUserid").val().trim();
		var loginPwd = $("#loginPwd").val().trim();
		
		if(loginUserid == "") {
			alert ("아이디를 입력하세요!!");
			$("#loginUserid").val("").focus();
			return;
		}
		
		if(loginPwd == "") {
			alert ("비밀번호를 입력하세요!!");
			$("#loginPwd").val("").focus();
			return;
		}
		
		var frm = document.loginFrm;
		frm.method = "POST";
		frm.action = "loginEnd.do";
		frm.submit();
		
	} // end of function goLogin() {
		
	function goLogOut() {
		
		location.href = "<%=request.getContextPath()%>/logout.do";
		
	} // end of function goLogOut() {
		
	function goEditPersonal(idx) {
		
		var url = "memberEdit.do?idx="+idx;
		
		window.open(url, "memberEdit",
				    "left=150px, top=50px, width=800px, height=650px");

	}
	
	// *** payment gateway(결제) 시작
	function goCoinPurchaseTypeChoice(idx) { // 코인충전버튼 클릭시 실행되는 함수 -> 코인충전 창을 띄운다.
		
		var url = "coinPurchaseTypeChoice.do?idx="+idx;
	
		window.open(url, "coinPurchaseTypeChoice",
					"left=350px, top=100px, width=650px, height=570px");
		
	} // end of function goCoinPurchaseTypeChoice(idx) { // 팝업창 띄우기

		
	function goCoinPurchaseEnd(idx, coinmoney) { // 코인구매액 결제창 띄우기
	
		var url = "coinPurchaseEnd.do?idx="+idx+"&coinmoney="+coinmoney;
		
		window.open(url, "coinPurchaseTypeEnd",
					"left=350px, top=100px, width=850px, height=600px");
	
	}
	
	function goCoinUpdate(idx){ //, coinmoney) {
		
		var frm = document.coinUpdateFrm;
		frm.idx.value = idx;
		// frm.idx.value = coinmoney;
		frm.method = "POST";
		frm.action = "<%=request.getContextPath()%>/coinAddUpdateLoginuser.do";
		frm.submit();
	}
	
	// *** payment gateway 끝
	
</script>

<%
	MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
	
	if(loginuser == null) { // 로그인 하기 전 화면을 띄운다.
		/*
		        로그인 하려고 할때 WAS(톰캣서버)는 사용자 컴퓨터 웹브라우저로 부터 전송받은 쿠키를 검사해서 그 쿠키의 사용유효시간이 0초 짜리가 아니라면 그 쿠키를 가져와서 웹브라우저에 적용시키도록 해준다.
		        우리는 쿠키의 키 값이 "saveid" 가 있으면  로그인 ID 텍스트박스에 아이디 값을 자동적으로 올려주면 된다.
		*/
	
		Cookie[] cookieArr = request.getCookies();
		//  쿠키는 쿠키의 이름별로 여러개 저장되어 있으므로 쿠키를 가져올때는 배열타입으로 가져와서 가져온 쿠키배열에서 개발자가 원하는 쿠키의 이름과 일치하는것을 뽑기 위해서는 쿠키 이름을 하나하나씩 비교하는 것 밖에 없다.
		
		String cookie_key = "";
		String cookie_value = "";
		boolean flag = false;
		
		if(cookieArr != null) { // 클라이언트(사용자) 컴퓨터에서 보내온 쿠키의 정보가 있다면
			for(Cookie c :cookieArr) { 
				cookie_key = c.getName(); // 쿠키의 이름(키값)을 꺼내오는 메소드
			
				if("saveid".equals(cookie_key)) {
					cookie_value = c.getValue(); // 쿠키의 value값을 꺼내는 메소드
					flag = true; 
					break;
				}
			} // end of for
			
			
		}
%>		
		<%-- =====로그인 하기 이전 화면===== --%>
		
		<form name="loginFrm">
			
			<table id="loginTbl">
				<thead>
					<tr>
						<th colspan="2" id="th" style="text-align: center;">LOGIN</th>
					</tr>
				</thead>
				
				<tbody>
					<tr>
						<td style="width: 30%; border-bottom: hidden; border-right: hidden; padding: 10px;">ID</td>
						<td style="width: 70%; border-bottom: hidden; border-left: hidden; padding: 10px;">
							<input type="text" id="loginUserid" name="userid" size="13" class="box" 
<%							if(flag) {
%>								value="<%=cookie_value%>"
<%							}
%>							/></td>
					</tr>
					<tr>
						<td style="width: 30%; border-bottom: hidden; border-right: hidden; padding: 10px;">암호</td>
						<td style="width: 70%; border-bottom: hidden; border-left: hidden; padding: 10px;">
							<input type="password" id="loginPwd" name="pwd" size="13" class="box" /></td>
					</tr>
					
					<%-- 아이디찾기 | 비밀번호 찾기 --%> 
					<tr>
						<td colspan="2" align="center">
							<a style="cursor:pointer;" data-toggle="modal" data-target="#userIdfind" data-dismiss="modal" >아이디찾기</a>
							<a style="cursor:pointer;" data-toggle="modal" data-target="#passwdFind" data-dismiss="modal" >비밀번호찾기</a>
						</td>
					</tr>	
					<tr>
						<td colspan="2" align="center" style="padding: 10px;">
							
							<%
							if(flag == false){
							%>
								<input type="checkbox" id="saveid" name="saveid" style="vertical-align: text-top;" />
								<label for="saveid" style=" vertical-align: middle; margin-right: 20px;">아이디저장</label>
							<%
							}
							else {
							%>
								<input type="checkbox" id="saveid" name="saveid" style="vertical-align: text-top;" checked />
								<label for="saveid" style=" vertical-align: middle; margin-right: 20px;">아이디저장</label>	
							<%
							}
							%>	
							<button type="button" id="btnSubmit" style="width: 67px; height: 27px; background-image: url('/MyMVC/images/login.png');
																		vertical-align: middle; border: none;" ></button>
						</td>
					</tr>
					
				</tbody>
			</table>
		
		</form>
<%	}
	
	else { // 로그인 후 화면을 띄운다.
%>
		<table style="width: 90%; height: 130px; margin-left: 15px;">
			<tr style="background-color: #ffeffc;">
				<td align="center">
					<span style="font-weight: bold; font-size: 14pt;">${(sessionScope.loginuser).name}</span>
	  	 			[<span style="color: red; font-style:italic; font-weight: bold; padding: 5px;">${(sessionScope.loginuser).userid}</span>]님<br/><br/>
			  	  	<div align="left" style="padding-left: 20px; line-height: 150%;">
			  	      <span style="font-weight: bold;">코인액 :</span>&nbsp;&nbsp;
			  	      <fmt:formatNumber value="${(sessionScope.loginuser).coin}" pattern="###,###" /> 원
			  	   	  <br/>   
			  	      <span style="font-weight: bold;">포인트 :</span>&nbsp;&nbsp;
			  	      <fmt:formatNumber value="${(sessionScope.loginuser).point}" pattern="###,###" /> POINT
			  	  	</div>
			  	  
			  	    <br/>로그인 중...<br/><br/>
			  	  
			  	  	[<a href="javascript:goEditPersonal('${sessionScope.loginuser.idx}');">나의정보</a>]&nbsp;&nbsp;
			  	  	[<a href="javascript:goCoinPurchaseTypeChoice('${sessionScope.loginuser.idx}');">코인충전</a>] 
			  	  	<br/><br/>
			  	  
			  	    <button type="button" onClick="goLogOut();">로그아웃</button> 
				</td>
			</tr>

		</table>
<%	}
	

%>

<%-- ****** 아이디 찾기 Modal ****** --%>
<div class="modal fade" id="userIdfind" role="dialog">
  <div class="modal-dialog">
  
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close myclose" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">아이디 찾기</h4>
      </div>
      <div class="modal-body" style="height: 400px; width: 100%;">
        <div id="idFind">
        	<iframe style="border: none; width: 100%; height: 350px;" src="<%= request.getContextPath() %>/idFind.do">
        	</iframe>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default myclose" data-dismiss="modal">Close</button>
      </div>
    </div>
    
  </div>
</div>   

<%-- ****** 비밀번호 찾기 Modal ****** --%>
<div class="modal fade" id="passwdFind" role="dialog">
  <div class="modal-dialog">
  
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close myclose" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">비밀번호 찾기</h4>
      </div>
      <div class="modal-body">
        <div id="pwFind">
        	<iframe style="border: none; width: 100%; height: 400px;" src="<%= request.getContextPath() %>/pwdFind.do">  
        	</iframe>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default myclose" data-dismiss="modal">Close</button>
      </div>
    </div>
    
  </div>
</div>

<%--
	// === Payment Gateway(결제) === //
	PG에 코인금액을 카드로 결제 후 DB상의 사용자의 코인액을 변경해주는 폼
--%>

<form name="coinUpdateFrm">
	<input type="hidden" name="idx" />
	<%-- <input type="hidden" name="coinmoney" /> --%>
</form>

