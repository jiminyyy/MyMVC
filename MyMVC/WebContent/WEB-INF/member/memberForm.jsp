<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<jsp:include page="../header.jsp" />

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
   		background-color: #dfefff;
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
		
		$("#userid").bind("keyup", function(){ // keyup(키보드를 눌렀다 떼는 event)가 생기면 아래 function 실행
			alert("아이디중복확인 버튼으로 중복검사 먼저 하세요!!");
			$(this).val(""); // userid의 값을 지운다.
		}); // end of $("#userid").bind("keyup", function(){
			
		//////////////////// ID 중복확인 팝업창 생성
		$("#idcheck").click(function(){ // 아이디 중복확인 버튼을 누르면 아래 function 실행
			// 팝업창 띄우기
			var url = "idDuplicateCheck.do";
			window.open(url, "idcheck", 
							 "left= 500px, top= 100px, width= 300px, height= 230px" );
			// 기본적으로 아무런 조건없이 그냥 어떤 창을 띄우면 method는 GET방식으로 움직인다.
		
		}); // end of $("#idcheck").click(function(){
			
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
		
	function goRegister(event) {
		
		if (!$("input:radio[name=gender]").is(":checked")) {
			alert("성별을 선택해주세요!!");
			return;
		}
		if (!$("input:checkbox[id=agree]").is(":checked")) {
			alert("이용약관에 동의해주세요!!")
			return;
		}
		
		var frm = document.registerFrm;
		frm.method = "POST";
		frm.action = "memberRegisterEnd.do";
		frm.submit();
		// 유효성 검사 통과하면 memberInsert.jsp로 보내준다.
		
	} // end of function goRegister(event) {

	
</script>

