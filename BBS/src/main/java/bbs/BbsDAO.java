package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;			//외부 라이브러리로 가져올 수 있게함.
import java.sql.ResultSet; 
import java.util.ArrayList;        

public class BbsDAO {
	private Connection conn;
	private ResultSet rs;
	
	public BbsDAO() { //실제로 mysql에 접속할 수 있도록 해주는 부분
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS";
			String dbID = "root";
			String dbPassword = "root";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch(Exception e) {
			e.printStackTrace();//오류 출력
		}
	}
//=============================================	
	public String getDate() { //현재의 시간을 가져오는 함수
		String SQL ="SELECT NOW()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) { //next(): String, 다음 토큰을 문자열로 return
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; // 빈 문자열을 반환 -> 데이터베이스 오류임을 알림
	}
//===================================
	public int getNext() { // "게시글 번호" 카운트 업업
		String SQL ="SELECT bbsID FROM BBS ORDER BY bbsID DESC";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // 첫 번째 게시물인 경우, return 1을 하여 위치를 알려줌
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
//=======================================ㅇㅇ
	public int write(String bbsTitle, String userID, String bbsContent) {
		String SQL ="INSERT INTO BBS VALUES(?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			return pstmt.executeUpdate(); // 성공적으로 수행하면 0이상의 숫자 리턴
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류 (게시물 번호로 적절하지 않은 -1 리턴)
	}
//====================================== 특정한 리스트를 담아서 반환할 수 있다
	public ArrayList<Bbs> getList(int pageNumber) { // 글의 목록을 가져오는 소스 코드 (함수)      
													// SQL문 해석 이건 좀 어렵네잉 
													// bbsID가 특정한 숫자보다 작을 때 
													// bbsAvailable에 1대입함(글들이 사라있음을 의미)
													// 위에서 내림차 순으로 10개 정렬
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable  = 1 ORDER BY bbsID DESC LIMIT 10";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1,getNext() - (pageNumber - 1) * 10); //getNext 다음으로 작성될 글의 번호
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	//===============================
	public boolean nextPage(int pageNumber) { //페이징 처리를 위해서 존재하는 함수
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable  = 1";
		try {							// 여기서 물음표는 특정한(값)이 들어간다는 뜻
			PreparedStatement pstmt = conn.prepareStatement(SQL); // conn객체를 이용 SQL문장을 실행준비로 만듬
			pstmt.setInt(1,getNext() - (pageNumber - 1) * 10); // getNext() 지금 작성된 게시글 수 +1 한 값. 
															   // ex)현재 게시글이 5개면 getNext()값은 6
			rs = pstmt.executeQuery(); // executeQuery ->반환값 ResultSet 이외엔 모두 false 
			if(rs.next()) {
				return true; //게시글 20개면 페이지 2개 , 게시글 21개면 3으로 늘어나는 것이다.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	//=============================================
	public Bbs getBbs(int bbsID) {	//하나의 글을 불러오는 함수
		String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int update(int bbsID, String bbsTitle, String bbsContent) { // 글쓰기 수정 함수
		String SQL ="UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ?"; 
		try {									// 여기서 물음표는 특정한(값)이 들어간다는 뜻
			PreparedStatement pstmt = conn.prepareStatement(SQL);//db연결해주는 함수
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
	
	public int delete(int bbsID) { //글 삭제 함수 ㅇㅇ
		String SQL ="UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?"; // 여기서 물음표는 특정한(값)이 들어간다는 뜻
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate(); //반환 값 성공!
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
}
