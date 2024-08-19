package br.com.alura.screenmatch.impl;

public interface ConvertsDataImpl {
    <T> T getData(String json, Class<T> tClass);
}
