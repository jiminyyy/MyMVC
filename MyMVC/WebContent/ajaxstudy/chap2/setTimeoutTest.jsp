<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%  String ctxPath = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>일정시간마다 함수호출을 해주는 setTimeout() 함수 예제(jQuery 사용) - 시계</title>
<script type="text/javascript" src="<%=ctxPath%>/js/jquery-3.3.1.min.js" ></script>
<script type="text/javascript">

	$(document).ready(function(){
		
		loopshowNowTime();
		
	});
	
	function showNowTime(){
		
		var now = new Date(); // 현재시각 가져오기
		var strNow = now.getFullYear() + "년 " + (now.getMonth()+1) + "월 " + now.getDate() + "일 ";
		// getFullYear-연도만 가져오기 getMonth-월만 가져오기(1월이 0이다.) getDate-일자만 가져오기
		
		var hour = "";
		if(now.getHours() < 12) {
			hour = "오전 "+now.getHours();	
		}
		else {
			if(now.getHours() >12)
				hour = "오후 "+(now.getHours()-12);
			else
				hour = "오후 "+now.getHours();
		}
		
		var minute = "";
		if(now.getMinutes()<10) {
			minute = "0"+now.getMinutes();
		}
		else {
			minute = now.getMinutes();
		}
		
		var second = "";
		if(now.getSeconds()<10) {
			second = "0"+now.getSeconds();
		}
		else {
			second = now.getSeconds();
		}
		
		strNow += hour + ":" + minute + ":" + second;
		
		// console.log(strNow);
		
		$("#clock").html(strNow);
		
	}//end of showNowTime

	function loopshowNowTime(){
		
		showNowTime();
		
		setTimeout(function(){
					loopshowNowTime();
					},1000);
		
		// 1000 밀리초(==1초) 후에 실행하라는 뜻
		/* 
			setTimeout(function(){
						하고자 하는 작업
						},delay);
			-> 하고자 하는 작업을 delay 밀리초가 지난 다음에 하라는 말
		*/
	}
	
</script>

</head>
<body>

	<span style="color: blue; font-size: 16pt;" >현재시각</span>
	<span id="clock" style="color: red; font-size:16pt; font-weight:bold;" ></span>

</body>
</html>