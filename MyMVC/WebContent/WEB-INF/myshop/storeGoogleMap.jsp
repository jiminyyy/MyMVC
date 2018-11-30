<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	String ctxPath = request.getContextPath();
%>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script type="text/javascript" src="<%=ctxPath %>/js/jquery-1.12.4.min.js"></script> 
<%-- jquery 1.x 또는 jquery 2.x 를 사용해야만 한다. 구글맵은 jquery 3.x 을 사용할 수 없다.   --%>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<%-- 구글맵 api 사용하기  --%>
<script src="http://maps.googleapis.com/maps/api/js?key=AIzaSyDDQx9Q_JsWUjWyssoeEaeBGSbhvGcTyrA"></script>

<style>
	#div_name {
		width: 70%;
		height: 15%;
		margin-bottom: 5%;
		margin-left: 10%;
		position: relative;
	}
	
	#div_mobile {
		width: 70%;
		height: 15%;
		margin-bottom: 5%;
		margin-left: 10%;
		position: relative;
	}
	
	#div_findResult {
		width: 70%;
		height: 15%;
		margin-bottom: 5%;
		margin-left: 10%;		
		position: relative;
	}
	
	#div_btnFind {
		width: 70%;
		height: 15%;
		margin-bottom: 5%;
		margin-left: 10%;
		position: relative;
	}
	
</style>

