package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.model.api.TranslationData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;

public class ConsultationMyMemory {
    private static final String URL = "https://api.mymemory.translated.net/get?q=";
    private static final String URL_ADDON = "&langpair=";

    public static String getTranslation(String text) {
        ObjectMapper mapper = new ObjectMapper();
        ConsumoApi consumer = new ConsumoApi();

        String textSend = URLEncoder.encode(text);
        String langpair = URLEncoder.encode("en|pt-br");

        String url = URL + textSend + URL_ADDON + langpair;

        String json = consumer.obterDados(url);

        TranslationData translationData;
        try {
            translationData = mapper.readValue(json, TranslationData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return translationData.responseData().translatedText();
    }
}
