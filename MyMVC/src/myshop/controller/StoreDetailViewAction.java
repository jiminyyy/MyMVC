package myshop.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;

public class StoreDetailViewAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String storeno = req.getParameter("storeno");
		
		InterProductDAO pdao = new ProductDAO();
		
		List<HashMap<String,String>> storeDetailList = pdao.getStoreDetail(storeno);
		
		req.setAttribute("storeDetailList", storeDetailList);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/storeDetailView.jsp");
		
	}

}
