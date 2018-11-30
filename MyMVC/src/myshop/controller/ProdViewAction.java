package myshop.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import my.util.MyUtil;
import myshop.model.ProductDAO;
import myshop.model.ProductVO;

public class ProdViewAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// *** 카테고리 목록을 가져와서 왼쪽 로그인폼 아래에 보여주도록 한다 ***
		super.getCategoryList(req);
		
		String str_pnum = req.getParameter("pnum");
		
		int pnum = 0;
		try {
			pnum = Integer.parseInt(str_pnum);
			
			ProductDAO pdao = new ProductDAO();
			ProductVO pvo = pdao.getProductOneByPnum(pnum);
			List<HashMap<String, String>> imgList = pdao.getImgByPnum(pnum);
			String goBackURL = MyUtil.getCurrentURL(req);
			
			if(pvo == null) {
				req.setAttribute("str_pnum", str_pnum);
			
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/myshop/errorNotProduct.jsp");
			
				return;
			}
			
			req.setAttribute("pvo", pvo);
			req.setAttribute("imgList", imgList);
			req.setAttribute("goBackURL", goBackURL);
			// 메소드로 만든 goBackURL을 보내준다.
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/proView.jsp");
			
			
		} catch (NumberFormatException e) {
			req.setAttribute("str_pnum", str_pnum);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/errorNotProduct.jsp");
			
			return;
		}
		
		
	}

}
