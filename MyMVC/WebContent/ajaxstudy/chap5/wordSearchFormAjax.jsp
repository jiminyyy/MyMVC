<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>문자열 검색시 글자동완성하기</title>

<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<style type="text/css">
	#displayList a {text-decoration: none;}
</style>

<script type="text/javascript">
	
	$(document).ready(function(){
		
		goSearch("", "", "1");      // 초기치 설정
		// goSearch(code, word, pageNo)
		// /wordSearchForm.do입력 시 
				
		var lgCategorycodeName = "";
		lgCategorycodeName += '<option value="">대분류</option>';
		
		<c:forEach var="map" items="${lgcategorycodeList}">
		   lgCategorycodeName += '<option value="${map.LGCATEGORYCODE}">${map.CODENAME}</option>';
	    </c:forEach>
		
	    $("#sel1").empty().append(lgCategorycodeName);
	    
	    
        var mdCategorycodeName = "";
        mdCategorycodeName += '<option value="">중분류</option>';
        /* 
        <c:forEach var="map" items="${mdcategorycodeList}">
        mdCategorycodeName += '<option value="${map.MDCATEGORYCODE}">${map.CODENAME}</option>';
	    </c:forEach>
		*/		
	    $("#sel2").empty().append(mdCategorycodeName);
	    
		
	    $("#sel1").bind("change", function(){
	    	
	    	var form_data = {lgcategorycode:$(this).val() }; // 키값:밸류값
	    	$.ajax({
				//	url:"http://localhost:9090/MyMVC/mdCategorycodeJSON.do" 또는
					url: "mdCategorycodeJSON.do",
					type: "GET",
					data: form_data,  // 위의 URL 페이지로 사용자가 보내는 ajax 요청 데이터.
					dataType: "JSON", // ajax 요청에 의해 URL 페이지 서버로 부터 리턴받는 데이터 타입. xml, json, html, script, text 이 있음.
					success: function(data){
											
						if(data.length > 0) { // 검색된 데이터가 있는 경우임. 만약에 조회된 데이터가 없을 경우 if(data == null) 이 아니고 if(data.length == 0) 이라고 써야 한다. 
							                  // 왜냐하면  넘겨준 값이 new JSONArray() 이므로 null 이 아니기 때문이다..
							
							var resultHTML = "";
						
							$.each(data, function(entryIndex, entry){
								resultHTML += "<option value='"+entry.MDCATEGORYCODE+"'>"+ entry.CODENAME +"</option>"; // json.키값
							});
							
							$("#sel2").empty().append(resultHTML); //대분류에 따른 중분류카테고리명이 모두 뜬다.
						}
						else { // 검색된 데이터가 없는 경우
							
						}

					},// end of success: function()------
					error: function(request, status, error){
						alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
					}
				});// end of $.ajax()--------------------------------
				
	    });// end of $("#sel1").bind("change", function(){})--------------------------
	    
	    
	    $("#btnSearch").click(function() { // 조회버튼 클릭시
	    	$("#searchword").val("");
	    	goSearch($("#sel2").val(), "", "1");
	    });
	    
	    
	    // ========= **** 글쓰기 **** ========= //
	    /*
	    	[jQuery] Ajax Form 데이터 전송하기

			ajax를 이용하여 데이터를 주고 받다 보면 Form 태그 하위에 있는 값들을 한꺼번에 전송해야 할 때가 있다.
			Form 태그내의 항목이 많지 않다면 일일이 각 항목의 value 값을 읽어와 넘길수 있겠지만, 
			Form 태그내의 모든 항목을 전송한다고 하면 굳이 그럴 필요는 없다.

			jQuery에서는 serialize() 라는 메소드를 제공해주는데, 
			serialize() 메소드를 사용하면 Form 태그내의 항목들을 자동으로 읽어와 queryString 형식으로 변환시켜 준다.

			즉, id=xxx&name=홍길동&age=26 이런식으로 변환하여 주는데, 
			이 값을 ajax 호출시 data 속성에 넣어 ajax 통신을 하면 된다.
	    */
	    $("#btnWrite").click(function(){
	    	document.testFrm.mdcategorycode.value = $("#sel2").val();
	    	document.testFrm.title.value = $("#title").val();
	    	document.testFrm.content.value = $("#content").val();
	    	
	    	var queryString = $("form[name=testFrm]").serialize();
	    	
	    	//console.log(queryString);
	    	
	    	$.ajax({
	    		url:"addWord.do",
	    		type:"POST",
	    		data:queryString,
	    		dataType:"JSON",
	    		success:function(json){
	    		    // console.log(json.n);
	    			 if(json.n == 1) {
	    				 $("#title").val("");
	    				 $("#content").val("");
	    				 goSearch("", "", "1");      // 초기치 설정
	    			 }
	    		},
	    		error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
	    	});
	    	
	    });// end of  $("#btnWrite").click()-----------------------------
	    
	    
	    // ========= **** 글자동완성  시작 **** ========= //
		$("#displayList").hide();
	    
	    $("#searchword").keyup(function(){
			// 사용자가 텍스트박스 안에서 키보드를 눌렀다가 up 했을때 이벤트 발생함.
			
			var form_data = {searchword:$("#searchword").val() }; // 키값:밸류값
			
			$.ajax({
			//	url:"http://localhost:9090/MyMVC/wordSearchJSON.do" 또는
				url: "wordSearchJSON.do",
				type: "GET",
				data: form_data,  // 위의 URL 페이지로 사용자가 보내는 ajax 요청 데이터.
				dataType: "JSON", // ajax 요청에 의해 URL 페이지 서버로 부터 리턴받는 데이터 타입. xml, json, html, script, text 이 있음.
				success: function(data){
										
					if(data.length > 0) { // 검색된 데이터가 있는 경우임. 만약에 조회된 데이터가 없을 경우 if(data == null) 이 아니고 if(data.length == 0) 이라고 써야 한다. 
						                  // 왜냐하면  넘겨준 값이 new JSONArray() 이므로 null 이 아니기 때문이다..
						  
						var resultHTML = "";
					
						$.each(data, function(entryIndex, entry){
							var wordstr = entry.searchwordresult.trim();
							var index = wordstr.toLowerCase().indexOf( $("#searchword").val().toLowerCase() );
							//  alert("index : " + index);
							var len = $("#searchword").val().length;
							var result = "";
							
							result = "<span class='first' style='color:blue;'>" +wordstr.substr(0, index)+ "</span>" + "<span class='second' style='color:red; font-weight:bold;'>" +wordstr.substr(index, len)+ "</span>" + "<span class='third' style='color:blue;'>" +wordstr.substr(index+len, wordstr.length - (index+len) )+ "</span>";  
							
							resultHTML += "<span style='cursor:pointer;'>"+ result +"</span><br/>"; 
						});
						
						$("#displayList").html(resultHTML);
						$("#displayList").show();
					}
					else { // 검색된 데이터가 없는 경우
						$("#displayList").hide();
					}

				},// end of success: function()------
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});// end of $.ajax()-------------------
			
		});// end of keyup(function(){})-------------
		
		
		$("#displayList").click(function(event){
			var word = "";
			var $target = $(event.target);
			
			if($target.is(".first")) {
				word = $target.text() + $target.next().text() + $target.next().next().text();
			}
			else if($target.is(".second")) {
				word = $target.prev().text() + $target.text() + $target.next().text();
			}
			else if($target.is(".third")) {
				word = $target.prev().prev().text() + $target.prev().text() + $target.text();
			}
			
			$("#searchword").val(word); // 텍스트박스에 검색된 결과의 문자열을 입력해준다.
			
			$(this).hide();
			
			goSearch("", word, "1");
			makePageBar("", word, "1");
		});
		// ========= **** 글자동완성  끝 **** ========= //
		
	});// end of $(document).ready()----------------------------------------
	
	
	function goSearch(code, word, pageNo) {
	// 조회버튼 클릭 시 ($("#sel2").val(), "", "1"); 코드는 중분류코드번호, word(검색어없이 선택한 중분류에 해당하는 모든 결과물을 보여준다.), pageNo는 1(검색된결과물들의 1페이지를 보여준다.)
		
		var form_data = {mdcode:code,                // 키:밸류	자바에서 req.getparameter("키값");
				         searchword:word,            // 키:밸류
				         currentShowPageNo:pageNo};  // 키:밸류
		
		$.ajax({
				url: "wordSearchResultJSON.do",
				type: "GET",
				data: form_data,  // 위의 URL 페이지로 사용자가 보내는 ajax 요청 데이터.
				dataType: "JSON", // ajax 요청에 의해 URL 페이지 서버로 부터 리턴받는 데이터 타입. xml, json, html, script, text 이 있음.
				success: function(data){
										
					if(data.length > 0) { // 검색된 데이터가 있는 경우임. 만약에 조회된 데이터가 없을 경우 if(data == null) 이 아니고 if(data.length == 0) 이라고 써야 한다. 
						                  // 왜냐하면  넘겨준 값이 new JSONArray() 이므로 null 이 아니기 때문이다..
					     var resultHTML = "";
					
						 $.each(data, function(entryIndex, entry){
							
							   resultHTML += "<tr>" +
				      		                   "<td>"+(entryIndex+1)+"</td>" +
				      		                   "<td>"+entry.seq+"</td>" +
				      		                   "<td>"+entry.lgcodename+"</td>" +
				      		                   "<td>"+entry.mdcodename+"</td>" +
				      		                   "<td>"+entry.title+"</td>" +
				      		                   "<td>"+entry.content+"</td>" +
				      	                     "</tr>";
				      	        
						 });// end of $.each(data, function(entryIndex, entry){ })-------------------
						 	
						 $("#result").empty().html(resultHTML);
						 
						 makePageBar(code, word, pageNo);
					}
					else { // 검색된 데이터가 없는 경우
						 $("#result").empty();
					}

				},// end of success: function()------
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});// end of $.ajax()------------------------
		
	}// end of goSearch(pageNo)-----------------------------
	
	
	function makePageBar(code, word, currentShowPageNo) {
		
		var form_data = {mdcode:code,       // 키:밸류
				         searchword:word,   // 키:밸류
				         sizePerPage:"5"};  // 키:밸류
		
		$.ajax({
				url: "wordSearchPageBarJSON.do",
				type: "GET",
				data: form_data,  // 위의 URL 페이지로 사용자가 보내는 ajax 요청 데이터.
				dataType: "JSON", // ajax 요청에 의해 URL 페이지 서버로 부터 리턴받는 데이터 타입. xml, json, html, script, text 이 있음.
				success: function(data){
										
					if(data.totalPage != 0) { 
						
					     var totalPage = data.totalPage;
					     var pageBarHTML = "";
						 
					     /////////////////////////
					     
					     var blockSize = 3;
					     // blockSize 은 1개 블럭(토막)당 보여지는 페이지번호의 갯수이다.
					     /*
					         1 2 3   -- 1개블럭
					         4 5 6   -- 1개블럭
					         7 8 9   -- 1개블럭
					     */
	      
					     var loop = 1;
					      /* 
					         loop 는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 갯수(지금은 3개)까지만 
					                증가하는 용도이다.
					      */
	                     // 자바스크립트에서는 1/3 이 0 이 아니라 0.33333 이므로 소수점을 버리기 위해 Math.floor(0.33333) 을 하면 소수점을 버린 정수만 나온다.
	                     var pageNo = Math.floor((currentShowPageNo - 1)/blockSize) * blockSize + 1; 
					     //**** !!! 공식이다. !!! ****//
					      
					     /*
					         1 2 3   -- 첫번째 블럭의 페이지번호 시작값(pageNo)은 1 이다. 
					         4 5 6   -- 두번째 블럭의 페이지번호 시작값(pageNo)은 4 이다.
					         7 8 9   -- 세번째 블럭의 페이지번호 시작값(pageNo)은 4 이다.
					         
					          currentShowPageNo    pageNo
					         -----------------------------
					                1                1  == ((1 - 1)/3) * 3 + 1
					                2                1  == ((2 - 1)/3) * 3 + 1  
					                3                1  == ((3 - 1)/3) * 3 + 1 
					               
					                4                4  == ((4 - 1)/3) * 3 + 1 
					                5                4  == ((5 - 1)/3) * 3 + 1 
					                6                4  == ((6 - 1)/3) * 3 + 1
					                
					                7                7  == ((7 - 1)/3) * 3 + 1 
					                8                7  == ((8 - 1)/3) * 3 + 1 
					                9                7  == ((9 - 1)/3) * 3 + 1    
					      */
    	                					     
						 // *** [이전] 만들기 *** //
					      if(pageNo != 1) {
					    	  pageBarHTML += "<a href='javascript:goSearch(\""+code+"\" , \""+word+"\" , \""+(pageNo-1)+"\")'>[이전]</a>";
					      }
					     
	                     /////////////////////////////////////////////////
					      while( !(loop > blockSize || pageNo > totalPage) ) {
					       	 
					    	  if(pageNo == currentShowPageNo) {
					    		  pageBarHTML += "&nbsp;<span style=\"color: red; font-size: 12pt; font-weight: bold; text-decoration: underline; \">"+pageNo+"</span>&nbsp;";
					    	  }
					    	  else {
					    	  	  pageBarHTML += "&nbsp;<a href='javascript:goSearch(\""+code+"\" , \""+word+"\" , \""+pageNo+"\")'>"+pageNo+"</a>&nbsp;";
					     	  }
	                     
					       	 loop++;
					    	 pageNo++;
					      } // end of while-----------------------------------
                         /////////////////////////////////////////////////

					  	  // *** [다음] 만들기 *** //
					     if( !(pageNo > totalPage) ) {
					    	 pageBarHTML += "&nbsp;<a href='javascript:goSearch(\""+code+"\" , \""+word+"\" , \""+pageNo+"\")'>[다음]</a>";
					     }
						 	
					     $("#pageBar").empty().html(pageBarHTML);
					     
					     pageBarHTML = "";
					}
					
					else { // 검색된 데이터가 없는 경우
						 $("#pageBar").empty();
					}

				},// end of success: function()------
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});// end of $.ajax()-------------------
		
	}// end of makePageBar(startPageNo)--------------------------
	
