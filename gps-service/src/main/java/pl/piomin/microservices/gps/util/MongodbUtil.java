package pl.piomin.microservices.gps.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import pl.piomin.microservices.gps.model.Order_track;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MongodbUtil {
    @Autowired
    private  MongoTemplate mongoTemplate;


    public  void  add(Map map, String collectionName){
        mongoTemplate.insert(map,collectionName);
    }

    public  void  addTrack(Order_track order_track, String collectionName){
        mongoTemplate.insert(order_track,collectionName);
    }

    public List<Order_track> queryTrack(String order_id, String collectionName){
        Query query=new Query(Criteria.where("order_id").is(order_id));
        return mongoTemplate.find(query,Order_track.class,collectionName);
    }


    public void createCollection(int size, String collectionName){
        CollectionOptions CollectionOptions = new CollectionOptions(size,null,null);
        mongoTemplate.createCollection(collectionName,CollectionOptions);
    }





    /*
    capped:是否启用集合限制，如果开启需要制定一个限制条件，默认为不启用，这个参数没有实际意义
    size:限制集合使用空间的大小，默认为没有限制   1024  1M
    max:集合中最大条数限制，默认为没有限制
    protected DBObject convertToDbObject(CollectionOptions collectionOptions) {
        DBObject dbo = new BasicDBObject();
        if (collectionOptions != null) {
            if (collectionOptions.getCapped() != null) {
                dbo.put("capped", collectionOptions.getCapped().booleanValue());
            }
            if (collectionOptions.getSize() != null) {
                dbo.put("size", collectionOptions.getSize().intValue());
            }
            if (collectionOptions.getMaxDocuments() != null) {
                dbo.put("max", collectionOptions.getMaxDocuments().intValue());
            }
        }
        return dbo;
    }*/




}