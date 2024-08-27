package br.com.alura.screenmatch.domain.repository;

import br.com.alura.screenmatch.domain.entity.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long> {}
