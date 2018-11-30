package memo.model;

import java.sql.SQLException;
import java.util.*;

public interface InterMemoDAO {
	
	//** 메모쓰기 추상 메소드 **
	int memoInsert(MemoVO memovo) throws SQLException;
	
	//** HashMap을 사용한 메모전체 조회 추상 메소드 **
	int getTotalCount() throws SQLException;

	List<HashMap<String, String>> getAllMemo(int sizePerPage, int currentShowPageNo) throws SQLException;
	
	// 내 메모 총 개수
	int getTotalCountMemo(String userid) throws SQLException;
	
	List<MemoVO> getAllMemo(String userid, int sizePerPage, int currentShowPageNo) throws SQLException;

	// 내메모 비공개처리
	void memoBlindOpen(String[] delCheckArr, int status) throws SQLException;
	
	// 내메모 삭제
	void memoDelete(String[] delCheckArr, int status) throws SQLException;
	
}
