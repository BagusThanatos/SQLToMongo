/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MongoInterface;

import com.mongodb.MongoClient;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
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
    private static MongoDB db= new MongoDB();
    private MongoDB(){
        mc = new MongoClient();
    }
    
    public static synchronized MongoDB getDatabaseConnection(){
        return db;
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
            result = result.projection(Projections.include(query.getFields()));
        if(query.isOrder()) {
          return result.sort(new Document(query.getSpecField(), query.isAsc()? 1 : -1));
        }
        return result;
    }
    public DistinctIterable<Document> executeDistinct(MongoQuery query){
      MongoCollection<Document> collection = md.getCollection(query.getCollection());
      if(query.getCond()!=null){
        return collection.distinct(query.getSpecField(),query.getCond(),Document.class);
      }
      return collection.distinct(query.getSpecField(), Document.class);
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
    public MongoIterable<String> getDatabaseList(){
      return mc.listDatabaseNames();
    }
    
}
