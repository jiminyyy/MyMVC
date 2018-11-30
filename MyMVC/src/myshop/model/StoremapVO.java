package myshop.model;

public class StoremapVO {

	private int storeno;       // 점포no 
	private String storeName;  // 점포명
	private double latitude;   // 위도
	private double longitude;  // 경도
	private int zindex;        // 우선순위(z-index) 점포no로 사용됨.
	private String tel;        // 전화번호
	private String addr;       // 주소
	private String transport;  // 오시는길
	
	public StoremapVO() { }

	public StoremapVO(int storeno, String storeName, double latitude, double longitude, int zindex, String tel,
			String addr, String transport) {
		this.storeno = storeno;
		this.storeName = storeName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.zindex = zindex;
		this.tel = tel;
		this.addr = addr;
		this.transport = transport;
	}

	public int getStoreno() {
		return storeno;
	}

	public void setStoreno(int storeno) {
		this.storeno = storeno;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getZindex() {
		return zindex;
	}

	public void setZindex(int zindex) {
		this.zindex = zindex;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}
		
	
}
