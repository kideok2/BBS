<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="bbs.Bbs" %>
<%@ page import="bbs.BbsDAO" %>
<%@ page import="comment.Comment" %>
<%@ page import="comment.CommentDAO" %><%--데이터베이스 접근 객체 가져오기 --%>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width", initial-scale="1"> <%--반응형 웹 --%>
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/custom.css">
<title>JSP 게시판 사이트</title>
</head>
<body>
	<%           // 로그인 정보를 담을 수 있게 해줌
		String userID = null;      
		if(session.getAttribute("userID") != null) {
			userID = (String)session.getAttribute("userID");
		}
		
		int bbsID = 0;
		if(request.getParameter("bbsID") != null) {
			bbsID = Integer.parseInt(request.getParameter("bbsID"));
		}
		if(bbsID == 0) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 글입니다.')");
			script.println("location.href = 'bbs.jsp'");
			script.println("</script>");
		}
		Bbs bbs = new BbsDAO().getBbs(bbsID);
	%>
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
			data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
			aria-expanded="false">
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="main.jsp">jsp 게시판 웹 사이트</a><%-- navbar-brand 로고 같은거임 --%>
		</div>
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="main.jsp">메인</a>
				<li class="active"><a href="bbs.jsp">게시판</a>
			</ul>  
			<%-- ul리스트 같은거 보여줄 때 주로 사용 --%>
			<%
				if(userID == null) {
				
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" 
					data-toggle="dropdown" role="button" aria-haspopup="true" 
					aria-expanded="false">접속하기<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="login.jsp">로그인</a></li>
						<li><a href="join.jsp">회원가입</a></li>
					</ul>
				</li>	
			</ul>
			<%
			} else {
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" 
					data-toggle="dropdown" role="button" aria-haspopup="true" 
					aria-expanded="false">회원관리<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="logoutAction.jsp">로그아웃</a></li>
					</ul>
				</li>	
			</ul>
			<%	
			}
			%>
		</div>
	</nav>
	<div class="container">
		<div class="fow">
			<table class="table table-striped" style="text-align: center; border: 1px solid #dddddd">
				<thead>
					<tr>
						<th colspan="3" style="background-color : #eeeeee; text-align: center;">게시판 글 보기</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td style="width: 20%">글 제목</td>
						<td colspan="2"><%= bbs.getBbsTitle().replaceAll(" ","&nbsp").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\n","<br>") %></td>
					</tr>
					<tr>
						<td>작성자</td>
						<td colspan="2"><%= bbs.getUserID() %></td>
					</tr>
					<tr>
						<td>작성일자</td>
						<td colspan="2"><%=bbs.getBbsDate().substring(0, 11) + bbs.getBbsDate().substring(11, 13)
							+ "시" + bbs.getBbsDate().substring(14, 16) + "분"%></td>
					</tr>
					<tr>
						<td>조회수</td>
						<td colspan="2"><%=bbs.getBbsCount() + 1%></td>
					</tr>
					<tr>
						<td>내용</td>
						<td colspan="2" style="min-height: 200px; text-align: left;"><%= bbs.getBbsContent().replaceAll(" ","&nbsp").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\n","<br>") %></td>
					</tr>
				</tbody>
			</table>
			<div class="container"> <%-- 게시판 댓글 부분 시작~ --%>
				<div class="row">
					<table class="table table-striped"
						style="text-align: center; border: 1px solid #dddddd">
						<tbody>
							<tr>
								<td align="left" bgcolor="skyblue">댓글</td>
							</tr>
							<tr>
								<%
									CommentDAO commentDAO = new CommentDAO();
									ArrayList<Comment> list = commentDAO.getList(bbsID);
									for (int i = 0; i < list.size(); i++) {
								%>
								<div class="container">
									<div class="row">
										<table class="table table-striped"
											style="text-align: center; border: 1px solid #dddddd">
											<tbody>
												<tr>
													<td align="left"><%=list.get(i).getUserID()%></td>

													<td align="right"><%=list.get(i).getCommentDate().substring(0, 11) + list.get(i).getCommentDate().substring(11, 13)
						+ "시" + list.get(i).getCommentDate().substring(14, 16) + "분"%></td>
												</tr>

												<tr>
													<td align="left"><%=list.get(i).getCommentContent()%></td>
													<td align="right"><a
														href="commentUpdate.jsp?bbsID=<%=bbsID%>&commentID=<%=list.get(i).getCommentID()%>"
														class="btn btn-warning">수정</a> <a
														onclick="return confirm('정말로 삭제하시겠습니까?')"
														href="commentDeleteAction.jsp?bbsID=<%=bbsID%>&commentID=<%=list.get(i).getCommentID()%>"
														class="btn btn-danger">삭제</a></td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
								<%
									}
								%>
							</tr>
					</table>
				</div>
			</div>
			<br>
			<div class="container">
				<div class="row">
					<form method="post" action="submitAction.jsp?bbsID=<%=bbsID%>">
						<table class="table table-bordered"
							style="text-align: center; border: 1px solid #dddddd">
							<tbody>
								<tr>
									<td align="left"><%=userID%></td>
								</tr>
								<tr>
									<td><input type="text" class="form-control"
										placeholder="댓글 쓰기" name="commentContent" maxlength="300"></td>
								</tr>
							</tbody>
						</table>
						<input type="submit" class="btn btn-success pull-right"
							value="댓글 쓰기">
					</form>
				</div>
			</div>
		</div>
	</div>	
		 <%-- 게시판 댓글 부분 ~끝 --%>
			<a href="bbs.jsp" class="btn btn-primary">목록</a>
			<%
				if(userID != null && userID.equals(bbs.getUserID())) {
			%>		
					<a href="update.jsp?bbsID=<%= bbsID %>" class="btn btn-primary">수정</a>			
					<a onclick="return confirm('정말로 삭제하시겠습니까?')" href="deleteAction.jsp?bbsID=<%= bbsID %>" class="btn btn-primary">삭제</a>			
			<%
				}
			%>
		
	
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script><%-- 가져다가 씀 --%>
	<script src="js/bootstrap.js"></script>
</body>
</html>