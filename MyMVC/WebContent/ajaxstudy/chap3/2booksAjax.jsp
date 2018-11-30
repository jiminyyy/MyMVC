<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<% String ctxPath = request.getContextPath(); %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>

<style type="text/css">
	h3 {color: red;}
	ul {list-style: square;}
</style>

<script type="text/javascript" src="<%=ctxPath %>/js/jquery-3.3.1.min.js"></script> 

<script type="text/javascript">
	
	$(document).ready(function(){

		$("#btn").click(function(){
			
			$.ajax({
				url:"2booksXML.do",	
				//type:"GET",
				//data:form_data,
				dataType:"xml",
				success:function(xml){
					$("#fictioninfo").empty();		// ul 엘리먼트 속의 모든것을 비워 새 테이터를 채울 준비를 한다.
					$("#programinginfo").empty();
					
					var rootElement = $(xml).find(":root");	//xml를 감싸는 가장 처음에 위치한(최상위) 태그 -> <books>
					
					//alert( "확인용 : " + $(rootElement).prop("tagName") );
					
					var bookArr = $(rootElement).find("book");		// book 이라는 특정한 엘리먼트를 찾는다. 검색된 book 이라는 엘리먼트는 복수이므로 저장될 bookArr 변수는 배열타입이다.
					
					bookArr.each(function(){
						var html = "<li>도서명 : "+ $(this).find("title").text() +", 작가명 : "+ $(this).find("author").text() +"</li>";
						var subject = $(this).find("subject").text();
						if(subject == "소설") {
							$("#fictioninfo").append(html);
						}
						else {
							$("#programinginfo").append(html);
						}
					}); // 반복문
						  
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

	<div align="center">
		<button type="button" id="btn">도서출력</button>
	</div>
	<div id="fiction">
		<h3>소설</h3>
		<ul id="fictioninfo"></ul>
	</div>
	<div id="it">
		<h3>프로그래밍</h3>
		<ul id="programinginfo"></ul>
	</div>

</body>
</html>