<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>:: 회원정보 수정 ::</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/MyMVC/css/style.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>


<script type="text/javascript">
$(document).ready(function(){ // jquery소스 설정 안해도 되는 이유 : include
	
	var now = new Date(); // 자바스크립트에서 현재날짜시각을 얻어오자.
	
	console.log(now.getFullYear());
	// 4자리 년도를 얻어온다.
	
	$(".error").hide();
	$("#error_passwd").hide();
	$("#name").focus();
	
	/*
	$("#name").blur(function(){
		var name = $(this).val().trim();
		if(name == ""){
			$(this).parent().find(".error").show();
			// name의 부모선택자 td에서 클래스명 error를 찾아서 보여주자.
		}
	}); // 선택자.blur();은 선택자에서 포커스를 잃어버렸을 경우 발생하는 이벤트
	*/
	
	$(".requiredInfo").each(function(){
		
		$(this).blur(function(){
			var data = $(this).val().trim();
			if(data == "") { //입력하지 않거나 공백만 입력했을 경우
				$(this).parent().find(".error").show();
				$(":input").attr("disabled", true).addClass("bgcol");
				$(this).attr("disabled", false).removeClass("bgcol").focus();
				// $(":input") -> 모든 input 태그 / .attr("disabled", true) ->
			}
			else { // 공백이 아닌 글자를 입력했을 경우
				$(this).parent().find(".error").hide();
				$(":input").attr("disabled", false).removeClass("bgcol");
			}
			}); // blur();
			
	}); // each(); -> requiredInfo가 있는 만큼 반복하라
		
	$("#pwd").blur(function(){
		var passwd = $(this).val();
		//var regExp_PW = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g; 아래처럼 쓸 수 있다.
		var regExp_PW = new RegExp(/^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g);
		
		var bool = regExp_PW.test(passwd);
		
		if(!bool) {
			$("#error_passwd").show();
			$(":input").attr("disabled", true).addClass("bgcol");
			$(this).attr("disabled", false).removeClass("bgcol");
			$(this).focus();
		}
		else {
			$("#error_passwd").hide();
			$(":input").attr("disabled", false).removeClass("bgcol");
			// $("#pwdcheck").focus(); 안먹어...
		}
		
	}); //end of $("#pwd").blur(function(){
		
	$("#pwdcheck").blur(function(){
	
		var passwd = $("#pwd").val();
		var passwdCheck = $(this).val();
		
		if(passwd != passwdCheck) {
			$(this).parent().find(".error").show();
			$(":input").attr("disabled", true).addClass("bgcol");
			$(this).attr("disabled", false).removeClass("bgcol");
			$("#pwd").attr("disabled", false).removeClass("bgcol");
			$("#pwd").focus();
		}
		else {
			$(this).parent().find(".error").hide();
			$(":input").attr("disabled", false).removeClass("bgcol");
		}
	
	}); // end of $("#pwdcheck").blur(function(){
	
	$("#email").blur(function(){
		
		var email = $(this).val();
		var regExp_EMAIL = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i; 
		var bool = regExp_EMAIL.test(email);
		
		if( !bool ) {
			$(this).parent().find(".error").show();
			$(":input").attr("disabled", true).addClass("bgcol");
			$(this).attr("disabled", false).removeClass("bgcol");
			$(this).focus();
		}
		else {
			$(this).parent().find(".error").hide();
			$(":input").attr("disabled", false).removeClass("bgcol");
		}
		
	}); //end of $("#email").blur(function(){
		
	$("#hp1").val("${loginuser.hp1}");
		
	$("#hp2").blur(function(){
	
		var hp2 = $(this).val();
		
		var bool1 = false;
		var regExp_HP2a = /[1-9][0-9][0-9]/g;
		var bool1 = (hp2.length == 3) && regExp_HP2a.test(hp2); 
		
		var bool2 = false;
		var regExp_HP2b = /[1-9][0-9][0-9][0-9]/g;
		var bool2 = (hp2.length == 4) && regExp_HP2b.test(hp2);
		
		if( !(bool1 || bool2) ) {
			$(this).parent().find(".error").show();
			$(":input").attr("disabled", true).addClass("bgcol");
			$(this).attr("disabled", false).removeClass("bgcol").focus();
		}
		else {
			$(this).parent().find(".error").hide();
			$(":input").attr("disabled", false).removeClass("bgcol");
			
		}
		
	}); //end of $("#hp2").blur(function(){
	
	$("#hp3").blur(function(){
	
		var hp3 = $(this).val();
		var regExp_HP3a = /[0-9][0-9][0-9][0-9]/g;
		
		var bool = regExp_HP3a.test(hp3);
		
		if(!bool) {
			$(this).parent().find(".error").show();
			$(":input").attr("disabled", true).addClass("bgcol");
			$(this).attr("disabled", false).removeClass("bgcol").focus();
		}
		else {
			$(this).parent().find(".error").hide();
			$(":input").attr("disabled", false).removeClass("bgcol");
			
		}
		
	}); // end of $("#hp3").blur(function(){
		
	$("#zipcodeSearch").click(function(){
		new daum.Postcode({
				oncomplete: function(data) {
				    $("#post1").val(data.postcode1);
				    $("#post2").val(data.postcode2);
				    $("#addr1").val(data.address); 
				    $("#addr2").focus();
				}
		}).open();
				
	});
	
	$(".address").blur(function(){
		var address = $(this).val().trim();
		if(address == ""){
			$(this).parent().find(".error").show();
			$(":input").attr("disabled", true).addClass("bgcol");
			$(this).attr("disabled", false).removeClass("bgcol").focus();
		}
		else {
			$(this).parent().find(".error").hide();
			$(":input").attr("disabled", false).removeClass("bgcol");
		}
	});
	
}); //end of $(document).ready(function(){
	
