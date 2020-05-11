package viewer.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class CacheUtil {

    //LRU算法,当容量超过100时会使用LRU置换算法
    private static LoadingCache<String,Object> localCache = CacheBuilder.newBuilder().initialCapacity(100).maximumSize(100).expireAfterAccess(20 , TimeUnit.MINUTES)
            .build(new CacheLoader<String, Object>() {
                //默认的数据加载实现，当调用get取值的时候，如果key没有对应的值，就调用这个方法进行加载
                @Override
                public String load(String s) throws Exception {
                    return null;
                }
            });

    public static void setKey(String key,Object value){
        localCache.put(key,value);
    }

    public static Object getKey(String key){
        try{
            return localCache.get(key);
        }catch (Exception ex){
            //throw new RuntimeException(ex);
        }
        return null;
    }

}