</script>

</head>
<body>
   <div class="container">
	   
	   <div class="row" style="margin-top: 5%;">
	   		<form class="form-inline">
  			  
  			  <div class="form-group col-md-4" style="border: 0px solid gray;" align="center">
			    <label for="sel1">대분류</label>
			      <select class="form-control" id="sel1" style="width: 180px; margin-left: 20px;">
			        
			      </select>
			  </div>
			  
			  <div class="form-group col-md-4" style="border: 0px solid gray;" align="center">
			    <label for="sel2">중분류</label>
			      <select class="form-control" id="sel2" style="width: 180px; margin-left: 20px;">
			        
			      </select>
			  </div>
  
  			  <div class="form-group col-md-4" style="border: 0px solid gray;" align="center">
  			  	 <button type="button" id="btnSearch" class="btn btn-info" style="margin-right: 20px;">조회</button>
  			  	 <button type="button" id="btnWrite" class="btn btn-success">글쓰기</button>
              </div>
              
			</form>
		</div>	   

	    <div class="row">	
			<div class="col-md-6 col-md-offset-3" style="margin-top: 8%; border: 0px solid blue;">
			    <div class="col-md-3" style="border: 0px solid gray;">
			       <div>
			       		<span style="font-size: 14pt; font-weight: bold;">글제목</span>&nbsp;
			       </div>
			       <div style="margin-top: 10px;">
			       		<span style="font-size: 14pt; font-weight: bold;">글내용</span>&nbsp;
			       </div>
			       
			       <div style="margin-top: 120px;">
			       		<span style="color: red; font-size: 14pt; font-weight: bold;">단어검색</span>&nbsp;
			       </div>
			    </div>
			    
			    <div class="col-md-9" style="border: 0px solid gray;">
                      <div>
			       		<input type="text" class="form-control" id="title" name="title" style="width: 100%;" /> 
			          </div>
			          <div style="margin-top: 10px;">
			       		<textarea class="form-control" id="content" name="content" rows="5"></textarea>
			          </div>
			          
                      <input type="text" class="form-control" id="searchword" name="searchword" style="width: 100%; margin-top: 20px;" /> 
                      <div id="displayList" style="max-height: 60px; overflow: auto; margin-top: 0.5%; border: 1px solid gray;"> 
			          </div>
                </div>
			</div>
	    </div>
	   	
	    <div class="row" style="margin-top: 5%; margin-bottom: 10%;">
	   		<div class="col-md-12">
		   		<table class='table table-bordered'>
	               <thead>
		               <tr>
		                  <th width="5%">순서</th>
		                  <th width="10%">글번호</th>
		                  <th width="10%">대분류명</th>
		                  <th width="10%">중분류명</th>
		                  <th width="25%">글제목</th>
		                  <th width="40%">글내용</th>
		               </tr>
	               </thead>
	               <tbody id="result"></tbody>   
		   		</table>
		   		<div id="pageBar"></div>
		   		<div id="clearBtn" align="center">
		   			<button type='button' onClick="javascript:location.href='wordSearchForm.do'">초기화</button> 
		   		</div>
	   		</div>
	    </div>
   
   </div>

   <form name="testFrm">
   	   <input type="hidden" name="mdcategorycode">
   	   <input type="hidden" name="title">
   	   <input type="hidden" name="content">
   </form>

</body>
</html>

   






