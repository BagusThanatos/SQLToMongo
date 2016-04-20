/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQLTranslator;

import MongoInterface.MongoDB;
import MongoInterface.MongoQuery;
import SQLParser.TokenLexic;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import gui.Main;
import java.util.ArrayList;
import org.bson.BsonValue;
import org.bson.Document;

/**
 *
 * @author BagusThanatos (github.com/BagusThanatos)
 */
public class TranslatorThread implements Runnable{
    Main m;
    ArrayList<String> queries;
    ArrayList<TokenLexic> tokens;
    SQLTranslator.SQLToMongo sqlToMongo;
    public TranslatorThread(Main m, ArrayList<TokenLexic> t){
        this.m =m;
        this.tokens = t;
        sqlToMongo = new SQLToMongo();
    }
    
    @Override
    public void run(){
        long start, finish=0;
        int i=0;
        String append="";
        start = System.nanoTime();
        MongoQuery query = sqlToMongo.translate(tokens);
        if(query==null){
          m.appendResult("Query error! Please check it again");
          m.setValid(false);
          return;
        }
        MongoDB db = MongoDB.getDatabaseConnection();
        if (null!=query.getType()) switch (query.getType()) {
          case SELECT:
            FindIterable<Document> result =  db.executeQuery(query);
            finish = System.nanoTime();
            for(Document d : result){;
              m.appendResult(d.toJson());
              i+=1;
            } 
            m.appendResult("count : "+i);
            break;
                
          case DISTINCT:
            for(BsonValue b : db.executeDistinct(query)){
              String s="";
              if(b.isBoolean()) s += b.asBoolean().getValue();
              if(b.isString()) s = b.asString().getValue();
//              if(b.isInt32()) s +=  b.asInt32().getValue();
//              if(b.isInt64()) s +=  b.asInt64().getValue();
              if(b.isNumber()) s += b.asNumber().intValue();
              finish = System.nanoTime();
              m.appendResult(s);
              i+=1;
            }
            break;
          case AGGREGATE:
            AggregateIterable<Document> r = db.executeAggregate(query);
            finish = System.nanoTime();
            for(Document d : r){
              m.appendResult(d.toJson());
              i++;
            }  
            break;
          case INSERT:
            db.executeInsert(query);
            finish= System.nanoTime();
            m.appendResult("One document inserted");
            break;
          case UPDATE:
            UpdateResult ur = db.executeUpdate(query);
            finish = System.nanoTime();
            m.appendResult(ur.getModifiedCount()+" rows updated");
            break;
          default:
            DeleteResult dr = db.executeDelete(query);
            finish = System.nanoTime();
            m.appendResult(dr.toString());
        }
        m.appendResult("Execution time : "+(finish -start)/1000000+" ms");
    }
}
