<!--HTML fields for new film creation -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Film Created</title>
</head>
<body>
	<form action="addFilm.do" method="POST">
		<input type="text" name="title" /> <br />
		<c:choose>
			<c:when test="${! empty film}">
				<h1>Film Created</h1>
				<table>
					<tr>
						<td>Film Title:</td>
						<td>${film.filmTitle}</td>
					</tr>
					<tr>
						<td>Film Description:</td>
						<td>${film.description}</td>
					</tr>
					<tr>
						<td>Release Year:</td>
						<td>${film.releaseYear}</td>
					</tr>
					<tr>
						<td>Rating:</td>
						<td>${film.rating}</td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
    No film found
  </c:otherwise>
		</c:choose>
		<br /> <input type="submit" value="Submit" /> <br />
	</form>
</body>
</html>