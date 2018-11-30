package myshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import myshop.model.ProductDAO;
import myshop.model.ProductVO;

public class MallByCategoryAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// *** 카테고리 목록을 가져와서 왼쪽 로그인폼 아래에 보여주도록 한다 ***
		super.getCategoryList(req);
	
		String code = req.getParameter("code");
		
		ProductDAO pdao = new ProductDAO();
		
		List<ProductVO> prodByCategoryList = pdao.getProductsByCategory(code);
		String cname = pdao.getCnameByCtgcode(code);
		//left join을 쓸 경우 hashmap을 이용하여 리스트를 만든다.
		//null이면 에러페이지로 이동한다. jsp에서 cname은 리스트.get(0).get("cname")으로 넘겨준다.  
		/* 선생님 답안
		List<HashMap<String, String>> prodByCategoryList = pdao.getProductsByCategory(code);
		
		if(prodByCategoryList == null) {
           req.setAttribute("str_pnum", code);

           super.setRedirect(false);
           super.setViewPage("/WEB-INF/myshop/errorNotProduct.jsp");
           return;
		}
		
		req.setAttribute("prodByCategoryList", prodByCategoryList);
		req.setAttribute("cname", prodByCategoryList.get(0).get("CNAME")); 
   	
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/mallByCategory.jsp");  
		
		================================================================= mallByCategory.jsp
		<script>
		// alert("${prodByCategoryList.get(0).PNAME}");
		</script>
    
		<h2>::: 쇼핑몰 상품 :::</h2>    
		<br/>

		<!-- HIT 상품 디스플레이 하기 -->
		<table style="width: 90%; border: 0px solid gray; margin-bottom: 30px;" >
			<thead>
				<tr>
					<th colspan="4" class="th" style="font-size: 12pt; background-color: #e1e1d0; height: 30px; text-align:center;">- ${cname} -</th>
				</tr>
				<tr>
					<th colspan="4" class="th">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
			<c:if test="${prodByCategoryList.get(0).PNAME == null}">
				<tr>
					<td colspan="4" class="td" align="center">현재 상품 준비중....</td>
				</tr>
			</c:if>	
			
			<c:if test="${prodByCategoryList.get(0).PNAME != null}">
				<tr>
					<c:forEach var="prodmap" items="${prodByCategoryList}" varStatus="status" >
					   <%--
					         varStatus 는 반복문의 상태정보를 알려주는 attribute(어트리뷰트) 이다.
					         status.index 는 0부터 시작하여 1씩 증가한다.
					         status.count 는 1부터 시작하여 1씩 증가한다. status.count 이 반복횟수를 알려주는 것이다. 
					   --%>  
					   <td class="td" align="center">
						 <a href="<%= request.getContextPath() %>/prodView.do?pnum=${prodmap.PNUM}">
						   <img width="120px;" height="130px;" src="images/${prodmap.PIMAGE1}">
						 </a>
						 <br/>
						 <br/><span style="background-color: navy; color: white;">${prodmap.PSPEC}</span> 
						 <br/>${prodmap.PNAME}
						 <br/><span style="text-decoration: line-through;"><fmt:formatNumber value="${prodmap.PRICE}" pattern="###,###" />원</span>
						 <br/><span style="color: red; font-weight: bold;"><fmt:formatNumber value="${prodmap.SALEPRICE}" pattern="###,###" />원</span>
						 <br/><span style="color: blue; font-weight: bold;">[${prodmap.PERCENT}% 할인]</span>
						 <br/><span style="color: orange;">${prodmap.POINT} POINT</span>
					   </td> 

		
		*/
		if(cname == null) {
			
			req.setAttribute("str_pnum", code);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/errorNotProduct.jsp");
		
			return;
		}
		
		req.setAttribute("prodByCategoryList", prodByCategoryList);
		req.setAttribute("cname", cname);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/mallByCategory.jsp");
		
	}

}
