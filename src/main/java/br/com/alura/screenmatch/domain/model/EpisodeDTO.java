package br.com.alura.screenmatch.domain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeDTO(
        @JsonAlias("Title") String title,
        @JsonAlias("Episode") Integer episode,
        String imdbRating,
        @JsonAlias("Released") String released
) {
}