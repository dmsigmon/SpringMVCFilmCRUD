package com.skilldistillery.film.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.skilldistillery.film.entities.Film;
import com.skilldistillery.film.dao.DatabaseAccessorObject;

@Controller
public class FilmProjectController {
	
	public DatabaseAccessorObject dao;
	
	public FilmProjectController() {
		dao = new DatabaseAccessorObject();
	}
	
	// TODO: This is an attempt to load the home.jsp as the index (not working)
	@RequestMapping(path="", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home.jsp");
		return mv;
	}

	@RequestMapping(path="getFilm.do", method = RequestMethod.GET)
	public ModelAndView getFilm(@RequestParam("filmId") int filmId) {
		Film film = dao.findFilmById(filmId);
		//insert if statements- if film = null render "Not Found" and return
		//http code 404
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("film.jsp");
		mv.addObject("film", film);
		return mv;
	}

	@RequestMapping(path="addFilm.do", method = RequestMethod.POST)
	public ModelAndView addFilm(@RequestParam("title") int title /* TODO: add delete fields */) {
		// call database
		Film film = new Film();
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("film.jsp");
		mv.addObject("film", film);
		return mv;
	}

	@RequestMapping(path="updateFilm.do", method = RequestMethod.POST)
	public ModelAndView updateFilm(@RequestParam("filmId") int filmId, @RequestParam("title") int title /* TODO: add update fields */) {
		// call database
		Film film = new Film();
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("film.jsp");
		mv.addObject("film", film);
		return mv;
	}
	
	@RequestMapping(path="deleteFilm.do", method = RequestMethod.DELETE)
	public ModelAndView deleteFilm(@RequestParam("filmId") int filmId) {
		// call database
		Film film = new Film();
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("film.jsp");
		mv.addObject("film", film);
		return mv;
	}

	@RequestMapping(path="searchFilms.do", method = RequestMethod.GET)
	public ModelAndView searchFilms(@RequestParam("keyword") String keyword) {
		List<Film> filmList = dao.searchFilms(keyword);
		//insert if statements- if film list size is 0 = render "No mathching searches" and return
		//http code 200
		
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("search.jsp");
		mv.addObject("keyword", keyword);
		mv.addObject("filmList", filmList);
		return mv;
	}

}