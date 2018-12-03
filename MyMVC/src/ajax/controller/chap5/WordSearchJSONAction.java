package ajax.controller.chap5;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ajax.model.AjaxDAO;
import ajax.model.InterAjaxDAO;
import common.controller.AbstractController;

public class WordSearchJSONAction extends AbstractController{

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		
		String searchword = req.getParameter("searchword"); 
		
		JSONArray jsonArray = new JSONArray();
		
		if(!searchword.trim().isEmpty()) {
			
			InterAjaxDAO adao = new AjaxDAO();
			List<String> titleList = adao.getSearchedTitle(searchword);
			
			if(titleList != null && titleList.size() > 0) {
				for(String resultstr : titleList) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("searchwordresult", resultstr);
					
					jsonArray.add(jsonObj);
				}
			}
			
			//  resultList = new ArrayList<String>();	
	        //  List<WordVO> allList = dao.getAllTitle();	
			
			//	searchword = searchword.toLowerCase();
				/* 검색어를 모두 소문자로 변경함.
				      검색할때는 대,소문자 구분없이 조회를 해주려고 함.
				   DB속에 들어간 문자로 select 시 lower 함수를 사용하여 소문자로 출력되게끔 하였다. 
				*/
					
				/*	for(WordVO wvo : allList) {
					  	String title = wvo.getTitle();
					  	if(title.startsWith(searchword)) {
					  		resultList.add(title);
					  	}
					}// end of for---------------
				 */				
			
		}// end of if------------------------------
		
        String str_jsonArray = jsonArray.toString();
		
		System.out.println(">>>> str_jsonArray 확인용 : " + str_jsonArray);
		// >>>> str_jsonArray 확인용 : [{"searchwordresult":"JAVA Programing"},{"searchwordresult":"Java 프로그래밍"},{"searchwordresult":"ajax 프로그래밍"},{"searchwordresult":"AJAX"}]
				
		req.setAttribute("str_jsonArray", str_jsonArray);
		
		super.setRedirect(false);
		super.setViewPage("/ajaxstudy/chap5/wordSearchJSON.jsp");

	}	
	
}
