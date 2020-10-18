package com.skilldistillery.film.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.film.entities.Actor;
import com.skilldistillery.film.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {

	private Connection conn;

	private String url;
	private String user;
	private String pass;

	public DatabaseAccessorObject() {
		url = "jdbc:mysql://localhost:3306/sdvid";
		user = "student";
		pass = "student";

		// when constructing a DB Accessor, let's already create one connection
		// all of our methods can use this.conn (connection) to access the db without
		// recreating it every single time
		ensureConnection();
	}

	private void ensureConnection() {
		try {
			this.conn = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;
		try {
			// joined language table on ID so query returns String
			String sql = "SELECT * \n" + "FROM film \n" + "INNER JOIN language ON film.language_id = language.id \n"
					+ "WHERE film.id = ?";
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet filmResult = stmt.executeQuery();
			if (filmResult.next()) {
				// Here is our mapping of query columns to our object fields:
				int id = filmResult.getInt(1);
				String title = filmResult.getString(2);
				String desc = filmResult.getString(3);
				int rYear = filmResult.getInt(4);
				String lang = filmResult.getString("language.name");
				int rd = filmResult.getInt(7);
				Double rr = filmResult.getDouble(6);
				int length = filmResult.getInt(8);
				Double repC = filmResult.getDouble(9);
				String rating = filmResult.getString(10);
				// Get the set values as one String, then split it.
				String sp_ft_str = filmResult.getString(11);
				String[] special_features = sp_ft_str.split(",");

				// let's also query for all of the Actors for this film:
				List<Actor> actorList = findActorsByFilmId(id);

				// Create the object
				film = new Film(id, title, desc, rYear, lang, rd, rr, length, repC, rating, special_features,
						actorList);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			ensureConnection();
			e.printStackTrace();
		}
		// ...
		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {
			String sql = "SELECT * \n" + "FROM actor \n" + "WHERE actor.id = ?";
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet filmResult = stmt.executeQuery();
			if (filmResult.next()) {
				// Here is our mapping of query columns to our object fields:
				int id = filmResult.getInt(1);
				String fName = filmResult.getString(2);
				String lName = filmResult.getString(3);

				actor = new Actor(id, fName, lName); // Create the object
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			ensureConnection();
			e.printStackTrace();
		}
		// ...
		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int actorID) {
		List<Actor> actorList = new ArrayList<Actor>();
		try {
			String sql = "SELECT DISTINCT actor.id, actor.first_name, actor.last_name \n" + "FROM film_actor \n"
					+ "INNER JOIN actor ON film_actor.actor_id = actor.id \n" + "WHERE film_actor.film_id = ? \n";
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			stmt.setInt(1, actorID);
			ResultSet actorResult = stmt.executeQuery();

			while (actorResult.next()) {
				// Here is our mapping of query columns to our object fields:
				int id = actorResult.getInt(1);
				String fName = actorResult.getString(2);
				String lName = actorResult.getString(3);

				actorList.add(new Actor(id, fName, lName));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			ensureConnection();
			e.printStackTrace();
		}
		// ...
		return actorList;
	}

	public List<Film> searchFilms(String keyword) {
		List<Film> filmList = new ArrayList<Film>();
		try {
			String sql = "SELECT * \n" + "FROM film \n" + "INNER JOIN language ON film.language_id = language.id \n"
			// Searches films titles allowing for titles and descriptions
					+ "WHERE film.title LIKE ? \n" + "OR film.description LIKE ? \n"
					// Make search case-insensitive
					+ "COLLATE utf8_general_ci";
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			stmt.setString(1, "%" + keyword + "%");
			stmt.setString(2, "%" + keyword + "%");
			ResultSet filmResult = stmt.executeQuery();

			while (filmResult.next()) {
				// Here is our mapping of query columns to our object fields:
				int id = filmResult.getInt(1);
				String title = filmResult.getString(2);
				String desc = filmResult.getString(3);
				int rYear = filmResult.getInt(4);
				String lang = filmResult.getString("language.name");
				int rd = filmResult.getInt(7);
				Double rr = filmResult.getDouble(6);
				int length = filmResult.getInt(8);
				Double repC = filmResult.getDouble(9);
				String rating = filmResult.getString(10);
				// Get the set values as one String, then split it.
				String sp_ft_str = filmResult.getString(11);
				String[] special_features = sp_ft_str.split(",");

				// let's also query for all of the Actors for this film:
				List<Actor> actorList = findActorsByFilmId(id);

				Film film = new Film(); // Create the object

				film.setId(id);
				film.setTitle(title);
				film.setDescription(desc);
				film.setReleaseYear(rYear);
				film.setLanguage(lang);
				film.setRentalDuration(rd);
				film.setRentalRate(rr);
				film.setLength(length);
				film.setReplacementCost(repC);
				film.setRating(rating);
				film.setSpecialFeatures(special_features);
				film.setActors(actorList);

				filmList.add(film);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			ensureConnection();
			e.printStackTrace();
		}
		// ...
		return filmList;
	}
	
	//Added by Jess: a few methods to handle creating, deleting, and saving content in the database.
	
	@Override
	public Film createFilm(Film film) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, pass);
			conn.setAutoCommit(false); // START TRANSACTION
			String sql = "INSERT INTO film (title, description, release_year, rental_duration, length, replacement_cost, rating)\n"
					+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, film.getTitle());
			stmt.setString(2, film.getDescription());
			stmt.setInt(3, film.getReleaseYear());
			stmt.setInt(4, film.getRentalDuration());
			stmt.setInt(5, film.getLength());
			stmt.setDouble(6, film.getReplacementCost());
			stmt.setString(7, film.getRating());
			int uc = stmt.executeUpdate();
			System.out.println(uc + " film was created.");
			ResultSet keys = stmt.getGeneratedKeys();
			if (keys.next()) {
				int newFilmID = keys.getInt(1);
				film.setId(newFilmID);
			}
			conn.commit(); // COMMIT TRANSACTION
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException sqle2) {
					System.err.println("Error trying to rollback");
				}
			}
			throw new RuntimeException("Error inserting film " + film);
		}
		return film;
	}

	@Override
	public boolean deleteFilm(int filmID) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, pass);
			conn.setAutoCommit(false); // START TRANSACTION
			String sql = "DELETE FROM film WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmID);
			int updateCount = stmt.executeUpdate();
			conn.commit(); // COMMIT TRANSACTION
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException sqle2) {
					System.err.println("Error trying to rollback");
				}
			}
			return false;
		}
		return true;
	}

	@Override
	public Film saveFilmAllFields(int filmID, Film film) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, pass);
			conn.setAutoCommit(false); // START TRANSACTION
			String sql = "UPDATE film SET title = ?, description = ?, release_year = ?,"
					+ "rental_duration = ?, length = ?, replacement_cost = ?, rating = ?\n"
					+ "WHERE film.id = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, film.getTitle());
			stmt.setString(2, film.getDescription());
			stmt.setInt(3, film.getReleaseYear());
			stmt.setInt(4, film.getRentalDuration());
			stmt.setInt(5, film.getLength());
			stmt.setDouble(6, film.getReplacementCost());
			stmt.setString(7, film.getRating());
			stmt.setInt(8, filmID);
			int updateCount = stmt.executeUpdate();
			if (updateCount == 1) {
				conn.commit(); // COMMIT TRANSACTION
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback(); // ROLLBACK TRANSACTION ON ERROR
				} catch (SQLException sqle2) {
					System.err.println("Error trying to rollback");
				}
			}
			return film;
		}
		return film;
	}

}
