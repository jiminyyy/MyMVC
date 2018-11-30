package myshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import myshop.model.ProductDAO;
import myshop.model.ProductVO;

public class MallHomeAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// *** 카테고리 목록을 가져와서 왼쪽 로그인폼 아래에 보여주도록 한다 ***
		super.getCategoryList(req);
		
		ProductDAO pdao = new ProductDAO();
		
		// 1. HIT상품을 가져오기 (페이징처리 xxxxxxx)
		List<ProductVO> hitList = pdao.selectByPsepc("HIT");
		
		req.setAttribute("hitList", hitList);

		super.setRedirect(false); // 데이터를보내줘야하니까.. false
		super.setViewPage("/WEB-INF/myshop/mallHome.jsp");
		
		// 2. NEW 상품 가져오기
		
	}

}
