<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/MyMVC/css/style.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<style>
	#div_name {
		border: 0px solid red;
		width: 70%;
		height: 15%;
		margin-top: 10%;
		margin-bottom: 5%;
		margin-left: 10%;
		position: relative;
		text-align: right;
	}
	
	#div_mobile {
		width: 70%;
		height: 15%;
		margin-bottom: 5%;
		margin-left: 10%;
		position: relative;
		text-align: right;
	}
	
	#div_findResult {
		border: 0px solid red;
		width: 70%;
		height: 15%;
		margin-bottom: 5%;
		margin-left: 15%;		
		position: relative;
	}
	
	#div_btnFind {
		width: 70%;
		height: 15%;
		margin-top: 10%;
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
		
		var method = "${method}";
		if(method == "GET") {
			$("#div_findResult").hide();
		}
		else if(method == "POST") {
			$("#name").val("${name}");
			$("#mobile").val("${mobile}");
			$("#div_findResult").show();
		}
		
		$("#btnFind").click(function(){
			
			var frm = document.idFindFrm;
			frm.action = "/MyMVC/idFind.do";
			frm.method = "POST";
			frm.submit();
			
		});
	});
	
</script>

<title></title>
</head>
<body>

<form name="idFindFrm">

   <div id="div_name" align="center">
      <span style="color: navy; font-size: 13pt;">성명</span>
      <input type="text" name="name" id="name" size="15" value="" placeholder="홍길동" required />
   </div>
   
   <div id="div_mobile" align="center">
   	  <span style="color: navy; font-size: 13pt;">휴대전화</span>
      <input type="text" name="mobile" id="mobile" size="15" value="" placeholder="-없이 입력하세요" required />
   </div>
   
   <div id="div_findResult" align="center">
   	  ID : <span style="color: red; font-size: 25pt; font-weight: bold;">${userid}</span> 
   </div>
   
   <div id="div_btnFind" align="center">
   		<button type="button" class="btn btn-success" id="btnFind">찾기</button>
   </div>
   
</form>

</body>
</html>