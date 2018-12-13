package kitchen.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchen.models.Product;
import kitchen.services.ProductRequestBody;
import kitchen.utils.JsonUtil;
import org.apache.http.HttpHost;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ElasticSearchClient {
    final private static Logger logger = LogManager.getLogger(ElasticSearchClient.class.getName());

    private static final String HOST = "localhost";
    private static final int PORT_ONE = 9200;
    private static final int PORT_TWO = 9201;
    private static final String SCHEME = "http";

    private static RestHighLevelClient restHighLevelClient;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final String INDEX = "inventory";
    private static final String TYPE = "product";


    private static synchronized void makeConnection() {

        if(restHighLevelClient == null) {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(HOST, PORT_ONE, SCHEME),
                            new HttpHost(HOST, PORT_TWO, SCHEME)));
        }

    }

    private static synchronized void closeConnection() throws IOException {
        restHighLevelClient.close();
        restHighLevelClient = null;
    }

    public static synchronized  <T> void insertIndex(T type, String id){

        String jsonString = JsonUtil.toJson(type);

        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, id)
                .source(jsonString, XContentType.JSON);

        try {
            makeConnection();
            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            logger.debug(response.toString());
            closeConnection();
        } catch(ElasticsearchException e) {
            logger.error(e.getResourceId());
            throw e;
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }
    }

    public static synchronized  <T> List<T> searchByField(Class<T> cl, String field, String searchString){
        makeConnection();


        SearchRequest searchRequest = new SearchRequest(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(field, searchString));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            closeConnection();

        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }

        if (searchResponse != null) {
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            List<T> results = new ArrayList<>();
            for (SearchHit hit : searchHits) {
                results.add(JsonUtil.jsonToObject(JsonUtil.toJson(hit.getSourceAsMap()), cl));
            }
            return results;

        } else {
            return null;
        }
    }
}
