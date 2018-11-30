package myshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;
import myshop.model.StoremapVO;

public class SearchStoreMapAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		InterProductDAO pdao = new ProductDAO();
		
		List<StoremapVO> storemapList = pdao.getStoreMap();
		
		req.setAttribute("storemapList", storemapList);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/storeGoogleMap.jsp");
		
	}

}
