package ajax.controller.chap5;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ajax.model.AjaxDAO;
import ajax.model.InterAjaxDAO;
import common.controller.AbstractController;


public class WordSearchResultJSONAction extends AbstractController {
	// 조회버튼 클릭 시 이곳
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String mdcode = req.getParameter("mdcode");
		
		String searchword = req.getParameter("searchword");
		String str_currentShowPageNo = req.getParameter("currentShowPageNo");
		
		if(str_currentShowPageNo == null || "".equals(str_currentShowPageNo)) {
			str_currentShowPageNo = "1";
		}
		int	currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
		int sizePerPage = 5; // 결과물은 다섯개씩 보여준다.
		
		InterAjaxDAO adao = new AjaxDAO();
				
		List<HashMap<String, String>> contentList = adao.getSearchContent(sizePerPage, currentShowPageNo, mdcode, searchword);  
		
		JSONArray jsonArray = new JSONArray();
		
		if(contentList != null && contentList.size() > 0) {
			for(HashMap<String, String> map : contentList) {
				JSONObject jsonObj = new JSONObject();
				// JSONObject 는 JSON형태(키:값)의 데이터를 관리해 주는 클래스이다.
				
			    jsonObj.put("seq", map.get("seq"));
			    jsonObj.put("lgcodename", map.get("lgcodename"));
			    jsonObj.put("mdcodename", map.get("mdcodename"));
			    jsonObj.put("title", map.get("title"));
			    jsonObj.put("content", map.get("content"));
			    
			    jsonArray.add(jsonObj);
			}
		}
		
		String str_jsonArray = jsonArray.toString();
		
		req.setAttribute("str_jsonArray", str_jsonArray);
				
		super.setRedirect(false);
		super.setViewPage("/ajaxstudy/chap5/wordSearchResultJSON.jsp");
		
	}

}
