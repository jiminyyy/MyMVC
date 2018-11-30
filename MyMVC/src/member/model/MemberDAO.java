package member.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.*;

import javax.naming.*;
import javax.sql.*;
import java.util.*;

import jdbc.util.AES256;
import jdbc.util.SHA256;
import my.util.MyKey;

public class MemberDAO implements InterMemberDAO {
	
	private DataSource ds = null;
	// 객체변수 ds는 아파치톰캣이 제공하는 DBCP

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	AES256 aes = null;
	
	/*
		MemoDAO 생성자에서 해야할 일은 아파치톰캣이 제공하는 DBCP(DB Connection Pool) 객체인 ds를 얻어오는 것이다.
	*/
	
	public MemberDAO () {
		
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

	@Override
	public MemberVO loginOKmemberInfo(String userid, String pwd) throws SQLException {
			
		MemberVO membervo = null;
		
		try {
			conn = ds.getConnection();
			// DBCP 객체 ds를 통해 톰캣의 context.xml에 설정되어진 Connection 객체를 빌려오는 것이다.
			
			String sql = " select idx, userid, name, coin, point " +
						 " , trunc ( MONTHS_BETWEEN (sysdate, lastPwdChangeDate ) ) as pwdchangegap " +
						 " , trunc ( MONTHS_BETWEEN (sysdate, lastLoginDate ) ) as loginDategap " +
						 " from jsp_member " +
						 " where status = 1 " +
						 " and userid = ? and pwd = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			pstmt.setString(2, SHA256.encrypt(pwd));
			
			rs = pstmt.executeQuery();
			
			boolean bool = rs.next();
			
			if(bool) {
				// 회원이 존재하는 경우
				int idx = rs.getInt("idx");
				String v_userid = rs.getString("userid");
				String name = rs.getString("name");
				int coin = rs.getInt("coin");
				int point = rs.getInt("point");
				int pwdchangegap = rs.getInt("pwdchangegap");
				int logindategap = rs.getInt("logindategap");
				
				membervo = new MemberVO();
				membervo.setIdx(idx);
				membervo.setUserid(v_userid);
				membervo.setName(name);
				membervo.setCoin(coin);
				membervo.setPoint(point);
			
				// 마지막으로 암호를 변경한 날짜가 현재시각으로부터 6개월이 지낫으면 true
				if(pwdchangegap >= 6)
					membervo.setRequirePwdChange(true);
				
				if(logindategap >= 12) {
					membervo.setDormant(true);
				}
				else {
			
					// 로그인이 되었다면 로그인날짜 갱신
					sql = " update jsp_member set lastLoginDate = sysdate " +
						  " where userid = ? ";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, userid);
					pstmt.executeUpdate();
					
				}
			} // if(bool)
			else { // 미존재 혹은 탈퇴 시
				
			}
						
		} finally {
			close();
		}
		
		return membervo;
	}
	
	@Override
	public boolean idDuplicateCheck(String userid) throws SQLException {
		
		try {	
			conn = ds.getConnection();
			
			String sql = " select count(*) as CNT "
					 	+	" from jsp_member "
						+	" where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			int cnt = rs.getInt("CNT");
			
			if(cnt == 1) { // id가 중복
				return false;
			}
			else {
				return true;
			}
		}  finally {
			close();
		}
	}
	
