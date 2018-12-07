package myshop.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jdbc.util.AES256;
import jdbc.util.SHA256;
import member.model.MemberVO;
import my.util.MyKey;

public class ProductDAO implements InterProductDAO {
	
	private DataSource ds = null;
	// 객체변수 ds는 아파치톰캣이 제공하는 DBCP

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	AES256 aes = null;
	
	/*
		MemoDAO 생성자에서 해야할 일은 아파치톰캣이 제공하는 DBCP(DB Connection Pool) 객체인 ds를 얻어오는 것이다.
	*/
	
	public ProductDAO () {
		
		Context initContext;
		try {
			
			initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			ds = (DataSource)envContext.lookup("jdbc/myoracle");
			
			String key = MyKey.key; // 암호화, 복호화 키
			aes = new AES256(key);
		
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
	} // 기본생성자
	
	// 사용한 자원 반납하는 메소드 생성
	public void close() {
		try {
			if(rs != null) {
			   rs.close();
			   rs = null;
			}
			
			if(pstmt != null) {
			   pstmt.close();
			   pstmt = null;
			}
			
			if(conn != null) { 
				conn.close();
				conn = null;
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	} // end of close();


	//////////////////////////////////////////////////////////////////////////////////////////// 기본 설정
	
	/// *** jsp_product 테이블에서 pspec 컬럼의 값 (HIT, NEW, BEST)별로 상품 목록을 가져오는 추상 메소드 ***
	@Override
	public List<ProductVO> selectByPsepc(String pspec) throws SQLException {
	
		List<ProductVO> productList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, " + 
						 " pspec, pcontent, point, to_char(pinputdate, 'yyyy-mm-dd') as pinputdate "+
						 " from jsp_product "+
						 " where pspec = ? "+
						 " order by pnum desc ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pspec);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			while(rs.next()) {
				
				cnt ++;
				if(cnt == 1) {
					
					productList = new ArrayList<ProductVO>();
				}
				
				int pnum = rs.getInt("pnum");
				String pname = rs.getString("pname");
				String pcategory = rs.getString("pcategory_fk");
				String pcompany = rs.getString("pcompany");
				String pimage1 = rs.getString("pimage1");
				String pimage2 = rs.getString("pimage2");
				int pqty = rs.getInt("pqty");
				int price = rs.getInt("price");
				int saleprice = rs.getInt("saleprice");
				String v_pspec = rs.getString("pspec");
				String pcontent = rs.getString("pcontent");
				int point = rs.getInt("point");
				String pinputdate = rs.getString("pinputdate");
				
				ProductVO productvo = new ProductVO(pnum, pname, pcategory, pcompany, pimage1, pimage2, pqty, price, 
													saleprice, v_pspec, pcontent, point, pinputdate);
				
				productList.add(productvo);
			
			}
			
		} finally {
			close();
		}
		
		return productList;
	}

	@Override
	public ProductVO getProductOneByPnum(int pnum) throws SQLException {
		
		ProductVO productvo = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, " + 
						 " pspec, pcontent, point, to_char(pinputdate, 'yyyy-mm-dd') as pinputdate "+
						 " from jsp_product "+
						 " where pnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pnum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
			
				int v_pnum = rs.getInt("pnum");
				String pname = rs.getString("pname");
				String pcategory = rs.getString("pcategory_fk");
				String pcompany = rs.getString("pcompany");
				String pimage1 = rs.getString("pimage1");
				String pimage2 = rs.getString("pimage2");
				int pqty = rs.getInt("pqty");
				int price = rs.getInt("price");
				int saleprice = rs.getInt("saleprice");
				String v_pspec = rs.getString("pspec");
				String pcontent = rs.getString("pcontent");
				int point = rs.getInt("point");
				String pinputdate = rs.getString("pinputdate");
				
				productvo = new ProductVO(v_pnum, pname, pcategory, pcompany, pimage1, pimage2, pqty, price, 
													saleprice, v_pspec, pcontent, point, pinputdate);
			}

		} finally {
			close();
		}
		
