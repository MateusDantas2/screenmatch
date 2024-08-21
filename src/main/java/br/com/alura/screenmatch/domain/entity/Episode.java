package br.com.alura.screenmatch.domain.entity;

import br.com.alura.screenmatch.domain.model.EpisodeDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Data
public class Episode {
    private Integer seasonNumber;
    private String title;
    private Integer episodeNumber;
    private Double rating;
    private LocalDate releaseDate;

    public Episode(Integer seasonNumber, EpisodeDTO episodeDTO) {
        this.seasonNumber = seasonNumber;
        this.title = episodeDTO.title();
        this.episodeNumber = episodeDTO.episode();
        try {
            this.rating = Double.parseDouble(episodeDTO.imdbRating());
        } catch (NumberFormatException e) {
            this.rating = 0.0;
        }
        try {
            this.releaseDate = LocalDate.parse(episodeDTO.released());
        } catch (DateTimeParseException e) {
            this.releaseDate = LocalDate.MIN;
        }
    }
}
