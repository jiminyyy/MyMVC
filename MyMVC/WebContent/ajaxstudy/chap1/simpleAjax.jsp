<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%  String ctxPath = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>간단한 jQuery Ajax 예제1(서버의 응답을 text로 받아 text로 출력)</title>

<script type="text/javascript" src="<%=ctxPath%>/js/jquery-3.3.1.min.js" ></script>

<script type="text/javascript">

	$(document).ready(function(){
		
		$("#btn1").click(function(){
			
			$.ajax({
				
				url:"simple1.txt",
				type:"GET",
				dataType:"text", //xml,json,html,script,text...
				success:function(data){ // data: 넘겨받은 결과물
					$("#result").empty(); // 해당요소("#result")를 비우고 새 데이터로 채울 준비를 한다.
					$("#result").text(data);
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
				
			});
			
		});
		
		$("#btn2").click(function(){
			
			$.ajax({
				
				url:"simple2.jsp",
				type:"GET",
				dataType:"text",
				success:function(data){ // data: 넘겨받은 결과물
					$("#result").empty(); // 해당요소("#result")를 비우고 새 데이터로 채울 준비를 한다.
					$("#result").text(data);
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}				
				
			});
			
		});
		
	});

</script>

</head>
<body>

	<button type="button" id="btn1">simple1.txt</button> &nbsp;&nbsp;
	<button type="button" id="btn2">simple2.jsp</button> &nbsp;&nbsp;
	
	<%-- 위 버튼을 누르면 해당하는 파일이 아래  id=result에 들어간다. --%>
		
	<p>
	여기는 simpleAjax.jsp 페이지 입니다.
	<p>
	<div id="result"></div>

</body>
</html>
