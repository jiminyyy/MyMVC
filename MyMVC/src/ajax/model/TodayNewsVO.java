package ajax.model;

public class TodayNewsVO {

	private int seqtitleno;
	private String title;
	private String registerday;
	
	
	public TodayNewsVO() {
	
	}

	public TodayNewsVO(int seqtitleno, String title, String registerday) {
		super();
		this.seqtitleno = seqtitleno;
		this.title = title;
		this.registerday = registerday;
	}

	public int getSeqtitleno() {
		return seqtitleno;
	}

	public void setSeqtitleno(int seqtitleno) {
		this.seqtitleno = seqtitleno;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRegisterday() {
		return registerday;
	}

	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}
	
}
