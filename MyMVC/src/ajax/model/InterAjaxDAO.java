package ajax.model;

import java.sql.SQLException;
import java.util.*;

import member.model.MemberVO;

public interface InterAjaxDAO {
		
	  // === *** tbl_ajaxnews 테이블에 입력된 데이터중 오늘 날짜에 해당하는 행만 추출(select)하는 추상 메소드 *** ==== 
		List<TodayNewsVO> getNewsTitleList() throws SQLException;
		
	  // === *** 검색된 회원을 보여주는  추상 메소드 *** === //
		List<MemberVO> getSearchMembers(String searchname) throws SQLException;

	  // === *** tbl_images 테이블을 보여주는  추상 메소드 *** === //	
		List<HashMap<String,String>> getImages() throws SQLException;	
			
	  // === *** tbl_books 테이블을 보여주는  추상 메소드 *** === //  	
		List<HashMap<String,String>> getAllBooks() throws SQLException;
		
	  // === *** ID 중복 검사하기를 위한 추상 메소드 *** ===	
		int idDuplicateCheck(String userid) throws SQLException;
		
	}