<div class="row">
	<div class="col-md-12" align="center">
	<form name="registerFrm">
	
	<table id="tblMemberRegister">
		<thead>
		<tr>
			<th colspan="2" id="th">::: 회원가입 (<span style="font-size: 10pt; font-style: italic;"><span class="star">*</span>표시는 필수입력사항</span>) :::</th>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td style="width: 20%; font-weight: bold;">성명&nbsp;<span class="star">*</span></td>
			<td style="width: 80%; text-align: left;">
			    <input type="text" name="name" id="name" class="requiredInfo" required /> 
				<span class="error">성명은 필수입력 사항입니다.</span>
			</td>
		</tr>
		<tr>
			<td style="width: 20%; font-weight: bold;">아이디&nbsp;<span class="star">*</span></td>
			<td style="width: 80%; text-align: left;">
			    <input type="text" name="userid" id="userid" class="requiredInfo" required />&nbsp;&nbsp;
			    <!-- 아이디중복체크 -->
			    <img id="idcheck" src="/MyMVC/images/b_id_check.gif" style="vertical-align: middle;" />
			    <span class="error">아이디는 필수입력 사항입니다.</span>
			</td> 
		</tr>
		<tr>
			<td style="width: 20%; font-weight: bold;">비밀번호&nbsp;<span class="star">*</span></td>
			<td style="width: 80%; text-align: left;">
			<input type="password" name="pwd" id="pwd" class="requiredInfo" required />
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
			<td style="width: 80%; text-align: left;"><input type="text" name="email" id="email" class="requiredInfo" placeholder="abc@def.com" /> 
			    <span class="error">이메일 형식에 맞지 않습니다.</span>
			</td>
		</tr>
		<tr>
			<td style="width: 20%; font-weight: bold;">연락처</td>
			<td style="width: 80%; text-align: left;">
			    <select name="hp1" id="hp1" style="width: 75px; padding: 8px;">
					<option value="010" selected>010</option>
					<option value="011">011</option>
					<option value="016">016</option>
					<option value="017">017</option>
					<option value="018">018</option>
					<option value="019">019</option>
				</select>&nbsp;-&nbsp;
			    <input type="text" name="hp2" id="hp2" size="6" maxlength="4" />&nbsp;-&nbsp;
			    <input type="text" name="hp3" id="hp3" size="6" maxlength="4" />
			    <span class="error error_hp">휴대폰 형식이 아닙니다.</span>
			</td>
		</tr>
		<tr>
			<td style="width: 20%; font-weight: bold;">우편번호</td>
			<td style="width: 80%; text-align: left;">
			   <input type="text" id="post1" name="post1" size="6" maxlength="3" />
			   &nbsp;-&nbsp;
			   <input type="text" id="post2" name="post2" size="6" maxlength="3" />&nbsp;&nbsp;
			   <!-- 우편번호 찾기 -->
			   <img id="zipcodeSearch" src="/MyMVC/images/b_zipcode.gif" style="vertical-align: middle;" />
			   <span class="error error_post">우편번호 형식이 아닙니다.</span>
			</td>
		</tr>
		<tr>
			<td style="width: 20%; font-weight: bold;">주소</td>
			<td style="width: 80%; text-align: left;">
			   <input type="text" id="addr1" class= "address" name="addr1" size="60" maxlength="100" /><br style="line-height: 200%"/>
			   <input type="text" id="addr2" class= "address" name="addr2" size="60" maxlength="100" />
			   <span class="error">주소를 입력하세요.</span>
			</td>
		</tr>
		
		<tr>
			<td style="width: 20%; font-weight: bold;">성별</td>
			<td style="width: 80%; text-align: left;">
			   <input type="radio" id="male" name="gender" value="1" /><label for="male" style="margin-left: 2%;">남자</label>
			   <input type="radio" id="female" name="gender" value="2" style="margin-left: 10%;" /><label for="female" style="margin-left: 2%;">여자</label>
			</td>
		</tr>
		
		<tr>
			<td style="width: 20%; font-weight: bold;">생년월일</td>
			<td style="width: 80%; text-align: left;">
			   <input type="number" id="birthyyyy" name="birthyyyy" min="1950" max="2050" step="1" value="1995" style="width: 80px;" required />
			   
			   <select id="birthmm" name="birthmm" style="margin-left: 2%; width: 60px; padding: 8px;">
					<option value ="01">01</option>
					<option value ="02">02</option>
					<option value ="03">03</option>
					<option value ="04">04</option>
					<option value ="05">05</option>
					<option value ="06">06</option>
					<option value ="07">07</option>
					<option value ="08">08</option>
					<option value ="09">09</option>
					<option value ="10">10</option>
					<option value ="11">11</option>
					<option value ="12">12</option>
				</select> 
			   
			   <select id="birthdd" name="birthdd" style="margin-left: 2%; width: 60px; padding: 8px;">
					<option value ="01">01</option>
					<option value ="02">02</option>
					<option value ="03">03</option>
					<option value ="04">04</option>
					<option value ="05">05</option>
					<option value ="06">06</option>
					<option value ="07">07</option>
					<option value ="08">08</option>
					<option value ="09">09</option>
					<option value ="10">10</option>
					<option value ="11">11</option>
					<option value ="12">12</option>
					<option value ="13">13</option>
					<option value ="14">14</option>
					<option value ="15">15</option>
					<option value ="16">16</option>
					<option value ="17">17</option>
					<option value ="18">18</option>
					<option value ="19">19</option>
					<option value ="20">20</option>
					<option value ="21">21</option>
					<option value ="22">22</option>
					<option value ="23">23</option>
					<option value ="24">24</option>
					<option value ="25">25</option>
					<option value ="26">26</option>
					<option value ="27">27</option>
					<option value ="28">28</option>
					<option value ="29">29</option>
					<option value ="30">30</option>
					<option value ="31">31</option>
				</select> 
			</td>
		</tr>
		
		<tr>
			<td colspan="2">
				<label for="agree">이용약관에 동의합니다</label>&nbsp;&nbsp;<input type="checkbox" id="agree" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="text-align: center; vertical-align: middle;">
				<iframe src="/MyMVC/agree.html" width="85%" height="150px" class="box" ></iframe>
			</td>
		</tr>
		<tr>
			<td colspan="2" style="line-height: 90px;">
				<button type="button" id="btnRegister" style="background-image:url('/MyMVC/images/join.png'); border:none; width: 135px; height: 34px; margin-left: 45%;" onClick="goRegister(event);"></button> 
			</td>
		</tr>
		</tbody>
	</table>
	</form>
	</div>
</div>

<jsp:include page="../footer.jsp" />