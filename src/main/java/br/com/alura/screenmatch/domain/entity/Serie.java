package br.com.alura.screenmatch.domain.entity;

import br.com.alura.screenmatch.domain.enumaration.Category;
import br.com.alura.screenmatch.domain.model.SerieDTO;
import lombok.Data;

import java.util.OptionalDouble;

/**
 * @author Mateus Dantas
 */
@Data
public class Serie {
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    private String genero;
    private String atores;
    private String poster;
    private String sinopse;

    public Serie(SerieDTO serieDTO) {
        this.titulo = serieDTO.titulo();
        this.totalTemporadas = serieDTO.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.parseDouble(serieDTO.avaliacao())).orElse(0.0);
        this.genero = String.valueOf(Category.fromString(serieDTO.genero().split(",")[0])).trim();
        this.atores = serieDTO.atores();
        this.poster = serieDTO.poster();
        this.sinopse = serieDTO.sinopse();
    }

    @Override
    public String toString() {
        return
                "genero='" + genero + '\'' +
                "titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao=" + avaliacao +
                ", atores='" + atores + '\'' +
                ", poster='" + poster + '\'' +
                ", sinopse='" + sinopse + '\'';
    }
}
