package com.zt;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Init implements ServletContextListener {

  static boolean connected = false;

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    String host = System.getProperty("mongo.host", "localhost");
    int port = 27017;
    System.out.println("Initializing Mongo at " + host + ":" + port);

    try {
      MongoClient mongoClient = new MongoClient(host, port);

      DB db = mongoClient.getDB("mydb");

      DBCollection supplements = db.getCollection("supplements");
      DBCollection supplementPrices = db.getCollection("supplement_prices");

      supplements.drop();
      supplementPrices.drop();

      System.out.println("Collections cleaned up");

      supplements.insert(new BasicDBObject("name", "apple").append("price_id", "1"));
      supplements.insert(new BasicDBObject("name", "babana").append("price_id", "2"));
      supplements.insert(new BasicDBObject("name", "carrot").append("price_id", "3"));
      supplements.insert(new BasicDBObject("name", "potato").append("price_id", "4"));
      supplements.insert(new BasicDBObject("name", "onion").append("price_id", "5"));
      supplements.insert(new BasicDBObject("name", "melon").append("price_id", "6"));

      supplementPrices.insert(new BasicDBObject("id", "1").append("value", "123.23"));
      supplementPrices.insert(new BasicDBObject("id", "2").append("value", "234.54"));
      supplementPrices.insert(new BasicDBObject("id", "3").append("value", "345.65"));
      supplementPrices.insert(new BasicDBObject("id", "4").append("value", "543.44"));
      supplementPrices.insert(new BasicDBObject("id", "5").append("value", "112.11"));
      supplementPrices.insert(new BasicDBObject("id", "6").append("value", "662.13"));

      System.out.println("Number of docs in supplements collection: " + supplements.getCount());
      System.out.println("Number of docs in supplementsPrices collection: " + supplementPrices.getCount());

      mongoClient.close();

      connected = true;
    } catch (Exception e) {
      System.err.println("Could not connect to Mongo instance: " + e.getMessage());
    }

  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
