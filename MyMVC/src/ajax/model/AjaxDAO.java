package ajax.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jdbc.util.AES256;
import member.model.MemberVO;
import my.util.MyKey;

public class AjaxDAO implements InterAjaxDAO {
	
	private DataSource ds = null;
	// 객체변수 ds는 아파치톰캣이 제공하는 DBCP

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	AES256 aes = null;
	
	/*
		MemoDAO 생성자에서 해야할 일은 아파치톰캣이 제공하는 DBCP(DB Connection Pool) 객체인 ds를 얻어오는 것이다.
	*/
	
	public AjaxDAO () {
		
		Context initContext;
		try {
			
			initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			ds = (DataSource)envContext.lookup("jdbc/myoracle");
			
			String key = MyKey.key;
			aes = new AES256(key);
		
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException  e) {
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
	
// === *** tbl_ajaxnews 테이블에 입력된 데이터중 오늘 날짜에 해당하는 행만 추출(select)하는 메소드 생성하기 *** // 
	@Override
	public List<TodayNewsVO> getNewsTitleList() 
	   throws SQLException {

		List<TodayNewsVO> todayNewsList = null;
		
		try {
			conn = ds.getConnection();
						
			String sql = " select seqtitleno "
					+ "         , case when length(title) > 22 then substr(title, 1, 20)||'..' "
					+ "           else title end as title "
					+ "         , to_char(registerday, 'yyyy-mm-dd') as registerday "
					+ "    from tbl_ajaxnews " 
					+ "    where to_char(registerday, 'yyyy-mm-dd') = to_char(sysdate, 'yyyy-mm-dd') "; 
			
			pstmt = conn.prepareStatement(sql);  
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt==1)
					todayNewsList = new ArrayList<TodayNewsVO>();
				
				int seqtitleno = rs.getInt("seqtitleno");
			    String title = rs.getString("title"); 
			    String registerday = rs.getString("registerday");
			    			    
			    TodayNewsVO todayNewsvo = new TodayNewsVO();
			    todayNewsvo.setSeqtitleno(seqtitleno);
			    todayNewsvo.setTitle(title);
			    todayNewsvo.setRegisterday(registerday);
				  
				todayNewsList.add(todayNewsvo);  
			}// end of while-----------------
			
		} finally{
			close();
		}	

		return todayNewsList;
	}// end of getNewsTitleList()-------------	

// === *** 검색된 회원을 보여주는 메소드 생성하기 *** === // 
	@Override	
	public List<MemberVO> getSearchMembers(String searchname) 
		throws SQLException {
		
		List<MemberVO> memberList = null; 
		
		try {
			conn = ds.getConnection();
			// 객체 ds 를 통해 아파치톰캣이 제공하는 DBCP(DB Connection pool)에서 생성된 커넥션을 빌려온다.
			
			String sql = "select idx, userid, name, pwd, email, hp1, hp2, hp3, post1, post2, addr1, addr2 "
		              +	"      , gender, birthday, coin, point "
		              +	"      , to_char(registerday, 'yyyy-mm-dd') as registerday "
					  +	"      , status "
		              +	" from jsp_member "
		              + " where status = 1 "
					  + " and name like '%'|| ? || '%' "
					  + " order by idx desc ";
			
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, searchname);		
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt==1)
					memberList = new ArrayList<MemberVO>();

				int idx = rs.getInt("idx");
				String userid = rs.getString("userid");
				String v_name = rs.getString("name");
				String pwd = rs.getString("pwd");
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
				
				MemberVO membervo = new MemberVO(idx, userid, v_name, pwd, email, hp1, hp2, hp3, post1, post2, addr1, addr2,  
				                                 gender, birthday.substring(0, 4), birthday.substring(4, 6), birthday.substring(6), coin, point,  
				                                 registerday, status);
			    
			    memberList.add(membervo);
			    
			}// end of while------------------------
			
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally{
			close();
		}
		
		return memberList;
	}// end of List<MemberVO> getSearchMembers(String name) ------------

	@Override
	public List<HashMap<String, String>> getImages() throws SQLException {
		
		List<HashMap<String, String>> imgList = null;
		
		try {
			conn = ds.getConnection();
						
			String sql = " select userid, name, img "
						+ " from tbl_images " 
						+ " order by userid "; 
			
			pstmt = conn.prepareStatement(sql);  
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt==1)
					imgList = new ArrayList<HashMap<String, String>>();
				
				String userid = rs.getString("userid");
			    String name = rs.getString("name"); 
			    String img = rs.getString("img");
			    			    
			    HashMap<String, String> map = new HashMap<String, String>();  
			    map.put("userid", userid);
			    map.put("name", name);
			    map.put("img", img);
				  
			    imgList.add(map);  
			}// end of while-----------------
			
		} finally{
			close();
		}	
		
		return imgList;
	}

	@Override
	public List<HashMap<String, String>> getAllBooks() throws SQLException {
		
		List<HashMap<String, String>> bookList = null;
		
		try {
			conn = ds.getConnection();
						
			String sql = " select subject, title, author "
						+ " from tbl_books " 
						+ " order by registerday desc "; 
			
			pstmt = conn.prepareStatement(sql);  
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt==1)
					bookList = new ArrayList<HashMap<String, String>>();
				
				String subject = rs.getString("subject");
			    String title = rs.getString("title"); 
			    String author = rs.getString("author");
			    			    
			    HashMap<String, String> map = new HashMap<String, String>();  
			    map.put("subject", subject);
			    map.put("title", title);
			    map.put("author", author);
				  
			    bookList.add(map);  
			    
			}// end of while-----------------
			
		} finally{
			close();
		}
		return bookList;
	}
	
	// === *** ID 중복 검사하기를 위한 메소드 생성하기 *** ===	
	@Override
	public int idDuplicateCheck(String userid) 
		throws SQLException {
		
		int n = 0;
		
		try {
			conn = ds.getConnection();
						
			String sql = " select count(*) AS CNT "
					+ "    from jsp_member " 
					+ "    where userid = ? "; 
			
			pstmt = conn.prepareStatement(sql);  
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			n = rs.getInt("CNT");
			
		} finally{
			close();
		}	
		
		return n;
	}// end of int idDuplicateCheck(String userid)---------------
	
}
