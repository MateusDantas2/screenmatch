package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.domain.entity.Serie;
import br.com.alura.screenmatch.domain.model.SeasonDTO;
import br.com.alura.screenmatch.domain.model.SerieDTO;
import br.com.alura.screenmatch.service.ConsumerAPI;
import br.com.alura.screenmatch.service.ConvertsData;

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
    private List<SerieDTO> dataSeries = new ArrayList<>();

    public void showMenu() {
        var option = -1;
        while (option != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    findWebSeries();
                    break;
                case 2:
                    findEpisodeBySeries();
                    break;
                case 3:
                    listSearchedSeries();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void findWebSeries() {
        SerieDTO data = getDataSeries();
        dataSeries.add(data);
        System.out.println(data);
    }

    private SerieDTO getDataSeries() {
        System.out.println("Digite o nome da série para busca");
        var seriesName = scanner.nextLine();
        var json = consumerAPI.getData(ADDRESS + seriesName.replace(" ", "+") + API_KEY);
        return convertData.getData(json, SerieDTO.class);
    }

    private void findEpisodeBySeries() {
        SerieDTO seriesData = getDataSeries();
        List<SeasonDTO> seasons = new ArrayList<>();

        for (int i = 1; i <= seriesData.totalTemporadas(); i++) {
            var json = consumerAPI.getData(ADDRESS + seriesData.titulo().replace(" ", "+") + SEASON + i + API_KEY);
            SeasonDTO dataSeasons = convertData.getData(json, SeasonDTO.class);
            seasons.add(dataSeasons);
        }
        seasons.forEach(System.out::println);
    }

    private void listSearchedSeries() {
        List<Serie> series;
        series = dataSeries.stream()
                .map(Serie::new)
                .toList();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
        dataSeries.forEach(System.out::println);
    }
}