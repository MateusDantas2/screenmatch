package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

/**
 * @author Mateus Dantas
 */
public class ConsultationChatGPT {
    public static String getTranslation(String text) {
        OpenAiService service = new OpenAiService("sk-_AOW5atx51OlNynyauFoGuElPomJkblkdfq01lZPVcT3BlbkFJf7rK7M3NaZVWhg-jj-7dGAa5tn2U_Pvr5UE6wzwlAA");

        CompletionRequest request = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduza para o português o texto: " + text)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var response = service.createCompletion(request);
        return response.getChoices().get(0).getText();
    }
}
