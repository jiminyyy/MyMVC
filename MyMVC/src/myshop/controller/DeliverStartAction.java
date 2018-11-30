package myshop.controller;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.controller.GoogleMail;
import member.model.MemberDAO;
import member.model.MemberVO;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;
import myshop.model.ProductVO;

public class DeliverStartAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String method = req.getMethod();
		
		if(!"POST".equalsIgnoreCase(method)) { // 포스트가 아니라면
			String msg =  "비정상적인 경로로 들어왔습니다!!";
			String loc = "javascript:history.back()";
						
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		
		MemberVO loginuser = super.getLoginUser(req);
		
		if(loginuser == null) return;
		if(!"admin".equalsIgnoreCase(loginuser.getUserid())) {
			String msg =  "접근권한이 없습니다!!";
			String loc = "javascript:history.back()";
						
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		
		// post면서 관리자로 로그인 한 경우
		// 체크박스의 value 값을 받아온다.
		String[] odrcodeArr = req.getParameterValues("odrcode");
		String[] pnumArr = req.getParameterValues("deliverStartPnum");
		String[] oqtyArr = req.getParameterValues("oqty");
		
		InterProductDAO pdao = new ProductDAO();
		// 자식클래스 객체는 인터페이스 혹은 부모클래스로 받을 수 있다. 위 경우는 인터페이스에 구현된 메소드만 사용 가능.
		
		// 's20181122-3/1', 's20181122-4/3', 코드뒤에 바로 상품번호가 온다. 이런식으로 고고
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<odrcodeArr.length; i++) {
			
			sb.append("\'"+odrcodeArr[i]+"/"+pnumArr[i]+"\',");
		}
		
		String odrcodePnum = sb.toString().trim();
		odrcodePnum = odrcodePnum.substring(0, odrcodePnum.length()-1);
		
		// System.out.println("확인용 : " + odrcodePnum);
		
		int n = pdao.updateDeliverStart(odrcodePnum, odrcodeArr.length);
		
		if(n == 1) {
			
			// === *** 배송을 했다라는 email을 주문을 한 사람(여러명)에게 보내기 시작 *** === //  
			
			// 1. 주문코드에 대한 제품번호 붙여가기(동일한 주문코드에 서로다른 제품번호가 있으므로)
			//    KEY는 주문코드, VALUE 는 여러제품번호들로 한다.  
			HashMap<String, String> odrcodeMap = new HashMap<String, String>(); 
			Set<String> odrcodeMapKeysets = odrcodeMap.keySet(); //odrcodeMap의 키값만 모아 odrcodeMapKeysets에 넣는다.
			
			for(int i=0; i<odrcodeArr.length; i++) {
				if(i==0) {
					odrcodeMap.put(odrcodeArr[i], pnumArr[i]); // odrcodeMap에 키값은 odrcodeArr, 밸류값은 pnumArr
				}
				else {
					boolean flag = false;
					for(String key : odrcodeMapKeysets) {// odrcodeMapKeysets안의 키값의 수만큼 for문을 돌리고
						if(odrcodeArr[i].equals(key)) { // odrcodeArr가 key와 같다면 (key s20181122-1, value 1,2) 이런식으로  odrcodeMap에 들어감
							odrcodeMap.put(odrcodeArr[i], odrcodeMap.get(key)+","+pnumArr[i]); 
							flag = true;
							break;
						}
					}// end of for--------------
					
					if(flag == false) {
						odrcodeMap.put(odrcodeArr[i], pnumArr[i]);  // (key s20181122-1, value 1)이런 식으로 들어감
					}
				}// end of if~else--------------
			}// end of for----------------------
			
			// **** 콘솔 확인용 **** //
			for(String key : odrcodeMapKeysets) {
				System.out.println("주문코드 : " + key + ", 제품번호 : " + odrcodeMap.get(key)); 
			}
			/*
			 	주문코드 : s20181122-5, 제품번호 : 3
				주문코드 : s20181122-4, 제품번호 : 3,4
				주문코드 : s20181122-3, 제품번호 : 1 
			 */
			
		
			// 2. 주문코드에 대한 제품번호들을 읽어와서   
			//    그 제품번호들에 대한 제품의 정보를 메일내용속에 넣고  
			//    주문코드를 사용하여 사용자의 정보를 읽어와서 
			//    그 사용자정보에서 email 주소와 사용자명을 찾아온다.
			for(String key : odrcodeMapKeysets) { 
				// key 가 주문코드 이다.
				
				String pnumes = odrcodeMap.get(key);
				// odrcodeMap 에서 주문코드에 대한 제품번호들을 읽어온다. 
				
				// **** 콘솔 확인용 **** //
				System.out.println("~~~ 확인용 pnumes : " + pnumes); 
				/*
				     ~~~ 확인용 pnumes : 3
				     ~~~ 확인용 pnumes : 3,4
				     ~~~ 확인용 pnumes : 1
				 */
				
				List<ProductVO> productList = pdao.getOrdProdList(pnumes);
				
				sb.setLength(0); // StringBuilder sb 초기화하기
				
				for(int j=0; j<productList.size(); j++) {
					if(j==0) {
						sb.append("주문코드번호 : <span style='color: blue; font-weight: bold;'>"+key+"</span><br/><br/>");	
						sb.append("<주문상품><br/>");
						sb.append(productList.get(j).getPname()+"&nbsp;");
						sb.append("<img src='http://192.168.50.53:9090/MyMVC/images/"+productList.get(j).getPimage1()+"'/>");  
						sb.append("<br/>");
					}
					else {
						sb.append(productList.get(j).getPname()+"&nbsp;");
						sb.append("<img src='http://192.168.50.53:9090/MyMVC/images/"+productList.get(j).getPimage1()+"'/>");  
						sb.append("<br/>");
					}
				}// end of for--------------------------
				
				sb.append("<br/><br/>우체국택배로 배송했습니다.");
				
				String emailContents = sb.toString();
				
				MemberVO membervo = pdao.getMemberInfoByOdrcode(key);
				// 해당 전표(영수증번호)의 소유주(회원) 찾아오기
				
				String emailAddress = membervo.getEmail();
				// 해당 전표(영수증번호)의 소유주(회원)의 이메일주소 가져오기 
				
				String name = membervo.getName();
				// 해당 전표(영수증번호)의 소유주(회원)의 성명 가져오기 
				
			// 3. 메일을 발송한다.	
				GoogleMail mail = new GoogleMail();	
				
				mail.sendmail_DeliverStartFinish(emailAddress, name, emailContents); 
				
			}// end of for---------------------
		// === *** 배송을 했다라는 email을 주문을 한 사람(여러명)에게 보내기 종료 *** === //
			
			String msg =  "배송시작 변경완료!!";
			String loc = "orderList.do";
						
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
		else {
			
			String msg =  "배송처리 변경 실패!!";
			String loc = "javascript:history.back()";
						
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
		
		
	}

}
