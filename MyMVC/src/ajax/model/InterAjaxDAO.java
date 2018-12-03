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
		
	// === *** 테이블 tbl_wordLargeCategory 에서 lgcategorycode 와 codename 을 가져오는(select) 추상 메소드 *** //  
	List<HashMap<String, String>> getLgcategorycode() throws SQLException;
	
	// === *** 테이블 tbl_wordLargeCategory 에서 lgcategorycode 컬럼의 값이 제일적은 1개만 가져오는 추상 메소드 *** //  
	String getMinLgcategorycode() throws SQLException;
	
	// === *** 테이블 tbl_wordMiddleCategory 에서 mdcategorycode 와 codename 을 가져오는(select) 추상 메소드 *** //  
	List<HashMap<String, String>> getMdcategorycode(String lgcategorycode) throws SQLException;
	
	// === *** tbl_wordsearchtest 테이블에 insert 해주는 추상 메소드 *** //
	int addWord(HashMap<String, String> map) throws SQLException;
	
	// === *** 검색어 입력시 글자동완성을 위해 tbl_wordsearchtest 테이블의 제목에서 해당글자가 포함된 데이터를 가져오는(select) 메소드 생성하기 *** //  
	List<String> getSearchedTitle(String searchword) throws SQLException;
	
	// === *** 검색조건에 맞는 총 게시물갯수 알아오기 *** //  
	int getTotalCountByMdcode(String mdcode) throws SQLException; 
	int getTotalCountBySearchword(String searchword) throws SQLException; 
	
	// === *** tbl_wordsearchtest 테이블에서 중분류 코드에 해당하는 글내용 또는 글제목에 해당하는 글내용을 조회해주는 추상 메소드 *** //
	List<HashMap<String, String>> getSearchContent(int sizePerPage, int currentShowPageNo, String mdcode, String word) throws SQLException;
	
	}
