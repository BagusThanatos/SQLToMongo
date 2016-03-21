/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQLTranslator;

import MongoInterface.MongoDB;
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
    public MongoQuery translate(ArrayList<TokenLexic> tokens){
        String mongo=null;
        MongoQuery query = new MongoQuery();
        String type = tokens.get(0).getValue();
        if (type.toUpperCase().equals(SQLKeywords.INSERT)){
            String collection = tokens.get(2).getValue();
            ArrayList<String> fields = new ArrayList(), values = new ArrayList();
            boolean value = false;
            for (int i=4; i< tokens.size();i++){
                String temp = tokens.get(i).getValue();
                if (temp.equals(SQLKeywords.VALUES)) value = true;
                else {
                    if (!(temp.equals(SQLKeywords.COMMA) || 
                            temp.equals(SQLKeywords.KURTUP) ||
                            temp.equals(SQLKeywords.KURKA)|| temp.equals(SQLKeywords.SEMICOLON))) {
                        if(value) values.add(temp);
                        else fields.add(temp);
                    } 
                        
                }
            }
            mongo = "db."+collection+".insert({";
            mongo += fields.get(0)+": "+values.get(0);
            Document doc = new Document();
            for(int i =1 ;i<fields.size();i++){
                mongo +=","+fields.get(i)+": "+values.get(i);
                doc.append(fields.get(i), values.get(i));
            }
            mongo +="})";
            query.setCollection(collection);
            query.setValues(doc);
            query.setQuery(mongo);
            query.setType(MongoQuery.Type.INSERT);
        }
        else if (type.equals(SQLKeywords.SELECT)){
            String temp = tokens.get(1).getValue().toUpperCase();
            List<String> fields=null;
            Bson cond = null;
            int i =1;
            if(!temp.equals("ALL")&& !temp.equals("*")){
                temp = tokens.get(i).getValue();
                fields = new ArrayList();
                while (!temp.toUpperCase().equals(SQLKeywords.FROM)){
                    if(!temp.equals(SQLKeywords.COMMA)){
                        fields.add(temp);
                    }
                    temp=tokens.get(++i).getValue();
                }    
            }
            String coll = tokens.get(++i).getValue();
            temp = tokens.get(++i).getValue().toUpperCase();
            if(temp.equals(SQLKeywords.WHERE)){
                cond= translateWhere(tokens, i+1);
            }
            query.setFields(fields);
            query.setCollection(coll);
            query.setCond(cond);
            query.setType(MongoQuery.Type.SELECT);
        }
            
        
        return query;
    }
    private Bson translateWhere(List<TokenLexic> token,int i){
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
            String valueT1 = temp1.getValue();
            String valueT2 = temp2.getValue();
            boolean var1 = temp1.getTokenCode()==Parser.VARIABLE;
            if (temp.equals(SQLKeywords.EQ)){
                if (var1) d = eq(valueT1,valueT2);
                else d = eq(valueT2, valueT1);
            } else if (temp.equals(SQLKeywords.GTE)){
                if (var1) d = gte(valueT1,valueT2);
                else d = gte(valueT2, valueT1);
            } else if (temp.equals(SQLKeywords.LTE)){
                if (var1) d = lte(valueT1,valueT2);
                else d = lte(valueT2, valueT1);
            } else if (temp.equals(SQLKeywords.LT)){
                if (var1) d = lt(valueT1,valueT2);
                else d = lt(valueT2, valueT1);
            } else if (temp.equals(SQLKeywords.GT)){
                if (var1) d = gt(valueT1,valueT2);
                else d = gt(valueT2, valueT1);
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
        temp = token.get(i).getValue().toUpperCase();
        if(temp.equals(SQLKeywords.AND) ){
            d= and(d,translateWhere(token, i+1));
        }
        else if (temp.equals(SQLKeywords.OR))
            d = or(d, translateWhere(token, i+1));
     //   }
        return d;
    }
}
