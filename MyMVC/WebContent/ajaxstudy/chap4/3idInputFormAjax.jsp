<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% String ctxPath = request.getContextPath(); %>  
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<script type="text/javascript" src="<%=ctxPath %>/js/jquery-3.3.1.min.js"></script>

<script type="text/javascript">

	$(document).ready(function(){
		
		$("#error").empty();
		$("#good").empty();
		
		$("#btn").click(function(){
			
			if($("#userid").val().trim() == "") {
				alert("ID를 입력하세요!!");
				return;
			}
			
			var form_data = {userid:$("#userid").val()};
			$.ajax({
				url:"3idDuplicateCheck.do",
				type:"POST",
				data:form_data,
			    dataType:"JSON",
			    success:function(json){
			    	if(json.n == 0) {
			    		$("#error").empty();
			    		$("#good").empty().html("ID로 사용가능");
			    	}
			    	else if(json.n == 1) {
			    		$("#good").empty();
			    		$("#error").empty().html("이미사용중");
			    	}
			    },
			    error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});
			
		});// end of $("#btn").click()-----------
		
	});
</script>

</head>
<body>
	<form>
		<input type="text" id="userid" />
		<button type="button" id="btn">ID중복검사</button>
		<span id="error" style="color: red; font-weight: bold;"></span>
		<span id="good" style="color: blue; font-weight: bold;"></span>
	</form>
</body>
</html>




