package myshop.model;

import java.sql.*;
import java.util.*;

import member.model.MemberVO;

public interface InterProductDAO {
	
	/// *** jsp_product 테이블에서 pspec 컬럼의 값 (HIT, NEW, BEST)별로 상품 목록을 가져오는 추상 메소드 ***
	List<ProductVO> selectByPsepc(String pspec) throws SQLException;
	
	// 제품번호에 해당하는 제품을 불러오는 메소드
	ProductVO getProductOneByPnum(int pnum) throws SQLException;
	
	// 현재 존재하는 카테고리를 불러오는 메소드
	List<HashMap<String, String>> getCategory() throws SQLException;

	List<HashMap<String, String>> getPspec() throws SQLException;
	
	// 채번 메소드
	int getPnumOfProduct() throws SQLException;
	
	int productInsert(ProductVO pvo) throws SQLException;
	
	// 추가이미지  존재 시 등록하는 메소드
	int product_imagefile_Insert(int pnum, String attachFilename) throws SQLException;
	
	// 추가등록한 이미지 선택
	List<HashMap<String, String>> getImgByPnum(int pnum) throws SQLException;
	
	// 카테고리 리스트 보여주기
	List<CategoryVO> getCategoryList() throws SQLException;
	
	// 코드를 통한 카테고리의 상품 리스트
	List<ProductVO> getProductsByCategory(String code) throws SQLException;
	
	// 코드를 통해 카테고리명 받아오기
	String getCnameByCtgcode(String code) throws SQLException;
	
	// 장바구니에 상품 담기
	int addCart(String userid, String pnum, String oqty) throws SQLException;
	
	List<CartVO> getCartList(String userid) throws SQLException;
	
	int updateDeleteCart(String cartno, String oqty) throws SQLException;
	
	int getSeq_jsp_order() throws SQLException;
	
	int addOrder(String odrcode
			  	, String userid
			  	, int sumtotalprice
			  	, int sumtotalpoint
			  	, String[] pnumArr
			  	, String[] oqtyArr
			  	, String[] salepriceArr
			  	, String[] cartnoArr) throws SQLException;
	
	List<ProductVO> getOrdProdList(String pnumes) throws SQLException;
	
	List<HashMap<String, String>> getOrderList(String userid) throws SQLException;
	
	MemberVO getMemberDetailByOrdcode(String odrcode) throws SQLException;
	
	// 배송시작으로 변경해주는 추상 메소드
	int updateDeliverStart(String odrcodePnum, int length) throws SQLException;
	
	int updateDeliverEnd(String odrcodePnum, int length) throws SQLException;

	MemberVO getMemberInfoByOdrcode(String odrcode) throws SQLException;
	
	List<StoremapVO> getStoreMap() throws SQLException;
	
	List<HashMap<String,String>> getStoreDetail(String storeno) throws SQLException;
	
	// Ajax 페이징 처리를 위한 제품 갯수 알아오기
	int totalPspecCount(String pspec) throws SQLException;
	
	// ajax 더보기방식 상품정보를 설정한 단위로 잘라서 보여주는 추상메소드
	List<ProductVO> getProductByPspecAppend(String pspec,int startRno,int endRno) throws SQLException;
	
	HashMap<String, Integer> getLikeDislikeCnt(String pnum) throws SQLException;
	
	int insertLike(String userid, String pnum) throws SQLException;
	
	int insertDislike(String userid, String pnum) throws SQLException;
	
}
