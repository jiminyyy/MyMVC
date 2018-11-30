<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% String ctxPath = request.getContextPath(); %>  
  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jQuery Ajax 예제2(서버의 응답을 HTML로 받아서 HTML로 출력하는 예제)</title>

<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/ajaxstudy/chap2/css/style.css" />

<script type="text/javascript" src="<%=ctxPath %>/js/jquery-3.3.1.min.js"></script> 

<script type="text/javascript">
	
	$(document).ready(function(){
		
		<%-- newsTitle.do의 내용 --%>
		startAjaxCalls();
		
		$("#btn1").click(function(){
			
			var form_data = {searchname:$("#name").val()}; // 키값:밸류값
			
			$.ajax({
				
				url:"memberHTML.do",	
				type:"GET",
				data:form_data,
				dataType:"html",
				success:function(data){
					$("#memberInfo").empty().html(data);
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
				
			});
		});
		
		$("#btn2").click(function(){
			
			$.ajax({
				
				url:"imageHTML.do",	
				type:"GET",
				dataType:"html",
				success:function(html){
					$("#imgInfo").empty().html(html);
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
				
			});
		});
		
	$("#btn3").click(function(){
			
			$("#memberInfo").empty();
			$("#imgInfo").empty();
			$("#name").val("");
			
		});	
		
	});// end of $(document).ready();------------------
	
	function getNewsTitle(){
		
		$.ajax({
			
			url:"newsTitleHTML.do",	
			type:"GET",
			dataType:"html",
			success:function(data){
				
				$("#newsTitle").html(data);
				/* 			
				console.log("== data 값 보기 ==");
				console.log(data);
				*/
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
			
		});
		
	}
	
	<%-- newsTitle.do의 내용 --%>
	function startAjaxCalls(){
		
		getNewsTitle();
		
		setTimeout(function(){
					startAjaxCalls();
					},10000); //10초
		// 뉴스정보를 10초마다 자동갱신
	} // function startAjaxCalls({
	
</script>

</head>
<body>
    <h2>이곳은 MyMVC/ajaxstudy.do 페이지의 데이터가 보이는 곳입니다.</h2>
    <br/><br/>    
	<div align="center">
		<form name="searchFrm">
			회원명 : <input type="text" name="name" id="name" />
		</form>
		<br/>
		<button type="button" id="btn1">회원명단보기</button> &nbsp;&nbsp;
		<button type="button" id="btn2">사진보기</button> &nbsp;&nbsp;
		<button type="button" id="btn3">지우기</button> 
	</div>
	
	<div id="newsTitle" style="margin-top: 20px; margin-bottom: 20px;"></div> 
	
	<div id="container">
		<div id="memberInfo"></div>
		<div id="imgInfo"></div>
	</div>

</body>
</html>




