<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../header.jsp" />

<style type="text/css">
  .th, .td {border: 0px solid gray;}
  a:hover {text-decoration: none;}
</style>
    
<script type="text/javascript">

	$(document).ready(function(){
		
		$("#totalHITCount").hide();
		$("#countHIT").hide();
		
		// HIT상품 게시물을 더보기 위하여 "더보기▼" 버튼 클릭액션에 대한 초기값 호출
		displayHitAppend("1");
		// HIT상품 게시물을 더보기 위하여 "더보기▼" 버튼 클릭액션 이벤트 등록
		$("#btnMoreHIT").click(function(){
			
			if($(this).text() == "TOP▲") {
				$("#displayResult").empty();
				displayHitAppend("1");
				$(this).text("더보기▼");
			}
			else {
				displayHitAppend($(this).val());
			}
			
		});
		
		/////////////////////////////////////////////////////////////////////////////
		
		$("#totalNEWCount").hide();
		$("#countNEW").hide();
		
		// HIT상품 게시물을 더보기 위하여 "더보기▼" 버튼 클릭액션에 대한 초기값 호출
		displayNewAppend("1");
		// HIT상품 게시물을 더보기 위하여 "더보기▼" 버튼 클릭액션 이벤트 등록
		$("#btnMoreNEW").click(function(){
			
			if($(this).text() == "TOP▲") {
				$("#displayResultNEW").empty();
				displayNewAppend("1");
				$(this).text("더보기▼");
			}
			else {
				displayNewAppend($(this).val());
			}
			
		});
		
		
	});

	var lenHIT = 8; // HIT 상품 "더보기" 클릭에 보여줄 상품의 수 단위크기 (8개씩 보여준다.)
	
	// display 할 HIT 제품정보 추가 요청하기(Ajax)
	function displayHitAppend(start) {
		
		var form_data = { "start" : start, // 받은 start가 "start"로 들어가고 "start"를 "malldisplayXML.do"에 넘겨준다.
						  "len" : lenHIT,
						  "pspec" : "HIT"}; 
			
		$.ajax({
			
			url:"malldisplayXML.do",
			type:"get",
			data:form_data,
			dataType:"XML",
			success:function(xml){
				var rootElement = $(xml).find(":root");
				var productArr = $(rootElement).find("product");
				var html ="";
				if(productArr.length == 0){ // 데이터가 없는 경우에도 length는 null이 아님
					html += "<tr>" +
							"<td colspan=\"4\" class=\"td\" align=\"center\">현재 상품 준비중....</td>" +
							"</tr>";
							
					// HIT상품 출력하기
					$("#displayResult").html(html);
					
					$("#btnMoreHIT").attr("disabled", true);
					$("#btnMoreHIT").css("cursor", "not-allowed");
				
				}
				else { // 데이터존재
					html += "<tr>" ;
					
					for(var i=0; i<productArr.length; i++) {
						
						var product = $(productArr).eq(i); 
						// !!!! .eq(i)는 선택한 요소들을 index번호로 찾을 수 있는 선택자이다. ex) 배열의 인덱스(index)로 값(value)을 찾는것과 동일. productArr[i]와 흡사 var product에는 객체가 들어감.
						
						html += "<td class=\"td\" align=\"center\">" +
								"<a href=\"/MyMVC/prodView.do?pnum="+ $(product).find('pnum').text() +"\">" +
								"<img width=\"120px;\" height=\"130px;\" src=\"images/"+ $(product).find('pimage1').text() +"\"></a>" +
								"  <br/> 제품명 : " + $(product).find("pname").text() +
							    "  <br/> 정가 : <span style='color: red; text-decoration: line-through;'>" + $(product).find("price").text() +"원</span>" +
							    "  <br/> 판매가 : <span style='color: red; font-weight: bold;'>" + $(product).find("saleprice").text() +"원</span>" +
							    "  <br/> 할인율 : <span style='color: blue; font-weight: bold;'>[" + $(product).find("percent").text() +"% 할인]</span>" +
							    "  <br/> 포인트 : <span style='color: orange;'>" + $(product).find("point").text() + " POINT </span>" +
							    "</td>";
								
						if( (i+1)%4 == 0 ) {
							html += "</tr>" +
									"<tr><td colspan='4' style='line-height: 50px;'>&nbsp;</td></tr>" +
									"<tr>";
						}
						
					}// end of for
					
					html += "</tr>";

					// HIT상품 출력하기
					$("#displayResult").append(html);
					
					// 더보기버튼의 value 속성에 값을 넣는다.
					$("#btnMoreHIT").val( parseInt(start) + lenHIT );
					/* 
					mallHome.do를 처음 띄울 때 parseInt(start) 값은 1이다.
					더보기버튼을 클릭할 경우 parseInt(start) + lenHIT는 9
					*/
				}
				
				// 웹브라우저상의 count 출력
				$("#countHIT").text(parseInt($("#countHIT").text()) + $(productArr).length);
				
				// 더보기 버튼을 계속해서 눌러서 countHIT와 totalHITCount가 일치하는 경우 버튼의 이름을 "TOP▲"로 변경 / countHIT는 0으로 초기화한다.
				if($("#countHIT").text() == $("#totalHITCount").text()) {
					$("#btnMoreHIT").text("TOP▲");
					$("#countHIT").text("0"); 
				}
				
			},// end of success
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
			
		});
		
	}// function displayHitAppend(pageNo) {
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	var lenNEW = 8; // HIT 상품 "더보기" 클릭에 보여줄 상품의 수 단위크기 (8개씩 보여준다.)
	
	// display 할 HIT 제품정보 추가 요청하기(Ajax)
	function displayNewAppend(start) {
		
		var form_data = { "start" : start, // 받은 start가 "start"로 들어가고 "start"를 "malldisplayXML.do"에 넘겨준다.
						  "len" : lenNEW,
						  "pspec" : "NEW"}; 
			
		$.ajax({
			
			url:"malldisplayJSON.do",
			type:"get",
			data:form_data,
			dataType:"json",
			success:function(json){
				
				var html ="";
				
				if(json.length == 0){ // 데이터가 없는 경우에도 length는 null이 아님
				
					html += "현재 상품 준비중입니다.";
							
					// HIT상품 출력하기
					$("#displayResultNEW").empty().html(html);
					
					$("#btnMoreNEW").attr("disabled", true);
					$("#btnMoreNEW").css("cursor", "not-allowed");
				}
				else { // 데이터존재
					
					$.each(json, function(entryIndex, entry){ 
					
			        	  html += "<div style=\"display: inline-block; margin: 30px; border: solid gray 0px;\" align=\"left\">" 
			        	        + "  <a href=\"/MyMVC/prodView.do?pnum="+entry.pnum+"\">"
			        	        + "    <img width=\"120px;\" height=\"130px;\" src=\"images/"+entry.pimage1+"\">"
			        	        + "  </a><br/>"
			        	        + "제품명 : "+entry.pname+"<br/>"
			        	        + "정가 : <span style=\"color: red; text-decoration: line-through;\">"+entry.price.toLocaleString()+" 원</span><br/>"
			        	        + "판매가 : <span style=\"color: red; font-weight: bold;\">"+entry.saleprice.toLocaleString()+" 원</span><br/>"
			        	        + "할인율 : <span style=\"color: blue; font-weight: bold;\">["+entry.percent+"%] 할인</span><br/>"
			        	        + "포인트 : <span style=\"color: orange;\">"+entry.point+" POINT</span><br/>"
			        	        + "</div>";
			        	        
		        	        if( (entryIndex+1)%4 == 0 ) {
								html += "<br/>";
							}	        
				        	  
			              } ); // end of $.each()---------------------------
			          
			           html += "<div style=\"clear: both;\">&nbsp;</div>";
					
					// HIT상품 출력하기
					$("#displayResultNEW").append(html);
					
					// 더보기버튼의 value 속성에 값을 넣는다.
					$("#btnMoreNEW").val( parseInt(start) + lenNEW );
					/* 
					mallHome.do를 처음 띄울 때 parseInt(start) 값은 1이다.
					더보기버튼을 클릭할 경우 parseInt(start) + lenHIT는 9
					*/
				}
				
				// 웹브라우저상의 count 출력
				$("#countNEW").text(parseInt($("#countNEW").text()) + json.length);
				
				// 더보기 버튼을 계속해서 눌러서 countHIT와 totalHITCount가 일치하는 경우 버튼의 이름을 "TOP▲"로 변경 / countHIT는 0으로 초기화한다.
				if($("#countNEW").text() == $("#totalNEWCount").text()) {
					$("#btnMoreNEW").text("TOP▲");
					$("#countNEW").text("0"); 
				}
				
			},// end of success
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
			
		});
		
	}// function displayNewAppend(start) {
	