function goEdit(event) {
	
	var flagBool = false;
	
	$(".requiredInfo").each(function(){
		var data = $(this).val().trim();
		if(data == "") {
			flagBool = true;
			return false;
			/*
			   for문에서의 continue; 와 동일한 기능을 하는것이 
			   each(); 내에서는 return true; 이고,
			   for문에서의 break; 와 동일한 기능을 하는것이 
			   each(); 내에서는 return false; 이다.
			*/
		}
	});
	
	if(flagBool) {
		alert("필수입력란은 모두 입력하셔야 합니다.");
		event.preventDefault(); // click event 를 작동치 못하도록 한다.
		return;
	}
	else {
		if(confirm('회원정보를 수정하시겠습니까?')){
	       	 var frm = document.registerFrm;
	          frm.method = "post";
	          frm.action = "memberEditEnd.do";
	          frm.submit();
	    }
	}
	// 유효성 검사 통과하면 memberInsert.jsp로 보내준다.
	
} // end of function goRegister(event) {

</script>

<style>
   table#tblMemberRegister {
   	    width: 93%;
   	    
   	    /* 선을 숨기는 것 */
   	    border: hidden;
   	    
   	    margin: 10px;
   }  
   
   table#tblMemberRegister #th {
   		height: 40px;
   		text-align: center;
   		background-color: silver;
   		font-size: 14pt;
   }
   
   table#tblMemberRegister td {
   		/* border: solid 1px gray;  */
   		line-height: 30px;
   		padding-top: 8px;
   		padding-bottom: 8px;
   }
   
   .star { color: red;
           font-weight: bold;
           font-size: 13pt;
   }
   
