package ikea.imc.pam.demo.component.test.dockerized;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component
public class RestClient {
    private static final Logger log = LoggerFactory.getLogger(RestClient.class);
    private final WebClient webClient;
    private final String applicationEndpoint;

    public RestClient(
            @Value("${ikea.imc.pam.url}") String applicationEndpoint,
            @Value("${ikea.imc.pam.timeout:3000}") int applicationTimeout) {
        this.applicationEndpoint = applicationEndpoint;

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(applicationTimeout);
        factory.setReadTimeout(applicationTimeout);

        HttpClient httpClient =
                HttpClient.create()
                        .responseTimeout(Duration.ofMillis(applicationTimeout))
                        .baseUrl(applicationEndpoint);

        webClient = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }

    public ResponseEntity getStatus() {
        return execute(HttpMethod.GET, "/getStatus");
    }

    private <T> T execute(HttpMethod operation, String contextUrl) {
        return execute(operation, contextUrl, "");
    }

    private <T> T execute(HttpMethod operation, String contextUrl, Object body) {
        String url = applicationEndpoint + contextUrl;

        log.debug("Calling endpoint {}", url);
        HttpStatus data =
                webClient
                        .method(operation)
                        .uri(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(HttpStatus.class)
                        .block();
        if (operation == HttpMethod.DELETE) {
            return null;
        }

        T result = (T) data;
        log.debug("Result from call {}", result);

        return result;
    }
}
