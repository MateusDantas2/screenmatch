package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.domain.entity.Episodio;
import br.com.alura.screenmatch.domain.entity.Serie;
import br.com.alura.screenmatch.domain.model.DadosSerie;
import br.com.alura.screenmatch.domain.model.DadosTemporada;
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
                    4 - Buscar série por título
                    5 - Buscar séries por ator
                    6 - Top 5 Séries
                    
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
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarTop5Series();
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

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(serieName);

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

    private void buscarSeriePorTitulo() {
        System.out.println("Digite o título da série: ");
        String nomeSerie = leitura.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("Dados da série: " + serieBuscada.get());
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Qual o nome para busca?");
        String nomeAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de qual valor?");
        Double avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
        seriesEncontradas.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> serieTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        serieTop.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao())
        );
    }
}