<script type="text/javascript">
	
	$(document).ready(function(){
		google.maps.event.addDomListener(window, 'load', initialize);
	}); // end of $(document).ready()-------------------------
	
	
	function initialize() {
		// 구글 맵 옵션 설정
		var mapOptions = { 
	        zoom : 16, // 기본 확대율
	        center : new google.maps.LatLng(37.566011, 126.982621), // 지도 중앙 위치
	        disableDefaultUI : false,  // 기본 UI 비활성화 여부
	        scrollwheel : true,        // 마우스 휠로 확대, 축소 사용 여부
	        zoomControl : true,        // 지도의 확대/축소 수준을 변경하는 데 사용되는 "+"와 "-" 버튼을 표시
	        mapTypeControl : true,     // 지도 유형 컨트롤은 드롭다운이나 가로 버튼 막대 스타일로 제공되며, 사용자가 지도 유형(ROADMAP, SATELLITE, HYBRID 또는 TERRAIN)을 선택할 수 있다. 이 컨트롤은 기본적으로 지도의 왼쪽 위 모서리에 나타난다.
	        streetViewControl : true,  // 스트리트 뷰 컨트롤에는 지도로 드래그해서 스트리트 뷰를 활성화할 수 있는 펙맨 아이콘이 있다. 기본적으로 이 컨트롤은 지도의 오른쪽 아래 근처에 나타난다.
	        scaleControl: true,        // 배율 컨트롤은 지도 배율 요소를 표시한다. 이 컨트롤은 기본적으로 비활성화되어 있다.
	    };
	    // 구글맵 옵션내역 사이트 아래 참조 
	    // https://developers.google.com/maps/documentation/javascript/reference#MapOptions
	 
	    var targetmap = new google.maps.Map(document.getElementById('googleMap'), mapOptions);  
		// 구글 맵을 사용할 타겟
		// !!! 주의 !!!  document.getElementById('googleMap') 라고 해야지
		//              $("#googleMap") 이라고 하면 지도가 나타나지 않는다.
	    
	    google.maps.event.addDomListener(window, "resize", function() {
	        var center = targetmap.getCenter();
	        google.maps.event.trigger(targetmap, "resize");
	        map.setCenter(center); 
	    });
		 
<%--		    
    // *** JAVA 의 List 에 저장되어 있는 것을 자바스크립트에서 불러오는 방법은 배열을 이용하면 된다.
			var storeNameArr = new Array(); 
			var latitudeArr  = new Array();
			var longitudeArr = new Array();
			var zindexArr    = new Array();
			// 자바스크립트에서 배열선언 하는 것
			
			<c:forEach var="storemapvo" items="${storemapList}">
				// 자바스크립트에서 선언된 배열에 값을 입력하는 명령어는
				// 배열객체명.push("데이터값"); 이다.
				// 그리고 객체변수가 HashMap 일 경우 HashMap에 저장된
				// 데이터값을 호출 하는 것은 딸러{객체변수.키명} 으로 호출하고
				// 객체변수가 VO 일 경우 
				// 데이값을 호출 하는 것은 딸러{객체변수.프로퍼티명} 으로 호출한다.
				// 지금 여기서는 객체변수가 HashMap 이므로 딸러{객체변수.키명} 으로 데이값을 호출한다.
				storeNameArr.push("${storemapvo.storeName}");
				latitudeArr.push("${storemapvo.latitude}");
				longitudeArr.push("${storemapvo.longitude}");
				zindexArr.push("${storemapvo.zindex}");
			</c:forEach>
			
			// >>>>>>>>> 확인용
			for(var i=0; i<storeNameArr.length; i++) {
					console.log(storeNameArr[i]);
			}
			
	
		    var companyArr = [
				[storeNameArr[0], latitudeArr[0], longitudeArr[0], zindexArr[0]],      //  1번 타이틀, 마커 좌표값, 우선순위(z-index)
				[storeNameArr[1], latitudeArr[1], longitudeArr[1], zindexArr[1]],      //  2번 타이틀, 마커 좌표값, 우선순위(z-index)
				[storeNameArr[2], latitudeArr[2], longitudeArr[2], zindexArr[2]]       //  3번 타이틀, 마커 좌표값, 우선순위(z-index)
			];
	
			// z-index 속성으로 요소 표시 순서 변경하기 참조사이트
			// http://html5dev.tistory.com/626
		    
			// z-index 속성은 position 속성과 관계가 있는 것으로 요소가 겹쳐져 있을 때 
			// 우선순위를 지정하는 속성이다. position 속성으로 요소 배치를 변경할 경우, 
			// 여러개의 요소가 겹쳐져 있다면 일반적으로 나중에 추가한 요소가 앞에 보여지지만
			// z-index 를 사용하면 우선순위를 지정하여 뒤에 있는 요소를 앞으로 재배치할 수 있다.
			// 이때 z-index 번호의 값이 클수록 앞에 보여지게 된다.

--%>			
		var storeArr = [
					     <c:set var="cnt" value="1" />
					     <c:forEach var="storemapvo" items="${storemapList}" varStatus="status">					
				   		  [
						    	"${storemapvo.storeName}",
						    	"${storemapvo.latitude}",
						    	"${storemapvo.longitude}",
						    	"${storemapvo.zindex}"
						  ]
				     	<c:if test="${cnt < storemapList.size()}">
				     	,
				     	</c:if>
				     
					     <c:set var="cnt" value="${cnt + 1}" />   // 변수 cnt 를 1씩 증가
					     </c:forEach>
			      	   ];
			
		setMarkers(targetmap, storeArr);
		    
	} // end of function initialize()--------------------------------
		 
		
	var markerArr;  // 전역변수로 사용됨.
		
	function setMarkers(targetmap, storeArr){
		    
		markerArr = new Array(storeArr.length);
			
		for(var i=0; i < storeArr.length; i++){
			var store = storeArr[i];
			//  console.log(store[0]);  // 점포명  ${storevo.storeName} 출력하기
				
			var myLatLng = new google.maps.LatLng(Number(store[1]), Number(store[2]));  
			// Number() 함수를 꼭 사용해야 함을 잊지 말자.               위도, 경도 
				
			<%--	
			if(i == 0) {
				var image = "<%= request.getContextPath() %>/images/pointerBlue.png";  // 1번 마커 이미지	
			} 
			else if(i == 1) {
				var image = "<%= request.getContextPath() %>/images/pointerPink.png"; // 2번 마커 이미지
			}
			else {
				var image = "<%= request.getContextPath() %>/images/pointerGreen.png"; // 3번 마커 이미지
			}
		    --%>	
				
		    // *** 마커 설정하기 *** //
			markerArr[i] = new google.maps.Marker({  
				position: myLatLng,        // 마커 위치
				map: targetmap,
			//	icon : image,              // 마커 이미지
				title : store[0],          // 위에서 정의한 "${store.storeName}" 임
				zIndex : Number(store[3])  // 위에서 정의한 "${storevo.zindex}" 임.  Number() 함수를 꼭 사용해야 함을 잊지 말자.
			});
			
			// **** 마커를 클릭했을때 어떤 동작이 발생하도록 하는 함수 호출하기 ****//   
			markerListener(targetmap, markerArr[i]);
				
		} // end of for------------------------------	
			
	}// end of setMarkers(map, locations)--------------------------
	 
		
