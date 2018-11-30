package memo.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.*;
import javax.sql.*;

import jdbc.util.AES256;
import my.util.MyKey;

/*
	아파치톰캣이 제공하는 DBCP(DB Connection Pool)을 이용하여 MemoDAO 클래스를 생성한다.
*/

public class MemoDAO implements InterMemoDAO {
	
	private DataSource ds = null;
	// 객체변수 ds는 아파치톰캣이 제공하는 DBCP

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	AES256 aes = null;
	
	/*
		MemoDAO 생성자에서 해야할 일은 아파치톰캣이 제공하는 DBCP(DB Connection Pool) 객체인 ds를 얻어오는 것이다.
	*/
	
	public MemoDAO () {
		
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

	// ** 메모쓰기 메소드 생성하기 **
	@Override
	public int memoInsert(MemoVO memovo) throws SQLException {
		
		int result = 0;
		try {
			conn = ds.getConnection();
			// DBCP 객체 ds를 통해 톰캣의 context.xml에 설정되어진 Connection 객체를 빌려오는 것이다.
			
			String sql = " insert into jsp_memo(idx, fk_userid, name, msg, writedate, cip, status) "
					   + " values(jsp_memo_idx.nextval, ?, ?, ?, default, ?, default) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, memovo.getFk_userid());
			pstmt.setString(2, memovo.getName());
			pstmt.setString(3, memovo.getMsg());
			pstmt.setString(4, memovo.getCip());
			
			result = pstmt.executeUpdate();
			// 1이면 입력완료.
		} finally {
			close();
		}
		
		return result;
	
	}// end of public int memoInsert(MemoVO memovo) throws SQLException {

	@Override
	public List<HashMap<String, String>> getAllMemo(int sizePerPage, int currentShowPageNo) throws SQLException {
		
		List<HashMap<String, String>> memoList = null;

		try {
			
			conn = ds.getConnection();
			
			String sql = " select RNO, IDX, FK_USERID, NAME, MSG, WRITEDATE, CIP\n"+
						 " from\n"+
						 " (\n"+
						 "	select  rownum AS RNO\n"+
						 "			, IDX, FK_USERID, NAME, MSG, WRITEDATE, CIP\n"+
						 "	from\n"+
						 "	(\n"+
						 "		select IDX, FK_USERID, NAME, MSG, to_char(WRITEDATE, 'yyyy-mm-dd hh24:mi:ss') as WRITEDATE, CIP\n"+
						 "		from jsp_memo\n"+
						 "		where status = 1\n"+
						 "		order by idx desc\n"+
						 "	) V\n"+
						 " ) T\n"+
						 " where T.RNO between ? and ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (currentShowPageNo*sizePerPage)-(sizePerPage-1) );
			pstmt.setInt(2, (currentShowPageNo*sizePerPage) );
			
			rs = pstmt.executeQuery();

			int cnt = 0;
			while (rs.next()) {
				cnt++;
				
				if(cnt == 1)
					memoList = new ArrayList<HashMap<String, String>>();
				
				String idx = rs.getString("IDX");
				String fk_userid = rs.getString("FK_USERID");
				String name = rs.getString("NAME");
				String msg = rs.getString("MSG");
				String writedate = rs.getString("WRITEDATE");
				String cip = rs.getString("CIP");
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("idx", idx);
				map.put("fk_userid", fk_userid);
				map.put("name", name);
				map.put("msg", msg);
				map.put("writedate", writedate);
				map.put("cip", cip);
				
				memoList.add(map);
				
			}// end of while
		
		} finally {
			close();
		}
		
		return memoList;
		
	}// end of public List<HashMap<String, String>> getAllMemo() throws SQLException {
	
	@Override
	public int getTotalCount() throws SQLException {
		
		int count = 0;
		
		try {
			conn = ds.getConnection();	
		
			String sql = " select count(*) as CNT "+
							" from jsp_memo "+
							" where status = 1 ";
			

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			
			rs.next();
			
			count = rs.getInt("CNT");
		} finally {
			close();
		}
		
		return count;
	}

	@Override
	public int getTotalCountMemo(String userid) throws SQLException {
		
		int count = 0;
		
		try {
			conn = ds.getConnection();	
		
			String sql = " select count(*) as CNT "+
							" from jsp_memo "+
							" where fk_userid = ? ";
			

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);

			rs = pstmt.executeQuery();
			
			rs.next();
			
			count = rs.getInt("CNT");
		} finally {
			close();
		}
		
		return count;
	}

