/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MongoInterface;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author BagusThanatos (github.com/BagusThanatos)
 */
public class MongoDB {
    MongoClient mc;
    MongoDatabase md;
    public MongoDB(){
        mc = new MongoClient();
    }
    
    public void setDatabase(String db){
        this.md = mc.getDatabase(db);
    }
    public FindIterable<Document> executeQuery(MongoQuery query ){
        MongoCollection<Document> collection = md.getCollection(query.getCollection());
        FindIterable<Document> result;
        if(query.getCond()!=null){
            result = collection.find(query.getCond());
        }
        else result = collection.find();
        if(query.getFields()!=null) 
            return result.projection(Projections.include(query.getFields()));
        return result;
    }
    public void executeUpdate(MongoQuery query){
        
    }
    public void executeDelete(MongoQuery query){
        
    }
    public void executeInsert(MongoQuery query){
        MongoCollection<org.bson.Document> collection = md.getCollection(query.getCollection());
        collection.insertOne(query.getValues());
    }
    public void close(){
        mc.close();
    }
}
