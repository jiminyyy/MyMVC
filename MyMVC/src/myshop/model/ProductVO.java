package myshop.model;

public class ProductVO {

	private int    pnum;            // 제품번호
	private String pname;           // 제품명
	private String pcategory_fk;    // 카테고리코드
	private String pcompany;        // 제조회사명
	private String pimage1;         // 제품이미지1
	private String pimage2;         // 제품이미지2
	private int    pqty;            // 제품 재고량
	private int    price;           // 제품 정가
	private int    saleprice;       // 제품 판매가
	private String pspec;           // "HIT", "BEST", "NEW" 등의 값을 가짐 
	private String pcontent;        // 제품설명
	private int    point;           // 포인트 점수
	private String pinputdate;      // 제품입고일자
	

	// 제품판매가(할인해서 팔 것이므로)와 포인트점수 컬럼의 값은 관리자에 의해서 변경(update)될 수 있으므로 해당 제품의 판매총액과 포인트부여 총액은 판매당시의 제품판매가와 포인트점수로 구해와야 한다. 

	private int totalPrice;   // 주문량 * 제품판매가(할인해서 팔 것이므로)
	private int totalPoint;   // 주문량 * 포인트점수
	 
	public ProductVO() { }
	 
	public ProductVO(int pnum, String pname, String pcategory_fk,
			          String pcompany, String pimage1, String pimage2, int pqty,
			          int price, int saleprice, String pspec, String pcontent, int point,
			          String pinputdate) {
		
		this.pnum = pnum;
		this.pname = pname;
		this.pcategory_fk = pcategory_fk;
		this.pcompany = pcompany;
		this.pimage1 = pimage1;
		this.pimage2 = pimage2;
		this.pqty = pqty;
		this.price = price;
		this.saleprice = saleprice;
		this.pspec = pspec;
		this.pcontent = pcontent;
		this.point = point;
		this.pinputdate = pinputdate;
	}
	 
	public int getPnum() {
		return pnum;
	}

	public void setPnum(int pnum) {
		this.pnum = pnum;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPcategory_fk() {
		return pcategory_fk;
	}

	public void setPcategory_fk(String pcategory_fk) {
		this.pcategory_fk = pcategory_fk;
	}

	public String getPcompany() {
		return pcompany;
	}

	public void setPcompany(String pcompany) {
		this.pcompany = pcompany;
	}

	public String getPimage1() {
		return pimage1;
	}

	public void setPimage1(String pimage1) {
		this.pimage1 = pimage1;
	}

	public String getPimage2() {
		return pimage2;
	}

	public void setPimage2(String pimage2) {
		this.pimage2 = pimage2;
	}

	public int getPqty() {
		return pqty;
	}

	public void setPqty(int pqty) {
		this.pqty = pqty;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSaleprice() {
		return saleprice;
	}

	public void setSaleprice(int saleprice) {
		this.saleprice = saleprice;
	}

	public String getPspec() {
		return pspec;
	}

	public void setPspec(String pspec) {
		this.pspec = pspec;
	}

	public String getPcontent() {
		return pcontent;
	}

	public void setPcontent(String pcontent) {
		this.pcontent = pcontent;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getPinputdate() {
		return pinputdate;
	}

	public void setPinputdate(String pinputdate) {
		this.pinputdate = pinputdate;
	}

	public void setTotalPriceTotalPoint(int oqty) {
		 // 총판매가(실제판매가 * 주문량) 입력하기
		 totalPrice = saleprice * oqty;
		 
		 // 총포인트(제품1개당 포인트 * 주문량) 입력하기
		 totalPoint = point * oqty;
	}
	 
	public int getTotalPrice() {
		 return totalPrice;
	}
	 
	public int getTotalPoint() {
		 return totalPoint;
	}
	 
	
	// 제품의 할인률(%)을 계산해주는 메소드 생성하기 
	public int getPercent() {
		// 정가 : 실제판매가 = 100 : x
		// x = (실제판매가*100)/정가
		// 할인률 = 100 - (실제판매가*100)/정가
		// 예: 100 - (2800*100)/3000 = 6.66
		
		double per = 100 - (saleprice * 100.0)/price;
		long percent = Math.round(per);
		
		return (int)percent;
		
	}// end of getPercent()---------------
	 
	
}
