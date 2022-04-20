package comment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CommentDAO {

	private Connection conn;
	private ResultSet rs;
	
	public CommentDAO() { //실제로 mysql에 접속할 수 있도록 해주는 부분
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
		String SQL ="SELECT NOW()";//현재 시간을 가져오는 mysql문장
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
//-------------------------------------------
	public int write(String commentContent, String userID, int bbsID) {
		String SQL="insert into COMMENT VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setString(1, commentContent);
			pstmt.setInt(2, getNext());
			pstmt.setString(3, userID);
			pstmt.setInt(4, 1);
			pstmt.setString(5, getDate());
			pstmt.setInt(6, bbsID);
			return pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;//데이터베이스 오류
	}
//--------------------------
	public int getNext() { // "댓글 번호" 카운트 업업        // 마지막 게시물 반환?
		String SQL ="SELECT commentID FROM COMMENT ORDER BY commentID DESC"; 
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // 첫 번째 댓글인 경우, return 1을 하여 위치를 알려줌
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
//================================================
	public ArrayList<Comment> getList(int bbsID){//특정한 리스트를 받아서 반환
		String SQL="SELECT * from comment where bbsID = ? AND commentAvailable = 1 order by bbsID desc limit 10";//마지막 게시물 반환, 삭제가 되지 않은 글만 가져온다.
		ArrayList<Comment> list = new ArrayList<Comment>();
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				Comment comment = new Comment();
				comment.setCommentContent(rs.getString(1));
				comment.setCommentID(rs.getInt(2));
				comment.setUserID(rs.getString(3));
				comment.setCommentAvailable(rs.getInt(4));
				comment.setCommentDate(rs.getString(5));
				comment.setBbsID(rs.getInt(6));
				list.add(comment);
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;//댓글 리스트 반환
	}
//===========================
	public Comment getComment(int commentID) {//하나의 댓글 내용을 불러오는 함수
		String SQL="SELECT * from comment where commentID = ?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setInt(1, commentID);
			rs=pstmt.executeQuery();//select
			if(rs.next()) {//결과가 있다면
				Comment comment = new Comment();
				comment.setCommentContent(rs.getString(1));
				comment.setCommentID(rs.getInt(2));
				comment.setUserID(rs.getString(3));
				comment.setCommentAvailable(rs.getInt(4));
				comment.setCommentDate(rs.getString(5));
				comment.setBbsID(rs.getInt(6));
				return comment;
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
//================================================
	public int update(int bbsID, int commentID,String commentContent ) {
		String SQL="update comment set commentContent = ? where bbsID = ? and commentID = ?";//특정한 아이디에 해당하는 제목과 내용을 바꿔준다. 
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setString(1, commentContent);//물음표의 순서
			pstmt.setInt(2, bbsID);
			pstmt.setInt(3, commentID);
			return pstmt.executeUpdate();//insert,delete,update			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;//데이터베이스 오류
	}
//---------------------
	public int delete(int commentID) { //글 삭제 함수 ㅇㅇ
		String SQL ="UPDATE COMMENT SET commentAvailable = 0 WHERE commentID = ?"; // 여기서 물음표는 특정한(값)이 들어간다는 뜻
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, commentID);
			return pstmt.executeUpdate(); //반환 값 성공!
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
}
