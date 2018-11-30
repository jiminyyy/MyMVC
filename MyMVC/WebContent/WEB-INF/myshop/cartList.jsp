<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<jsp:include page="../header.jsp" />

<style type="text/css" >
      table#tblCartList {width: 90%;
      					 border-top: solid black 1px;
                         border-bottom: solid black 1px;
                         margin-top: 20px;
                         margin-left: 10px;
                         margin-bottom: 20px;}
                         
      table#tblCartList th {border-top: solid black 1px;
                         	border-bottom: solid black 1px;}
      table#tblCartList td {border-top: solid black 1px;
                       		border-bottom: solid gblackray 1px;} 

      .delcss {background-color: cyan;
               font-weight: bold;
               color: red;}
        	   
      .ordershoppingcss {background-color: cyan;
        	         font-weight: bold;
        	         color: blue;}  
        	         
	   .del:hover {font-weight: bold; text-decoration:underline; color: red;}        	         	   
</style>

<script type="text/javascript">

	$(document).ready(function(){

	   $(".spinner").spinner( {
		   
		   spin: function(event, ui) {
			   
			   if(ui.value > 100) {
				   $(this).spinner("value", 100);
				   return false;
			   }
			   else if(ui.value < 0) {
				   $(this).spinner("value", 0);
				   return false;
			   }
		   }
		   
	   } );// end of $("#spinner").spinner({});----------------


	}); // end of $(document).ready()--------------------------

	
	function goOqtyEdit(cartnoID, oqtyID) {
	
		var cartno = $("#"+cartnoID).val();
		var oqty = $("#"+oqtyID).val();
		
		// alert("장바구니 번호 : " + cartno + ", 주문량 : " + oqty)
		
		var regExp = /^[0-9]+$/g;
		var bool = regExp.test(oqty);
		
		if(!bool) {
			alert ("주문량은 0개 이상이어야 합니다!");
			location.href="cartList.do";
			return;
		}
		
		var frm = document.updateOqtyFrm;
		frm.cartno.value = cartno;
		frm.oqty.value = oqty;
		frm.method = "POST";
		frm.action = "cartEdit.do";
		frm.submit();
				
	}// end of goOqtyEdit(oqtyID, cartnoID)---------------------
	
	// 특정 제품 삭제
	function goDel(cartno) {
		
		var frm = document.deleteFrm;
		frm.cartno.value = cartno;
		frm.method = "POST";
		frm.action = "cartDel.do";
		frm.submit();
		
	}// end of goDel(cartno)--------------
	
	
	// 체크박스 모두선택 및 모두해제 되기 위한 함수
	function allCheckBox() {
		
		var bool = $("#allCheckOrNone").is(':checked'); // 선택자가 체크되어있는지
		$(".ckboxpnum").prop('checked', bool); // true면 선택자도 체크
			
	}// end of allCheckBox()------------
	
	// **** 주문하기 *****
	function goOrder() { // 체크박스 모두 노체크 시 주문 못해
		
		var flag = false;
	
		$(".ckboxpnum").each(function(){
				
			if($(this).is(':checked')) {
				flag = true;
				return false; // break;
			}
		}); // $(".ckboxpnum").each(function(){		
	
		if(!flag) {
			alert("주문 할 상품을 선택해 주세요!");
			return;
		}
		else { // 체크된 상품이 있을 경우 
			
			var YN = confirm("선택한 제품을 주문하시겠습니까?");
			
			if(YN == false){ // 취소선택 시
				return;
			}
			else{ // 컨펌 확인 선택 시 (((((((((((((((((((( 주문창으로 넘어간다 ))))))))))))))))))))
				
				// 주문총액이 보유코인액을 초과할 경우 주문불가
				var sumtotalprice = 0;
				var sumtotalpoint = 0;
				$(".ckboxpnum").each(function(){
				
					if($(this).is(':checked')) {
						var totalprice = $(this).parent().parent().find("#totalprice").text().trim();
						var totalpoint = $(this).parent().parent().find("#totalpoint").text().trim();
						
						var strPriceArr = totalprice.split(',');
						var strPointArr = totalpoint.split(',');
						
						/* for(var i=0; i<strArr.length; i++){
								alert(strArr[i]);	
							} 									*/
							
						var strPrice = strPriceArr.join('');
						var strPoint = strPointArr.join('');
						// alert(str); 콤마를 제거한 숫자만 남는다.
						
						sumtotalprice += parseInt(strPrice);
						sumtotalpoint += parseInt(strPoint);
					}
				}); // end each

				// 주문총액이 보유코인액을 초과할 경우 주문불가
				var coin = parseInt(${(sessionScope.loginuser).coin});
 
				var frm = document.orderFrm;
				
				// 2. 주문하려는 제품의 주문량이 제품의 재고량보다 크면 주문을 할 수 없도록 해야 한다.
			    var index = 0;
				var flag = false;
				var pname = "";
			    $(".ckboxpnum").each(function(){
				   
			    	if($(this).is(':checked')) {
					   var oqty = parseInt($("#oqty"+index).val());
					   var pqty = parseInt($("#pqty"+index).val());
					   pname = $("#pname"+index).text();
					   
					   if(oqty > pqty) {
						   flag = true;
						   return;
					   } 
				   }
				   index++;
			    });
			    
				if(flag){
					alert(pname + "재고량보다 주문량이 많습니다!!");
					return;
				}
				if(sumtotalprice > coin) {
					alert("코인이 부족합니다! 코인충전창으로 이동합니다!");
					var idx = ${(sessionScope.loginuser).idx};
					var url = "coinPurchaseTypeChoice.do?idx="+idx;
					
					window.open(url, "coinPurchaseTypeChoice",
								"left=350px, top=100px, width=650px, height=570px");
					return;
				}
				
				// 3. form의 값 중 체크된 행 외에는 비활성 시키기
				index = 0;
				$(".ckboxpnum").each(function(){
				   
			    	if(!$(this).is(':checked')) { // 체크가 안되어 있는 상품
			    		$(this).parent().parent().find(":input").attr("disabled", true);
			    	}	
				   index++;
			    });
			    
				
				frm.sumtotalprice.value = sumtotalprice;
				frm.sumtotalpoint.value = sumtotalpoint;
				
				frm.method = "POST";
				frm.action = "orderAdd.do";
				frm.submit();
			
				
			}	
		}
	}// end of goOrder()-----------------------------

