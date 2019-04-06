package fr.univ.orleans.grrgrrcar.utilisateur.filters;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class SecurityInterceptor implements HandlerInterceptor {

    private List<String> urlExceptions = Arrays.asList("/api/signIn", "/api/testOK", "/api/testKO", "/api/login", "swagger-ui.html#/main-controller", "swagger-ui.html", "swagger-ui.html#");
    private boolean debugMode = false;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        if (!debugMode) {

            if (!urlExceptions.contains(httpServletRequest.getRequestURI())) {

                // On commence par vérifier si le token est valide

                String token = httpServletRequest.getHeader("Authorization");
                RestTemplate restTemplate = new RestTemplate();

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.set("Authorization", token);

                String url = "http://localhost:8080/grrgrrcar/checkToken";
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

                HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity(httpHeaders);
                // On test si success ou si ko ou erreur renvoyee par le serveur authentification
                try {
                    ResponseEntity<String> resultat = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, httpEntity, String.class);

                    if (resultat.getStatusCode().equals(HttpStatus.OK)) {

                        // Ensuite on regarde s'il y a des paramètre pseudo qui ne correspondent pas au token pour vérifier s'il y a une tentative d'usurpation
                        if (httpServletRequest.getParameterValues("pseudo") != null) {
                            System.out.println("Test usurpation pour : " + httpServletRequest.getParameterValues("pseudo")[0]);

                            url = "http://localhost:8080/grrgrrcar/checkTokenWithPseudo";
                            builder = UriComponentsBuilder.fromUriString(url)
                                    .queryParam("pseudo", httpServletRequest.getParameterValues("pseudo")[0])
                                    .queryParam("token", token);

                            httpEntity = new HttpEntity(httpHeaders);
                            try {
                                resultat = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, httpEntity, String.class);

                                if (resultat.getStatusCode().equals(HttpStatus.OK)) {
                                    System.out.println("ok connexion");
                                    return true;
                                }
                                else {
                                    System.out.println("ko connexion suite a usurpation");
                                    httpServletResponse.setStatus(HttpStatus.REQUEST_TIMEOUT.value());
                                    return false;
                                }
                            }
                            catch (Exception e) {
                                System.out.println("ko connexion suite a usurpation");
                                httpServletResponse.setStatus(HttpStatus.REQUEST_TIMEOUT.value());
                                return false;
                            }
                        }
                        else {
                            System.out.println("Pas de paramètre pseudo à vérifier");
                            System.out.println("ok connexion");
                            return true;
                        }
                    }
                    else {
                        System.out.println("ko connexion");
                        httpServletResponse.setStatus(HttpStatus.REQUEST_TIMEOUT.value());
                        return false;
                    }
                }
                catch (HttpClientErrorException e) {
                    System.out.println("ko connexion");
                    httpServletResponse.setStatus(HttpStatus.REQUEST_TIMEOUT.value());
                    return false;
                }
            }
            else {
                System.out.println("Appel service non securise OK");
                return true;
            }
        }
        else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}