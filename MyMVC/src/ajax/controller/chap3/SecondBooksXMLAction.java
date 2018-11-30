package ajax.controller.chap3;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.model.AjaxDAO;
import ajax.model.InterAjaxDAO;
import common.controller.AbstractController;

public class SecondBooksXMLAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		InterAjaxDAO adao = new AjaxDAO();
		List<HashMap<String,String>> bookList = adao.getAllBooks();
		
		req.setAttribute("bookList", bookList);
		super.setRedirect(false);
		super.setViewPage("/ajaxstudy/chap3/2booksXML.jsp");
		
	}

}
