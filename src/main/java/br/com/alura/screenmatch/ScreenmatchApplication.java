package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.Season;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.service.ConsumerAPI;
import br.com.alura.screenmatch.service.ConvertsData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumerAPI consumerAPI = new ConsumerAPI();
		String json = consumerAPI.getData("http://www.omdbapi.com/?t=supernatural&apikey=3bfc9137&");
//		System.out.println(json);
//		json = consumerAPI.getData("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);
		ConvertsData convertData = new ConvertsData();
		Serie serie = convertData.getData(json, Serie.class);
		System.out.println(serie);
		json = consumerAPI.getData("http://www.omdbapi.com/?t=supernatural&season=1&episode=2&apikey=3bfc9137&");
		Episode episode = convertData.getData(json, Episode.class);
		System.out.println(episode);

		List<Season> seasons = new ArrayList<>();

		for (int i = 1; i <= serie.totalSeasons(); i++) {
			json = consumerAPI.getData("http://www.omdbapi.com/?t=supernaturalgit&season=" + i + "&apikey=3bfc9137&");
			Season season = convertData.getData(json, Season.class);
			seasons.add(season);
		}

		seasons.forEach(System.out::println);
	}
}
