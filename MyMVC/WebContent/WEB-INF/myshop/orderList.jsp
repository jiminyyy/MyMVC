<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<jsp:include page="../header.jsp" />

<style type="text/css" >
      table#tblOrderList {width: 90%;
      					 border-top: solid black 1px;
                         border-bottom: solid black 1px;
                         margin-top: 20px;
                         margin-left: 10px;
                         margin-bottom: 20px;}
                         
      table#tblOrderList th {border-top: solid black 1px;
                         	border-bottom: solid black 1px;}
      table#tblOrderList td {border-top: solid black 1px;
                       		border-bottom: solid gblackray 1px;}
        	   
      .ordershoppingcss {background-color: cyan;
        	         font-weight: bold;
        	         color: blue;}  
        	         
	   .ordershopping:hover {font-weight: bold; text-decoration:underline; color: red;}        	         	   
</style>

<script type="text/javascript">

	$(document).ready(function(){
		
		$("#btnDeliverStart").click(function(){ 
			
			var flag = false;
			
			// 1. 일단 모두 비활성화(중복체크했을 경우를 대비하여)
			$(".odrcode").prop("disabled",true);
			
			// 선택자 : 체크박스 배송시작
			$(".deliverStartPnum").each(function(){
				var bool = $(this).is(':checked');
				
				if(bool == true) { // 체크박스에 체크가 되어있으면
					$(this).next().next().prop("disabled", false);
					// 해당 태그의 바로 다음 태그 (label)의 다음 태그 (input)를 활성화 시킨다.
					flag = true;
				}
			}); // 배송시작 체크박스 체크 유무 검사 (end of each)
			
			if(flag == false) {
				alert("하나 이상의 배송시작을 체크해주세요!");
				return;
			}
			else {
				var frm = document.frmDeliver;
				frm.method = "POST";
				frm.action = "deliverStart.do";
				frm.submit();
			}
		}); // end of $("#btnDeliverStart").click(function(){
		
		$("#btnDeliverEnd").click(function(){ 
			
			var flag = false;
			
			// 1. 일단 모두 비활성화(중복체크했을 경우를 대비하여)
			$(".odrcode").prop("disabled",true);
			
			// 선택자 : 체크박스 배송시작
			$(".deliverEndPnum").each(function(){
				var bool = $(this).is(':checked');
				
				if(bool == true) { // 체크박스에 체크가 되어있으면
					$(this).next().next().prop("disabled", false);
					// 해당 태그의 바로 다음 태그 (label)의 다음 태그 (input)를 활성화 시킨다.
					flag = true;
				}
			}); // 배송시작 체크박스 체크 유무 검사 (end of each)
			
			if(flag == false) {
				alert("하나 이상의 배송완료를 체크해주세요!");
				return;
			}
			else {
				var frm = document.frmDeliver;
				frm.method = "POST";
				frm.action = "deliverEnd.do";
				frm.submit();
			}
		}); // end of $("#btnDeliverEnd").click(function(){
		

	}); // end of $(document).ready()--------------------------
	
	function allCheckBoxStart() {
		
		var bool = $("#allCheckStart").is(':checked'); // 선택자가 체크되어있는지
		$(".deliverStartPnum").prop('checked', bool); // true면 선택자도 체크
			
	}// end of allCheckBoxStart()------------
	
	function allCheckBoxEnd() {
		
		var bool = $("#allCheckEnd").is(':checked'); // 선택자가 체크되어있는지
		$(".deliverEndPnum").prop('checked', bool); // true면 선택자도 체크
			
	}// end of allCheckBoxEnd()------------

	function openMember(odrcode){
		
		var url = "memberInfo.do?odrcode="+odrcode;
		
		// 팝업창 띄우기
		window.open(url, "memberInfo",
					"width=550px, height=600px, top=50px, left=100px");
		
	}// end of openMember()------------
	
</script>
<c:set var = "userid" value="${sessionScope.loginuser.userid}" />
<c:if test="${userid eq 'admin'}" >
	<h2>::: 전체 주문내역 :::</h2>	
</c:if>

<c:if test="${userid ne 'admin'}" >
	<h2>::: ${(sessionScope.loginuser).name} 님[ ${userid} ]의 주문내역목록 :::</h2>	
</c:if>

 <form name="frmDeliver">
