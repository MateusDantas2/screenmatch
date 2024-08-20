package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.Season;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.service.ConsumerAPI;
import br.com.alura.screenmatch.service.ConvertsData;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        Serie serie = convertData.getData(json, Serie.class);
        System.out.println(serie);

        List<Season> seasons = new ArrayList<>();
        for (int i = 1; i <= serie.totalSeasons(); i++) {
            json = consumerAPI.getData(
                    ADDRESS + seriesName.replace(" ", "+") + SEASON + i + API_KEY
            );
            Season season = convertData.getData(json, Season.class);
            seasons.add(season);
        }
        seasons.forEach(System.out::println);
    }
}