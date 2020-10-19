package com.hyperglance.webcrawler.dataproviders;

import lombok.Data;
import org.springframework.boot.actuate.audit.listener.AuditListener;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CRAWLED_DATA")
@EntityListeners(AuditListener.class)
public class CrawledDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "CRAWL_ID")
    private String crawlId;

    @Column(name = "PAGE")
    private String page;

    @Column(name = "IMAGE")
    private String image;

    @CreatedDate
    private LocalDateTime createdDateTime;

    @CreatedBy
    private String createdBy;
}
