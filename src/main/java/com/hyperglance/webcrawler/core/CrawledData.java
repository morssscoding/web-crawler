package com.hyperglance.webcrawler.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrawledData {

    private String crawlId;
    private String page;
    private Set<String> images;
}