</script>

<h2>::: ${(sessionScope.loginuser).name} 님[ ${(sessionScope.loginuser).userid} ] 장바구니 목록 :::</h2>	

  <%-- 장바구니에 담긴 제품목록을 보여주고서 
       실제 주문을 하도록 form 생성한다. --%>
       
 <form name="orderFrm">
	<table id="tblCartList" >
	 <thead>
	   <tr>
		 <th style="border-right-style: none;">
		     <input type="checkbox" id="allCheckOrNone" onClick="allCheckBox();" />
		     <span style="font-size: 10pt;"><label for="allCheckOrNone">전체선택</label></span>
		 </th>
		 <th colspan="5" style="border-left-style: none; font-size: 12pt; text-align: center;">
		     주문하실 제품을 선택하신후 주문하기를 클릭하세요
		 </th>
	   </tr>
	   
	   <tr style="background-color: #dfefff;">
		  <th style="width:10%; text-align: center; height: 30px;">제품번호</th>
		  <th style="width:23%; text-align: center;">제품명</th>
	   	  <th style="width:17%; text-align: center;">수량</th>
	   	  <th style="width:20%; text-align: center;">판매가/포인트(개당)</th>
	   	  <th style="width:20%; text-align: center;">총액</th>
	   	  <th style="width:10%; text-align: center;">삭제</th>
	   </tr>	
	 </thead>
	 
	 <tbody>
	   <c:if test="${cartList == null || empty cartList}">
	   <tr>
	   	  <td colspan="6" align="center">
	   	    <span style="color: red; font-weight: bold;">
	   	    	장바구니에 담긴 상품이 없습니다.
	   	    </span>
	   	  </td>	
	   </tr>
	   </c:if>	
	   
	   <c:if test="${cartList != null && not empty cartList}">
	   <%-- 장바구니 전체 총금액(누적용) 변수 선언 및 초기치 선언 --%>
	   	  <c:set var="cartTotalPrice" value="0" />
	   	  <c:set var="cartTotalPoint" value="0" />
	   	  <c:forEach var="cvo" items="${cartList}" varStatus="status" >
	   	  	<c:set var="cartTotalPrice" value="${cartTotalPrice + cvo.item.totalPrice}" />
	   	  	<c:set var="cartTotalPoint" value="${cartTotalPoint + cvo.item.totalPoint}" />
	   	  	<tr>
	   	  		<td align="center"> <%-- 체크박스 및 제품번호 --%>
	   	  			<input type="checkbox" name="pnum" class="ckboxpnum" id="pnum${status.index}" value="${cvo.pnum}"/>&nbsp;<label for="pnum${status.index}">${cvo.pnum}</label>
	   	  		</td>
	   	  		<td style="text-align: center;"> <%-- 제품이미지 및 제품명 --%>
	   	  			<img width="130px;" height="100px;" src="images/${cvo.item.pimage1}" /><br/>
	   	  			${cvo.item.pname}
	   	  		</td>
	   	  		<td align="center"> <%-- 주문량 --%>
			    	 <%--			  <<<!! 알아두기 !!>>>
			    	  			 	  c:forEach 에서 선택자 id를 고유하게 사용하는 방법
			    	  			 	  status.index를 이용하자 !!!					 --%>
	   	  			<input class="spinner" id="oqty${status.index}" name="oqty" value="${cvo.oqty}" style="width: 40px; height: 18px; font-size: 12pt;" /> 개
	   	  			<input type="hidden" id="pqty${status.index}"  value="${cvo.item.pqty}"/>
	   	  			<%-- 장바구니번호 --%>
	   	  			<input type="hidden" id="cartno${status.index}" name="cartno" value="${cvo.cartno}" />
	   	  			
	   	  			<button type="button" onClick="goOqtyEdit('cartno${status.index}', 'oqty${status.index}');">수정</button>
	   	  		</td>
	   	  		<td align="center"> <%-- 판매가 포인트 --%>
	   	  		<span style="color: #bb0000;"><fmt:formatNumber value="${cvo.item.saleprice}" pattern="#,###,###" />원</span>
	   	  		<input type="hidden" name="saleprice" id="saleprice${status.index}" value="${cvo.item.saleprice}" /><br/>
	   	  		<span style="color: #b9b9ff;"><fmt:formatNumber value="${cvo.item.point}" pattern="#,###,###" /> point </span>
	   	  		</td>
	   	  		<td align="center"> <%-- 총 금액 및 총 포인트 --%>
	   	  		<span id="totalprice" style="font-weight:bold; font-size:15pt; color: #bb0000;">
	   	  		<fmt:formatNumber value="${cvo.item.totalPrice}" pattern="###,###" /></span>
	   	  		<span style="font-weight:bold; font-size:15pt; color: #bb0000;">원
	   	  		</span><br/>
	   	  		<span id="totalpoint" style="font-weight:bold; font-size:15pt; color: #b9b9ff;">
	   	  		<fmt:formatNumber value="${cvo.item.totalPoint}" pattern="###,###" /></span>
	   	  		<span style="font-weight:bold; font-size:15pt; color: #b9b9ff;"> point</span>
	   	  		</td> 
	   	  		<td align="center"> <%-- 삭제 --%>
	   	  			<span class="del" style="cursor: pointer;" onClick="goDel('${cvo.cartno}');">삭제</span>
	   	  		</td>
	   	  	</tr>
	   	  </c:forEach>
	   
	   </c:if>	
	   
	   <tr>
	   	  <td colspan="5" align="right">
	   	  	<span style="font-weight: bold;">장바구니 총액 : </span><span style="color: red;"><fmt:formatNumber value="${cartTotalPrice}" pattern="#,###,###" />원</span><br/>
	   	  	<input type="hidden" name="sumtotalprice" />
	   	  	<span style="font-weight: bold;">총 포인트 : </span><span style="color: red;"><fmt:formatNumber value="${cartTotalPoint}" pattern="#,###,###" /> point</span>
	   	  	<input type="hidden" name="sumtotalpoint" />
	   	  </td>
	   	  <td colspan="1" align="center">
	   	     <span class="ordershopping" style="cursor: pointer;" onClick="goOrder();">[주문하기]</span><br/>
	   	     <span class="ordershopping" style="cursor: pointer;" onClick="javascript:location.href='<%= request.getContextPath() %>/mallHome.do'">[계속쇼핑]</span>
	   	  </td>
	   </tr>
	
	 </tbody>
	</table> 
 
 </form>          

 

 <%-- 장바구니에 담긴 제품수량을 수정하는 form --%>
 <form name="updateOqtyFrm">
 	<input type="hidden" name="cartno" />
	<input type="hidden" name="oqty" />
 </form>
 
 <%-- 장바구니에 담긴 제품을 삭제하는 form --%>
 <form name="deleteFrm">
	<input type="hidden" name="cartno" />
 </form>
  
    
<jsp:include page="../footer.jsp" />
    