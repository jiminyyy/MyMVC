package myshop.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.ProductDAO;
import myshop.model.ProductVO;

public class ProductRegisterEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// 1. 반드시 관리자로 로그인 했을 경우에만 접근이 가능하도록 하자
		MemberVO loginuser = super.getLoginUser(req);
		
		if(loginuser == null) return;
		
		if(!"admin".equalsIgnoreCase(loginuser.getUserid())) {
			
			req.setAttribute("msg", "접근권한이 없습니다!");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		
		// 1. 첨부파일을 어느 경로에 업로드 할 것인지 설정해야한다.
		HttpSession session = req.getSession();
		ServletContext svlCtx = session.getServletContext();
		String imagesDir = svlCtx.getRealPath("/images");
		
		System.out.println("imagesDir ==> "+imagesDir);
		//C:\myjsp\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\MyMVC\images
		
		MultipartRequest mtreq = null;
		// mtreq은 req가 하는 일을 그대로 승계받아 처리하는 동시에 파일을 받아서 업로드, 다운로드 하는 기능을 한다.
		// input타입이 file이니까! req는 못쓰고 mtreq를 쓴다!!
		
		try {
			/*
		    MultipartRequest의 객체가 생성됨과 동시에 파일 업로드가 이루어 진다.
					 
		    MultipartRequest(HttpServletRequest request,
							 String saveDirectory,			-- 파일이 저장될 경로
							 int maxPostSize,				-- 업로드할 파일 1개의 최대 크기(byte)
							 String encoding,
							 FileRenamePolicy policy)		-- 중복된 파일명이 올라갈 경우 파일명다음에 자동으로 숫자가 붙어서 올라간다. 
				   
			파일을 저장할 디렉토리를 지정할 수 있으며, 업로드제한 용량을 설정할 수 있다.(바이트단위). 
			이때 업로드 제한 용량을 넘어서 업로드를 시도하면 IOException 발생된다. 
			또한 국제화 지원을 위한 인코딩 방식을 지정할 수 있으며, 중복 파일 처리 인터페이스를사용할 수 있다.
					   		
			이때 업로드 파일 크기의 최대크기를 초과하는 경우이라면 IOException이 발생된다.
			그러므로 Exception 처리를 해주어야 한다.*/
			
			mtreq = new MultipartRequest(req, imagesDir, 10*1024*1024, "UTF-8", new DefaultFileRenamePolicy());
			
		}catch (IOException e) {
			
			req.setAttribute("msg", "용량(10MB)초과로 파일업로드 실패!");
			req.setAttribute("loc", "productRegister.do");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		
		// 파일을 올렸으니 그 다음으로는 상품정보(상품명, 정가, 제품설명...)를 DB jsp_product 테이블에 insert 를 해주어야 한다. 

		ProductDAO pdao = new ProductDAO();

		// 1. 새로운 제품 등록시 HTML form 에서 입력한 값들을 얻어오기 
		String pname = mtreq.getParameter("pname");
		String pcategory_fk = mtreq.getParameter("pcategory_fk");
		String pcompany = mtreq.getParameter("pcompany");
							
		String pimage1 = mtreq.getFilesystemName("pimage1");
		String pimage2 = mtreq.getFilesystemName("pimage2");
		
		// 업로드되어진 시스템의 파일 이름을 얻어 올때는 
		// cos.jar 라이브러리에서 제공하는 MultipartRequest 객체의 getFilesystemName("form에서의 첨부파일 name명") 메소드를 사용 한다. 
		// 이때 업로드 된 파일이 없는 경우에는 null을 반환한다.
		/*
		<<참고>> 
		※ MultipartRequest 메소드

		-------------------------------------------------
		반환타입			설명
		--------------------------------------------------
		Enumeration		getFileNames()
				
						업로드 된 파일들에 대한 이름을 Enumeration객체에 String형태로 담아 반환한다. 
						이때의 파일 이름이란 클라이언트 사용자에 의해서 선택된 파일의 이름이 아니라, 
						개발자가 form의 file타임에 name속성으로 설정한 이름을 말한다. 
						만약 업로드 된 파일이 없는 경우엔 비어있는 Enumeration객체를 반환한다.
				
				 
		String			getContentType(String name)
				
						업로드 된 파일의 컨텐트 타입을 얻어올 수 있다. 
						이 정보는 브라우저로부터 제공받는 정보이다. 
						이때 업로드 된 파일이 없는 경우에는 null을 반환한다.
				
				
		File			getFile(String name)
				
						업로드 된 파일의 File객체를 얻는다. 
						우리는 이 객체로부터 파일사이즈 등의 정보를 얻어낼 수 있다. 
						이때 업로드 된 파일이 없는 경우에는 null을 반환한다.
				
				
		String			getFilesystemName(String name)
				
						시스템의 파일 이름을 반환한다. 
						이때 업로드 된 파일이 없는 경우에는 null을 반환한다.
				
				
		String			getOriginalFimeName(String name)

						중복 파일 처리 인터페이스에 의해 변환되기 이전의 파일 이름을 반환한다. 
						이때업로드 된 파일이 없는 경우에는 null을 반환한다.


		String			getParameter(String name)

						지정한 파라미터의 값을 반환한다. 
						이때 전송된 값이 없을 경우에는 null을 반환한다.


		Enumeration		getParameternames()

						폼을 통해 전송된 파라미터들의 이름을 Enumeration객체에 String 형태로 담아 반환한다. 
						전송된 파라미터가 없을 경우엔 비어있는 Enumeration객체를 반환한다


		String[]		getparameterValues(String name)

						동일한 파라미터 이름으로 전송된 값들을 String배열로 반환한다. 
						이때 전송된파라미터가 없을 경우엔 null을 반환하게 된다. 
						동일한 파라미터가 단 하나만 존재하는 경우에는 하나의 요소를 지닌 배열을 반환하게 된다.				*/
									
		String pqty = mtreq.getParameter("pqty");
		String price = mtreq.getParameter("price"); 
		String saleprice = mtreq.getParameter("saleprice");
		String pspec = mtreq.getParameter("pspec");
		String pcontent = mtreq.getParameter("pcontent").replaceAll("\r\n", "<br/>");
		String point = mtreq.getParameter("point");
		
		ProductVO pvo = new ProductVO();
		int pnum = pdao.getPnumOfProduct(); // 제품번호 채번
		
		pvo.setPnum(pnum);
		pvo.setPname(pname);
		pvo.setPcategory_fk(pcategory_fk);
		pvo.setPcompany(pcompany);
		pvo.setPimage1(pimage1);
		pvo.setPimage2(pimage2);
		pvo.setPqty(Integer.parseInt(pqty));
		pvo.setPrice(Integer.parseInt(price));
		pvo.setSaleprice(Integer.parseInt(saleprice));
		pvo.setPspec(pspec);
		pvo.setPcontent(pcontent);
		pvo.setPoint(Integer.parseInt(point));
		
		int n = pdao.productInsert(pvo); // jsp_product 테이블에 insert 하기
		int m = 0;
		
		String str_attachCount = mtreq.getParameter("attachCount");
		if(!"".equals(str_attachCount)) {
			int attachCount = Integer.parseInt(str_attachCount);
			
			for(int i=0; i<attachCount; i++) {
				String attachFilename = mtreq.getFilesystemName("attach"+i);
				
				m = pdao.product_imagefile_Insert(pnum, attachFilename);
				
				if(m == 0) break;
				
			}// end of for--------------------------------
		} // end of if
		
		String msg = "";
		String loc = "";
		
		if(n*m == 1) {
			msg = "제품등록 성공!!";
			loc = req.getContextPath()+"/mallHome.do";
		}
		else {
			msg = "제품등록 실패!!";
			loc = req.getContextPath()+"/admin/productRegister.do";
		}
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/msg.jsp");
		
	}// end of public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

}
