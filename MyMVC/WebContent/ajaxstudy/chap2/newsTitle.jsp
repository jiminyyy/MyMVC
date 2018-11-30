<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<div style="width: 40%; min-height: 200px; margin: 10px auto; padding: 10px; border: solid orange 1px; overflow: auto;">
	<c:if test="${newsTitleList != null && not empty newsTitleList}">
	<c:forEach var="todaynewsVO" items="${newsTitleList}" varStatus="status"> 
		<div style="float: left; width:60%; min-height: 30px; margin-left: 0px; margin-right: 10px; ">
			<c:if test="${status.count == 1}">
				<span style="color: red; font-weight: bold; font-size: 14pt;">${todaynewsVO.title}</span>
			</c:if>
			<c:if test="${status.count > 1}">
				<span style="color: blue; font-size: 12pt;">${todaynewsVO.title}</span>
			</c:if>
	    </div>
	    <div style="float: left; min-height: 30px; margin-left: 10px; margin-right: 0px; ">
			[기사입력 : <span style="color: blue; font-size: 10pt; ">${todaynewsVO.registerday}</span>]<br/>
	    </div>
	</c:forEach>
	</c:if>
</div>
    
    
    
    
    