package myshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.controller.AbstractController;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;
import myshop.model.ProductVO;

public class MalldisplayJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String start = req.getParameter("start"); 
		// 1페이지는 상품을 1(start)~8까지 보여준다.
		// 2페이지는 상품을 9(start)~16까지
		// 3페이지는 상품을 17(start)~24까지
		String len = req.getParameter("len"); // 8개, 더보기 클릭 시 보여줄 상품 갯수
		String pspec = req.getParameter("pspec");
		
		if(start == null || start.trim().isEmpty()) {
			start = "1";
		}
		if(len == null || len.trim().isEmpty()) {
			len = "8";
		}
		if(pspec == null || pspec.trim().isEmpty()) {
			pspec = "NEW";
		}
		
		int startRno = Integer.parseInt(start);
		// 공식! 시작행번호		1				9				17
		
		int endRno = startRno + Integer.parseInt(len) -1;
		// 공식! 끝행번호		8(=> 1+8 -1)	16(=> 9+8 - 1)	24(=> 17+8 -1)
		
		InterProductDAO pdao = new ProductDAO();
		
		List<ProductVO> productList = pdao.getProductByPspecAppend(pspec,startRno,endRno);
		
		JSONArray jsonArray = new JSONArray();
		
		if(productList != null && productList.size() > 0 ) {
			
			for( ProductVO pvo : productList ) {
				JSONObject jsonObj = new JSONObject();
				
				jsonObj.put("pnum", pvo.getPnum());
				jsonObj.put("pname", pvo.getPname());
				jsonObj.put("pcategory_fk", pvo.getPcategory_fk());
				jsonObj.put("pcompany", pvo.getPcompany());
				jsonObj.put("pimage1", pvo.getPimage1());
				jsonObj.put("pimage2", pvo.getPimage2());
				jsonObj.put("pqty", pvo.getPqty());
				jsonObj.put("price", pvo.getPrice());
				jsonObj.put("saleprice", pvo.getSaleprice());
				jsonObj.put("pspec", pvo.getPspec());
				jsonObj.put("pcontent", pvo.getPcontent());
				jsonObj.put("point", pvo.getPoint());
				jsonObj.put("pinputdate", pvo.getPinputdate());
				jsonObj.put("percent", pvo.getPercent());
				
				jsonArray.add(jsonObj);
			}
		}
		
		String str_jsonArray = jsonArray.toString();
		
		req.setAttribute("str_jsonArray", str_jsonArray);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/mallDisplayJSON.jsp");
		
		
		
		
	}

}
