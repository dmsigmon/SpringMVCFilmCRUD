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

// where connectors driver etc
public class DatabaseAccessorObject implements DatabaseAccessor {

	public DatabaseAccessorObject() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) throws SQLException {
		Film film = null;
		String sql = "SELECT * FROM film JOIN language ON language.id = film.language_id WHERE film.id = ?";
		String user = "student";
		String pass = "student";

		Connection conn = DriverManager.getConnection(URL, user, pass);
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			film = new Film();
			film.setId(filmId);
			film.setTitle(rs.getString("title"));
			film.setDescription(rs.getString("description"));
			film.setReleaseYear(rs.getInt("release_year"));
//			film.setRentalDuration(rs.getInt("rental_duration"));
//			film.setRentalRate(rs.getDouble("rental_rate"));
//			film.setLength(rs.getInt("length"));
//			film.setReplacementCost(rs.getDouble("replacement_cost"));
			film.setRating("rating");
//			film.setSpecialFeatures("special_features");
//			film.setLanguage(rs.getString("name"));
			film.setActors(findActorsByFilmId(filmId));
		}
		conn.close();
		stmt.close();
		rs.close();
		return film;
	}

	@Override
	public Actor findActorById(int actorId) throws SQLException {
		Actor actor = null;
		String sql = "SELECT * FROM actor WHERE id = ?";
		String user = "student";
		String pass = "student";

		Connection conn = DriverManager.getConnection(URL, user, pass);
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, actorId);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			actor = new Actor();
			actor.setId(actorId);
			actor.setFirstName(rs.getString("first_name"));
			actor.setLastName(rs.getString("last_name"));

		}

		conn.close();
		stmt.close();
		rs.close();
		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) throws SQLException {
		List<Actor> actors = new ArrayList<Actor>();
		String sql = "SELECT * FROM actor JOIN film_actor ON actor.id = film_actor.actor_id WHERE film_id = ?;";
		String user = "student";
		String pass = "student";

		Connection conn = DriverManager.getConnection(URL, user, pass);
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			Actor actor = new Actor();
			actor.setId(rs.getInt("id"));
			actor.setFirstName(rs.getString("first_name"));
			actor.setLastName(rs.getString("last_name"));

			actors.add(actor);
		}

		conn.close();
		stmt.close();
		rs.close();
		return actors;

	}

	@Override
	public List<Film> findFilmByKeyword(String keyword) throws SQLException {
		List<Film> films = new ArrayList<Film>();
		String sql = "SELECT * FROM film;";
		String user = "student";
		String pass = "student";

		Connection conn = DriverManager.getConnection(URL, user, pass);
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			Film film = new Film();
			film.setId(rs.getInt("id"));
			film.setTitle(rs.getString("title").toUpperCase());
			film.setDescription(rs.getString("description").toUpperCase());
			film.setReleaseYear(rs.getInt("release_year"));
			film.setRating("rating");
			film.setActors(findActorsByFilmId(film.getId()));

			if (film.getDescription().contains(keyword) || film.getTitle().contains(keyword)) {
				films.add(film);
			}
		}

		conn.close();
		stmt.close();
		rs.close();

		return films;
	}

//Implement an createFilm() method that takes a Film object and inserts it into the database. 
//It should return the Film object, or null if the insert fails.

	public Film createFilm(Film film) {
		String user = "student";
		String pass = "student";
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(URL, user, pass);
			conn.setAutoCommit(false);
			String sql = "INSERT INTO film (title, description, releaseYear,  rating, language_id) "
					+ " VALUES (?,?,?,?)";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, film.getTitle());
			stmt.setString(2, film.getDescription());
			stmt.setInt(3, film.getReleaseYear());
			stmt.setString(4, film.getRating());
			stmt.setInt(5, film.getLanguage_id());
//			stmt.setInt(5, film.getRentalDuration());
//			stmt.setDouble(6, film.getRentalRate());
//			stmt.setInt(7, film.getLength());
//			stmt.setDouble(8, film.getReplacementCost());
//			stmt.setString(10, film.getSpecialFeatures());

			int updateCount = stmt.executeUpdate();
			if (updateCount == 1) {
				ResultSet keys = stmt.getGeneratedKeys();
				if (keys.next()) {
					int newFilmID = keys.getInt(1);
					film.setId(newFilmID);
					if (film.getActors() != null && film.getActors().size() > 0) {
						stmt = conn.prepareStatement(sql);
						sql = "INSERT INTO film_actor (film_id, actor_id) VALUES (?,?)";
						for (Actor actor : film.getActors()) {
							stmt.setInt(1, actor.getId());
							stmt.setInt(2, newFilmID);
							updateCount = stmt.executeUpdate();
						}
					}
				}
			} else {
				film = null;
			}
			conn.commit();

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
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

}