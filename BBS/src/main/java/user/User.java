package user;

public class User { 
	//클래스 필드
	private String userID; 
	private String userPassword;
	private String userName;
	private String userGender;
	private String userEmail;
	
	// 클래스 메소드
	public String getUserID() { //getter은 인스턴스 변수를 반환한다
		return userID;
	}
	public void setUserID(String userID) { //setter는 인스턴스 변수를 대입하거나 수정한다
		this.userID = userID;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserGender() {
		return userGender;
	}
	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	
}
