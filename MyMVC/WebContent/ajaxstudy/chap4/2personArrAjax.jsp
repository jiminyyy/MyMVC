<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
  <% String ctxPath = request.getContextPath(); %>      

<script type="text/javascript" src="<%=ctxPath %>/js/jquery-3.3.1.min.js"></script> 
<script type="text/javascript">
	
	$(document).ready(function(){
		
		$("#displayArea").hide();
		
		$("#btn").click(function(){
			
			$.ajax({
				url:"2personArrJSON.do",	
				//type:"GET",
				//data:form_data,
				dataType:"json",
				success:function(json){
					
					$("#displayArea").show();
					
					var html = "";
					
					if(json.length > 0) {
						$.each(json, function(entryIndex, entry){
							
						   html += "<tr>" +
								   "<td>"+(entryIndex+1)+"</td>" +
								   "<td>"+entry.name+"</td>" +
								   "<td>"+entry.age+"</td>" +
								   "<td>"+entry.phone+"</td>" +
								   "<td>"+entry.email+"</td>" +
								   "<td>"+entry.addr+"</td>" +
								   "</tr>"; 
						});	
					}
					
					$("#personinfo").html(html); 
						  
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
				
			});
			
		});
		
	});
	
</script>

<div>
	<button type="button" id="btn">회원보기</button>
</div>
	<h3>회원내역</h3>
	<div id="displayArea">
	    <table>
			<thead>
				<tr>
					<th>번호</th>
					<th>성명</th>
					<th>나이</th>
					<th>전화번호</th>
					<th>이메일</th>
					<th>주소</th>
				</tr>
			</thead>
		<tbody id="personinfo"></tbody>
		</table>	
	</div>
