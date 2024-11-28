package br.com.fatec.donationhaapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class ViaCepService {



    public String getZipeCode(String cep) {

        RestTemplate restTemplate =  new RestTemplate();

        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        try {
            String response = restTemplate.getForObject(url, String.class);
            if (response != null && response.contains("erro")) {
                return "CEP inválido. Por favor, verifique e tente novamente.";
            }
            return response;
        } catch (HttpClientErrorException e) {
            return "Erro ao acessar a API do ViaCEP: " + e.getStatusCode();
        } catch (Exception e) {
            return "Ocorreu um erro inesperado: " + e.getMessage();
        }
    }
}


