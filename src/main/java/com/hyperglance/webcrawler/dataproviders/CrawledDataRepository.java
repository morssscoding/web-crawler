package com.hyperglance.webcrawler.dataproviders;

import com.hyperglance.webcrawler.core.CrawledData;

import java.util.List;

public interface CrawledDataRepository {
    void save(CrawledData crawledData);
    boolean hasBeenCrawled(String url);
    List<CrawledData> getExtracts();
}
