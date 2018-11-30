<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<%
    String ctxPath = request.getContextPath();
    //    /MyMVC
%>    
    
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%= ctxPath %>/css/style.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


<style>
	#div_userid {
		border: 0px solid red;
		width: 70%;
		height: 15%;
		margin-top: 10%;
		margin-bottom: 5%;
		margin-left: 10%;
		position: relative;
		text-align: right;
	}
	
	#div_email {
		width: 70%;
		height: 15%;
		margin-bottom: 5%;
		margin-left: 10%;
		position: relative;
		text-align: right;
	}
	
	#div_findResult {
		border: 0px solid red;
		width: 80%;
		height: 10%;
		margin-bottom: 5%;
		margin-left: 10%;		
		position: relative;
	}
	
	#div_btnFind {
		width: 70%;
		height: 15%;
		margin-top: 5%;
		margin-bottom: 5%;
		margin-left: 15%;
		position: relative;
	}
	
input {
		border: 2px solid gray;
		width: 70%;
}
	
</style>

<script type="text/javascript">
	
	$(document).ready(function(){
			
		$("#btnFind").click(function(){
			var frm = document.pwdFindFrm;
			frm.method = "POST";
			frm.action = "<%=ctxPath%>/pwdFind.do";
			frm.submit();
		});
		
		var method = "${method}";
		var userid = "${userid}";
		var email = "${email}";
		var n = "${n}";
		
		if(method=="POST" && userid != "" && email != "") {
			$("#userid").val(userid);
			$("#email").val(email);
		}
		
		if(method=="POST" && n==1) {
			$("#div_btnFind").hide();
		}
		else if(method=="POST" && (n == -1 || n == 0)) {
			$("#div_btnFind").show();
		}		
		
		$("#btnConfirmCode").click(function(){
			
			var frm = document.pwdFindFrm;
		 
			var n1 = frm.input_confirmCode.value;
			var n2 = frm.certificationCode.value;
		
			if( n1 == n2 ) { // 올바른 인증키를 넣었는지..
				alert("인증성공 되었습니다.");
				
				frm.method = "GET"; // 새암호와 새암호확인을 입력받기 위한 폼만을 띄워주기 때문에 GET 방식으로 한다.
				frm.action = "<%= ctxPath %>/pwdConfirm.do";
				frm.submit();
			}
			else {
				alert("인증코드를 다시 입력하세요!!");
				$("#input_confirmCode").val("");
				$("#input_confirmCode").focus();
			}
			
		});
			
	}); //  $(document).ready(function(){
	
</script>


<form name="pwdFindFrm">
   <div id="div_userid" align="center">
      <span style="color: navy; font-size: 12pt;">아이디</span>
      <input type="text" name="userid" id="userid" size="15" placeholder="ID" required />
   </div>
   
   <div id="div_email" align="center">
   	  <span style="color: navy; font-size: 12pt;">이메일</span>
      <input type="text" name="email" id="email" size="15" placeholder="abd@def.com" required />
   </div>
   
   <div id="div_findResult" align="center">
   	   <c:if test="${n == 1}">
   	      <div id="pwdConfirmCodeDiv">
   	      	  인증코드가 ${email}로 발송되었습니다.<br/>
   	      	  인증코드를 입력해주세요<br/><br/>
   	      	 <input type="text" name="input_confirmCode" id="input_confirmCode" required />
   	      	 <input type="hidden" name="certificationCode" id="input_confirmCode" value="${certification}" required />
   	      	 <br/><br/>
   	      	 <button type="button" class="btn btn-info" id="btnConfirmCode">인증하기</button>    
   	      </div>
   	   </c:if>
   	   
   	   <c:if test="${n == 0}">
   	   	  <span style="color: red;">사용자 정보가 없습니다.</span>
   	   </c:if>
   	   
   	   <c:if test="${n == -1}">
   	   	  <span style="color: red;">${sendFailmsg}</span>
   	   </c:if>
   	   
   </div>
   
   <div id="div_btnFind" align="center">
   		<button type="button" class="btn btn-success" id="btnFind">찾기</button>
   </div>
   
</form>

    