package ajax.controller.chap4;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class IdInputAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) 
		throws Exception {
		
		super.setRedirect(false);
		super.setViewPage("/ajaxstudy/chap4/3idInputFormAjax.jsp");
		
	}

}
