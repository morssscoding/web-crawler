package com.hyperglance.webcrawler.entrypoints

import com.fasterxml.jackson.databind.ObjectMapper
import com.hyperglance.webcrawler.core.CrawlerService
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = WebCrawlerController)
@Import(CustomTestConfiguration)
class WebCrawlerControllerSpec extends Specification {

    @Autowired
    private MockMvc mockMvc

    private ObjectMapper objectMapper = new ObjectMapper()

    def "testWebCrawlerEndpointForGet"() throws Exception {
        when:
        def response = mockMvc.perform(get("/crawls")
                .contentType(MediaType.APPLICATION_JSON)
        )
        then:
        verifyAll {
            response.andExpect(status().isOk())
        }
    }

    def "testWebCrawlerEndpoint"() throws Exception {
        when:
        def response = mockMvc.perform(post("/crawls")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"url\": \"https://en.wikipedia.org/wiki/Web_crawler\",\n" +
                        "\t\"depth\": 3\n" +
                        "}"))
        then:
        verifyAll {
            response.andExpect(status().isOk())
        }
    }

    def "testWebCrawlerEndpointForValidations"() throws Exception {
        given:
        WebCrawlerController.CrawlRequestDto request = WebCrawlerController.CrawlRequestDto.builder()
                .url(url).depth(depth).build()

        def response = mockMvc.perform(post("/crawls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        expect:
        verifyAll {
            response.andExpect(status().isBadRequest())
        }

        where:
        url     | depth
        ""      | 1
        null    | 1
        "http://example.com"    | 0
        "not a url"    | 1
    }

    @TestConfiguration
    private static class CustomTestConfiguration {

        @Bean
        CrawlerService crawlerService(){
            return Mockito.mock(CrawlerService)
        }
    }
}
