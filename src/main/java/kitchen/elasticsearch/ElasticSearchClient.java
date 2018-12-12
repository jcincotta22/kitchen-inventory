package kitchen.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchen.utils.JsonUtil;
import org.apache.http.HttpHost;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class ElasticSearchClient {
    final private static Logger logger = LogManager.getLogger(ElasticSearchClient.class.getName());

    private static final String HOST = "localhost";
    private static final int PORT_ONE = 9200;
    private static final int PORT_TWO = 9201;
    private static final String SCHEME = "http";

    private static RestHighLevelClient restHighLevelClient;

    private static final String INDEX = "kitchen_inventory";

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

    public static <T> void insertIndex(T type, String id){

        String jsonString = JsonUtil.toJson(type);

        IndexRequest indexRequest = new IndexRequest(INDEX, type.getClass().getName(), id)
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
}
