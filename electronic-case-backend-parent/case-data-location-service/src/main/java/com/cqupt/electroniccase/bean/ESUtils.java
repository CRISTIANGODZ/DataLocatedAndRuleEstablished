package com.cqupt.electroniccase.bean;

import com.cqupt.electroniccase.domain.model.ESResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @auther DyingZhang
 * @Create 2023-03-29 下午 8:04
 * @Discriptioon
 */
@Component
public class ESUtils {
    @Autowired
    @Qualifier("esClient")
    private RestHighLevelClient client;

    public ESUtils() {
    }

    /**
     * 创建索引，返回是否成功
     * @param index
     * @return
     */
    public Boolean createIndex(String index) {
        CreateIndexRequest request = new CreateIndexRequest(index);
        CreateIndexResponse response = null;
        try {
            response = client.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Create ES Index Error!");
        }
        return response != null ? response.isAcknowledged() : false;
    }

    /**
     * 获取索引，返回参数为GetIndexResponse类型，其中属性有Alias,Mapping,Settings
     * @param index
     * @return
     */
    public GetIndexResponse getIndex(String index) {
        GetIndexRequest request = new GetIndexRequest(index);
        GetIndexResponse response = null;
        try {
            response = client.indices().get(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Get ES Index Error!");
        }
        return response;
    }

    /**
     * 删除索引
     * @param index
     * @return
     */
    public Boolean delIndex(String index) {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse response;
        try {
            response = client.indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Delete Document Error!");
        }
        return response != null ? response.isAcknowledged() : false;
    }

    /**
     * 插入一条文档
     * true为插入成功
     * false为修改成功
     * @param index
     * @param id
     * @param t
     * @return
     * @param <T>
     * @throws IOException
     */
    public <T> Boolean insertDoc(String index, String id, T t) throws IOException {
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index(index).id(id);

        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(t);
        indexRequest.source(userStr, XContentType.JSON);

        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
        return response.getResult().name().equals("CREATED") ? true : false;
    }

    /**
     * 查询一条文档
     * index属性为String类型
     * type属性为String类型
     * id属性为String类型
     * source属性为Map<String, Object>类型
     * 但都要先进行类型转换！
     * @param index
     * @param id
     * @return
     */
    public ESResult getDocById(String index, String id) {
        GetRequest getRequest = new GetRequest(index, id);
        GetResponse response = null;
        try {
            response = client.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Get Doc Error!");
        }

        return new ESResult().data("index", response.getIndex())
                .data("type", response.getType())
                .data("id", response.getId())
                .data("source", response.getSource());
    }

    /**
     * true为删除一条文档
     * false为没有找到该文档，不报错
     * @param index
     * @param id
     * @return
     */
    public Boolean delDoc(String index, String id) {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.index(index).id(id);
        DeleteResponse response = null;
        try {
            response = client.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Delete Doc Error!");
        }
        return response.getResult().name().equals("DELETED") ? true : false;
    }

    /**
     * 查询一个索引的所有记录
     * @param index
     * @return
     */
    public SearchHits getAllDocsFromIndex(String index) {
        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);

        //构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(sourceBuilder);

        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Get All Documents Have Error !");
        }
        //查询匹配
        SearchHits hits = response.getHits();
        return hits;
    }

    /**
     * 查询一个索引的所有记录，顺序排序
     * @param index
     * @return
     */
    public SearchHits getAllDocsFromIndexASC(String index, String sortKey) {
        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);

        //构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(QueryBuilders.matchAllQuery());//查询所有数据
        sourceBuilder.sort(sortKey, SortOrder.ASC);//排序

        searchRequest.source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Get All Documents Have Error !");
        }
        //查询匹配
        SearchHits hits = response.getHits();
        return hits;
    }

    /**
     * 查询一个索引的所有记录，逆序排序
     * @param index
     * @return
     */
    public SearchHits getAllDocsFromIndexDESC(String index, String sortKey) {
        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);

        //构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(QueryBuilders.matchAllQuery());//查询所有数据
        sourceBuilder.sort(sortKey, SortOrder.DESC);//排序

        searchRequest.source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Get All Documents Have Error !");
        }
        //查询匹配
        SearchHits hits = response.getHits();
        return hits;
    }

    /**
     * 根据term进行查询
     * @param index
     * @param column
     * @param value
     * @return
     */
    public SearchHits getDocByTerm(String index, String column, String value) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);

        //构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery(column, value));

        searchRequest.source(sourceBuilder);

        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Get Document By Term Error!");
        }
        //查询匹配：
        SearchHits hits = response.getHits();
        return hits;
    }

    /**
     * 模糊查询
     * @param index
     * @param column
     * @param value
     * @return
     */
    public SearchHits getDocFuzzily(String index, String column, String value) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.fuzzyQuery(column, value));

        searchRequest.source(sourceBuilder);

        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Get Document Fuzzily Error!");
        }
        //查询匹配：
        SearchHits hits = response.getHits();
        return hits;
    }

    /**
     * 范围查询
     * @param index
     * @param column
     * @param gte #左范围
     * @param lte #右范围
     * @return
     */
    public SearchHits getDocInRange(String index, String column, Integer gte, Integer lte) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(column);
        rangeQuery.gte(gte);//左范围（闭）
        rangeQuery.lte(lte);//右范围（闭）

        sourceBuilder.query(rangeQuery);
        searchRequest.source(sourceBuilder);

        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Get Document in Range Error!");
        }

        //查询匹配：
        SearchHits hits = response.getHits();
        return hits;
    }
}
