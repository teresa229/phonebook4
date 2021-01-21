<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
		<h1>전화번호 수정 화면</h1>
		<p> 수정 화면입니다.
			아래 항목을 수정하고 "수정" 버튼을 클릭하세요
		</p>
		
		<form action="${pageContext.request.contextPath}/phone/modify" method="get">
			이름(name):<input type="text" name="name" value="${pvo.name}"><br>   <!-- requestScope 생략가능.pvo.name 아니다. -->
			핸드폰(hp):<input type="text" name="hp" value="${pvo.hp}"><br>  
			회사(company):<input type="text" name="company" value="${pvo.company}"><br>  
			
			<!-- name= id를 personId -->
			<input type="text" name="personId" value="${pvo.personId}"> <!-- 이 데이터가 있어야 서버에서 수정을 할 수 있다. 넘어갈 id값이 없었다. 수정버튼을 눌렀을 경우를 대비하는 것이다. -->  <!--수업 pvo.getPersonId() -->
			<button type="submit">수정</button>
		</form>
		<!-- id값이 안 넘어가고 있다. -->
		 
		<br>
		<a href="${pageContext.request.contextPath}/phone/list">리스트 바로 가기</a>
	
</body>
</html>