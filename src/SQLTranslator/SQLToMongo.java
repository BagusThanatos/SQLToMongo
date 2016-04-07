/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQLTranslator;

import MongoInterface.MongoQuery;
import SQLParser.Parser;
import SQLParser.SQLKeywords;
import SQLParser.TokenLexic;
import java.util.ArrayList;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;
import java.util.List;
import org.bson.conversions.Bson;

/**
 *
 * @author BagusThanatos (github.com/BagusThanatos)
 */
public class SQLToMongo {
    private int i ;
    
    public MongoQuery translate(ArrayList<TokenLexic> tokens){
        i=0;
        String mongo;
        MongoQuery query = new MongoQuery();
        String type = tokens.get(0).getValue().toUpperCase();
        if (type.equals(SQLKeywords.INSERT)){
            String collection = tokens.get(2).getValue();
            ArrayList<String> fields = new ArrayList();
            ArrayList<Object> values = new ArrayList();
            boolean value = false;
            for (i=4; i< tokens.size();i++){
                String temp = tokens.get(i).getValue();
                if (temp.toUpperCase().equals(SQLKeywords.VALUES)) value = true;
                else {
                    if (!(temp.equals(SQLKeywords.COMMA) || 
                            temp.equals(SQLKeywords.KURTUP) ||
                            temp.equals(SQLKeywords.KURKA)|| temp.equals(SQLKeywords.SEMICOLON))) {
                        if(value) {
                          Object t=temp;
                          int code = tokens.get(i).getTokenCode();
                          if(code == Parser.CONSTANT_NUMBER) 
                            t= new Double(temp);
                          
                          values.add(t);
                        }
                        else 
                          fields.add(temp);
                        
                    } 
                        
                }
            }
            mongo = "db."+collection+".insert({";
            mongo += fields.get(0)+": "+values.get(0);
            Document doc = new Document();
            for(int j =0 ;j<fields.size();j++){
                mongo +=","+fields.get(j)+": "+values.get(j);
                doc.append(fields.get(j), values.get(j));
            }
            mongo +="})";
            query.setCollection(collection);
            query.setValues(doc);
            query.setQuery(mongo);
            query.setType(MongoQuery.Type.INSERT);
        }
        else if (type.equals(SQLKeywords.SELECT)){
          boolean distinct = false;
          String temp = tokens.get(1).getValue().toUpperCase();
          List<String> fields=null;
          Bson cond = null;
          i =1;
          if(!temp.equals("ALL")&& !temp.equals("*")){
            if(temp.equals(SQLKeywords.DISTINCT)){
              query.setSpecField(tokens.get(2).getValue());
              distinct = true;
              i+=2;
            } 
            else {
              temp = tokens.get(i).getValue();
              fields = new ArrayList();
              while (!temp.toUpperCase().equals(SQLKeywords.FROM)){
                if(!temp.equals(SQLKeywords.COMMA)){
                  fields.add(temp);
                }
                temp=tokens.get(++i).getValue();
              }   
            }
          }
          else i++;
          String coll = tokens.get(++i).getValue();
          if (++i < tokens.size()) {
            temp = tokens.get(i).getValue().toUpperCase();
            if(temp.equals(SQLKeywords.WHERE)){
              i++;
              cond= translateWhere(tokens);
            }
          }
          if(i < tokens.size()){
            if(tokens.get(i).getValue().toUpperCase().equals("BY")){
              i+=1;
              query.setSpecField(tokens.get(i).getValue());
              query.setOrder(true);
              temp = tokens.get(++i).getValue().toUpperCase();
              query.setAsc(temp.equals("ASC"));
            }
          }
          query.setFields(fields);
          query.setCollection(coll);
          query.setCond(cond);
          if(distinct) query.setType(MongoQuery.Type.DISTINCT);
          else query.setType(MongoQuery.Type.SELECT);
        }
        else if(type.equals(SQLKeywords.UPDATE)){
            String coll = tokens.get(1).getValue();
            i = 3;
            Document doc = new Document();
            Bson cond = null;
            String temp = tokens.get(i).getValue();
            while(!temp.toUpperCase().equals(SQLKeywords.WHERE) && !temp.equals(";")){
                String value =tokens.get(i+2).getValue();
                doc = doc.append("$set", new Document(temp,value));
                i+=3;
                temp = tokens.get(i).getValue();
            }
            if(temp.toUpperCase().equals(SQLKeywords.WHERE)){
              i++;  
              cond = translateWhere(tokens);
            }
            query.setCollection(coll);
            query.setCond(cond);
            query.setValues(doc);
            query.setType(MongoQuery.Type.UPDATE);
        }
        else if (type.equals(SQLKeywords.DELETE)){
            String coll = tokens.get(2).getValue();
            Bson cond = null;
            if (tokens.get(3).getValue().toUpperCase().equals(SQLKeywords.WHERE)) {
              i=4;
              cond = translateWhere(tokens);
            }
            query.setCollection(coll);
            query.setCond(cond);
            query.setType(MongoQuery.Type.DELETE);
        }
            
        return query;
    }
    private Bson translateWhere(List<TokenLexic> token){
        Bson d = null;
 //       while(i<token.size()){
        String temp = token.get(i+1).getValue().toUpperCase();
        if(temp.equals(SQLKeywords.BETWEEN)){
            String field = token.get(i).getValue();
            String d1 = token.get(i+2).getValue();
            String d2 = token.get(i+4).getValue();
            d= and(gte(field, d1),lte(field,d2));
            i+=5;
        } else{
            TokenLexic temp1 = token.get(i);
            TokenLexic temp2 = token.get(i+2);
            int code1 = temp1.getTokenCode(),code2 = temp2.getTokenCode();            
            String valueT1 = temp1.getValue();
            String valueT2 = temp2.getValue();
            String val1;
            Object val2;
            boolean var1 = temp1.getTokenCode()==Parser.VARIABLE;
            if (var1) {
              val1 = valueT1;
              if(code2==Parser.CONSTANT_NUMBER) val2 = new Double(valueT2);
              else val2= valueT2;
            } else {
              val1 = valueT2;
              if(code1==Parser.CONSTANT_NUMBER) val2 = new Double(valueT1);
              else val2= valueT1;
            }
            if (temp.equals(SQLKeywords.EQ)){
                d= eq(val1,val2);
            } else if (temp.equals(SQLKeywords.GTE)){
                d=gte(val1,val2);
            } else if (temp.equals(SQLKeywords.LTE)){
                d=lte(val1,val2);
            } else if (temp.equals(SQLKeywords.LT)){
                d=lt(val1,val2);
            } else if (temp.equals(SQLKeywords.GT)){
                d=gt(val1,val2);
            } else if (temp.equals(SQLKeywords.LIKE)){
                if(valueT2.charAt(0)=='%')
                    valueT2 = "/"+valueT2.substring(1);
                else valueT2 = "/^"+valueT2.substring(1);
                if(valueT2.charAt(valueT2.length()-1)=='%') 
                    valueT2 = valueT2.substring(0,valueT2.length()-2)+"/";
                else valueT2 = valueT2.substring(0,valueT2.length()-2)+"/";
                d= regex(valueT1, valueT2);
            } 
            i+=3;
        }
        temp = token.get(i++).getValue().toUpperCase();
        if(temp.equals(SQLKeywords.AND) ){
            d= and(d,translateWhere(token));
        }
        else if (temp.equals(SQLKeywords.OR))
            d = or(d, translateWhere(token));
     //   }
        return d;
    }
}
