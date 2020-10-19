package com.hyperglance.webcrawler.core;

import java.util.List;

public interface CrawlerService {
    void extractAndSave(String url, int depth);
    List<CrawledData> extractImages(String url, int depth);
    CrawledData extractImages(String url);

    boolean hasBeenCrawled(String url);
    List<CrawledData> getExtracts();
}
