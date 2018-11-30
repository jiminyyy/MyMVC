<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="../header.jsp" />

<script type="text/javascript">
	$(document).ready(function(){
		
		$("#sizePerPage").bind("change", function(){
		/* 	var val = $(this).val();
			alert("확인용"+val);		*/
			
			var frm = document.sizePerPageFrm;
			frm.method = "GET";
			frm.action = "myMemoList.do";
			frm.submit();
			
		});
		
		$("#sizePerPage").val("${sizePerPage}");
		
		$("#allCheck").click(function(){
		
			$(".delCheck").prop('checked', $(this).is(':checked'));
			// $(this)는 allcheck를 말한다.
			// delcheck는 체크된다, allcheck가 체크일 경우
			
		});
		
	}); // end of $(document).ready
	
	function msgBlind() {
		
		var flag = false;
		
		$(".delCheck").each(function(){
			if($(this).is(':checked')) {
				flag = true;
				return false;
			}
		});
		
		if(flag == false) {
			alert("비공개할 글번호를 선택하세요!!");
			return;
		}
		
		else {
			
			var bool = confirm("선택한 메모를 정말로 비공개로 전환하시겠습니까?");
			
			if(bool) {
				var frm = document.delCheckFrm;
				frm.status.value = "0";
				frm.method = "POST";
				frm.action = "memoBlindOpen.do";
				frm.submit();
			}
			else {
				alert("메모 비공개 전환을 취소하셨습니다~");
				$(".delCheck").prop('checked', false);
			}
		}
	}// end of function msgBlind
	
	
	function msgDel() {
		
		var flag = false;
		
		$(".delCheck").each(function(){
			if($(this).is(':checked')) {
				flag = true;
				return false;
			}
		});
		
		if(flag == false) {
			alert("삭제할 글번호를 선택하세요!!");
			return;
		}
		
		else {
			
			var bool = confirm("선택한 메모를 정말로 삭제하시겠습니까?");
			
			if(bool) {
				var frm = document.delCheckFrm;
				frm.method = "POST";
				frm.action = "memoDelete.do";
				frm.submit();
			}
			else {
				alert("메모 삭제를 취소하셨습니다~");
				$(".delCheck").prop('checked', false);
			}
		}
		
	}
	
	function msgOpen() {
		
		var flag = false;
		
		$(".delCheck").each(function(){
			if($(this).is(':checked')) {
				flag = true;
				return false;
			}
		});
		
		if(flag == false) {
			alert("공개로 전환할 글번호를 선택하세요!!");
			return;
		}
		
		else {
			
			var bool = confirm("선택한 메모를 정말로 공개로 전환하시겠습니까?");
			
			if(bool) {
				var frm = document.delCheckFrm;
				frm.status.value = "1";
				frm.method = "POST";
				frm.action = "memoBlindOpen.do";
				frm.submit();
			}
			else {
				alert("메모 공개 전환을 취소하셨습니다~");
				$(".delCheck").prop('checked', false);
			}
		}
	}// end of function msgBlind
		
</script>

<div class="row">
   	<div class="col-md-12">
   	   <h2>::: 나의 한줄 메모장 목록 :::</h2>
   	</div>
</div>
   
<div class="row" style="margin-top: 20px; margin-bottom: 20px;">
	<div class="col-md-4" style="border: 0px solid gray;">
	     <button type="button" onClick="msgDel();">메모삭제</button>&nbsp;
	     <button type="button" onClick="msgBlind();">메모비공개</button>&nbsp;
	     <button type="button" onClick="msgOpen();">메모공개</button>
	</div>
	<div class="col-md-4" style="border: 0px solid gray;">	   
	   <form name="sizePerPageFrm">
	      <span style="color: red; font-weight: bold;">페이지당 글갯수-</span>
	   	  <select name="sizePerPage" id="sizePerPage">
	   	  	 <option value="10">10</option>
	   	  	 <option value="5">5</option>
	   	  	 <option value="3">3</option>
	   	  </select>
	   </form>
   </div>
</div>
   
<div class="row">
   <div class="col-md-12" style="border: 0px solid gray;">
   <table style="width: 95%;" class="outline">
	<thead>
		<tr>
			<th width="8%" style="text-align: center;">
				<span style="color: red; font-size: 10pt;"><label for="allCheck">전체선택</label></span>
				<input type="checkbox" id="allCheck" />
			</th>
			<th width="10%" style="text-align: center;">글번호</th>
			<th width="10%" style="text-align: center;">작성자</th>
			<th width="42%" style="text-align: center;">글내용</th>
			<th width="20%" style="text-align: center;">작성일자</th>
			<th width="10%" style="text-align: center;">ID주소</th>
		</tr>
	</thead>
	<tbody>

		<c:if test="${memoList == null}">
			<tr>
				<td colspan="6">데이터가 없습니다.</td>			
			</tr>
		</c:if>

		<c:if test="${memoList != null}">
			<form name="delCheckFrm">
				<input type="hidden" name="status" />
				<c:forEach var="vo" items="${memoList}">
					<c:if test="${vo.status == 1}" >
						<tr>
							<td style="text-align: center;">
								<input type="checkbox" name="delCheck" class="delCheck" value="${vo.idx}" />
							</td>
							<td style="text-align: center;">${vo.idx}</td>
							<td style="text-align: center;">${vo.name}</td>
							<td style="text-align: center;">${fn:replace(vo.msg,"<", "&lt;")}</td>
							<td style="text-align: center;">${vo.writedate}</td>
							<td style="text-align: center;">${vo.cip}</td>
							<!-- map.키값(dao)을 하면 값을 얻어올 수 있다. -->
						</tr>
					</c:if>
					<c:if test="${vo.status == 0}">
						<tr style="background-color: #ffeef7; font-style: italic;">
							<td style="text-align: center;">
								<input type="checkbox" name="delCheck" class="delCheck" value="${vo.idx}" />
							</td>
							<td style="text-align: center;">${vo.idx}</td>
							<td style="text-align: center;">${vo.name}</td>
							<td style="text-align: center;">${fn:replace(vo.msg,"<", "&lt;")}</td>
							<td style="text-align: center;">${vo.writedate}</td>
							<td style="text-align: center;">${vo.cip}</td>
							<!-- map.키값(dao)을 하면 값을 얻어올 수 있다. -->
						</tr>	
					</c:if>	
				</c:forEach>
			</form>
		</c:if>
	</tbody>
	<thead>
		<tr>
			<th colspan="4" class ="th" style="text-align: center;">
				${pageBar}
			</th>
			<th colspan="2" class ="th" style="text-align: right;">
					<span style="font-size: 16px;">현재 [<span style="color: #B43846;">${currentShowPageNo}</span>
					]페이지 / 총[${totalPage}]페이지입니다.&nbsp;메모수 : 총 [${totalMemoCount}]개</span>
			</th>
		</tr>
	</thead>
	</table>
	</div>
</div>

<jsp:include page="../footer.jsp" />