</script>
    
<h2>::: 쇼핑몰 상품 :::</h2>    
<br/>

<!-- HIT 상품 디스플레이 하기  table 태그 사용 -->
<table style="width: 90%; border: 0px solid gray; margin-bottom: 30px;" >
<thead>
	<tr>
		<th colspan="4" class="th" style="font-size: 12pt; background-color: #dfefff; height: 30px; text-align:center;">- HIT 상품 -</th>
	</tr>
	<tr>
		<th colspan="4" class="th">&nbsp;</th>
	</tr>
</thead>
<tbody id="displayResult">



</tbody>
</table>

<div style="margin-top:20px; margin-bottom: 20px;">
	<button type="button" id="btnMoreHIT" value="">더보기▼</button>
	<span id="totalHITCount">${totalHITCount}</span>
	<span id="countHIT">0</span>
</div>


<!-- NEW 제품 디스플레이(div 태그를 사용한것) -->
<div style="width: 90%; margin-top:50px; margin-bottom: 30px;">
	<div style="text-align: center; 
	            font-size: 14pt;
	            font-weight: bold;
	            background-color: #e1e1d0;
	            height: 30px;
	            margin-bottom: 15px;" >
	   <span style="vertical-align: middle;">- NEW 상품(DIV) -</span>
	 </div>

	 
	 <div id="displayResultNEW" style="margin: auto; border: solid 0px red;"></div>
	 <div style="margin-top: 20px; margin-bottom: 20px;">
		<button type="button" id="btnMoreNEW" value="">더보기▼</button>
		<span id="totalNEWCount">${totalNEWCount}</span>
		<span id="countNEW">0</span>
	 </div>
	
</div>

<jsp:include page="../footer.jsp" />

