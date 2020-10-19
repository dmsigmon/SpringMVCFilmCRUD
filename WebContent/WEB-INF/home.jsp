
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
	Get Film by ID:
	<form action="searchFilmByID.do" method="GET">
		<input type="text" name="filmID" /> <br /> <input type="submit"
			value="Submit" /> <br />
	</form>

	Create Film
	<form action="createFilm.do" method="POST">
		 Add your film: <br> 
		 Enter Title: <input type="text" name="title"><br>
		 Enter Description: <input type="text" name="description"><br>
		 Enter Release Year: <input type="number" name="releaseYear"><br>
		 Enter Rating <input type="text" name="rating">
		 Enter Language ID <input type="number" name="language_id"> 
		<input type="submit" value="Add your film">
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