<table id="tblOrderList" style="width: 95%;">

	<tr>
		<th colspan="2" style="text-align: left; font-size: 14pt; font-weight: bold; border-right-style: none;"> 주문내역 보기 </th>
		<th colspan="5" style="text-align: right; border-left-style: none;">
			<span style="color: red; font-weight: bold;">페이지당 갯수-</span>
			<select id="sizePerPage" style="width:60px;">
				<option value="10">10</option>
				<option value="5">5</option>
				<option value="3">3</option>
			</select>
	    </th>
	</tr>

  <c:if test='${userid eq "admin" }'>
	<tr>	
		<td colspan="7" align="right" > 
			<input type="checkbox" id="allCheckStart" onClick="allCheckBoxStart();"><label for="allCheckStart"><span style="color: green; font-weight: bold; font-size: 9pt;">전체선택(배송시작)</span></label>&nbsp;
			<input type="button" name="btnDeliverStart" id="btnDeliverStart" value="배송시작" style="width:80px;" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			
			<input type="checkbox" id="allCheckEnd" onClick="allCheckBoxEnd();"><label for="allCheckEnd"><span style="color: red; font-weight: bold; font-size: 9pt;">전체선택(배송완료)</span></label>&nbsp;
			<input type="button" name="btnDeliverEnd" id="btnDeliverEnd" value="배송완료" style="width:80px;" >
		</td>
	</tr>
  </c:if>
		
    <tr bgcolor="#cfcfcf">
		<td align="center" width="15%">주문코드(전표)</td>
		<td align="center" width="15%">주문일자</td>
		<td align="center" width="30%">제품정보</td> <%-- 제품번호,제품명,제품이미지1,판매정가,판매세일가,포인트 --%>
		<td align="center" width="10%">수량</td>
		<td align="center" width="10%">금액</td>   
		<td align="center" width="10%">포인트</td>
		<td align="center" width="10%">배송상태</td>
    </tr>
	<c:if test="${orderList==null || empty orderList}" > 
	  <tr>
		  <td colspan="7" align="center">
		  <span style="color: red; font-weight: bold;">주문내역이 없습니다.</span>
	  </tr>
	</c:if>

	<%-- ============================================ --%>
	<c:if test="${orderList != null && not empty orderList }">
		<c:forEach var="odrmap" items="${orderList}" varStatus="status">
			<%--
				 varStatus 는 반복문의 상태정보를 알려주는 애트리뷰트이다.
				 status.index : 0 부터 시작한다.
				 status.count : 반복문 횟수를 알려주는 것이다.
			 --%>
			<tr>
				<td align="center"> <%-- 주문코드(전표) 출력하기. 
				      만약에 관리자로 들어와서 주문내역을 볼 경우 해당 주문코드(전표)를 클릭하면 
				      주문코드(전표)를 소유한 회원정보를 조회하도록 한다.  --%>
				<c:if test='${userid eq "admin" }'>
					<a href="#" onClick="openMember('${odrmap.odrcode}')">${odrmap.odrcode}</a>
					<%-- 페이지이동 없이 함수실행 --%>
				</c:if>
				<c:if test='${userid ne "admin" }'>
					${odrmap.odrcode}
				</c:if>	 
				</td>
				<td align="center"> <%-- 주문일자 --%>
					${odrmap.odrdate}
				</td>
				<td align="center">  <%-- === 제품정보 넣기 === --%>
					<img src="images/${odrmap.pimage1} " width="130" height="100" />  <%-- 제품이미지1 --%>
					<br/>제품번호 : ${odrmap.pnum}  <%-- 제품번호 --%>
					<br/>제품명 : ${odrmap.pname}      <%-- 제품명 --%>
					<br/>판매정가 : <span style="text-decoration: line-through;"><fmt:formatNumber value="${odrmap.price}" pattern="###,###" /> 원</span>   <%-- 제품개당 판매정가 --%>
					<br/><span style="color: red; font-weight: bold;">판매가 : <fmt:formatNumber value="${odrmap.saleprice}" pattern="###,###" /> 원</span>  <%-- 제품개당 판매세일가 --%> 
					<br/><span style="color: red; font-weight: bold;">포인트 : <fmt:formatNumber value="${odrmap.point}" pattern="###,###" /> 포인트</span>   <%-- 제품개당 포인트 --%>
				</td>
				<td align="center">    <%-- 수량 --%>
					 ${odrmap.oqty} 개
					 <input type="hidden" class="oqty" name="oqty" value="${odrmap.oqty}"  />
				</td>
				<td align="center">    <%-- 금액 --%>
				     <c:set var="su" value="${odrmap.oqty}" />
				     <c:set var="danga" value="${odrmap.saleprice}" />
				     <c:set var="totalmoney" value="${su * danga}" />
				     
					 <fmt:formatNumber value="${totalmoney}" pattern="###,###" /> 원
				</td>
				<td align="center">    <%-- 포인트 --%>
				     <c:set var="point" value="${odrmap.point}" />
				     <c:set var="totalpoint" value="${su * point}" />
					 <fmt:formatNumber value="${totalpoint}" pattern="###,###" /> 포인트
				</td>
				<td align="center"> <%-- 배송상태 --%>
				
					<c:choose>
						<c:when test="${odrmap.deliverstatus == '주문완료'}">
							주문완료<br/>
						</c:when>
						<c:when test="${odrmap.deliverstatus == '배송시작'}">
							<span style="color: green; font-weight: bold; font-size: 12pt;">배송시작</span><br/>
						</c:when>
						<c:when test="${odrmap.deliverstatus == '배송완료'}">
							<span style="color: red; font-weight: bold; font-size: 12pt;">배송완료</span><br/>
						</c:when>
					</c:choose>
	
					<c:if test='${userid eq "admin" }'>
						<br/><br/>
						<c:if test="${odrmap.deliverstatus == '주문완료'}">
							<input type="checkbox" class="deliverStartPnum" name="deliverStartPnum" id="chkDeliverStart${status.index}" 
							value="${odrmap.pnum}"><label for="chkDeliverStart${status.index}">배송시작</label> 
							<input type="text" class="odrcode" name="odrcode" value="${odrmap.odrcode}"  />
						</c:if>
						<br/>
						<c:if test="${odrmap.deliverstatus == '주문완료' or odrmap.deliverstatus == '배송시작'}">
							<input type="checkbox" class="deliverEndPnum" name="deliverEndPnum" id="chkDeliverEnd${status.index}" 
							value="${odrmap.pnum}"><label for="chkDeliverEnd${status.index}">배송완료</label>
							<input type="text" class="odrcode" name="odrcode" value="${odrmap.odrcode}"  />
						</c:if>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</c:if>
		<%-- ============================================================================ --%>
		
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
    