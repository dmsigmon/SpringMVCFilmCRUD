
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<!-- NOTE: This is just a copy of index.html -->
<!--   see the TODO in the Film Controller for the home() method -->

<head>
<meta charset="UTF-8">
<title>Film MVC App</title>
</head>
<body>
	Get Film:
	<form action="getFilm.do" method="GET">
		<input type="text" name="filmId" /> <br /> <input type="submit"
			value="Submit" /> <br />
	</form>

	Add Film
	<a href="addFilm.jsp">Add Film</a>
	<form action="addFilm.do" method="POST">
		<input type="text" name="title" />
		<br />
		TODO: implement additional fields for addFilm controller method
		<br />
		<input type="submit"value="Submit" />
		<br />
	</form> 
	
	Search:
	<form action="searchFilms.do" method="GET">
		<input type="text" name="keyword" />
		<br />
		<input type="submit"value="Submit" />
		<br />
	</form>

</body>
</html>