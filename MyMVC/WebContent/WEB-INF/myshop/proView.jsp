<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../header.jsp" />

<style>

	.line			{border: 0px solid gray;
					 border-collapse: collapse;
					 margin-top: 30px;
					 margin-bottom: 20px;}
	li				{margin-bottom: 2%;}				 

</style>

<script type="text/javascript">
   $(document).ready(function() {
	   
	   $("#spinner").spinner( {
		   
		   spin: function(event, ui) {
			   var pqty = ${pvo.pqty};
			   
			   if(ui.value > pqty) {
				   $(this).spinner("value", 0);
				   return false;
			   }
			   else if(ui.value < 0) {
				   $(this).spinner("value", pqty);
				   return false;
			   }
		   }
		   
	   } );// end of $("#spinner").spinner({});----------------
	   
	   
   });// end of $(document).ready();------------------------------
   
   function goCart(pnum) { // pnum은 제품번호
	   
		// 주문량 유효성 검사
		var frm = document.cartOrderFrm;
   		
   		var regExp = /^[0-9]+$/;
   		var oqty = frm.oqty.value;
   		var bool = regExp.test(oqty);
		
   		if(!bool) { // 숫자 이외의 값이 들어온 경우
   			alert("주문량은 1개 이상 가능합니다!!");
   			frm.oqty.value = "1";
   			frm.oqty.focus();
   			return;
   		}
   		else { // 숫자가 들어온 경우
   			oqty = parseInt(oqty);
   			if(oqty < 1) {
   	   			alert("주문량은 1개 이상 가능합니다!!");
   	   			frm.oqty.value = "1";
   	   			frm.oqty.focus();
   	   			return;
   			}
   			else { // 올바르게 입력한 경우
   				frm.method = "POST";
   				frm.action = "cartAdd.do";
   				frm.submit();
   			}
   		}
	   
   } // end of function goCart(pnum)
   
   function goOrder(pnum) { // pnum은 제품번호
	   
	// 주문량 유효성 검사
		var frm = document.cartOrderFrm;
  		
  		var regExp = /^[0-9]+$/;
  		var oqty = frm.oqty.value;
  		var bool = regExp.test(oqty);
		
  		if(!bool) { // 숫자 이외의 값이 들어온 경우
  			alert("주문량은 1개 이상 가능합니다!!");
  			frm.oqty.value = "1";
  			frm.oqty.focus();
  			return;
  		}
  		else { // 숫자가 들어온 경우
  			oqty = parseInt(oqty);
  			if(oqty < 1) {
  	   			alert("주문량은 1개 이상 가능합니다!!");
  	   			frm.oqty.value = "1";
  	   			frm.oqty.focus();
  	   			return;
  			}
  			else { // 올바르게 입력한 경우
  				var sumtotalprice = oqty*parseInt("${pvo.saleprice}");
  				var sumtotalpoint = oqty*parseInt("${pvo.point}");
  				
  				frm.sumtotalprice.value = sumtotalprice;
  				frm.sumtotalpoint.value = sumtotalpoint;
  				frm.method = "POST";
  				frm.action = "orderAdd.do";
  				frm.submit();
  			}
  		}
	   
   } // end of function goOrder(pnum) { // pnum은 제품번호
   
</script>

<div style="width:95%;">
	<div class="row">
		<div class="col-md-12 line">
			<span style="font-size: 15pt; font-weight: bold;">::: 제품 상세 정보 :::</span>
		</div>
	</div>
	<div class="row">
		<div class="col-md-3 line" style="width: 40%; margin-left: 10%;">
			<img src="images/${pvo.pimage1}" style="width: 300px; height: 260px;" />
		</div>
		<div class="col-md-9 line" style="width: 50%; text-align: left;">
			<ul style="list-style-type: none;">
				<li>${pvo.pspec}</li>
				<li>제품번호 : ${pvo.pnum}</li>
				<li>제품이름 : ${pvo.pname}</li>
				<li style="text-decoration: line-through;">제품정가 : <fmt:formatNumber value="${pvo.price}" pattern="#,###,###원" /></li>
				<li>제품판매가 : <fmt:formatNumber value="${pvo.saleprice}" pattern="#,###,###원" /></li>
				<li>할인율 : ${pvo.percent} %할인</li>
				<li>포인트 : ${pvo.point} POINT</li>
				<li>재고량 : ${pvo.pqty} 개</li>
			</ul>
			<%-- *** 장바구니 담기 및 바로 주문하기 폼 *** --%>
			<form name="cartOrderFrm">
				<ul style="list-style-type: none; margin-top: 50px;">
					<li>
						<label for="spinner">주문량 : </label>
		  				<input id="spinner" name="oqty" value="1" style="width: 30px; height: 20px;" />
					</li>
					<li style="margin-bottom: 40px;">
						<button type="button" class="btn btn-info" onClick="goCart('${pvo.pnum}');" style="margin-right: 10px;">장바구니 담기</button>
						<button type="button" class="btn btn-warning" onClick="goOrder('${pvo.pnum}');">바로 주문</button>
					</li>
				</ul>
				<input type="hidden" name="pnum" value="${pvo.pnum}" />
				<input type="hidden" name="saleprice" value="${pvo.saleprice}" />
				<input type="hidden" name="sumtotalprice" />
				<input type="hidden" name="sumtotalpoint" />
				<input type="hidden" name="goBackURL" value="${goBackURL}" />
			</form>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12 line" >
			<img src="images/${pvo.pimage2}" style="width: 550px; height: 350px;" /><br/>
		</div>
	</div>	
	<div class="row">		
			<c:if test="${imgList != null}">
			<c:forEach var="map" items="${imgList}">
			<div class="col-md-12 line" >
				<img src="images/${map.imgfilename}" style="width: 550px; height: 350px;" /><br/>
			</div>
			</c:forEach>
			</c:if>
	 </div>
</div>


<jsp:include page="../footer.jsp" />