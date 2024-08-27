package br.com.alura.screenmatch.domain.model.api;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Mateus Dantas
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TranslationData(@JsonAlias(value = "responseData") ResponseData responseData) {}