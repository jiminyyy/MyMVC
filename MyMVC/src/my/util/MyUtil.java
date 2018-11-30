package my.util;

import javax.servlet.http.HttpServletRequest;

public class MyUtil {
	
	// *** ? 다음을 포함한 현재 URL주소를 나타내는 메소드
	public static String getCurrentURL(HttpServletRequest request) {
			
		String currentURL = request.getRequestURL().toString();
		// ? 전까지 나온다 == http://localhost:9090/MyWeb/member/memberDetail.jsp
			
		String queryString = request.getQueryString();
		// idx=61&goBackURL=/memberList.jsp?sizePerPage=3&currentShowPageNo=8&period=60&searchType=name&searchWord=%EC%9C%A0%EB%AF%B8
		
		currentURL += "?" + queryString;
		// http://localhost:9090/MyWeb/member/memberDetail.jsp?idx=61&
		// goBackURL=/memberList.jsp?sizePerPage=3&currentShowPageNo=8&period=60&searchType=name&searchWord=%EC%9C%A0%EB%AF%B8
		
		String ctxPath = request.getContextPath();
		// /MyWeb
		
		int beginIndex = currentURL.indexOf(ctxPath) + ctxPath.length();
		// 21+6
		
		currentURL = currentURL.substring(beginIndex+1);
		// member/memberDetail.jsp?idx=61&
		// goBackURL=/memberList.jsp?sizePerPage=3&currentShowPageNo=8&period=60&searchType=name&searchWord=%EC%9C%A0%EB%AF%B8
		
		return currentURL;
	} // end of public static String getCurrentURL() {
	
	// 검색어 및 날짜구간이 포함된 페이지바 만들기
	public static String getSearchPageBar(String url, int currentShowPageNo, int sizePerPage, int totalPage, int blockSize, 
										  String searchType, String searchWord, int period) {
		
		String pageBar = "";
		
		int pageNo = 1;
		int loop = 1;
		
		pageNo = ( (currentShowPageNo - 1) /blockSize ) * blockSize +1;
		
		if(pageNo != 1) {
			pageBar += "&nbsp;<a href='"+url+"?currentShowPageNo="+(pageNo-1)+"&sizePerPage="+sizePerPage+"&searchType="+searchType+
					   "&searchWord="+searchWord+"&period="+period+"'>[이전]</a>";	
			}
		
		while( !(pageNo > totalPage || loop > blockSize )) { // 전체페이지수를 넘으면 빠져나와 || 10개단위로 나눈다
			if(pageNo == currentShowPageNo) {	
				pageBar += "&nbsp;<span style='color: red; font-size: 13pt; font-weight: bold; text-decoration: underline;'>"+pageNo+"</span>&nbsp;";
			}
			else {
				pageBar += "&nbsp;<a href='"+url+"?currentShowPageNo="+pageNo+"&sizePerPage="+sizePerPage+"&searchType="+searchType+
					   "&searchWord="+searchWord+"&period="+period+"'>"+pageNo+"</a>";
			}
		pageNo++;
		loop++;
		} // end of while
		
		if( !(pageNo > totalPage)) {
			pageBar += "&nbsp;<a href='"+url+"?currentShowPageNo="+pageNo+"&sizePerPage="+sizePerPage+"&searchType="+searchType+
					   "&searchWord="+searchWord+"&period="+period+"'>[다음]</a>";
			}
		
		return pageBar;
	}
	
}
