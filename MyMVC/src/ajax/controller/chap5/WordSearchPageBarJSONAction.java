package ajax.controller.chap5;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import ajax.model.AjaxDAO;
import ajax.model.InterAjaxDAO;
import common.controller.AbstractController;

public class WordSearchPageBarJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String mdcode = req.getParameter("mdcode");
		String searchword = req.getParameter("searchword");
		String sizePerPage = req.getParameter("sizePerPage");
		
		InterAjaxDAO adao = new AjaxDAO();
				
		// -- 검색조건에 맞는 총 게시물갯수 totalCount 알아오기 
		int totalCount = 0;
		
		if(!"".equals(mdcode)) {
			totalCount = adao.getTotalCountByMdcode(mdcode); 
		}
		else {
			totalCount = adao.getTotalCountBySearchword(searchword); 
		}
				
		// -- 총 페이지수 totalPage 구하기
		int totalPage = (int)Math.ceil((double)totalCount/Integer.parseInt(sizePerPage)); 
		/*
		   57/10 ==> 5.7  ==> 6.0   ==> 6
		   57/5  ==> 11.4 ==> 12.0  ==> 12
		   57/3  ==> 19   ==> 19.0  ==> 19
		*/
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage);
		
		String str_totalPage = jsonObj.toString();
		
		req.setAttribute("str_totalPage", str_totalPage);
		
		super.setRedirect(false);
		super.setViewPage("/ajaxstudy/chap5/wordSearchPageBarJSON.jsp");
		
	}

}