///////////////////////////////////////////////////////////////////////////////////////

    var infowindowArr = new Array();  // 풍선창(풍선윈도우) 여러개를 배열로 저장하기 위한 용도 
        
 // **** 마커를 클릭했을때 어떤 동작이 발생하도록 하는 함수 생성하기 ****// 
	function markerListener(targetmap, marker){      
	
		// 확인용
		// console.log(marker.zIndex);	//  1  2  3 
	
		// === 풍선창(풍선윈도우)만들기 ===
		var infowindow = new google.maps.InfoWindow(  
				{// content: '안녕하세요~', 
				 // content: marker.title,	
				    content: viewContent(marker.title, marker.zIndex), 
				    size: new google.maps.Size(100,100) 
				});
		
		infowindowArr.push(infowindow); 
		// 생성한 풍선창(풍선윈도우) infowindow를 배열 infowindowArr 속에 집어넣기		
		
		// **** === marker에 click 이벤트 처리하기 === ***// 
		/*  마커를 클릭했을때 어떤 동작이 발생하게 하려면  
            addListener() 이벤트 핸들러를 사용하여 이벤트 알림을 등록하면 된다. 
                       이 함수는 소스객체, 수신할 이벤트, 지정된 이벤트가 발생할 때 호출할 함수를 인자로 받는다. */
		google.maps.event.addListener(marker, 'click', 
		    function(){ 
			      // marker에(i번째 마커) click(클릭)했을 때 실행할 내용들...
            
	              // 확인용
	              // console.log(marker.zIndex);  // 1  2  3
		     			
				  for(var i=0; i<markerArr.length; i++) { // 생성된 마커의 갯수만큼 반복하여
					  if(i != (marker.zIndex - 1) ) {     // 마커에 클릭하여 발생된 풍선창(풍선윈도우) infowindow 를 제외한 나머지 다른 마커에 달린 풍선창(풍선윈도우) infowindow 는
						 infowindowArr[i].close();	      // 닫는다.
					  }
					  else if(i == (marker.zIndex - 1)) {           // 마커에 클릭하여 발생된 풍선창(풍선윈도우) infowindow 는
						 infowindowArr[i].open(targetmap, marker);  // targetmap 상에 표시되어 있는 marker 위에 띄운다.
					  }
				  }// end of for-----------------------	 		
			 	
		  });  // end of google.maps.event.addListener()-------------------
		
	}// end of function markerListener(map, marker)-----------
	
	function viewContent(title, zIndex) {
		var html =  "<span style='color:red; font-weight:bold;'>"+title+"</span><br/>"
			     +  "<a href='javascript:goDetail("+zIndex+");'>상세보기</a>"; 
			         // 매장번호(marker.zIndex)를 넘겨받아 매장지점 상세정보 보여주기와 같은 팝업창 띄우기
		return html;	
	}
	
	function goDetail(storeno) {
		var url = "storeDetailView.do?storeno="+storeno;
		window.open(url, "storedetailViewInfo",
				    "width=700px, height=600px, top=50px, left=800px");
	}
	
</script>

<div id="googleMap"	style="width: 100%; height: 360px; margin: auto;"></div>










    