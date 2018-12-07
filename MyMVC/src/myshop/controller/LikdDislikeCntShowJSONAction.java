package myshop.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;

public class LikdDislikeCntShowJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String pnum = req.getParameter("pnum");
		
		InterProductDAO pdao = new ProductDAO();
		
		HashMap<String, Integer> cntmap = pdao.getLikeDislikeCnt(pnum);
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("likeCnt", cntmap.get("LIKECNT"));
		jsonObj.put("dislikeCnt", cntmap.get("DISLIKECNT"));
		
		String str_json = jsonObj.toString();
		
		req.setAttribute("str_json", str_json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/likeDislikeCntShow.jsp");
		
	}

}
