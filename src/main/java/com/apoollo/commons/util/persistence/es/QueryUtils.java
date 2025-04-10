/**
 * 
 */
package com.apoollo.commons.util.persistence.es;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apoollo.commons.util.Assert;
import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.persistence.PageInput;
import com.apoollo.commons.util.persistence.PageOutput;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

/**
 * @author liuyulong
 */
public class QueryUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryUtils.class);

    public static <TDocument, R> PageOutput<R> query(ElasticsearchClient elasticsearchClient, String index, Query query,
            Class<TDocument> clazz, Function<Hit<TDocument>, R> map, PageInput pageInput, SortOptions... sortOptions) {
        Integer targetPage = LangUtils.defaultValue(pageInput.getPage(), 1);
        Assert.isTrue(targetPage > 0, "page must great than 0");

        Integer targetPageSize = LangUtils.defaultValue(pageInput.getPageSize(), 20);
        Assert.isTrue(targetPageSize > 0 && targetPageSize <= 50,
                "pageSize must great than 0 and less than or equals 50");

        Integer maxPages = 2147483600 / targetPageSize;

        Assert.isTrue(maxPages >= targetPage, "查询页码数不能超过" + maxPages);

        Integer from = (targetPage - 1) * targetPageSize;

        SearchRequest request = SearchRequest.of(//
                builder -> builder//
                        .index(index)//
                        .query(query)//
                        .sort(LangUtils.getStream(sortOptions).toList())//
                        .from(from)//
                        .size(targetPageSize)//
        );

        LOGGER.info("ELASTICSEARCH条件查询DSL:" + request);

        SearchResponse<TDocument> searchResponse = null;
        try {
            searchResponse = elasticsearchClient.search(request, clazz);

        } catch (ElasticsearchException | IOException e) {
            throw new RuntimeException(e);
        }

        CountRequest countRequest = CountRequest.of(//
                builder -> builder//
                        .index(index)//
                        .query(query)//

        );
        CountResponse countResponse = null;
        try {
            countResponse = elasticsearchClient.count(countRequest);
        } catch (ElasticsearchException | IOException e) {
            throw new RuntimeException(e);
        }
        Long count = countResponse.count();

        List<R> datas = LangUtils.getStream(searchResponse.hits().hits()).map(map).collect(Collectors.toList());

        PageOutput<R> pageOutput = new PageOutput<>(count, datas);
        return pageOutput;
    }

    public static <T> T query(ElasticsearchClient elasticsearchClient, String index, Query query,
            Map<String, Aggregation> aggregations, Function<Map<String, Aggregate>, T> map) {
        SearchRequest request = SearchRequest.of(//
                builder -> builder//
                        .index(index)//
                        .size(0)//
                        .query(query)//
                        .aggregations(aggregations));

        LOGGER.info("ELASTICSEARCH聚合条件查询DSL:" + request);

        SearchResponse<Object> searchResponse = null;
        try {
            searchResponse = elasticsearchClient.search(request, Object.class);
        } catch (ElasticsearchException | IOException e) {
            throw new RuntimeException(e);
        }
        return map.apply(searchResponse.aggregations());
    }

}
