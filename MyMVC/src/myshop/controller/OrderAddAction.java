package myshop.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.controller.GoogleMail;
import member.model.MemberDAO;
import member.model.MemberVO;
import myshop.model.ProductDAO;
import myshop.model.ProductVO;

public class OrderAddAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		String method = req.getMethod();
		
		if (!"POST".equalsIgnoreCase(method)) { // 포스트방식으로 들어오지 않았을 때
			
			req.setAttribute("msg", "잘못된 경로로 들어왔습니다!!!");
			req.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {//포스트방식으로 들어옴
			
			int n = 0; // jsp_order, jsp_order_detail테이블에 insert 성공유무 체크
			
			HttpSession session = req.getSession();
					
			MemberVO loginuser = super.getLoginUser(req);
			
			if (loginuser == null) { // 로그인하지 않은 상태에서 장바구니 담기 시도, 로그인 시도시 유저가 보던 제품 페이지로 돌아가야한다.
				
				String goBackURL = req.getParameter("goBackURL");
				// goBackURL을 받아서 세션에 넣어준다.
				
				session.setAttribute("returnPage", goBackURL);
				
				return;
			
			}
			
			else { // 포스트방식, 로그인 완료
				/*				
				String pnum = req.getParameter("pnum");						// 상품번호
				String saleprice = req.getParameter("saleprice");			// 상품 현재 가격(정가x)
				*/
				String[] pnumArr = req.getParameterValues("pnum");			// jsp에서 name이 pnum인 곳의 value값들 (여러개임)
				String[] salepriceArr = req.getParameterValues("saleprice");
				String sumtotalprice = req.getParameter("sumtotalprice");	// 주문한 항목들의 주문량*상품 현재 가격의 합
				String sumtotalpoint = req.getParameter("sumtotalpoint");	// 주문한 항목들의 주문량*구매시 적립가능한 포인트 합
				String[] oqtyArr = req.getParameterValues("oqty");
				String[] cartnoArr = req.getParameterValues("cartno");
				/*
				for(int i=0; i<pnumArr.length; i++) {
					System.out.println("제품번호 : " + pnumArr[i]);
					System.out.println("실제판매가 : " + salepriceArr[i]);
				}
				System.out.println("주문총액 : " + sumtotalprice);
				System.out.println("주문총포인트 : " + sumtotalpoint);
				*/
				
				ProductDAO pdao = new ProductDAO();
				
				/*	***** Transaction 처리하기 *****
					1. 주문개요 테이블 (jsp_order) 입력 (insert)
					2. 주문상세 테이블(jsp_order_detail) 입력 (insert)
					3. 구매하는 사용자 coin 컬럼의 값을 구매한 가격만큼 빼고, point는 더한다. (jsp_member update)
					4. 주문한 상품 재고량을 주문량만큼 뺀다. (jsp_prod update)
					5. 장바구니에서 주문 시 장바구니 비우기 (jsp_cart status update)
					6. ProductDAO pdao 객체에서 메소드 호출
				*/
				String odrcode = getOdrcode(); // 채번
			
				n = pdao.addOrder(odrcode								// 주문코드(명세서번호) 형식 : s+날짜+sequence : s20180430-1
									  , loginuser.getUserid()				// 사용자ID
									  , Integer.parseInt(sumtotalprice)		// 주문총액
									  , Integer.parseInt(sumtotalpoint)		// 주문 총 포인트
									  , pnumArr								// 제품번호 배열
									  , oqtyArr								// 주문량 배열
									  , salepriceArr
									  , cartnoArr);
				
				if( n == 1) { // 주문완료
					MemberDAO mdao = new MemberDAO();
					loginuser = mdao.getMemberDetail(loginuser.getIdx());
					session.setAttribute("loginuser", loginuser);
					
					// email 발송작업
					GoogleMail mail = new GoogleMail();
					int length = pnumArr.length;
					StringBuilder sb = new StringBuilder();
					
					for(int i=0; i<length; i++) {
						sb.append("\'"+pnumArr[i]+"\',");
						// jsp_product 테이블에서 select 시 where절의 in() 속에 제품번호가 들어간다. 홑따옴표(')가 필요한 경우 위와 같이 한다.
					} // end of for---------------------- sb 변수 안에 'pnumArr[i]',를 반복하여 넣어준다.
					
					String pnumes = sb.toString().trim(); // sb 변수를 string타입으로, 공백제거
					pnumes = pnumes.substring(0, pnumes.length()-1); // 가장 끝 콤마를 제거해야하므로 0부터 마지막 콤마 앞까지만 따와서 변수 pnumes에 넣는다.
					
					System.out.println("확인용 : " + pnumes); //확인용 : '3','4','6'
					
					List<ProductVO> ordProdList = pdao.getOrdProdList(pnumes);
					// 주문한 제품번호를 통해 제품정보를 얻어온다.
					
					sb.setLength(0); // StringBuilder 초기화
					
					sb.append("주문코드번호 : <span style='color:blue; font-weight:bold;'>" + odrcode + "</span><br/><br/><br/>");
					sb.append("<주문상품><br/>");
					for(int i=0; i<ordProdList.size(); i++) {
						
						sb.append(ordProdList.get(i).getPname()+"&nbsp;"+oqtyArr[i]+"개");
						sb.append("<img src='http://localhost:9090/MyMVC/images/"+ ordProdList.get(i).getPimage1() +"'/>");
						sb.append("<br/>");
					} // end of for
					sb.append("<br/>이용해 주셔서 감사합니다 *^^*");
					
					String emailContents = sb.toString();
							
					mail.sendmail_OrderFinish(loginuser.getEmail(), loginuser.getName(), emailContents); // 메일 보내기
					
					// email 발송작업 종료
				} 
			
			} // end of else 포스트방식이면서 로그인 했을 경우
			
			if( n == 1) { // 주문완료 
				req.setAttribute("msg", "주문이 정상적으로 완료되었습니다!!!");
				req.setAttribute("loc", "orderList.do");
			}
			else {
				req.setAttribute("msg", "주문이 실패되었습니다!!!");
				req.setAttribute("loc", "javascript:history.back()");
			}
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
		} // end of else { 포스트방식이라면
		
	} // end execute

	private String getOdrcode() throws SQLException { // 주문코드 생성하기
		
		Date now = new Date();
		SimpleDateFormat smdatefm = new SimpleDateFormat("yyyyMMdd");
		String today = smdatefm.format(now); 
		
		ProductDAO pdao = new ProductDAO();
		int seq = pdao.getSeq_jsp_order(); // 시퀀스 seq_jsp_order값을 채번
		
		return "s"+today+"-"+seq;
	} // end of getOdrcode()
	
}
