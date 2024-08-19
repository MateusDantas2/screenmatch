package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DataSeries;
import br.com.alura.screenmatch.service.ConsumerAPI;
import br.com.alura.screenmatch.service.ConvertsData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumerAPI consumerAPI = new ConsumerAPI();
		String json = consumerAPI.getData("http://www.omdbapi.com/?t=gilmore+girls&apikey=3bfc9137&");
//		System.out.println(json);
//		json = consumerAPI.getData("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);
		ConvertsData convertData = new ConvertsData();
		DataSeries dataSeries = convertData.getData(json, DataSeries.class);
		System.out.println(dataSeries);
	}
}
