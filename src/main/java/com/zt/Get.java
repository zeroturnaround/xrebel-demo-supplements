package com.zt;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


public class Get extends javax.servlet.http.HttpServlet {

  PrintWriter writer;

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {

    try {
      resp.setHeader("Content-Type", "application/json");

      writer = resp.getWriter();

      String host = System.getProperty("mongo.host", "localhost");
      int port = 27017;

      System.out.println("Talking to Mongo at " + host + ":" + port);

      MongoClient mongoClient = new MongoClient(host, port);
      DB db = mongoClient.getDB("mydb");
      DBCollection supplements = db.getCollection("supplements");
      DBCollection supplementPrices = db.getCollection("supplement_prices");

      DBCursor cursor = supplements.find();

      StringBuilder sj = new StringBuilder();
      sj.append("[");
      while (cursor.hasNext()) {
        DBObject supplement = cursor.next();
        Object priceId = supplement.get("price_id");

        BasicDBObject query = new BasicDBObject("id", new BasicDBObject("$eq", priceId));
        DBObject price = supplementPrices.findOne(query);

        String name = String.valueOf(supplement.get("name"));
        String value = String.valueOf(price.get("value"));
        String item = "{\"name\": \"" + name + "\", \"price\": \"" + value + "\"}";

        sj.append(item);
        if (cursor.hasNext()) {
          sj.append(",");
        }
      }
      sj.append("]");
      writer.write(sj.toString());
      cursor.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      writer.flush();
      writer.close();
    }
  }

}
