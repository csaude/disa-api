package mz.org.fgh.disaapi.integ.test.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import mz.org.fgh.disaapi.integ.DisaApiIntegApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = DisaApiIntegApplication.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("classpath:SwaggerUiTest.sql")
public class SwaggerUiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldRequireAuthenticationToView() {
        ResponseEntity<String> response = restTemplate.getForEntity("/", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldServeDocumentationInSwaggerUi() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("fgh", "fgh")
                .getForEntity("/", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("DISA API", "swagger-ui");
    }
}