	@Override
	public int registerMember(MemberVO membervo) throws SQLException {
		
		int result = 0;
		try {	
			conn = ds.getConnection();
			
			String sql = " insert into jsp_member(IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3, POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY, COIN, POINT, REGISTERDAY, STATUS) " + 
							" values(seq_jsp_member.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, default, default, default, default) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, membervo.getUserid());
			pstmt.setString(2, membervo.getName());
			pstmt.setString(3, SHA256.encrypt( membervo.getPwd() ));	// SHA256알고리즘으로 단방향 암호화
			pstmt.setString(4, aes.encrypt( membervo.getEmail() ));	// AES256알고리즘으로 양방향 암호화
			pstmt.setString(5, membervo.getHp1());
			pstmt.setString(6, aes.encrypt( membervo.getHp2() ));			// AES256알고리즘으로 양방향 암호화
			pstmt.setString(7, aes.encrypt( membervo.getHp3() ));			// AES256알고리즘으로 양방향 암호화
			pstmt.setString(8, membervo.getPost1());
			pstmt.setString(9, membervo.getPost2());
			pstmt.setString(10, membervo.getAddr1());
			pstmt.setString(11, membervo.getAddr2());
			pstmt.setString(12, membervo.getGender());
			pstmt.setString(13, membervo.getBirthyyyy()+membervo.getBirthmm()+membervo.getBirthdd());
			
			result = pstmt.executeUpdate();
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	} // end of public int registerMember(MemberVO membervo) throws SQLException {
	
	// 탈퇴회원 포한 총 회원 명수
	@Override
	public int getTotalCount(String searchType, String searchWord, int period) throws SQLException {
		int count = 0;
		
		try {
			conn = ds.getConnection();	
		
			String sql = " select count(*) as CNT "+
							" from jsp_member "+
							" where 1 = 1 ";
			
			if("email".equals(searchType)) 
				searchWord = aes.encrypt(searchWord);
			
			if (period == -1) { // 날짜 구간이 전체 일 때
				sql += " and " + searchType + " like '%' || ? || '%' ";

				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);

			}
			else { // 날짜 구간이 3, 10, 30, 60일 때
				sql += " and " + searchType + " like '%' || ? || '%' " + 
					   " and to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') - to_date(to_char(registerday, 'yyyy-mm-dd'), 'yyyy-mm-dd') <= ? ";

				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, period);

			}

			rs = pstmt.executeQuery();
			
			rs.next();
			
			count = rs.getInt("CNT");
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return count;
	}
	// 탈퇴회원 포한 총 회원 목록
	@Override
	public List<MemberVO> getAllMember(int sizePerPage, int currentShowPageNo, int period, String searchType, String searchWord) throws SQLException {
		
		List<MemberVO> memberList = null;
		
		try {	
			conn = ds.getConnection();	
			
			String sql	  = " select RNO, IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3 "+
							 "		, POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY "+
							 "		, COIN, POINT, REGISTERDAY, STATUS, pwdchangegap, loginDategap "+
							 " from "+
							 " ( "+
							 "	select  rownum AS RNO "+
							 "			, IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3 "+
							 "			, POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY "+
							 "			, COIN, POINT, REGISTERDAY, STATUS, pwdchangegap, loginDategap "+
							 "	from "+
							 "	( "+
							 "		select IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3 "+
							 "				, POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY "+
							 "				, COIN, POINT, to_char(REGISTERDAY, 'yyyy-mm-dd') as REGISTERDAY, STATUS "+
							 " 				, trunc ( MONTHS_BETWEEN (sysdate, lastPwdChangeDate ) ) as pwdchangegap " +
							 "				, trunc ( MONTHS_BETWEEN (sysdate, lastLoginDate ) ) as loginDategap " +
							 "		from jsp_member " +
							 " 		where 1 = 1 " ;
			
			if("email".equals(searchType)) 
				searchWord = aes.encrypt(searchWord);
			
			if(period == -1 ) {
				
				sql += " and " + searchType + " like '%' || ? || '%' " + 
						 " order by idx desc "+
			     		 "	) V "+
			     	     " ) T "+
			     		 " where T.RNO between ? and ? ";

					pstmt = conn.prepareStatement(sql);
					
					pstmt.setString(1, searchWord);
					pstmt.setInt(2, (currentShowPageNo*sizePerPage)-(sizePerPage-1) );
					pstmt.setInt(3, (currentShowPageNo*sizePerPage) );
			}
			else {
				sql +=  " and " + searchType + " like '%' || ? || '%' " + 
						" and to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') - to_date(to_char(registerday, 'yyyy-mm-dd'), 'yyyy-mm-dd') <= ? "+
						" order by idx desc "+
			     		"	 ) V "+
			     	    " ) T "+
			     		" where T.RNO between ? and ? ";
						  
				
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setString(1, searchWord);
					pstmt.setInt(2, period);
					pstmt.setInt(3, (currentShowPageNo*sizePerPage)-(sizePerPage-1) );
					pstmt.setInt(4, (currentShowPageNo*sizePerPage) );
					

			}
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt == 1)
					memberList = new ArrayList<MemberVO>();
					
				int idx = rs.getInt("IDX");
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
				int pwdchangegap = rs.getInt("pwdchangegap");
				int logindategap = rs.getInt("logindategap");
				
				MemberVO membervo = new MemberVO(idx, userid, name, pwd, email, hp1, hp2, hp3, post1, post2, addr1, addr2, gender, 
												 birthday.substring(0,4), birthday.substring(4,6), birthday.substring(6), coin, point, registerday, status);
				
				if(pwdchangegap >= 6)
					membervo.setRequirePwdChange(true);
				
				if(logindategap >= 12) {
					membervo.setDormant(true);
				}
				
				memberList.add(membervo); // 개수만큼 while문이 돌아간다
				
			}
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		}  finally {
			close();
		}
		
		return memberList;
	}
	
	@Override
	public int lastPwdChangeDateCheck(String userid) throws SQLException {
		
		int result = 0;
		
		try {               
			conn = ds.getConnection();
			// DBCP(DB Connection Pool) 객체 ds를 통해  
			// 톰캣의 context.xml 에 설정되어진 Connection 객체를 빌려오는 것이다.
			
			String sql = " select MONTHS_BETWEEN (add_months(sysdate, -6), lastPwdChangeDate ) as lastpwdchangedateDifference \n "+
							 "from jsp_member\n "+
							 " where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userid);
				
			rs = pstmt.executeQuery();
			
			rs.next();
			
			result = rs.getInt("lastpwdchangedateDifference");
			
		} finally {
			close();
		}
		
		return result;
	}

	@Override
	public List<MemberVO> getActMember(int sizePerPage, int currentShowPageNo, int period, String searchType,
			String searchWord) throws SQLException {
		
		List<MemberVO> actMemberList = null;
		
		try {	
			conn = ds.getConnection();	
			
			String sql	  = " select RNO, IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3 "+
							 "		, POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY "+
							 "		, COIN, POINT, REGISTERDAY, STATUS, pwdchangegap, loginDategap "+
							 " from "+
							 " ( "+
							 "	select  rownum AS RNO "+
							 "			, IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3 "+
							 "			, POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY "+
							 "			, COIN, POINT, REGISTERDAY, STATUS, pwdchangegap, loginDategap "+
							 "	from "+
							 "	( "+
							 "		select IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3 "+
							 "				, POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY "+
							 "				, COIN, POINT, to_char(REGISTERDAY, 'yyyy-mm-dd') as REGISTERDAY, STATUS "+
							 " 				, trunc ( MONTHS_BETWEEN (sysdate, lastPwdChangeDate ) ) as pwdchangegap " +
							 "				, trunc ( MONTHS_BETWEEN (sysdate, lastLoginDate ) ) as loginDategap " +
							 "		from jsp_member " +
							 " 		where status = 1 " ;
			
			if("email".equals(searchType)) 
				searchWord = aes.encrypt(searchWord);
			
			if(period == -1 ) {
				
				sql += " and " + searchType + " like '%' || ? || '%' " + 
						 " order by idx desc "+
			     		 "	) V "+
			     	     " ) T "+
			     		 " where T.RNO between ? and ? ";

					pstmt = conn.prepareStatement(sql);
					
					pstmt.setString(1, searchWord);
					pstmt.setInt(2, (currentShowPageNo*sizePerPage)-(sizePerPage-1) );
					pstmt.setInt(3, (currentShowPageNo*sizePerPage) );
			}
			else {
				sql +=  " and " + searchType + " like '%' || ? || '%' " + 
						" and to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') - to_date(to_char(registerday, 'yyyy-mm-dd'), 'yyyy-mm-dd') <= ? "+
						" order by idx desc "+
			     		"	 ) V "+
			     	    " ) T "+
			     		" where T.RNO between ? and ? ";
						  
				
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setString(1, searchWord);
					pstmt.setInt(2, period);
					pstmt.setInt(3, (currentShowPageNo*sizePerPage)-(sizePerPage-1) );
					pstmt.setInt(4, (currentShowPageNo*sizePerPage) );
					

			}
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt == 1)
					actMemberList = new ArrayList<MemberVO>();
					
				int idx = rs.getInt("IDX");
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
				int pwdchangegap = rs.getInt("pwdchangegap");
				int logindategap = rs.getInt("logindategap");
				
				MemberVO membervo = new MemberVO(idx, userid, name, pwd, email, hp1, hp2, hp3, post1, post2, addr1, addr2, gender, 
												 birthday.substring(0,4), birthday.substring(4,6), birthday.substring(6), coin, point, registerday, status);
				
				if(pwdchangegap >= 6)
					membervo.setRequirePwdChange(true);
				
				if(logindategap >= 12) {
					membervo.setDormant(true);
				}
				
				if(!membervo.isDormant())
				actMemberList.add(membervo); // 개수만큼 while문이 돌아간다
				
			}
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		}  finally {
			close();
		}
		
		return actMemberList;
	}

	@Override
	public int getActCount(String searchType, String searchWord, int period) throws SQLException { // 휴면, 탈퇴가 아닌 회원
		int count = 0;
		
		try {
			conn = ds.getConnection();	
		
			String sql = " select count(*) as CNT "+
							" from jsp_member " +
							" where status = 1" + 
							" and trunc ( MONTHS_BETWEEN (sysdate, lastLoginDate ) ) < 12 ";
			
			if("email".equals(searchType)) 
				searchWord = aes.encrypt(searchWord);
			
			if (period == -1) { // 날짜 구간이 전체 일 때
				sql += " and " + searchType + " like '%' || ? || '%' ";

				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);

			}
			else { // 날짜 구간이 3, 10, 30, 60일 때
				sql += " and " + searchType + " like '%' || ? || '%' " + 
					   " and to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') - to_date(to_char(registerday, 'yyyy-mm-dd'), 'yyyy-mm-dd') <= ? ";

				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, period);

			}

			rs = pstmt.executeQuery();
			
			rs.next();
			
			count = rs.getInt("CNT");
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return count;
	}

	@Override
	public int getMemberEnable(int idx) throws SQLException {

		int result = 0;
		
		try {
			conn = ds.getConnection();
			// DBCP 객체 ds를 통해 톰캣의 context.xml에 설정되어진 Connection 객체를 빌려오는 것이다.
		
			String sql = " update jsp_member set lastLoginDate = sysdate " +
						 " where idx = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, idx);
				result = pstmt.executeUpdate();
				
		} finally {
			close();
		}
		return result;
	}
	
	@Override
	public int getMemberDelete(int idx) throws SQLException {

		int result = 0;
		
		try {
			conn = ds.getConnection();
			// DBCP 객체 ds를 통해 톰캣의 context.xml에 설정되어진 Connection 객체를 빌려오는 것이다.
		
			String sql = " update jsp_member set status = 0 " +
						 " where idx = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, idx);
				result = pstmt.executeUpdate();
				
		} finally {
			close();
		}
		return result;
	}

	@Override
	public int getMemberRecovery(int idx) throws SQLException {

		int result = 0;
		
		try {
			conn = ds.getConnection();
			// DBCP 객체 ds를 통해 톰캣의 context.xml에 설정되어진 Connection 객체를 빌려오는 것이다.
		
			String sql = " update jsp_member set status = 1 " +
						 " where idx = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, idx);
				result = pstmt.executeUpdate();
				
		} finally {
			close();
		}
		return result;
	}

	@Override
	public MemberVO getMemberDetail(int idx) throws SQLException {
		
		MemberVO memberInfo = null;
		
		try {	
			
			conn = ds.getConnection();
			
			String sql	  = " select IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3\n"+
							 "		, POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY \n"+
							 "		, COIN, POINT, to_char(REGISTERDAY, 'yyyy-mm-dd') as REGISTERDAY, STATUS \n"+
							 " from jsp_member\n" +
							 " where idx = ? " ;

			pstmt = conn.prepareStatement(sql);
					
			pstmt.setInt(1, idx);

			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			if (rs.next()) {
				cnt++;
				
				if(cnt == 1)
					memberInfo = new MemberVO();
					
				idx = rs.getInt("IDX");
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
				// 기본 생성자를 호출하여 membervo.set~~ 으로 값을 넣어줄 수도 있다!!
			}
			
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		}  finally {
			close();
		}
		
		return memberInfo;
	}
	
	@Override
	public int updateMember(MemberVO membervo) throws SQLException {
		
		int result = 0;
		
		try {	
			
			conn = ds.getConnection();
			
			String sql = " update jsp_member set NAME = ? "
						 + ", PWD = ? "
						 + ", EMAIL = ? "
						 + ", HP1 = ? "
						 + ", HP2 = ? "
						 + ", HP3 = ? "
						 + ", POST1 = ? "
						 + ", POST2 = ? "
						 + ", ADDR1 = ? "
						 + ", ADDR2 = ? "
						 + ", lastPwdChangeDate = sysdate "
						 + " where IDX = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, membervo.getName());
			pstmt.setString(2, SHA256.encrypt( membervo.getPwd() ));	// SHA256알고리즘으로 단방향 암호화
			pstmt.setString(3, aes.encrypt( membervo.getEmail() ));	// AES256알고리즘으로 양방향 암호화
			pstmt.setString(4, membervo.getHp1());
			pstmt.setString(5, aes.encrypt( membervo.getHp2() ));			// AES256알고리즘으로 양방향 암호화
			pstmt.setString(6, aes.encrypt( membervo.getHp3() ));			// AES256알고리즘으로 양방향 암호화
			pstmt.setString(7, membervo.getPost1());
			pstmt.setString(8, membervo.getPost2());
			pstmt.setString(9, membervo.getAddr1());
			pstmt.setString(10, membervo.getAddr2());
			pstmt.setInt(11, membervo.getIdx());
			
			result = pstmt.executeUpdate();
		
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
		
	} // end of public int updateMember(MemberVO membervo) throws SQLException {

	@Override
	public String getUserid(String name, String mobile) throws SQLException {
		
		String userid = null;
		
		try {
			conn = ds.getConnection();
			// DBCP 객체 ds를 통해 톰캣의 context.xml에 설정되어진 Connection 객체를 빌려오는 것이다.
		
			String sql = " select userid " +
						 " from jsp_member " +
						 " where status = 1 and name = ? and hp1||hp2||hp3 = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, name);
				
				String mobilenumber = mobile.substring(0, 3);
				
				if(mobile.length() == 10) {
					mobilenumber += aes.encrypt(mobile.substring(3,6));
					mobilenumber += aes.encrypt(mobile.substring(6));
				}
				if(mobile.length() == 11) {
					mobilenumber += aes.encrypt(mobile.substring(3,7));
					mobilenumber += aes.encrypt(mobile.substring(7));
				}
				
				pstmt.setString(2, mobilenumber);
				 
				rs = pstmt.executeQuery();
				
				if (rs.next()) {
					userid = rs.getString("userid");
				}
				
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		
		return userid;
	}

	@Override
	public int isUserExists(String userid, String email) throws SQLException {
		
		int n = 0;
		
		try {
			conn = ds.getConnection();
			// DBCP 객체 ds를 통해 톰캣의 context.xml에 설정되어진 Connection 객체를 빌려오는 것이다.
		
			String sql = " select count(*) as CNT " +
						 " from jsp_member " +
						 " where status = 1 and userid = ? and email = ? ";
				
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			pstmt.setString(2, aes.encrypt(email));
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			n = rs.getInt("CNT");
				
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return n;
	}

	@Override
	public int updatePwdUser(String userid, String pwd) throws SQLException {
		
		int result = 0;
		
		try {
			conn = ds.getConnection();
			// DBCP 객체 ds를 통해 톰캣의 context.xml에 설정되어진 Connection 객체를 빌려오는 것이다.
		
			String sql = " update jsp_member set pwd = ? " +
						 " , lastpwdchangedate = sysdate " +
						 " where userid = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, aes.encrypt(pwd));
				pstmt.setString(2, userid);
				
				result = pstmt.executeUpdate();
		
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}

	@Override
	public int coinAddUpdate(String idx, int coinmoney) throws SQLException {
		
		int result = 0;
		
		System.out.println(idx);
		System.out.println(coinmoney);
		
		try {
			conn = ds.getConnection();
			// DBCP 객체 ds를 통해 톰캣의 context.xml에 설정되어진 Connection 객체를 빌려오는 것이다.
		
			String sql = " update jsp_member set coin = coin + ? " +
						 " , point = point + ? " +
						 " where idx = ? and status = 1 ";
				
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, coinmoney);
			pstmt.setInt(2, coinmoney/100 );
			pstmt.setString(3, idx);
			
			result = pstmt.executeUpdate();
	
		} finally {
			close();
		}
		
		return result;
	}
}

