package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO { //데이터 베이스의 접근객체의 약자
	
	private Connection conn; //Connection 데이터 베이스 접근하게 해주는 하나의 객체
	private PreparedStatement pstmt; //자바프로그램과 DB를 연결해주는  
	private ResultSet rs; //어떠한 정보를 담을 수 있는 하나의 객체
	
	// ctrl + shift + o 를 눌러 외부 라이브러리를 넣어준다.
	
	//생성자를 만들어준다.
	public UserDAO() { //실제로 mysql에 접속할 수 있도록 해주는 부분
		try {
			// localhost:3306은 우리 컴퓨터에 설치된 mysql 서버 자체를 의미하고 bbs는 우리가 만든 테이블 이름이다.
			String dbURL = "jdbc:mysql://localhost:3306/BBS";
			String dbID = "root"; //db계정
			String dbPassword = "root"; //db 비밀번호
			Class.forName("com.mysql.jdbc.Driver");
			//driver는 mysql에 접속할 수 있도록 도와주는 하나의 라이브러리 매개체
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch(Exception e) {
			e.printStackTrace(); //오류가 무엇인지 출력
		}
	}
//-----------------------------------------------------
	//실제로 로그인을 시도하는 함수 
	public int login(String userID, String userPassword) { // userID, userPassword 부분을 받아옴
		String SQL = "SELECT userPassword FROM USER WHERE userID = ?"; //실제 DB에 입력할 sql문이다
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword)) 
					return 1; // 로그인 성공
				else 
					return 0; // 비밀번호 불일치
			}
			return -1; // 아이디가 없음
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2; // -2는 데이터베이스 오류를 의미함
	}
//---------------------------------------------
	public int join(User user) {
		String SQL = "INSERT INTO USER VALUES(?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate(); // 해당 statement 결과를 넣을수 있게 함
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
//===========================================
	public User getUser(String userID) {//하나의 글 내용을 불러오는 함수
		String SQL="SELECT * from USER where userID = ?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setString(1, userID);//물음표
			rs=pstmt.executeQuery();//select
			if(rs.next()) {//결과가 있다면
				User user = new User();
				user.setUserID(rs.getString(1));//첫 번째 결과 값
				user.setUserPassword(rs.getString(2));
				user.setUserName(rs.getString(3));
				user.setUserGender(rs.getString(4));
				user.setUserEmail(rs.getString(5));
				return user;//6개의 항목을 user인스턴스에 넣어 반환한다.
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
//--------------------------------------- 회원 정보 수정
	public int update(String userID, String userPassword, String userName, String userGender, String userEmail ) {
		String SQL="update user set userPassword = ?, userName = ?, userGender = ?, userEmail = ? where userID = ?"; 
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setString(1, userPassword);
			pstmt.setString(2, userName);
			pstmt.setString(3, userGender);
			pstmt.setString(4, userEmail);
			pstmt.setString(5, userID);
			return pstmt.executeUpdate();		
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;//데이터베이스 오류
	}
//================================== 회원탈퇴
	public int delete(String userID) {
		String SQL="delete from user where userID = ?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;//데이터베이스 오류
	}
	
}