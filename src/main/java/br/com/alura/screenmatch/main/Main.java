package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.domain.entity.Episode;
import br.com.alura.screenmatch.domain.model.EpisodeDTO;
import br.com.alura.screenmatch.domain.model.SeasonDTO;
import br.com.alura.screenmatch.domain.model.SerieDTO;
import br.com.alura.screenmatch.service.ConsumerAPI;
import br.com.alura.screenmatch.service.ConvertsData;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static final String ADDRESS = "http://www.omdbapi.com/?t=";
    private static final String API_KEY = "&apikey=3bfc9137&";
    private static final String SEASON = "&season=";

    private ConsumerAPI consumerAPI = new ConsumerAPI();
    private ConvertsData convertData = new ConvertsData();
    private Scanner scanner = new Scanner(System.in);


    public void showMenu() {
        System.out.println("Digite o nome da série que deseja: ");

        String seriesName = scanner.nextLine();
        String json = consumerAPI.getData(ADDRESS + seriesName.replace(" ", "+") + API_KEY);
        SerieDTO serieDTO = convertData.getData(json, SerieDTO.class);
        System.out.println(serieDTO);

        List<SeasonDTO> seasonDTOS = new ArrayList<>();
        for (int i = 1; i <= serieDTO.totalSeasons(); i++) {
            json = consumerAPI.getData(
                    ADDRESS + seriesName.replace(" ", "+") + SEASON + i + API_KEY
            );
            SeasonDTO seasonDTO = convertData.getData(json, SeasonDTO.class);
            seasonDTOS.add(seasonDTO);
        }
        seasonDTOS.forEach(System.out::println);

        seasonDTOS.forEach(s -> s.episodeDTOS().forEach(e -> System.out.println(e.title())));

        List<EpisodeDTO> episodeDTOS = seasonDTOS.stream()
                .flatMap(s -> s.episodeDTOS().stream())
                .toList();

        System.out.println("\nTop 5 episódios!");
        episodeDTOS.stream()
                .filter(e -> !e.imdbRating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodeDTO::imdbRating)
                        .reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episode> episodes = seasonDTOS.stream()
                .flatMap(t -> t.episodeDTOS().stream()
                        .map(d -> new Episode(t.seasonNumber(), d))
                ).toList();

        episodes.forEach(System.out::println);

        System.out.println("A partir de que ano você quer visualizar os episódios? ");
        int year = scanner.nextInt();
        scanner.nextInt();

        LocalDate searchDate = LocalDate.of(year, 1, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodes.stream()
                .filter(e -> e.getReleaseDate() != LocalDate.MIN && e.getReleaseDate().isAfter(searchDate))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getSeasonNumber() +
                                " - Episódio: " + e.getTitle() +
                                " - Data Lançamento: " + e.getReleaseDate().format(formatter)
                ));
    }
}