package com.xlj.tools.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xlj.tools.bean.User;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Arrays;

/**
 * ElasticSearch基本操作使用
 *
 * @author xulejun
 * @date 2021/9/16
 */
public class BasicOperation {
    public static void main(String[] args) throws IOException {
        // 创建客户端对象
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        // ————————————基本使用—————————————
        // 创建索引
//        createIndex(client);
        // 查询索引
//        queryIndex(client);
        // 删除索引
//        deleteIndex(client);

        // 创建文档
//        createData(client);
        // 修改文档
//        updateData(client);
        // 删除文档
//        deleteData(client);

        // 批量新增
//        bulkCreate(client);
        // 批量删除
//        bulkDele7te(client);

        // ——————————高级查询————————————
        // 查询所有索引数据
//        queryAllIndexData(client);
        // term查询，查询条件为关键字
//        termQuery(client);
        // 分页查询
//        pageQuery(client);
        // 数据排序
//        sortData(client);
        // 过滤字段
//        filterFields(client);
        // Bool 查询
//        boolQuery(client);
        // 范围查询
//        rangeQuery(client);
        // 模糊查询
//        vagueQuery(client);
        // 高亮查询
//        highlightQuery(client);
        // 聚合查询
//        aggQuery(client);
        // 分组统计
        groupByQuery(client);
    }

    private static void groupByQuery(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest().indices("student");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.aggregation(AggregationBuilders.terms("age groupby").field("age"));
        getResult(client, request, sourceBuilder);
    }

    private static void aggQuery(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest().indices("student");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.aggregation(AggregationBuilders.max("maxAge").field("id"));
        getResult(client, request, sourceBuilder);
    }

    private static void highlightQuery(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest().indices("student");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 构建高亮查询
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "zhangsan");
        sourceBuilder.query(termQueryBuilder);
        // 构建高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        // 设置标签前缀
        highlightBuilder.preTags("<font color='red'>");
        // 设置标签后缀
        highlightBuilder.postTags("</font>");
        // 设置高亮字段
        highlightBuilder.field("name");
        // 设置高亮构建对象
        sourceBuilder.highlighter(highlightBuilder);
        getResult(client, request, sourceBuilder);
    }

    private static void vagueQuery(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("student");
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.fuzzyQuery("name", "zhangsan").fuzziness(Fuzziness.ONE));
        getResult(client, request, sourceBuilder);
    }

    private static void rangeQuery(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("student");
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("id");
        // 大于等于
        rangeQuery.gte("1");
        // 小于等于
        rangeQuery.lte("10");
        sourceBuilder.query(rangeQuery);
        getResult(client, request, sourceBuilder);
    }

    private static void boolQuery(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("student");
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 必须包含
        boolQueryBuilder.must(QueryBuilders.matchQuery("name", "zhangsan"));
        // 一定不包含
        boolQueryBuilder.mustNot(QueryBuilders.matchQuery("id", "2"));
        // 可能包含
        boolQueryBuilder.should(QueryBuilders.matchQuery("name", "lisi"));
        sourceBuilder.query(boolQueryBuilder);
        getResult(client, request, sourceBuilder);
    }

    private static void filterFields(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("student");
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        // 查询过滤字段
        String[] excludes = {};
        String[] includes = {"name", "id"};
        sourceBuilder.fetchSource(includes, excludes);
        getResult(client, request, sourceBuilder);
    }

    private static void sortData(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        // 排序
        sourceBuilder.sort("id", SortOrder.ASC);
        getResult(client, request, sourceBuilder);
    }

    private static void pageQuery(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("student");
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        // 分页查询
        sourceBuilder.from(0);
        sourceBuilder.size(2);
        getResult(client, request, sourceBuilder);
    }

    private static void termQuery(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("student");
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("name", "zhangsan"));
        getResult(client, request, sourceBuilder);
    }

    private static void queryAllIndexData(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("student");
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 查询所有数据
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        getResult(client, request, sourceBuilder);
    }

    private static void getResult(RestHighLevelClient client, SearchRequest request, SearchSourceBuilder sourceBuilder) throws IOException {
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 查询匹配
        SearchHits hits = response.getHits();
        System.out.println(response.getTook());
        System.out.println(response.isTimedOut());
        System.out.println(hits.getTotalHits());
        System.out.println(hits.getMaxScore());
        hits.forEach(System.out::println);
    }

    private static void bulkDelete(RestHighLevelClient client) throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(new DeleteRequest().index("user").id("1001"));
        request.add(new DeleteRequest().index("user").id("1002"));
        request.add(new DeleteRequest().index("user").id("1003"));
        // 客户端发送请求
        BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
        System.out.println(response.getTook());
        System.out.println(Arrays.toString(response.getItems()));
    }

    private static void bulkCreate(RestHighLevelClient client) throws IOException {
        // 创建批量新增请求对象
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest().index("user").id("1001").source(XContentType.JSON, "name", "zhangsan"));
        request.add(new IndexRequest().index("user").id("1002").source(XContentType.JSON, "name", "lisi"));
        request.add(new IndexRequest().index("user").id("1003").source(XContentType.JSON, "name", "wangwu"));
        // 客户端发送请求，获取响应对象
        BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
        System.out.println(response.getTook());
        System.out.println(Arrays.toString(response.getItems()));
    }

    private static void deleteData(RestHighLevelClient client) throws IOException {
        // 创建请求对象
        DeleteRequest request = new DeleteRequest().index("user").id("1001");
        // 客户端发请求，获取响应对象
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
    }

    private static void updateData(RestHighLevelClient client) throws IOException {
        // 修改文档
        UpdateRequest request = new UpdateRequest();
        // 配置修改参数
        request.index("user").id("1001");
        // 设置请求体，对数据进行修改
        request.doc(XContentType.JSON, "name", "lisi");
        // 客户端发送请求，获取响应对象
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println(response.getIndex());
        System.out.println(response.getId());
        System.out.println(response.getResult());
    }

    private static void createData(RestHighLevelClient client) throws IOException {
        IndexRequest request = new IndexRequest();
        // 创建索引及唯一性标识
        request.index("student").id("1001");
        // 创建数据对象
        User user = User.builder().id(10).name("zhangsan").build();
        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(user);
        // 添加文档数据，数据格式为Json格式
        request.source(productJson, XContentType.JSON);
        // 客户端发送请求，获取响应对象
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getIndex());
        System.out.println(response.getId());
        System.out.println(response.getResult());
    }

    private static void deleteIndex(RestHighLevelClient client) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("user");
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    private static void queryIndex(RestHighLevelClient client) throws IOException {
        GetIndexRequest request = new GetIndexRequest("user");
        GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);
        System.out.println(response.getAliases());
        System.out.println(response.getMappings());
        System.out.println(response.getSettings());
    }

    private static void createIndex(RestHighLevelClient client) throws IOException {
        // 创建索引 -请求对象
        CreateIndexRequest request = new CreateIndexRequest("student");
        // 发送请求，获取响应
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        // 响应状态
        System.out.println(response.isAcknowledged());
    }
}
