package ajax.controller.chap5;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import ajax.model.AjaxDAO;
import ajax.model.InterAjaxDAO;
import common.controller.AbstractController;

public class AddWordAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String mdcategorycode = req.getParameter("mdcategorycode");
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		
		HashMap<String,String> map = new HashMap<String,String>();
		
		map.put("MDCATEGORYCODE", mdcategorycode);
		map.put("TITLE", title);
		map.put("CONTENT", content);
		
		InterAjaxDAO adao = new AjaxDAO();
		int n = adao.addWord(map);
				
		JSONObject jsonObj = new JSONObject();
		// JSONObject 는 JSON형태(키:값)의 데이터를 관리해 주는 클래스이다.
		jsonObj.put("n", n);
		
		String str_nInfo = jsonObj.toString();  
		// JSONObject personObj 을 String 으로 변환하기
				
		req.setAttribute("str_nInfo", str_nInfo);
		
		super.setRedirect(false);
		super.setViewPage("/ajaxstudy/chap5/insertResultJSON.jsp");

	}

}