	@Override
	public List<MemoVO> getAllMemo(String userid, int sizePerPage, int currentShowPageNo) throws SQLException {
		
		List<MemoVO> memoList = null;

		try {
			
			conn = ds.getConnection();
			
			String sql = " select RNO, IDX, FK_USERID, NAME, MSG, WRITEDATE, CIP, STATUS \n"+
						 " from\n"+
						 " (\n"+
						 "	select  rownum AS RNO\n"+
						 "			, IDX, FK_USERID, NAME, MSG, WRITEDATE, CIP, STATUS \n"+
						 "	from\n"+
						 "	(\n"+
						 "		select IDX, FK_USERID, NAME, MSG, to_char(WRITEDATE, 'yyyy-mm-dd hh24:mi:ss') as WRITEDATE, CIP, STATUS \n"+
						 "		from jsp_memo\n"+
						 "		where fk_userid = ? \n"+
						 "		order by idx desc\n"+
						 "	) V\n"+
						 " ) T\n"+
						 " where T.RNO between ? and ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userid);
			pstmt.setInt(2, (currentShowPageNo*sizePerPage)-(sizePerPage-1) );
			pstmt.setInt(3, (currentShowPageNo*sizePerPage) );
			
			rs = pstmt.executeQuery();

			int cnt = 0;
			while (rs.next()) {
				cnt++;
				
				if(cnt == 1)
					memoList = new ArrayList<MemoVO>();
				
				int idx = rs.getInt("IDX");
				String fk_userid = rs.getString("FK_USERID");
				String name = rs.getString("NAME");
				String msg = rs.getString("MSG");
				String writedate = rs.getString("WRITEDATE");
				String cip = rs.getString("CIP");
				int status = rs.getInt("STATUS");
				
				MemoVO memovo = new MemoVO();
				memovo.setIdx(idx);
				memovo.setFk_userid(fk_userid);
				memovo.setName(name);
				memovo.setMsg(msg);
				memovo.setWritedate(writedate);
				memovo.setCip(cip);
				memovo.setStatus(status);
				
				memoList.add(memovo);
				
			}// end of while
		
		} finally {
			close();
		}
		
		return memoList;
		
	}

	@Override
	public void memoBlindOpen(String[] delCheckArr, int status) throws SQLException {

		try {
			conn = ds.getConnection();
			// DBCP 객체 ds를 통해 톰캣의 context.xml에 설정되어진 Connection 객체를 빌려오는 것이다.
			
			for(String idx : delCheckArr ) {
			
				String sql = " update jsp_memo set status = ? "
						   + " where idx = ? ";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, status);
				pstmt.setString(2, idx);
				
				pstmt.executeUpdate();
			}
			
			// 1이면 입력완료.
		} finally {
			close();
		}

	}

	@Override
	public void memoDelete(String[] delCheckArr, int status) throws SQLException {

		try {
			conn = ds.getConnection();
			// DBCP 객체 ds를 통해 톰캣의 context.xml에 설정되어진 Connection 객체를 빌려오는 것이다.
			
			// 트랜잭션처리를 해야하므로 수동커밋으로 전환
			conn.setAutoCommit(false);
			
			int sum = 0;
			
			for(String idx : delCheckArr ) {
				
				String sql = " insert into jsp_memo_delete(idx, userid, name, msg, writedate, cip, status) "
						   + " select idx, fk_userid, name, msg, writedate, cip, status "
						   + " from jsp_memo "
						   + " where idx = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, idx);
				int n1 = pstmt.executeUpdate();
				
			
				sql = " delete from jsp_memo "
				    + " where idx = ? ";
			
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, idx);
				int n2 = pstmt.executeUpdate();

				sum += (n1*n2);
				
			}
			
			if (delCheckArr.length == sum)
				conn.commit();
			else
				conn.rollback();
			
			conn.setAutoCommit(true);
			
			// 1이면 입력완료.
		} finally {
			close();
		}

	}
	
}
