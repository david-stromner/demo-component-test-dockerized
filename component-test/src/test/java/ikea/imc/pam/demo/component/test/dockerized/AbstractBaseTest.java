package ikea.imc.pam.demo.component.test.dockerized;

import com.github.tomakehurst.wiremock.client.WireMock;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = AbstractBaseTest.TestConfig.class)
public abstract class AbstractBaseTest {
    @Value("${ikea.imc.pam.network.port}")
    private int applicationPort;

    @Value("${ikea.imc.pam.docker.file.location}")
    private String dockerFileLocation;

    @Value("${ikea.imc.pam.docker.container.name}")
    private String applicationContainerName;

    @Value("${ikea.imc.pam.docker.standalone:false}")
    private boolean dockerStandalone;

    @Value("${ikea.imc.pam.wiremock.host}")
    private String wiremockHost;

    @Value("${ikea.imc.pam.wiremock.port}")
    private int wiremockPort;

    private DockerComposeContainer container;

    @Configuration
    @ComponentScan("ikea.imc.pam.demo.component.test.dockerized")
    public static class TestConfig {}

    @PostConstruct
    public void init() {
        WireMock.configureFor(wiremockHost, wiremockPort);
        WireMock.reset();
        if (!dockerStandalone) {
            container =
                    new DockerComposeContainer(new File(dockerFileLocation))
                            .withRemoveImages(DockerComposeContainer.RemoveImages.ALL)
                            .withExposedService(
                                    applicationContainerName,
                                    applicationPort,
                                    Wait.forHealthcheck());
        }
    }

    @BeforeEach
    public void setup() {
        if (!dockerStandalone) {
            container.start();
        }
    }
}
