/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MongoInterface;

import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author BagusThanatos (github.com/BagusThanatos)
 */
public class MongoQuery {
    private String collection,query,orderField;
    private Type type;
    private Bson cond;
    private Document values;
    private List<String> fields;
    private boolean order;
    private boolean asc;
 
    public static enum Type {
        INSERT,
        SELECT,
        UPDATE,
        DELETE
    }
    
    public MongoQuery(){
      this.order = false;
    }
    public String getCollection() {
        return collection;
    }

  public boolean isAsc() {
    return asc;
  }

  public void setAsc(boolean asc) {
    this.asc = asc;
  }

  public String getOrderField() {
    return orderField;
  }
  public void setOrder(boolean b){
    this.order= b;
  }

  public void setOrderField(String orderField) {
    this.orderField = orderField;
  }

    public boolean isOrder() {
      return order;
    }
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public Bson getCond() {
        return cond;
    }

    public void setCond(Bson cond) {
        this.cond = cond;
    }

    public Document getValues() {
        return values;
    }

    public void setValues(Document values) {
        this.values = values;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
    
    @Override
    public String toString(){
        return query;
    }
}
