package com.skilldistillery.film.dao;

import java.sql.SQLException;
import java.util.List;

import com.skilldistillery.film.entities.Actor;
import com.skilldistillery.film.entities.Film;

public interface DatabaseAccessor {
  static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false&serverTimezone=UTC";
  public Film findFilmById(int filmId) throws SQLException;
  public Actor findActorById(int actorId) throws SQLException;
  public List<Actor> findActorsByFilmId(int filmId) throws SQLException;
  public List<Film> findFilmByKeyword(String keyword) throws SQLException;
}
