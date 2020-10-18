package com.skilldistillery.film.dao;

import java.util.List;

import com.skilldistillery.film.entities.Actor;
import com.skilldistillery.film.entities.Film;

public interface DatabaseAccessor {
	public Film findFilmById(int filmId);
	  public Actor findActorById(int actorId);
	  public List<Actor> findActorsByFilmId(int filmId);
	  public List<Film> searchFilms(String keyword);
	  public void close();
	boolean deleteFilm(int filmID);
	Film createFilm(Film film);
	Film saveFilmAllFields(int filmID, Film film);

}
