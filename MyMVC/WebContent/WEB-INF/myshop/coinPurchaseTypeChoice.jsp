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
	
	<style type="text/css">
	
		span {margin-right: 10px;}
							 
		.stylePoint {color: red; 
		             font-weight: bold;
		             font-size: 13pt;}
		             
		.purchase {cursor: pointer;
		           color: red;
		           font-weight: bold;}             
		
	</style>

	<script type="text/javascript">
	<%--
		jQuery 를 이용하여 radio 버튼의 체크 유무 및 체크된 값을 검사합니다.
		jQuery 의 selector 부분이 중요 포인트로 4개의 구문으로 나눠 볼 수 있습니다.
		 
		1. :input
		    - Selects all input, textarea, select and button elements.
		2. [name=animal]
		    - Selects elements that have the specified attribute with a value exactly equal to a certain value.
		3. :radio
		    - Selects all elements of type radio.
		4. :checked
		    - Matches all elements that are checked.
	--%>
		var coinmoney = 0;
		
		$(document).ready(function(){
			
			$(":input[name=coinmoney]:radio").bind("change", function(event){
				var $target = $(event.target);
				var isChecked = $target.is(":checked");
			 
			 // var isChecked = $(this).is(":checked");
				
				if(isChecked) { 
					coinmoney = parseInt($target.val());
					
					var num = parseInt($target.val())/100000;
				 // var num = parseInt($(this).val())/100000;
				    $("td span").removeClass("stylePoint");
					$target.parent().parent().parent().find(".point"+num).addClass("stylePoint");
				
					$("#error").hide();
				} 
			}); // end of $(":input[name=coinmoney]:radio").bind("change")------------------
			
			
			$("#purchase").hover(function(){
								$(this).addClass("purchase");
			                 }, function(){
			                	$(this).removeClass("purchase");
			}); 
			
			$("#error").hide();
			
			$("#purchase").click(function(event){
				
				var flag = false;
				
				$(":input[name=coinmoney]:radio").each(function(){
					var isChecked = $(this).is(":checked");
					if(isChecked) {
						flag = true;
						return false;
					}
				});
				
				if(!flag) { // 결제금액을 선택하지 않았을 경우
					$("#error").show();
					event.preventDefault();
				}
				else { // 결제금액을 선택했을 경우
					
					/* === 팝업창에서 부모창 함수 호출 방법 3가지 ===
					    1-1. 일반적인 방법
						opener.location.href = "javascript:부모창스크립트 함수명();";
						opener.location.href = "http://www.aaa.com";
						
						1-2. 일반적인 방법
						window.opener.부모창스크립트 함수명();

						2. jQuery를 이용한 방법
						$(opener.location).attr("href", "javascript:부모창스크립트 함수명();");
					*/
				//	opener.location.href = "javascript:goCoinPurchaseEnd('${idx}', "+coinmoney+" );";
				//	window.opener.goCoinPurchaseEnd('${idx}', coinmoney);
				    $(opener.location).attr("href", "javascript:goCoinPurchaseEnd('${idx}', "+coinmoney+" );");
					
				    self.close();
				}
				
			});// end of $("#purchase").click()-------------------------
			
		});// end of $(document).ready()----------------------------------
		
	</script>
</head>	

<body>
	<div class="container">
	  <h2>코인충천 결제방식 선택</h2>
	  <p>코인충전 금액 높을수록 POINT를 무료로 많이 드립니다.</p> 
	  
	  <div class="table-responsive" style="margin-top: 30px;">           
		  <table class="table">
		    <thead>
		      <tr>
		        <th>결제종류</th>
		        <th>금액</th>
		        <th>POINT</th>
		      </tr>
		    </thead>
		    <tbody>
		      <tr>
		        <td>신용카드</td>
		        <td>
			        <label class="radio-inline"><input type="radio" name="coinmoney" id="coinmoney1" value="300000" />300,000원</label>
					<label class="radio-inline"><input type="radio" name="coinmoney" class="coinmoney" value="200000" />200,000원</label>
					<label class="radio-inline"><input type="radio" name="coinmoney" class="coinmoney" value="100000" />100,000원</label>
				</td>
		        <td>
					<span class="point3">3000</span><span class="point2">2000</span><span class="point1">1000</span>
				</td>
		      </tr>
		      <tr>
		        <td>휴대폰결제</td>
		        <td>
		        	<label class="radio-inline"><input type="radio" name="coinmoney" class="coinmoney" value="300000" />300,000원</label>
					<label class="radio-inline"><input type="radio" name="coinmoney" class="coinmoney" value="200000" />200,000원</label>
					<label class="radio-inline"><input type="radio" name="coinmoney" class="coinmoney" value="100000" />100,000원</label>
		        </td>
		        <td>
		        	<span class="point3">3000</span><span class="point2">2000</span><span class="point1">1000</span>
				</td>
		      </tr>
		      <tr>
		        <td>카카오페이</td>
		        <td>
		        	<label class="radio-inline"><input type="radio" name="coinmoney" class="coinmoney" value="300000" />300,000원</label>
					<label class="radio-inline"><input type="radio" name="coinmoney" class="coinmoney" value="200000" />200,000원</label>
					<label class="radio-inline"><input type="radio" name="coinmoney" class="coinmoney" value="100000" />100,000원</label>
		        </td>
		        <td>
		        	<span class="point3">3000</span><span class="point2">2000</span><span class="point1">1000</span>
				</td>
		      </tr>
		      <tr>
		      	<td id="error" colspan="3" align="center" style="height: 50px; vertical-align: middle; color: red;">결제종류에 따른 금액을 선택하세요!!</td>
		      </tr>
		      <tr>
		        <td id="purchase" colspan="3" align="center" style="height: 100px; vertical-align: middle;">[충전결제하기]</td>
		      </tr>
		    </tbody>
		  </table>
	  </div>
	  
	</div>
</body>
</html>