		return productvo;
	}

	// 카테고리를 불러오자
	@Override
	public List<HashMap<String, String>> getCategory() throws SQLException {
		
		List<HashMap<String, String>> ctgList = null;
		try {
			conn = ds.getConnection();
			
			String sql = " select cnum, code, cname "+
						 " from jsp_category " +
						 " order by cnum ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			while(rs.next()) {
				
				cnt ++;
				if(cnt == 1) {
					
					ctgList = new ArrayList<HashMap<String, String>>();
				}
				
				String cnum = rs.getString("cnum");
				String code = rs.getString("code");
				String cname = rs.getString("cname");
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("cnum", cnum);
				map.put("code", code);
				map.put("cname", cname);
		
				ctgList.add(map);
			}
			
		} finally {
			close();
		}
		
		return ctgList;
	}

	@Override
	public List<HashMap<String, String>> getPspec() throws SQLException {
		
		List<HashMap<String, String>> specList = null;
		try {
			conn = ds.getConnection();
			
			String sql = " select snum, sname "+
						 " from jsp_spec " +
						 " order by snum desc ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			while(rs.next()) {
				
				cnt ++;
				if(cnt == 1) {
					
					specList = new ArrayList<HashMap<String, String>>();
				}
				
				String snum = rs.getString("snum");
				String sname = rs.getString("sname");
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("snum", snum);
				map.put("sname", sname);
		
				specList.add(map);
			}
			
		} finally {
			close();
		}
		
		return specList;
	}

	// 채번 메소드
	@Override
	public int getPnumOfProduct() throws SQLException {
		
		int pnum = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select seq_jsp_product_pnum.nextval as seq " +
						 " from dual ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			pnum = rs.getInt("seq");
			
			
		} finally {
			close();
		}
		return pnum;
	}

	@Override
	public int productInsert(ProductVO pvo) throws SQLException {
		
		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " insert into jsp_product(pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, " + 
						 "pqty, price, saleprice, pspec, pcontent, point, pinputdate) " +
						 " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, default) ";

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, pvo.getPnum());
			pstmt.setString(2, pvo.getPname());
			pstmt.setString(3, pvo.getPcategory_fk());
			pstmt.setString(4, pvo.getPcompany());
			pstmt.setString(5, pvo.getPimage1());
			pstmt.setString(6, pvo.getPimage2());
			pstmt.setInt(7, pvo.getPqty());
			pstmt.setInt(8, pvo.getPrice());
			pstmt.setInt(9, pvo.getSaleprice());
			pstmt.setString(10, pvo.getPspec());
			pstmt.setString(11, pvo.getPcontent());
			pstmt.setInt(12, pvo.getPoint());

			result = pstmt.executeUpdate();
						 	
		} finally {
			close();
		}
		return result;
	}

	@Override
	public int product_imagefile_Insert(int pnum, String attachFilename) throws SQLException {

		int result = 0;
		
		try {
			
			conn = ds.getConnection();
			String sql = " insert into jsp_product_imagefile(imgfileno, fk_pnum, imgfilename) "
					   + " values(seq_imgfileno.nextval, ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, pnum);
			pstmt.setString(2, attachFilename);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		
		return result;
	} // end of 이미지인서트

	@Override
	public List<HashMap<String, String>> getImgByPnum(int pnum) throws SQLException {
		
		List<HashMap<String, String>> imgList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select imgfileno, fk_pnum, imgfilename "+
					 	 " from jsp_product_imagefile " +
					 	 " where fk_pnum = ? " +
					 	 " order by imgfileno ";
		
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pnum);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			while(rs.next()) {
				
				cnt ++;
				if(cnt == 1) {
					
					imgList = new ArrayList<HashMap<String, String>>();
				}
				
				String imgfileno = rs.getString("imgfileno");
				String v_pnum = rs.getString("fk_pnum");
				String imgfilename = rs.getString("imgfilename");
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("imgfileno", imgfileno);
				map.put("pnum", v_pnum);
				map.put("imgfilename", imgfilename);
		
				imgList.add(map);
			}
				
		} finally {
			close();
		}
		
		return imgList;
	}

	@Override
	public List<CategoryVO> getCategoryList() throws SQLException {

		List<CategoryVO> categoryList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select cnum, code, cname " +
						 " from jsp_category" +
						 " order by cnum ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			while(rs.next()) {
				
				cnt ++;
				if(cnt == 1) {
					
					categoryList = new ArrayList<CategoryVO>();
				}
				
				int cnum = rs.getInt("cnum");
				String code = rs.getString("code");
				String cname = rs.getString("cname");
				
				CategoryVO ctgvo = new CategoryVO();
				
				ctgvo.setCnum(cnum);
				ctgvo.setCode(code);
				ctgvo.setCname(cname);
		
				categoryList.add(ctgvo);
			}
			
		} finally {
			close();
		}
		
		return categoryList;
	}

	@Override
	public List<ProductVO> getProductsByCategory(String code) throws SQLException {

		List<ProductVO> prodListByCtg = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, " + 
						 " pspec, pcontent, point, to_char(pinputdate, 'yyyy-mm-dd') as pinputdate "+
						 " from jsp_product "+
						 " where pcategory_fk = ? "+
						 " order by pnum desc ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, code);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			while(rs.next()) {
				
				cnt ++;
				if(cnt == 1) {
					
					prodListByCtg = new ArrayList<ProductVO>();
				}
				
				int pnum = rs.getInt("pnum");
				String pname = rs.getString("pname");
				String pcategorycode = rs.getString("pcategory_fk");
				String pcompany = rs.getString("pcompany");
				String pimage1 = rs.getString("pimage1");
				String pimage2 = rs.getString("pimage2");
				int pqty = rs.getInt("pqty");
				int price = rs.getInt("price");
				int saleprice = rs.getInt("saleprice");
				String pspec = rs.getString("pspec");
				String pcontent = rs.getString("pcontent");
				int point = rs.getInt("point");
				String pinputdate = rs.getString("pinputdate");
				
				ProductVO pvo = new ProductVO(pnum, pname, pcategorycode, pcompany, pimage1, pimage2, 
											  pqty, price, saleprice, pspec, pcontent, point, pinputdate);
		
				prodListByCtg.add(pvo);
			}
			
		} finally {
			close();
		}
		
		return prodListByCtg;
	}

	@Override
	public String getCnameByCtgcode(String code) throws SQLException {

		String cname = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select cname " +
						 " from jsp_category " +
						 " where code = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, code);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
			
			cname = rs.getString("cname");
			}
			
		} finally {
			close();
		}
		return cname;
	}
	
	/* 선생님 답안
		// **** jsp_jsp_product 테이블에서 카테고리코드(code)별로 제품정보를 읽어오는 메소드 생성하기 *** // 
	@Override
	public List<HashMap<String, String>> getProductsByCategory(String code) 
		throws SQLException {
		
		List<HashMap<String, String>> productList = null;
		
		try {
			 conn = ds.getConnection();

			 String sql = "select A.cname, pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, pspec, pcontent, point \n"+
					 "     , to_char(pinputdate, 'yyyy-mm-dd') as pinputdate\n"+
					 "from jsp_category A left join jsp_product B \n"+
					 "on A.code = B.pcategory_fk \n"+
					 "where A.code = ? \n"+
					 "order by pnum desc";
			 
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, code);
			 
			 rs = pstmt.executeQuery();
			 
			 int cnt = 0;
			 while(rs.next()) {
				 cnt++;
				 
				 if(cnt==1) {
					 productList = new ArrayList<HashMap<String, String>>();
				 }
				 
				 String cname = rs.getString("cname");
				 int pnum = rs.getInt("pnum");
				 String pname = rs.getString("pname");
				 String pcategory_fk = rs.getString("pcategory_fk");
				 String pcompany = rs.getString("pcompany");
				 String pimage1 = rs.getString("pimage1");
				 String pimage2 = rs.getString("pimage2");
				 int pqty = rs.getInt("pqty");
				 int price = rs.getInt("price");
				 int saleprice = rs.getInt("saleprice");
				 String pspec = rs.getString("pspec");
				 String pcontent = rs.getString("pcontent");
				 int point = rs.getInt("point");
				 String pinputdate = rs.getString("pinputdate");
				 
				 HashMap<String, String> productMap = new HashMap<String, String>(); 
				 
				 productMap.put("CNAME", cname);
				 productMap.put("PNUM", String.valueOf(pnum));
				 productMap.put("PNAME", pname);
				 productMap.put("PCATEGORY_FK", pcategory_fk);
				 productMap.put("PCOMPANY", pcompany);
				 productMap.put("PIMAGE1", pimage1);
				 productMap.put("PIMAGE2", pimage2);
				 productMap.put("PQTY", String.valueOf(pqty));
				 productMap.put("PRICE", String.valueOf(price));
				 productMap.put("SALEPRICE", String.valueOf(saleprice));
				 productMap.put("PSPEC", pspec);
				 productMap.put("PCONTENT", pcontent);
				 productMap.put("POINT", String.valueOf(point));
				 productMap.put("PINPUTDATE", pinputdate);
				 
				 productList.add(productMap);
				 
			 }// end of while-----------------------
			 
		} finally {
			close();
		}
		
		return productList;
		
	}// end of List<HashMap<String, String>> getProductsByCategory(String code)----------------
 
	*/
	
	@Override
	public int addCart(String userid, String pnum, String oqty) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			// 새제품인지 장바구니에 있는 제품 추가 등록인지 확인해야 한다.
			String sql = " select cartno " +
						 " from jsp_cart " +
						 " where status = 1 and fk_userid = ? and fk_pnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			pstmt.setString(2, pnum);
			rs = pstmt.executeQuery();
			
			if (rs.next()) { // 해당 아이디에 해당 상품번호가 이미 존재한다.
				
				int cartno = rs.getInt("cartno");
				
				sql = " update jsp_cart set oqty = oqty + ? " +
					  " where cartno = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, oqty);
				pstmt.setInt(2, cartno);
				
				result = pstmt.executeUpdate();
				
			}
			
			else {// 새제품이라면 insert
			
				sql = " insert into jsp_cart (cartno, fk_userid, fk_pnum, oqty) "+
					  " values (SEQ_JSP_CART_CARTNO.nextval, ?, ?, ?) ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userid);
				pstmt.setString(2, pnum);
				pstmt.setString(3, oqty);
				
				result = pstmt.executeUpdate();
			}
			
		} finally {
			close();
		}
		return result;
	}

	@Override
	public List<CartVO> getCartList(String userid) throws SQLException {
		
		List<CartVO> cartList = null;
		
		try {
			conn = ds.getConnection();
			

			String sql = " select A.cartno, A.fk_userid, A.fk_pnum, B.pname, \n"+
						 "        B.pcategory_fk, B.pimage1, B.price, B.saleprice, B.point, B.pqty, A.oqty, A.status\n"+
						 " from jsp_cart A join jsp_product B\n"+
						 " on A.fk_pnum = B.pnum\n"+
						 " where A.status = 1 and A.fk_userid = ?\n"+
						 " order by A.cartno desc ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			while(rs.next()) {
				
				cnt ++;
				if(cnt == 1) {
					
					cartList = new ArrayList<CartVO>();
				}
				
				int cartno = rs.getInt("cartno");
				String v_userid = rs.getString("fk_userid");
				int pnum = rs.getInt("fk_pnum");
				String pname = rs.getString("pname");
				String pcategory = rs.getString("pcategory_fk");
				String pimage1 = rs.getString("pimage1");
				int price = rs.getInt("price");
				int saleprice = rs.getInt("saleprice");
				int point = rs.getInt("point");
				int pqty = rs.getInt("pqty");
				int oqty = rs.getInt("oqty");
				int status = rs.getInt("status");
				
				CartVO cvo = new CartVO();
				ProductVO item = new ProductVO();
				
				item.setPnum(pnum);
				item.setPname(pname);
				item.setPcategory_fk(pcategory);
				item.setPimage1(pimage1);
				item.setPrice(price);
				item.setSaleprice(saleprice);
				item.setPoint(point);
				item.setPqty(pqty);
				
				// 중요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				item.setTotalPriceTotalPoint(oqty); // 총액과 얻을 수 있는 포인트를 set
				
				cvo.setCartno(cartno);
				cvo.setPnum(pnum);
				cvo.setUserid(v_userid);
				cvo.setItem(item);
				cvo.setOqty(oqty);
				cvo.setStatus(status);
				
		
				cartList.add(cvo);
			}
			
		} finally {
			close();
		}
		
		return cartList;
	}

	@Override
	public int updateDeleteCart(String cartno, String oqty) throws SQLException {
	
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			if (Integer.parseInt(oqty) == 0) { // 수량이 0이면 status 0
				
				String sql = " update jsp_cart set status = 0 " +
							 " where cartno = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, cartno);
				
				result = pstmt.executeUpdate();
				
			}
			
			// 수량이 1이상
			
			String sql = " update jsp_cart set oqty = ? " +
						 " where cartno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, oqty);
			pstmt.setString(2, cartno);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close();
		}
		return result;
	}

	@Override
	public int getSeq_jsp_order() throws SQLException{
		
		int seq = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select seq_jsp_order.nextval as seq " +
						 " from dual ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			rs.next();
			
			seq = rs.getInt("seq");
			
		}finally {
			close();
		}
		return seq;
	}
	/*	***** Transaction 처리하기 *****
	1. 주문개요 테이블 (jsp_order) 입력 (insert)
	2. 주문상세 테이블(jsp_order_detail) 입력 (insert)
	3. 구매하는 사용자 coin 컬럼의 값을 구매한 가격만큼 빼고, point는 더한다. (jsp_member update)
	4. 주문한 상품 재고량을 주문량만큼 뺀다. (jsp_prod update)
	5. 장바구니에서 주문 시 장바구니 비우기 (jsp_cart status update)
	6. ProductDAO pdao 객체에서 메소드 호출
	*/
	@Override
	public int addOrder(String odrcode, String userid, int sumtotalprice, int sumtotalpoint, String[] pnumArr,
			String[] oqtyArr, String[] salepriceArr, String[] cartnoArr) throws SQLException {
		
		int n1 = 0, n2 = 0, n3 = 0, n4 = 0, n5 = 0;
		
		try {
			conn = ds.getConnection();
			
			conn.setAutoCommit(false);
		
			// 1. 주문개요 테이블 입력
			String sql = " insert into jsp_order (odrcode, fk_userid, odrtotalPrice, odrtotalPoint) " +
						 " values(?, ?, ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, odrcode);
			pstmt.setString(2, userid);
			pstmt.setInt(3, sumtotalprice);
			pstmt.setInt(4, sumtotalpoint);
			
			n1 = pstmt.executeUpdate();
			
			if(n1 != 1) {
				conn.rollback();
				return 0;
			}
			// 2. 주문상세 테이블 입력
			for(int i=0; i<pnumArr.length; i++) {
				
				sql = " insert into jsp_order_detail (odrseqnum, fk_odrcode, fk_pnum, oqty, odrprice) " +
					  " values(seq_jsp_order_detail.nextval, ?, ?, ?, ?) ";
			
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, odrcode);
				pstmt.setString(2, pnumArr[i]);
				pstmt.setString(3, oqtyArr[i]);
				pstmt.setString(4, salepriceArr[i]);
				
				n2 = pstmt.executeUpdate();
			
				if(n2 != 1) {
					conn.rollback();
					return 0;
				}
			
			}
			
			
			// 3. 사용자 coin을 지불한 가격만큼 빼기 (update)
			
			sql = " update jsp_member set coin = coin - ?, point = point + ? " +
				  " where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sumtotalprice);
			pstmt.setInt(2, sumtotalpoint);
			pstmt.setString(3, userid);
			
			n3 = pstmt.executeUpdate();
			
			if(n3 != 1) {
				conn.rollback();
				return 0;
			}
			
			// 4. 주문한 상품 재고량을 주문량만큼 뺀다. (jsp_prod update)
			
			for(int i=0; i<pnumArr.length; i++) {
				
				sql = " update jsp_product set pqty = pqty - ? " +
					  " where pnum = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, oqtyArr[i]);
				pstmt.setString(2, pnumArr[i]);
				
				n4 = pstmt.executeUpdate();	
			
				if (n4 != 1) {
					conn.rollback();
					return 0;
				}
			}
			
			// 5. 장바구니에서 주문 시 장바구니 비우기 (jsp_cart status update)
			
			if( cartnoArr != null ) {
				for(int i=0; i<cartnoArr.length; i++) {
					sql = " update jsp_cart set status = 0 " +
						  " where cartno = ? ";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, cartnoArr[i]);
					
					n5 = pstmt.executeUpdate();
					
					if (n5 != 1) {
						conn.rollback();
						return 0;
					}
					
				} // for(int i=0; i<cartnoArr.length; i++) {
				
			} // if( cartnoArr != null ) {
			
			// ** 바로 주문 커밋
			if(cartnoArr == null && n1+n2+n3+n4 == 4) {
				conn.commit();
				return 1;
			}
			// ** 장바구니 커밋
			else if(cartnoArr != null && n1+n2+n3+n4+n5 == 5) {
				conn.commit();
				return 1;
			}
			else {
				return 0;
			}
		} finally {
			close();
		}
		
	}

	// 주문한 상품 정보
	@Override
	public List<ProductVO> getOrdProdList(String pnumes) throws SQLException {
		
		List<ProductVO> ordList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, " + 
						 " pspec, pcontent, point, to_char(pinputdate, 'yyyy-mm-dd') as pinputdate "+
						 " from jsp_product "+
						 " where pnum in (" + pnumes + ") ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt ++;
				if(cnt == 1) {
					ordList = new ArrayList<ProductVO>();
				}
				
				int v_pnum = rs.getInt("pnum");
				String pname = rs.getString("pname");
				String pcategory = rs.getString("pcategory_fk");
				String pcompany = rs.getString("pcompany");
				String pimage1 = rs.getString("pimage1");
				String pimage2 = rs.getString("pimage2");
				int pqty = rs.getInt("pqty");
				int price = rs.getInt("price");
				int saleprice = rs.getInt("saleprice");
				String pspec = rs.getString("pspec");
				String pcontent = rs.getString("pcontent");
				int point = rs.getInt("point");
				String pinputdate = rs.getString("pinputdate");
				
				ProductVO pvo = new ProductVO(v_pnum, pname, pcategory, pcompany, pimage1, pimage2, pqty, price, 
													saleprice, pspec, pcontent, point, pinputdate);				
				
				ordList.add(pvo);
			}
			
		}finally {
			close();
		}
		return ordList;
	}

	@Override
	public List<HashMap<String, String>> getOrderList(String userid) throws SQLException {
		
		List<HashMap<String, String>> orderList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select A.odrcode, A.fk_userid\n"+
						 "	     , to_char(A.odrdate, 'yyyy-mm-dd hh24:mi:ss') as odrdate\n"+
						 " 	     , B.odrseqnum, B.fk_pnum, B.oqty, B.odrprice\n"+
						 "	     , case B.deliverstatus\n"+
						 "	       when 1 then '주문완료'\n"+
						 "	 	  when 2 then '배송시작'\n"+
						 "		  when 3 then '배송완료'\n"+
						 "		  end as deliverstatus\n"+
						 "		 , C.pname, C.pimage1, C.price, C.saleprice, C.point\n"+
						 " from jsp_order A join jsp_order_detail B\n"+
						 " on A.odrcode = B.fk_odrcode\n"+
						 " join jsp_product C\n"+
						 " on B.fk_pnum = C.pnum\n"+                  
						 " where 1=1 ";	
			
			if(!"admin".equals(userid)) { // 관리자가 아닌 일반사용자로 로그인한 경우
				sql += " and A.fk_userid = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userid);
				
			}
			
			else { // 관리자로 로그인 한 경우
				
				pstmt = conn.prepareStatement(sql);
				
			}
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				
				cnt ++;
				if(cnt == 1) {
					orderList = new ArrayList<HashMap<String, String>>();
				}
				
				String odrcode = rs.getString("odrcode");
				String v_userid = rs.getString("fk_userid");
				String odrdate = rs.getString("odrdate");
				String odrseqnum = rs.getString("odrseqnum");
				String pnum = rs.getString("fk_pnum");
				String oqty = rs.getString("oqty");
				String odrprice = rs.getString("odrprice");
				String deliverstatus = rs.getString("deliverstatus");
				String pname = rs.getString("pname");
				String pimage1 = rs.getString("pimage1");
				String price = rs.getString("price");
				String saleprice = rs.getString("saleprice");
				String point = rs.getString("point");
				
				HashMap<String, String> map = new HashMap<String, String>();		
				
				map.put("odrcode", odrcode);
				map.put("userid", v_userid);
				map.put("odrdate", odrdate);
				map.put("odrseqnum", odrseqnum);
				map.put("pnum", pnum);
				map.put("oqty", oqty);
				map.put("odrprice", odrprice);
				map.put("deliverstatus", deliverstatus);
				map.put("pname", pname);
				map.put("pimage1", pimage1);
				map.put("price", price);
				map.put("saleprice", saleprice);
				map.put("point", point);
				
				orderList.add(map);
			}
						
		} finally {
			close();
		}
		return orderList;
	}

	@Override
	public MemberVO getMemberDetailByOrdcode(String odrcode) throws SQLException {
		
		MemberVO memberInfo = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3 " + 
						 "		  , POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY " + 
						 "		  , COIN, POINT, to_char(REGISTERDAY, 'yyyy-mm-dd') as REGISTERDAY, STATUS "+
						 "from jsp_member "+
						 "where userid = (select fk_userid "+
						 "				  from jsp_order "+
						 "				  where odrcode = ?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, odrcode);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {

				memberInfo = new MemberVO();
				
				int idx = rs.getInt("idx");
				String userid = rs.getString("USERID");
				String name =  rs.getString("NAME");
				String pwd = rs.getString("PWD");
				String email = aes.decrypt(rs.getString("EMAIL"));
				String hp1 = rs.getString("HP1");
				String hp2 = aes.decrypt(rs.getString("HP2"));
				String hp3 = aes.decrypt(rs.getString("HP3"));
				String post1 = rs.getString("POST1");
				String post2 = rs.getString("POST2");
				String addr1 = rs.getString("ADDR1");
				String addr2 = rs.getString("ADDR2");
				String gender = rs.getString("GENDER");
				String birthday = rs.getString("BIRTHDAY");
				int coin = rs.getInt("COIN");
				int point = rs.getInt("POINT");
				String registerday = rs.getString("REGISTERDAY");
				int status = rs.getInt("STATUS");
				
				memberInfo = new MemberVO(idx, userid, name, pwd, email, hp1, hp2, hp3, post1, post2, addr1, addr2, gender, 
																	 birthday.substring(0,4), birthday.substring(4,6), birthday.substring(6), coin, point, registerday, status);
			}	
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();	
		}finally {
			close();
		}
		return memberInfo;
	}

	@Override
	public int updateDeliverStart(String odrcodePnum, int length) throws SQLException {
		
		int result = 0;
		try {
			conn = ds.getConnection();
			
			conn.setAutoCommit(false);
			
			String sql = " update jsp_order_detail set deliverStatus = 2 "
					   + " where (fk_odrcode||'/'||fk_pnum) in ("+odrcodePnum+")";
			
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate();
			
			if(result == length) {
				conn.commit();
				result = 1;
			}
			else {
				conn.rollback();
				result = 0;
			}
		}finally {
			close();
		}
		return result;
	}

	@Override
	public int updateDeliverEnd(String odrcodePnum, int length) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			
			conn.setAutoCommit(false);
			
			String sql = " update jsp_order_detail set deliverStatus = 3 "
					   + " where (fk_odrcode||'/'||fk_pnum) in ("+odrcodePnum+")";
			
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate();
			
			if(result == length) {
				conn.commit();
				result = 1;
			}
			else {
				conn.rollback();
				result = 0;
			}
		}finally {
			close();
		}
		return result;
	}

	// *** 영수증전표(odrcode)소유주에 대한 사용자 정보를 조회해오는 메소드 생성하기 *** // 
		@Override
		public MemberVO getMemberInfoByOdrcode(String odrcode) 
			throws SQLException {
			
			MemberVO membervo = null;
			
			try {
				conn = ds.getConnection();
				// DBCP(DB Connection Pool) 객체 ds를 통해  
				// 톰캣의 context.xml 에 설정되어진 Connection 객체를 빌려오는 것이다.
				
				String sql = "select idx, userid, name, email, hp1, hp2, hp3, post1, post2, addr1, addr2 "
			              +	"      , gender, birthday, coin, point "
			              +	"      , to_char(registerday, 'yyyy-mm-dd') as registerday "
						  +	"      , status "
			              +	" from jsp_member "
						  + " where userid = (select fk_userid "  
						  + "                 from jsp_order "  
						  + "                 where odrcode = ? ) ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, odrcode);
				
				rs = pstmt.executeQuery();
				
				boolean isExists = rs.next();
				
				if(isExists) {
					int v_idx = rs.getInt("idx");
					String userid = rs.getString("userid");
					String name = rs.getString("name");
					String email = aes.decrypt(rs.getString("email"));  // 이메일을 AES256 알고리즘으로 복호화 시키기
					String hp1 = rs.getString("hp1");
					String hp2 = aes.decrypt(rs.getString("hp2"));      // 휴대폰을 AES256 알고리즘으로 복호화 시키기
					String hp3 = aes.decrypt(rs.getString("hp3"));      // 휴대폰을 AES256 알고리즘으로 복호화 시키기
					String post1 = rs.getString("post1");
					String post2 = rs.getString("post2");
					String addr1 = rs.getString("addr1");
					String addr2 = rs.getString("addr2");
					
					String gender = rs.getString("gender");
					String birthday = rs.getString("birthday");
					int coin = rs.getInt("coin");
					int point = rs.getInt("point");
					
					String registerday = rs.getString("registerday");
					int status = rs.getInt("status");
					
					membervo = new MemberVO();
					
					membervo.setIdx(v_idx);
					membervo.setUserid(userid);
					membervo.setName(name);
					membervo.setEmail(email);
					membervo.setHp1(hp1);
					membervo.setHp2(hp2);
					membervo.setHp3(hp3);
					membervo.setPost1(post1);
					membervo.setPost2(post2);
					membervo.setAddr1(addr1);
					membervo.setAddr2(addr2);
					membervo.setGender(gender);
					membervo.setBirthyyyy(birthday.substring(0, 4));
					membervo.setBirthmm(birthday.substring(4, 6));
					membervo.setBirthdd(birthday.substring(6));
					membervo.setCoin(coin);
					membervo.setPoint(point);
					membervo.setRegisterday(registerday);
					membervo.setStatus(status);
				}
				
			} catch (UnsupportedEncodingException | GeneralSecurityException e) {
				e.printStackTrace();
			} finally {
				close();
			}
			
			return membervo;
		}// end of MemberVO getMemberInfoByOdrcode(String odrcode)---------------

		// 매장지도 테이블의 모든 정보 select
		@Override
		public List<StoremapVO> getStoreMap() throws SQLException {
			
			List<StoremapVO> storemapList = null;
			
			try {
				
				conn = ds.getConnection();
				
				String sql = " select storeno, storeName, latitude, longitude, zindex, tel, addr, transport \n"+
							 " from jsp_storemap \n"+
							 " order by storeno ";
				
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				int cnt = 0;
				while(rs.next()) {
				
					cnt++;
					
					if(cnt==1)
						storemapList = new ArrayList<StoremapVO>();
					
					int storeno = rs.getInt("storeno");
					String storeName = rs.getString("storeName");
					double latitude = rs.getDouble("latitude");
					double longitude = rs.getDouble("longitude");
					int zindex = rs.getInt("zindex");
					String tel = rs.getString("tel");
					String addr = rs.getString("addr");
					String transport = rs.getString("transport");
									
					StoremapVO mapvo = new StoremapVO(storeno, storeName, latitude, longitude, zindex, tel, addr, transport);
					
					storemapList.add(mapvo);
					
				}// end of while---------------------
				
			}finally {
				close();
			}
			
			return storemapList;
		}

		@Override
		public List<HashMap<String, String>> getStoreDetail(String storeno) throws SQLException {
			
			List<HashMap<String, String>> mapList = null;
			
			try{
				conn = ds.getConnection();
				// DBCP객체 ds를 통해 context.xml에서 이미 설정된 Connection 객체를 빌려오는 것이다.
				
				String sql = " select storeno, storeName, tel, addr, transport "
						   + " from jsp_storemap  "
						   + " where storeno = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, storeno);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					mapList = new ArrayList<HashMap<String, String>>();
					
					storeno = rs.getString("storeno");
					String storeName = rs.getString("storeName");
					String tel = rs.getString("tel");
					String addr = rs.getString("addr");
					String transport = rs.getString("transport");
									
					HashMap<String, String> map = new HashMap<String, String>();
					
					map.put("storeno", storeno);
					map.put("storeName", storeName);
					map.put("tel", tel);
					map.put("addr", addr);
					map.put("transport", transport);
					
					mapList.add(map);
					
				}// end of if---------------------
				
				sql =  " select img " 
	                + " from jsp_storedetailImg "
	                + " where fk_storeno = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, storeno);
				
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
						
					String img = rs.getString("img");
													
					HashMap<String, String> map = new HashMap<String, String>();
					
					map.put("img", img);
									
					mapList.add(map);
					
				}// end of while---------------------
				
			 } finally{
				close();
			 }
			
			return mapList;
		}

		@Override
		public int totalPspecCount(String pspec) throws SQLException {
			
			int totalCount = 0;
			
			try {
				conn = ds.getConnection();
				// DBCP객체 ds를 통해 context.xml에서 이미 설정된 Connection 객체를 빌려오는 것이다.
				
				String sql = " select count (*) as CNT "
						   + " from jsp_product  "
						   + " where pspec = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, pspec);
				
				rs = pstmt.executeQuery();
				
				rs.next();
				
				totalCount = rs.getInt("CNT");
				
			} finally{
				close();
			}
			
			return totalCount;
		}

		@Override
		public List<ProductVO> getProductByPspecAppend(String pspec, int startRno, int endRno) throws SQLException {
			
			List<ProductVO> productList = null;
			try {
				conn = ds.getConnection();
				
				String sql = " select RNO, pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, pspec, pcontent, point, pinputdate "+
							 " from "+
							 " ( "+
							 " select row_number() over(order by pnum desc) as RNO, pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, "+
							 "		 pspec, pcontent, point, to_char(pinputdate, 'yyyy-mm-dd') as pinputdate "+
							 " from jsp_product "+
							 " where pspec = ? "+
							 " ) V "+
							 " where RNO between ? and ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, pspec);
				pstmt.setInt(2, startRno);
				pstmt.setInt(3, endRno);
				
				rs = pstmt.executeQuery();
				
				int cnt = 0;
				while(rs.next()) {
					cnt ++;
					if(cnt == 1) {
						productList = new ArrayList<ProductVO>();
					}
					
					int pnum = rs.getInt("pnum");
					String pname = rs.getString("pname");
					String pcategory = rs.getString("pcategory_fk");
					String pcompany = rs.getString("pcompany");
					String pimage1 = rs.getString("pimage1");
					String pimage2 = rs.getString("pimage2");
					int pqty = rs.getInt("pqty");
					int price = rs.getInt("price");
					int saleprice = rs.getInt("saleprice");
					String v_pspec = rs.getString("pspec");
					String pcontent = rs.getString("pcontent");
					int point = rs.getInt("point");
					String pinputdate = rs.getString("pinputdate");
					
					ProductVO pvo = new ProductVO(pnum, pname, pcategory, pcompany, pimage1, pimage2, pqty, price, 
														saleprice, v_pspec, pcontent, point, pinputdate);				
					
					productList.add(pvo);
				}
				
			}finally {
				close();
			}
			return productList;
		}
		
		// 좋아요 및 싫어요 카운트 알아오기
		@Override
		public HashMap<String, Integer> getLikeDislikeCnt(String pnum) throws SQLException {
			
			HashMap<String, Integer> cntmap = new HashMap<String, Integer>();
			
			try {
				conn = ds.getConnection();
				
				String sql = " select (select count(*) "+
							 "         from jsp_like "+
							 "         where pnum = ?) as likecnt "+
							 "     ,  (select count(*) "+
							 "         from jsp_dislike "+
							 "         where pnum = ?) as dislikecnt "+
							 " from dual ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, pnum);
				pstmt.setString(2, pnum);
				
				rs = pstmt.executeQuery();
				
				rs.next();
				
				int likecnt = rs.getInt("likecnt");
				int dislikecnt = rs.getInt("dislikecnt");
				
				cntmap.put("LIKECNT", likecnt);
				cntmap.put("DISLIKECNT", dislikecnt);
			
			} finally {
				close();
			}
			
			return cntmap;
		}

		@Override
		public int insertLike(String userid, String pnum) throws SQLException {
			
			int n = 0;
			
			try {
				conn = ds.getConnection();
				
				String sql = " delete from jsp_dislike where userid = ? and pnum = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userid);
				pstmt.setString(2, pnum);

				pstmt.executeUpdate();
				
				sql = " insert into jsp_like (userid, pnum) values (?,?) ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userid);
				pstmt.setString(2, pnum);
				
				n = pstmt.executeUpdate();
				
			
			} finally {
				close();
			}
			
			return n;
		}

		@Override
		public int insertDislike(String userid, String pnum) throws SQLException {
			
			int n = 0;
			
			try {
				conn = ds.getConnection();
				
				String sql = " delete from jsp_like where userid = ? and pnum = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userid);
				pstmt.setString(2, pnum);

				pstmt.executeUpdate();
				
				sql = " insert into jsp_dislike (userid, pnum) values (?,?) ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userid);
				pstmt.setString(2, pnum);
				
				n = pstmt.executeUpdate();
				
			
			} finally {
				close();
			}
			
			return n;
		}
}
