package fr.univ.orleans.grrgrrcar.utilisateur.gestionToken.proxyservice;

import fr.univ.orleans.grrgrrcar.utilisateur.gestionToken.proxyservice.exceptions.NoAuthenticationServerIsKnownException;
import fr.univ.orleans.grrgrrcar.utilisateur.gestionToken.proxyservice.exceptions.NoServerFoundException;
import fr.univ.orleans.grrgrrcar.utilisateur.gestionToken.proxyservice.exceptions.UnknownUserException;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static fr.univ.orleans.grrgrrcar.utilisateur.controller.GrrGrrCarController.AUTHORIZATION;

//import static services.Constantes.*;

public class ProxyService {
    private static final String LOGIN ="login" ;
    private static final String PASSWORD ="pwd" ;

    public static String getToken(String urlAuthentication, String login, String password) throws ResourceAccessException, NoAuthenticationServerIsKnownException, NoServerFoundException, UnknownUserException {

        if (urlAuthentication == null || urlAuthentication.isEmpty()) {
            throw new NoAuthenticationServerIsKnownException();
        }

        RestTemplate restTemplate = new RestTemplate();
        // headers
        HttpHeaders httpHeaders = new HttpHeaders();
        // body
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.put(LOGIN, Arrays.asList(login));
        map.put(PASSWORD,Arrays.asList(password));
        // headers + body
        HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<MultiValueMap<String,String>>(map , httpHeaders);
        // REST call for Location

        try {
            ResponseEntity<String> resultat = restTemplate.postForEntity(urlAuthentication, httpEntity, String.class);
            String token = (String)resultat.getHeaders().get(AUTHORIZATION).get(0);
            return token;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
                throw new NoServerFoundException();
            }
            if (e.getStatusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
                throw new UnknownUserException();
            }
        }
        return null;
    }
}
