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

<style type= "text/css">

				 
#memberlist			{border: none;
					 width: 20%;
				 	 background-color: #9999ff;
				 	 color: white;
				 	 cursor: pointer;
				 	 padding: 10px;
				 	 margin-top: 10%;
				 	 margin-left: 40%;
				 	 text-align: center;
					 }

h3					{/* border: 0px solid navy; */
					 border-bottom: 2px solid navy;
				 	 width: 70%; 
				 	 padding: 20px;
				 	 margin-left: 15%;
					 margin-bottom: 40px;
				 	 background-color: white;
				 	 /* font-size: 35pt; */
				 	 text-align: center;
				 	 font-weight: bold;
				 	 }

table				{border: 0px solid darkgray;
				 	 width: 70%; 
				 	 font-size: 20pt;
				 	 font-weight: bold;
				 	 margin-bottom: 5%;
				 	 }
				 	 
td					{border: 0px solid darkgray;
					 width : 30%;
					 text-align: left;}

</style>
</head>
<body>
<div class= "row">
	<div class= "col-md-12">
		<h3>::: ${memberInfo.name} 님의 정보 :::</h3>
		
			<div>
				<table style="border: 0px solid black; margin-left: 15%;">
					<tr>
						<td id= "first">▶ 회원 번호</td>
						<td>${memberInfo.idx}</td>
					</tr>
					<tr>
						<td>▶ 성명</td>	
						<td>${memberInfo.name}</td>
					</tr>
					<tr>
						<td>▶ 아이디</td>	
						<td>${memberInfo.userid}</td>
					</tr>
					<tr>
						<td>▶ 이메일</td>	
						<td>${memberInfo.email}</td>
					</tr>
					<tr>
						<td>▶ 연락처</td>
						<td>${memberInfo.allHp}</td>	
					</tr>
					<tr>
						<td>▶ 우편번호</td>
						<td>${memberInfo.allPost}</td>	
					</tr>
					<tr>
						<td>▶ 주소</td>	
						<td>${memberInfo.allAddr}</td>
					</tr>
					<tr>
						<td>▶ 생년월일</td>
						<td>${memberInfo.birthyyyy}년 ${memberInfo.birthmm}월 ${memberInfo.birthdd}일</td>	
					</tr>
					<tr>
						<td>▶ 성별</td>	
						<td>${memberInfo.showGender}</td>
					</tr>
					<tr>
						<td>▶ 나이</td>	
						<td>${memberInfo.showAge}</td>
					</tr>
					<tr>
						<td>▶ 가입일자</td>	
						<td>${memberInfo.registerday}</td>
					</tr>
				</table>
				
				<div id="memberlist" onClick="javascript:location.href='javascript:self.close();'">닫기</div>
				<%-- <div id="memberlist" onClick="javascript:location.href='javascript:history.back();'">회원목록</div> --%>
				<%-- history.back();을 이용할 경우 이전페이지를 캡쳐하여 그대로 보여준다. => 변경 후 새로고침을 눌러도 반영이 안된다.(status를 0으로 변경해도 회원목록이 보임)
						goBackURL을 이용하여 되돌아 갈 경우 해당 페이지를 보여준다. => 변경 후 새로고침 실행하면 반영됨(status를 0으로 변경하면 회원목록이 안보임)--%>
				
				
			</div>
	</div>
</div>

</body>
</html>