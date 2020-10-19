package com.hyperglance.webcrawler.dataproviders;

import com.hyperglance.webcrawler.core.CrawledData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Repository
@Transactional
@AllArgsConstructor
public class CrawledDataRepositoryImpl implements CrawledDataRepository {

    private CrawledDataEntityRepository crudRepository;

    @Override
    public boolean hasBeenCrawled(String url) {
        return crudRepository.findByPage(url).isPresent();
    }

    @Override
    public List<CrawledData> getExtracts() {
        List<CrawledDataEntity> entities = new ArrayList<>();
        crudRepository.findAll().iterator().forEachRemaining(entities::add);
        List<CrawledData> crawledDataList = new ArrayList<>();
        entities.stream()
                .collect(groupingBy(CrawledDataEntity::getPage)).forEach(
                        (s, crawledDataEntities) -> {
                            CrawledData crawledData = new CrawledData();
                            crawledData.setPage(s);
                            crawledData.setCrawlId(crawledDataEntities.get(0).getCrawlId());
                            crawledData.setImages(
                                    crawledDataEntities.stream()
                                            .map(CrawledDataEntity::getImage)
                                            .collect(Collectors.toSet())
                            );
                            crawledDataList.add(crawledData);
                        }
                );
        return crawledDataList;
    }

    @Override
    public void save(CrawledData crawledData) {
        List<CrawledDataEntity> entities = toEntities(crawledData);
        crudRepository.saveAll(entities);
        log.info("[SAVED] Count={}", entities.size());
    }

    private List<CrawledDataEntity> toEntities(CrawledData crawledData){
        return crawledData.getImages().stream()
                .map(image -> {
                    CrawledDataEntity entity = new CrawledDataEntity();
                    entity.setCrawlId(crawledData.getCrawlId());
                    entity.setImage(image);
                    entity.setPage(crawledData.getPage());
                    return entity;
                }).collect(Collectors.toList());
    }
}
