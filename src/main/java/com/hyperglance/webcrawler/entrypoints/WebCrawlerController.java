package com.hyperglance.webcrawler.entrypoints;

import com.hyperglance.webcrawler.core.CrawlerService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/crawls")
@AllArgsConstructor
public class WebCrawlerController {

    private CrawlerService crawlerService;

    @GetMapping
    public ResponseEntity<?> getCrawls(){
        return ResponseEntity.ok(crawlerService.getExtracts());
    }

    @PostMapping
    public ResponseEntity<?> doCrawl(@RequestBody @Validated CrawlRequestDto request){
        if (!crawlerService.hasBeenCrawled(request.url)){
            crawlerService.extractAndSave(request.getUrl(), request.getDepth());
            return ResponseEntity.ok("Web Crawl job submitted!");
        }else {
            return ResponseEntity.ok(request.getUrl() + " has already been crawled!");
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CrawlRequestDto{
        @NotEmpty @URL
        private String url;
        @Min(1)
        private int depth;
    }
}
