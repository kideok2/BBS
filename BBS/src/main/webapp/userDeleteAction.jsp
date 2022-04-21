<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO" %>
<%@ page import="user.User" %>
<%@ page import="java.io.PrintWriter" %> <%--자바스크립트 문장을 사용하기위해 씀 --%>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=UTF-8">
<title>JSP 게시판 사이트</title>
</head>
<body>
	<%
		String userID = null;
		if(session.getAttribute("userID")!=null){
			userID=(String)session.getAttribute("userID");
		}
		if(userID == null){
			PrintWriter script=response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");      // 이거 엄청 유용하다 많이 쓰임..
			script.println("location.href='login.jsp'");
			script.println("</script>");
		}
	
			UserDAO userDAO=new UserDAO();//하나의 인스턴스
			int result=userDAO.delete(userID);
			if(result == -1){//데이터 베이스 오류가 날 때
				PrintWriter script=response.getWriter();
				script.println("<script>");
				script.println("alert('회원탈퇴에 실패했습니다.')");
				script.println("history.back()");
				script.println("</script>");
			}
			else{
				session.invalidate(); // 설정된 세션의 값들을 모두 사라지도록한다. 어렵넹
				PrintWriter script=response.getWriter();
				script.println("<script>");
				script.println("alert('회원탈퇴에 성공했습니다.')");
				script.println("location.href='main.jsp'");
				script.println("</script>");
			}
	%>
</body>
</html>