<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../header.jsp" />

<style type= "text/css">
.th					{text-align: center;}
.td					{text-align: center;}
.namestyle			{background-color: #ffeffc;
					 font-weight: bold;
					 font-style:italic;
					 color: black;
					 cursor: pointer;}
.clickstyle			{font-weight: bold;
					 text-decoration: underline;
					 color: red;}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		
		$("#sizePerPage").bind("change", function(){
		/* 	var val = $(this).val();
			alert("확인용"+val);		*/
			
			var frm = document.memberFrm;
			frm.method = "GET";
			frm.action = "memberList.do";
			frm.submit();
			
		});
		
		$("#sizePerPage").val("${sizePerPage}");
		
		$("#period").bind("change", function(){
			var frm = document.memberFrm;
			frm.method = "GET";
			frm.action = "memberList.do";
			frm.submit();
		});
		
		$("#period").val("${period}");
		
		$("#searchType").val("${searchType}");
		
		$("#searchWord").val("${searchWord}");
		
		$(".name").hover(function(){
								$(this).addClass("namestyle");	
							}, function(){
								$(this).removeClass("namestyle");
							});
		
		$(".click").hover(function(){
			$(this).addClass("clickstyle");	
		}, function(){
			$(this).removeClass("clickstyle");
		});
		
		
		// 검색어 입력 후 엔터를 치면
		$("#searchWord").keydown(function(event){
			if( event.keyCode == 13 ){ // 엔터를 했을 때
				goSearch();
			}
				
		}); // end of $("#searchWord").keydown(function(event){
		
	}); // end of $(document).ready(function(){  
		
		function goSearch(){
			
			var searchWord = $("#searchWord").val().trim();
			
			if(searchWord == "") {
				alert("검색어를 입력하세요!!");
				return;
			}
			else {
				var frm = document.memberFrm;
				frm.method = "GET";
				frm.action = "memberList.do";
				frm.submit();				
			}
			
		} // end of goSearch
		
		function goEdit(idx, goBackURL) { // 수정취소 후 전 화면으로 돌아가는 것 구현해야함===========================================================================
		
			var url = "memberEdit.do?idx="+idx;
			
			window.open(url, "memberEdit",
					    "left=150px, top=50px, width=800px, height=650px");
		
		}
		
		function goEnable(idx, goBackURL) {
		
		var frm = document.idxFrm;
		frm.idx.value = idx;
		frm.goBackURL.value = goBackURL;
		frm.method = "POST";
		frm.action = "memberEnable.do";
		frm.submit();
	
		}
		
		function goDel(idx, goBackURL) {
			
			var bool = confirm(idx+"번 회원을 정말 삭제하시겠습니까?");
			// console.log( "bool : " + bool );
			// 확인이면 true, 취소면 false
			
			if(bool){
				var frm = document.idxFrm;
				frm.idx.value = idx;
				frm.goBackURL.value = goBackURL;
				frm.method = "POST";
				frm.action = "memberDelete.do";
				frm.submit();
			}
		}	
		
		
		function goRecovery(idx, goBackURL) {
			
			var bool = confirm(idx+"번 회원을 정말 복원하시겠습니까?");
			// console.log( "bool : " + bool );
			// 확인이면 true, 취소면 false
			
			if(bool){
				var frm = document.idxFrm;
				frm.idx.value = idx;
				frm.goBackURL.value = goBackURL;
				frm.method = "POST";
				frm.action = "memberRecovery.do";
				frm.submit();
			}
		}	
		
		function goDetail(idx, goBackURL) {
			
			var frm = document.idxFrm;
			frm.idx.value = idx;
			frm.goBackURL.value = goBackURL;
			frm.method = "GET";
			frm.action = "memberDetail.do";
			frm.submit();
		}
			
		

</script>

