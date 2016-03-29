/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQLTranslator;

import MongoInterface.MongoDB;
import MongoInterface.MongoQuery;
import SQLParser.TokenLexic;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import gui.Main;
import java.util.ArrayList;
import org.bson.Document;

/**
 *
 * @author BagusThanatos (github.com/BagusThanatos)
 */
public class TranslatorThread implements Runnable{
    Main m;
    ArrayList<String> queries;
    ArrayList<TokenLexic> tokens;
    public TranslatorThread(Main m, ArrayList<TokenLexic> t){
        this.m =m;
        this.tokens = t;
    }
    
    @Override
    public void run(){
        long start, finish;
        
        start = System.nanoTime();
        SQLTranslator.SQLToMongo sqlToMongo = new SQLTranslator.SQLToMongo();
        MongoQuery query = sqlToMongo.translate(tokens);
        MongoDB db = MongoDB.getDatabaseConnection();
        if (null!=query.getType()) switch (query.getType()) {
            case SELECT:
                for(Document d : db.executeQuery(query)){
                    m.appendResult(d.toJson());
                }  
                
                break;
                
            case INSERT:
                db.executeInsert(query);
                m.appendResult("One document inserted");
                break;
            case UPDATE:
                UpdateResult ur = db.executeUpdate(query);
                m.appendResult(ur.toString());
                break;
            default:
                DeleteResult dr = db.executeDelete(query);
                m.appendResult(dr.toString());
                break;
        }
        finish = System.nanoTime();
        m.appendResult("Execution time : "+(finish -start)/1000+" ms");
    }
}
