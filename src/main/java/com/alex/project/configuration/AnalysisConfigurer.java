package com.alex.project.configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

@ApplicationScoped
@Named("AnalysisConfigurer")
public class AnalysisConfigurer implements ElasticsearchAnalysisConfigurer {

    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext elasticsearchAnalysisConfigurationContext) {
        elasticsearchAnalysisConfigurationContext.analyzer("autocomplete")
                .custom()
                .tokenizer("standard")
                .tokenFilters("lowercase", "edge_ngram");
    }
}
