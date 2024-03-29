package org.wiremock.integrations.testcontainers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.testsupport.http.HttpResponse;
import org.wiremock.integrations.testcontainers.testsupport.http.TestHttpClient;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class WireMockContainerWithWireMockVersionThreeTest
{
    @Container
    WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:" + WireMockContainer.WIREMOCK_HEALTH_CHECK_SUPPORT_MINIMUM_VERSION)
            .withMapping("hello", WireMockContainerTest.class, "hello-world.json")
            .withMapping("hello-resource", WireMockContainerTest.class, "hello-world-resource.json")
            .withFileFromResource("hello-world-resource-response.xml", WireMockContainerTest.class,
                                  "hello-world-resource-response.xml");


    @ParameterizedTest
    @ValueSource(strings = {
            "hello",
            "/hello"
    })
    void helloWorld(String path) throws Exception {
        // given
        String url = wiremockServer.getUrl(path);

        // when
        HttpResponse response = new TestHttpClient().get(url);

        // then
        assertThat(response.getBody())
                .as("Wrong response body")
                .contains("Hello, world!");
    }
}
