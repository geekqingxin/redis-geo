package com.taione.redie;

import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by td on 2017/4/12.
 */
public class RedisGeo {
    private static Jedis jedis = null;
    static{
        jedis = new Jedis("127.0.0.1", 6379);
    }

    public static void main(String[] args) throws Exception {

        System.out.println(geoADD("beijing", 113.0, 10.0,"123"));
        System.out.println(geoADD("beijing", 115.0, 20.0,"chag"));
        System.out.println(geoADD("beijing", 127.0, 30.0,"1ggu"));
        System.out.println(geoADD("beijing", 116.0, 50.0,"hu4"));

       /* System.out.println(geoHash("beijing","haidian"));
        System.out.println(geoDist("beijing","haidian","shijingshan","km"));
        System.out.println(geoPos("beijing","haidian"));
        System.out.println(geoRadius("beijing",116.421822, 39.906809,10,"km",false));
        System.out.println(geoRadiusByMember("beijing","dongcheng",10,"km",true));*/
    }
    /**
     * 添加geo
     * @param key
     * @param longitude
     * @param latitude
     * @return
     */
    public static Long geoADD(String key,double longitude,double latitude,String dName){
        return (Long)jedis.eval("return redis.call('GEOADD',KEYS[1],KEYS[2],KEYS[3],KEYS[4])", 4,key,String.valueOf(longitude),String.valueOf(latitude),dName);
    }

    /**
     * 查询2位置距离
     * @param key
     * @param d1
     * @param d2
     * @param unit
     * @return
     */
    public static Double geoDist(String key,String d1,String d2,String unit){
        return Double.valueOf((String)jedis.eval("return redis.call('GEODIST',KEYS[1],KEYS[2],KEYS[3],KEYS[4])",4, key,d1,d2,unit));
    }

    /**
     * 查询位置的geohash
     * @param key
     * @param dName
     * @return
     */
    public static String geoHash(String key,String dName){
        Object data = jedis.eval("return redis.call('GEOHASH',KEYS[1],KEYS[2])", 2, key,dName);
        List<String> resultList = (List<String>)data;
        if(resultList!=null&&resultList.size() > 0){
            return resultList.get(0);
        }
        return null;
    }

    /**
     * 查询位置坐标
     * @param key
     * @param dName
     * @return
     */
    public static List<Double> geoPos(String key,String dName){
        Object data = jedis.eval("return redis.call('GEOPOS',KEYS[1],KEYS[2])", 2, key,dName);
        List<List<Double>> resultList = (List<List<Double>>)data;
        if(resultList!=null&&resultList.size() > 0){
            return resultList.get(0);
        }
        return null;
    }

    /**
     * 查询附近坐标地址
     * @param key
     * @param longitude
     * @param latitude
     * @param unit
     * @param asc
     * @return
     */
    public static List<String> geoRadius(String key,double longitude,double latitude,int radius,String unit,boolean asc){
        Object data = jedis.eval("return redis.call('GEORADIUS',KEYS[1],KEYS[2],KEYS[3],KEYS[4],KEYS[5],KEYS[6])", 6, key,String.valueOf(longitude),
                String.valueOf(latitude),String.valueOf(radius),unit,asc?"ASC":"DESC");
        return (List<String>)data;
    }

    /**
     * 根据位置查询附近点
     * @param key
     * @param dName
     * @param unit
     * @param asc
     * @return
     */
    public static List<String> geoRadiusByMember(String key,String dName,int radius,String unit,boolean asc){
        Object data = jedis.eval("return redis.call('GEORADIUSBYMEMBER',KEYS[1],KEYS[2],KEYS[3],KEYS[4],KEYS[5])", 5, key,dName,String.valueOf(radius),unit,asc?"ASC":"DESC");
        return (List<String>)data;
    }

  /*  public static double randowNum(){
        Random random = new Random();
        return random.nextInt(100);
    }

    public static String randowNam(){
        return  RandomStringUtils.randomAlphanumeric(4);
    }*/
}
