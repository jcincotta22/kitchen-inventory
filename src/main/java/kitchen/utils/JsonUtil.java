package kitchen.utils;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JsonUtil {
    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }

    public static <T> T jsonToObject(String json, Class<T> cl) {
        Gson gson = new Gson();
        return gson.fromJson(json, cl);
    }
}
