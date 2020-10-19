package com.hyperglance.webcrawler.dataproviders;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CrawledDataEntityRepository extends CrudRepository<CrawledDataEntity, Long> {
    Optional<CrawledDataEntity> findByPage(String page);
}