<div class= "row">
	<div class= "col-md-12">
		<%-- <%= currentURL %> <br/> --%>
		<h2 style="margin-bottom: 40px; font-weight:bold; ">::: 회원전체 정보보기 :::</h2>
		
		<form name= "memberFrm">
			<select id="searchType" name= "searchType">
				<option value="name">회원명</option>
				<option value="userid">아이디</option>
				<option value="email">이메일</option>
			</select>
			<input type="text" id="searchWord" name= "searchWord" size="25" class= "box" style="margin-left: 10px; margin-right: 10px; margin-bottom: 10px;" />
			<button type="button" onClick= "goSearch();">검색</button>
			<div>
				<div style="display: inline;">
					<span style="color: darkred; font-weight: bold; font-size: 12pt;">페이지당 회원명 수 -</span> 
					<select id="sizePerPage" name= "sizePerPage">
						<option value= "10">10</option>
						<option value= "5" >5</option>
						<option value= "3">3</option>
					</select>
				</div>
				<div style="display: inline; margin-left: 20px;">  <%-- 옆으로 위치할 수 있도록 inline값을 넣어준다. 기본값은 block; (아래로 위치)--%>
					<select id= "period" name= "period">
						<%-- <option value="">날짜구간</option> --%>
						<option value="-1">전체</option>
						<option value="3">최근 3일 이내</option>
						<option value="10">최근 10일 이내</option>
						<option value="30">최근 30일 이내</option>
						<option value="60">최근 60일 이내</option>
					</select>
				</div>
			</div>
		</form>
		
		<table class= "outline">
			<thead>
				<tr>
					<th class= "th" >회원번호</th>
					<th class= "th">회원명</th>
					<th class= "th">아이디</th>
					<th class= "th">이메일</th>
					<th class= "th">휴대폰</th>
					<th class= "th">가입일자</th>
					<c:if test="${sessionScope.loginuser.userid == 'admin'}">
						<th class= "th">수정&nbsp;|&nbsp;삭제</th>
					</c:if>
				</tr>
			</thead>
				
			<tbody>
				<c:if test="${not empty memberList}"> <%-- 회원이 있으면 --%>	
					<c:if test="${sessionScope.loginuser.userid == 'admin'}"> <%-- 관리자 로그인 시 --%>	
						<c:forEach var="membervo" items="${memberList}">
							<c:if test="${membervo.status == 0 }"> <%-- 탈퇴회원 --%>	
								<tr style="background-color: pink;">
							</c:if>
							<c:if test="${membervo.status == 1}"> <%-- 정상회원 --%>	
								<tr>
							</c:if>
							<c:if test="${membervo.dormant}"> <%-- 관리자 로그인 시 휴면상태 --%>	
								<tr style="background-color: yellow;">
							</c:if>
								<td class= "td">${membervo.idx}</td>
								<td class= "td name" onClick="goDetail('${membervo.idx}','${currentURL}');">${membervo.name}</td>
								<td class= "td">${membervo.userid}</td>
								<td class= "td">${membervo.email}</td>
								<td class= "td">${membervo.allHp}</td>
								<td class= "td">${membervo.registerday}</td>
								<c:if test="${membervo.status == 1 && membervo.dormant}">
									<td class= "td click"><a href= "javascript: goEnable('${membervo.idx}', '${currentURL}');">휴면해제</a></td>
								</c:if>
								<c:if test="${membervo.status == 1 && !membervo.dormant}">
									<td class= "td click"><span class="del" style="cursor: pointer" onClick="goDel('${membervo.idx}','${currentURL}');">삭제</span></td>
									<%-- <td class= "td"><a href= "javascript: goEdit(${membervo.idx});">수정</a></td> --%>
								</c:if>
								<c:if test="${membervo.status == 0}">
									<td class= "td click"><a href= "javascript: goRecovery('${membervo.idx}', '${currentURL}');">복원</a></td>
								</c:if>
							</tr>
						</c:forEach>							
					</c:if>
					<c:if test="${sessionScope.loginuser.userid != 'admin'}"> <%-- 일반회원 로그인 시 --%>	
						<c:forEach var="membervo" items="${actMemberList}">
							<c:if test="${membervo.status == 1}"> <%-- 정상회원 --%>	
								<tr>
							</c:if>
								<td class= "td">${membervo.idx}</td>
								<td class= "td name" onClick="goDetail('${membervo.idx}','${currentURL}');">${membervo.name}</td>
								<td class= "td">${membervo.userid}</td>
								<td class= "td">${membervo.email}</td>
								<td class= "td">${membervo.allHp}</td>
								<td class= "td">${membervo.registerday}</td>
							</tr>
						</c:forEach>
					</c:if>
				</c:if>
				<c:if test="${empty memberList}">
				<tr>
						<th colspan= "7" style= "text-align: center; color: red;">가입된 회원이 없습니다.</th>
					</tr>
				</c:if>

			</tbody>
		
			<thead>
				<tr>
					<th colspan="4" class ="th">
						${pageBar}
					</th>
						
					<th colspan="3" class ="th">
					현재 [<span style="color: #B43846;">${currentShowPageNo}</span>]페이지 / 총[${totalPage}]페이지입니다.&nbsp;
					<c:if test="${sessionScope.loginuser.userid == 'admin'}">
					회원수 : 총 [${totalMemberCount}]명.
					</c:if>
					<c:if test="${sessionScope.loginuser.userid != 'admin'}">
					회원수 : 총 [${actMemberCount}]명.
					</c:if>
					</th>
				</tr>
			</thead>
	
		</table>
	
		<%-- *** 특정회원 조회 및 삭제하기 위한 폼 생성하기 *** --%>
		<form name="idxFrm">
			<input type="hidden" name="idx" />
			<input type="hidden" name="goBackURL" />
		</form>
	</div>	
</div>

<jsp:include page="../footer.jsp" />