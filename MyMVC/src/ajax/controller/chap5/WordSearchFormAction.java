package ajax.controller.chap5;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.model.AjaxDAO;
import ajax.model.InterAjaxDAO;
import common.controller.AbstractController;

public class WordSearchFormAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res)
		throws Exception {
		
		String lgcategorycode = req.getParameter("lgcategorycode");
		InterAjaxDAO adao = new AjaxDAO();
		
		if(lgcategorycode == null)
			lgcategorycode = adao.getMinLgcategorycode(); // 처음 접속 시 A가 들어간다.
		
		List<HashMap<String, String>> lgcategorycodeList = adao.getLgcategorycode(); // 모든 대분류코드와 대분류명을 가져온다.
		List<HashMap<String, String>> mdcategorycodeList = adao.getMdcategorycode(lgcategorycode); // 대분류코드에 따른 중분류 코드와 중분류명 리스트를 가져온다.
		
		req.setAttribute("lgcategorycodeList", lgcategorycodeList); 
		req.setAttribute("mdcategorycodeList", mdcategorycodeList);
		
		super.setViewPage("/ajaxstudy/chap5/wordSearchFormAjax.jsp");

	}	
	
}
