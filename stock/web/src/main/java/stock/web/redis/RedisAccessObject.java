package stock.web.redis;

/**
 * Created by songyuanren on 2016/10/14.
 */
public interface RedisAccessObject {

    void put(String key, Object value);

    Object get(String key);
}
