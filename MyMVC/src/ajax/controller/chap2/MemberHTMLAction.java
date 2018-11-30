package ajax.controller.chap2;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.model.AjaxDAO;
import ajax.model.InterAjaxDAO;
import common.controller.AbstractController;
import member.model.MemberVO;

public class MemberHTMLAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String searchname = req.getParameter("searchname");
		
		InterAjaxDAO adao = new AjaxDAO();
		List<MemberVO> memberList = adao.getSearchMembers(searchname);
		
		req.setAttribute("memberList", memberList);
		
		super.setRedirect(false);
		super.setViewPage("/ajaxstudy/chap2/memberInfo.jsp");
		
	}

}
