package com.hyperglance.webcrawler.core;

import com.hyperglance.webcrawler.dataproviders.CrawledDataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CrawlerServiceImpl implements CrawlerService {

    private CrawledDataRepository crawledDataRepository;

    @Override
    public boolean hasBeenCrawled(String url) {
        return crawledDataRepository.hasBeenCrawled(url);
    }

    @Override
    public List<CrawledData> getExtracts() {
        return crawledDataRepository.getExtracts();
    }

    @Override
    @Async
    public void extractAndSave(String url, int depth) {
        log.info("[extractAndSave -- START: {}] DEPTH={}, URL={}", LocalDateTime.now(), depth, url);
        List<CrawledData> extracts = extractImages(url, depth);
        extracts.forEach(crawledData -> {
            if (!crawledData.getImages().isEmpty()) {
                crawledDataRepository.save(crawledData);
            }
        });
        log.info("[extractAndSave -- END  : {}] ", LocalDateTime.now());
    }

    @Override
    public List<CrawledData> extractImages(String url, int depth) {
        log.info("[extractImages -- START] DEPTH={}, URL={}", depth, url);
        if (depth <= 1){
            return Collections.singletonList(extractImages(url));
        } else {
            Set<String> links = extractLinks(url);
            List<CrawledData> extractImagesPerPage = new ArrayList<>();
            links.forEach(link -> extractImagesPerPage.addAll(extractImages(link, depth - 1)));
            return extractImagesPerPage;
        }
    }

    @Override
    public CrawledData extractImages(String url) {
        log.info("[extractImages -- START] URL={}", url);
        CrawledData crawledData = new CrawledData();
        crawledData.setPage(url);
        crawledData.setCrawlId(UUID.randomUUID().toString());

        if (hasBeenCrawled(url)){
            crawledData.setImages(Collections.emptySet());
            return crawledData;
        }

        try {
            Document page = Jsoup.connect(url).get();
            Elements images = page.select("img");
            Set<String> imageLinks = images.stream()
                    .map(image -> image.attr("abs:src"))
                    .filter(image -> !image.isBlank())
                    .collect(Collectors.toSet());
            log.info("[extractLinks  -- END  ] Count={}", imageLinks.size());
            crawledData.setImages(imageLinks);
        }catch (IOException e){
            log.error("[extractImages] Failed to connect to {}. Caused by: {}", url, e.getMessage());
            crawledData.setImages(Collections.emptySet());
        }
        return crawledData;
    }

    private Set<String> extractLinks(String url){
        log.info("[extractLinks -- START] URL={}", url);
        try {
            Document page = Jsoup.connect(url).get();
            Elements links = page.select("a[href]");
            Set<String> extractedLinks = links.stream()
                    .map(link -> link.attr("abs:href"))
                    .filter(link -> !link.startsWith(url))
                    .collect(Collectors.toSet());
            log.info("[extractLinks -- END  ] Count={}", extractedLinks.size());
            return extractedLinks;
        }catch (IOException e){
            log.error("[extractLinks] Failed to connect to {}. Caused by: {}", url, e.getMessage());
            return Collections.emptySet();
        }
    }
}
