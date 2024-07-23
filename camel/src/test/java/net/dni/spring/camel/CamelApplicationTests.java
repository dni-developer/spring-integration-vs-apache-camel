package net.dni.spring.camel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CamelApplicationTests {

    @LocalServerPort
    private String port;

    @Value("classpath:test.csv")
    Resource testResource;

    @Autowired
    TestRestTemplate testRestTemplate;

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private static HttpEntity<String> getHttpEntity(String body) {
        return new HttpEntity<>(body, getHttpHeaders());
    }

    @Test
    public void testBadRequestMissingRequiredData() {
        ResponseEntity<String> responseEntity = testRestTemplate.exchange("http://localhost:" + port + "/subscriber", HttpMethod.POST,
                getHttpEntity("{}"), String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity.getBody(), is("{\"errors\":[{\"field\":\"email\",\"message\":\"Invalid email.\"},{\"field\":\"firstName\",\"message\":\"Invalid firstName.\"},{\"field\":\"lastName\",\"message\":\"Invalid lastName.\"}]}"));
    }

    @Test
    public void testBadRequestMalformed() {
        ResponseEntity<String> responseEntity = testRestTemplate.exchange("http://localhost:" + port + "/subscriber", HttpMethod.POST,
                getHttpEntity("malformed input"), String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity.getBody(), is("\"Invalid JSon payload.\""));
    }

    @Test
    public void testGoodRequest() {
        ResponseEntity<String> responseEntity = testRestTemplate.exchange("http://localhost:" + port + "/subscriber", HttpMethod.POST,
                getHttpEntity("{\"email\":\"abc@test.com\",\"firstName\":\"Unit\",\"lastName\":\"Test\"}"), String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(responseEntity.getBody(), is("{\"subscriberId\":1}"));
    }

    @Test
    public void testUpload() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", testResource);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = testRestTemplate.exchange("http://localhost:" + port + "/upload", HttpMethod.POST, entity, String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
    }

}
