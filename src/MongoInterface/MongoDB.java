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
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonDocument;
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
    public UpdateResult executeUpdate(MongoQuery query){
        MongoCollection<Document> collection = md.getCollection(query.getCollection());
        if (query.getCond()==null) 
            return collection.updateMany(new BsonDocument(),query.getValues());
        return collection.updateMany(query.getCond(),query.getValues());
    }
    public DeleteResult executeDelete(MongoQuery query){
        MongoCollection<Document> collection = md.getCollection(query.getCollection());
        if (query.getCond()==null) 
            return collection.deleteMany(new BsonDocument());
        return collection.deleteMany(query.getCond());
    }
    public void executeInsert(MongoQuery query){
        MongoCollection<org.bson.Document> collection = md.getCollection(query.getCollection());
        collection.insertOne(query.getValues());
    }
    public void close(){
        mc.close();
    }
}
