/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqltomongo;

import MongoInterface.MongoDB;
import MongoInterface.MongoQuery;
import SQLParser.Parser;
import SQLParser.TokenLexic;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author BagusThanatos (github.com/BagusThanatos)
 */
public class SQLToMongo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Parser p = Parser.getSQLParser();
//        System.out.println(p.parse("\"b\""));
        
//        System.out.println(p.parse("SELECT *\n" +
//"FROM users where status =\"A\" and age > 25 and age < 50 order by id ASC;"));
//      System.out.println(p.parse("SELECT distinct id\n" +
//      "FROM users where status =\"A\" and age > 25 and age < 50 ;"));
//        System.out.println(p.parse("INSERT INTO users(user_id,\n" +
//"                  age,\n" +
//"                  status)\n" +
//"VALUES (\"bcd001\",\n" +
//"        45,\n" +
//"        \"A\");")+"\n");
//un-comment baris baris di bawah kalo mau konek ke db
        //MongoDB db = new MongoDB();
        //db.setDatabase("db1");
        String q= "update tabel set a=1,b=2 where _id=01 and cc like \"%l\";";
        System.out.println(q);
        System.out.println(p.parse(q));
        SQLTranslator.SQLToMongo sqlToMongo = new SQLTranslator.SQLToMongo();
        MongoQuery query = sqlToMongo.translate(p.getTokens());
        System.out.println(p.getTokens().toString());
        for(TokenLexic t : p.getTokens()){
            System.out.print(t.getTokenCode()+", ");
        }
        if(query==null){
          System.out.println("Query salah");
          return;
        }
        System.out.println();
        if(query.getFields()!=null) 
            System.out.println("Fields : "+query.getFields().toString());
        if (query.getCond()!=null)
            System.out.println("Condition : "+query.getCond().toString());
        if (query.getValues()!=null) {
            System.out.println("Values : ");
            Document v = query.getValues();
            for (String s : v.keySet()){
                System.out.println(v.get(s));
            }
        }
        System.out.println("Collection : "+query.getCollection());
        System.out.println("Type : "+query.getType());
        if (null!=query.getType()) switch (query.getType()) {
            case SELECT:
                //        for(Document d : db.executeQuery(query)){
//            System.out.println(d);
//        }
                System.out.println("coba");
                //db.insertQuery(query);
                System.out.println("Order : "+query.isOrder());
                System.out.println("Order Field : "+query.getSpecField());
                break;
                
            case INSERT:
                //db.executeInsert(query);
                break;
            case UPDATE:
                //db.executeUpdate(query);
                break;
            default:
                //db.executeDelete(query);
                break;
        }
            
    }
    
}