</style>
</head>
<body>
<div class="row">
	<div class="col-md-12" align="center">
	<form name="registerFrm">
	
	<table id="tblMemberRegister">
		<thead>
		<tr>
			<th colspan="2" id="th">::: 회원정보 수정 (<span style="font-size: 10pt; font-style: italic;"><span class="star">*</span>표시는 필수입력사항</span>) :::</th>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td style="width: 20%; font-weight: bold;">성명&nbsp;<span class="star">*</span></td>
			<td style="width: 80%; text-align: left;">
				<input type="hidden" name= "idx" value= "${loginuser.idx}"/>
			    <input type="text" name="name" id="name" class="requiredInfo" value="${loginuser.name}" required /> 
				<span class="error">성명은 필수입력 사항입니다.</span>
			</td>
		</tr>

		<tr>
			<td style="width: 20%; font-weight: bold;">비밀번호&nbsp;<span class="star">*</span></td>
			<td style="width: 80%; text-align: left;"><input type="password" name="pwd" id="pwd" class="requiredInfo" required />
				<span id="error_passwd">암호는 영문자,숫자,특수기호가 혼합된 8~15 글자로만 입력가능합니다.</span>
			</td>
		</tr>
		<tr>
			<td style="width: 20%; font-weight: bold;">비밀번호확인&nbsp;<span class="star">*</span></td>
			<td style="width: 80%; text-align: left;"><input type="password" id="pwdcheck" class="requiredInfo" required /> 
				<span class="error">암호가 일치하지 않습니다.</span>
			</td>
		</tr>
		<tr>
			<td style="width: 20%; font-weight: bold;">이메일&nbsp;<span class="star">*</span></td>
			<td style="width: 80%; text-align: left;"><input type="text" name="email" id="email" class="requiredInfo" value="${loginuser.email}" /> 
			    <span class="error">이메일 형식에 맞지 않습니다.</span>
			</td>
		</tr>
		<tr>
			<td style="width: 20%; font-weight: bold;">연락처</td>
			<td style="width: 80%; text-align: left;">
			    <select name="hp1" id="hp1" style="width: 75px; padding: 8px;" >
					<option value="010" selected>010</option>
					<option value="011">011</option>
					<option value="016">016</option>
					<option value="017">017</option>
					<option value="018">018</option>
					<option value="019">019</option>
				</select>&nbsp;-&nbsp;
			    <input type="text" name="hp2" id="hp2" size="6" maxlength="4"  value="${loginuser.hp2}"  />&nbsp;-&nbsp;
			    <input type="text" name="hp3" id="hp3" size="6" maxlength="4"  value="${loginuser.hp3}"  />
			    <span class="error error_hp">휴대폰 형식이 아닙니다.</span>
			</td>
		</tr>
		<tr>
			<td style="width: 20%; font-weight: bold;">우편번호</td>
			<td style="width: 80%; text-align: left;">
			   <input type="text" id="post1" name="post1" size="6" maxlength="3" value="${loginuser.post1}" />
			   &nbsp;-&nbsp;
			   <input type="text" id="post2" name="post2" size="6" maxlength="3" value="${loginuser.post2}" />&nbsp;&nbsp;
			   <!-- 우편번호 찾기 -->
			   <img id="zipcodeSearch" src="./images/b_zipcode.gif" style="vertical-align: middle;" />
			   <span class="error error_post">우편번호 형식이 아닙니다.</span>
			</td>
		</tr>
		<tr>
			<td style="width: 20%; font-weight: bold;">주소</td>
			<td style="width: 80%; text-align: left;">
			   <input type="text" id="addr1" class= "address" name="addr1" size="60" maxlength="100" value="${loginuser.addr1}" /><br style="line-height: 200%"/>
			   <input type="text" id="addr2" class= "address" name="addr2" size="60" maxlength="100" value="${loginuser.addr2}" />
			   <span class="error">주소를 입력하세요.</span>
			</td>
		</tr>
		
		<tr>
			<td>
			<button type="button" id="btnEdit" style="border:none; width: 150px; height: 30px; margin-top: 10%; margin-left: 200%;" onClick="goEdit(event);">수정완료</button>
			</td> 
		</tr>
		
		</tbody>
	</table>
</form>
	</div>
</div>
</body>
</html>