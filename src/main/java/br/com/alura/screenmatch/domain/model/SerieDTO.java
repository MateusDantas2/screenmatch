package br.com.alura.screenmatch.domain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SerieDTO(@JsonAlias("Title") String title, Integer totalSeasons, String imdbRating) {}