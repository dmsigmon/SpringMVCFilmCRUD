package com.skilldistillery.films.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.skilldistillery.film.dao.DatabaseAccessorObject;
import com.skilldistillery.film.entities.Actor;
import com.skilldistillery.film.entities.Film;

@Controller
public class FilmController {
	@Autowired
	private DatabaseAccessorObject filmDao;
	@RequestMapping(path = "index.do")
	public String goHome() {
		return "index.html";
	}
	@RequestMapping(path = "searchFilmByIDForm.do")
	public ModelAndView searchFilmByID() {
		ModelAndView mv = new ModelAndView("WEB-INF/searchFilmByIDForm.html");
		return mv;
	}
	@RequestMapping(path = "searchFilmByID.do", params = "filmID", method = RequestMethod.GET)
	public ModelAndView doSearch(int filmID) {
		ModelAndView mv = new ModelAndView();
		Film foundFilm = filmDao.findFilmById(filmID);
		List<Actor> actors = filmDao.findActorsByFilmId(filmID);
		mv.addObject("film", foundFilm);
		mv.addObject("actors", actors);
		mv.setViewName("WEB-INF/searchFilmByID.jsp");
		return mv;
	}
	@RequestMapping(path = "addFilmForm.do", method = RequestMethod.GET)
	public ModelAndView newFilmForm(@Valid Film film) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("WEB-INF/addFilmForm.jsp");
		return mv;
	}
	@RequestMapping(path = "addFilmNew.do", method = RequestMethod.POST)
	public ModelAndView newFilm(@Valid Film film, Errors errors) {
		ModelAndView mv = new ModelAndView();
		Film newFilm = filmDao.createFilm(film);
		if(newFilm == null) {
			  errors.rejectValue("title", "error.title", "Title must be 1 to 35 letters long");;
			}
		mv.addObject("film", newFilm);
		mv.setViewName("WEB-INF/searchFilmByID.jsp");
		return mv;
	}
	@RequestMapping(path = "deleteFilm.do", params = "filmID", method = RequestMethod.POST)
	public ModelAndView deleteFilmFromSearch(@RequestParam("filmID") int filmID) {
		boolean delete = filmDao.deleteFilm(filmID);
		ModelAndView mv = new ModelAndView();
		Film foundFilm = filmDao.findFilmById(filmID);
		mv.addObject("film", foundFilm);
		mv.setViewName("WEB-INF/deleteResult.jsp");
		return mv;
	}
	@RequestMapping(path = "getFilmFields.do", params = "filmID", method = RequestMethod.GET)
	public ModelAndView getFilmFields(@RequestParam("filmID") int filmID) {
		ModelAndView mv = new ModelAndView();
		Film foundFilm = filmDao.findFilmById(filmID);
		mv.addObject("film", foundFilm);
		mv.setViewName("WEB-INF/update.jsp");
		return mv;
	}
	@RequestMapping(path = "saveFilmFields.do", params = "filmID", method = RequestMethod.POST)
	public ModelAndView saveFilmFields(@RequestParam("filmID") int filmID, @Valid Film film) {
		ModelAndView mv = new ModelAndView();
		Film updatedFilm = filmDao.saveFilmAllFields(filmID, film);
		Film display = filmDao.findFilmById(filmID);
		mv.addObject("film", display);
		mv.setViewName("WEB-INF/searchFilmByID.jsp");
		return mv;
	}
	@RequestMapping(path = "searchKeyword.do", params = "keyword", method = RequestMethod.GET)
	public ModelAndView doKeywordSearch(@RequestParam("keyword") String keyword) {
		ModelAndView mv = new ModelAndView();
		List<Film> foundFilms = filmDao.searchFilms(keyword);
		mv.addObject("films", foundFilms);
		mv.setViewName("WEB-INF/keywordSearch.jsp");
		return mv;
	}
}