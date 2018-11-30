package member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;
import my.util.MyUtil;

public class MemberListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// ** 로그인 유무 검사 **
		MemberVO loginuser = super.getLoginUser(req);
		if(loginuser == null) return;
		
		// 1. MemberDAO 객체 생성
		MemberDAO memberdao = new MemberDAO();
		
		// 2. 검색어 및 날짜 구간을 받아서 검색
		// 페이징 처리를 위해 페이지 당 보여줄 sizePerPage 받아오기
		String str_sizePerPage = req.getParameter("sizePerPage");
		int sizePerPage = 0; 
		try {
			sizePerPage = Integer.parseInt(str_sizePerPage);
		} catch (NumberFormatException e) {
			sizePerPage = 5;
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
			searchType = "name";
		}
		
		if(searchWord == null) {
			searchWord = "";
		}
		
		// GET방식 잘못된 경로일 경우 차단
		if(sizePerPage != 3 && sizePerPage != 5 && sizePerPage != 10) {
			sizePerPage = 5;
		}
		
		if(period != -1 && period != 3 && period != 10 && period != 30 && period != 60) {
			period = -1;
		}
		
		if(!"name".equals(searchType) &&
		   !"userid".equals(searchType) &&
		   !"email".equals(searchType)) {
			searchType = "name";
		}
		
		// 3. 전체 페이지 갯수 알아오기
		// 3-1. 총 회원 명수
		int totalMemberCount = 0;
		totalMemberCount = memberdao.getTotalCount(searchType, searchWord, period);
		
		// 3-1-1. 활동 중인 회원 명수
		int actMemberCount = 0;
		actMemberCount = memberdao.getActCount(searchType, searchWord, period);
		
		// 3-2. 전체 페이지 수
		int totalPage = 0;
		
		if  ("admin".equalsIgnoreCase(loginuser.getUserid())) {
			totalPage = (int)Math.ceil((double)totalMemberCount / sizePerPage);
		}
		else {
			totalPage = (int)Math.ceil((double)actMemberCount / sizePerPage);
		}
		// 사용자가 보고자 선택한 페이지
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
		// 4. 검색 조건에 맞는 회원 정보 jsp_member 테이블에서 select한 결과물을 가져와서 담는다.
		
		List<MemberVO> memberList = null;
		memberList = memberdao.getAllMember(sizePerPage, currentShowPageNo, period, searchType, searchWord);
		
		// 4-1. 휴면계정 제외한 회원 정보를 담는다.
		List<MemberVO> actMemberList = null;
		actMemberList = memberdao.getActMember(sizePerPage, currentShowPageNo, period, searchType, searchWord);
			
		// 되돌아갈 URL
		String currentURL = MyUtil.getCurrentURL(req);
					
		// 5. 페이지 바 만들기
		String url = "memberList.do";
		int blockSize = 10;
		String pageBar = MyUtil.getSearchPageBar(url, currentShowPageNo, sizePerPage, totalPage, blockSize, searchType, searchWord, period);
		
		// 6. 지금까지 작성한 데이터값들을 VIEW단으로 넘긴다
		
		req.setAttribute("sizePerPage", sizePerPage);
		req.setAttribute("period", period);
		req.setAttribute("searchType", searchType);
		req.setAttribute("searchWord", searchWord);
		
		req.setAttribute("memberList", memberList);
		req.setAttribute("actMemberList", actMemberList);
		req.setAttribute("currentURL", currentURL);

		req.setAttribute("pageBar", pageBar);
		req.setAttribute("currentShowPageNo", currentShowPageNo);
		req.setAttribute("totalMemberCount", totalMemberCount);
		req.setAttribute("actMemberCount", actMemberCount);
		req.setAttribute("totalPage", totalPage);
		
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/member/memberList.jsp");
	}
}
