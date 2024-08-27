package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.domain.entity.Episodio;
import br.com.alura.screenmatch.domain.model.DadosSerie;
import br.com.alura.screenmatch.domain.model.DadosTemporada;
import br.com.alura.screenmatch.domain.entity.Serie;
import br.com.alura.screenmatch.domain.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final String ADDRESS = "https://www.omdbapi.com/?t=";
    private static final String API_KEY = "&apikey=6585022c";

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();

    public Main(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ADDRESS + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome: ");
        String serieName = leitura.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(serieName.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {
            Serie seriesFound = serie.get();
            List<DadosTemporada> seasons = new ArrayList<>();

            for (int i = 1; i <= seriesFound.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ADDRESS + seriesFound.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                seasons.add(dadosTemporada);
            }
            seasons.forEach(System.out::println);

            List<Episodio> episodes = seasons.stream()
                    .flatMap(s -> s.episodios().stream()
                            .map(e -> new Episodio(e.numero(), e)))
                    .collect(Collectors.toList());

            seriesFound.setEpisodios(episodes);
            repositorio.save(seriesFound);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void listarSeriesBuscadas() {
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}