package memo.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import memo.model.MemoDAO;
import my.util.MyUtil;

public class MemoListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// ** 로그인 유무 곰사 **
		MemberVO loginuser = super.getLoginUser(req);
		if(loginuser == null) return;
		
		MemoDAO memodao = new MemoDAO();

		String str_sizePerPage = req.getParameter("sizePerPage");
		int sizePerPage = 0; 
		try {
			sizePerPage = Integer.parseInt(str_sizePerPage);
		} catch (NumberFormatException e) {
			sizePerPage = 5;
		}
		
		int totalMemoCount = 0;
		totalMemoCount = memodao.getTotalCount();
		
		int totalPage = (int)Math.ceil((double)totalMemoCount / sizePerPage);
		
		String str_currentShowPageNo = req.getParameter("currentShowPageNo");
		int currentShowPageNo = 0;
		
			if(str_currentShowPageNo == null) { // 회원목록을 눌렀을 때, 설정 안했을 때
				currentShowPageNo = 1;
			}
			else { // 사용자가 보고자 하는 페이지번호를 설정한 경우
				try {
					currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
					if(currentShowPageNo < 1 || currentShowPageNo > totalPage ) {
						currentShowPageNo = 1;
					}
				} catch (NumberFormatException e) {
					currentShowPageNo = 1;
				}
			}
		
		String str_period = req.getParameter("period");
		int period = 0; 
		try {
			period = Integer.parseInt(str_period);
		} catch (NumberFormatException e) {
			period = -1;
		}
		
		String searchType = req.getParameter("searchType");
		String searchWord = req.getParameter("searchWord");
		
		// == 초기화면 설정 값 정하기 ==
		if(str_sizePerPage == null) {
			str_sizePerPage = "5";
		}
		
		if(str_period == null) {
			str_period = "-1";
		}
		
		if(searchType == null) {
			searchType = "";
		}
		
		if(searchWord == null) {
			searchWord = "";
		}

		if(sizePerPage != 3 && sizePerPage != 5 && sizePerPage != 10) {
			sizePerPage = 5;
		}
		
		if(period != -1 && period != 3 && period != 10 && period != 30 && period != 60) {
			period = -1;
		}
		
		if(!"name".equals(searchType) &&
		   !"userid".equals(searchType) &&
		   !"email".equals(searchType)) {
			searchType = "";
		}
			
		// *** 페이징처리 하기 전의 데이터 조회 결과물 가져오기
		List<HashMap<String, String>>memoList = memodao.getAllMemo(sizePerPage, currentShowPageNo);
		
		String url = "memoList.do";
		int blockSize = 3;
		String pageBar = MyUtil.getSearchPageBar(url, currentShowPageNo, sizePerPage, totalPage, blockSize, searchType, searchWord, period);
		
		req.setAttribute("sizePerPage", sizePerPage);
		req.setAttribute("period", period);
		req.setAttribute("searchType", searchType);
		req.setAttribute("searchWord", searchWord);
		
		req.setAttribute("memoList", memoList);
		//req.setAttribute("currentURL", currentURL);

		req.setAttribute("pageBar", pageBar);
		req.setAttribute("currentShowPageNo", currentShowPageNo);
		req.setAttribute("totalMemoCount", totalMemoCount);
		req.setAttribute("totalPage", totalPage);
		
		super.setRedirect(false);
		//super.setViewPage("/WEB-INF/memo/memoList.jsp");
		super.setViewPage("/WEB-INF/memo/memoListJSTL.jsp");

	}

}
