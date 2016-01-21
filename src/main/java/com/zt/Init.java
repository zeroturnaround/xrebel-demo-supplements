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

      supplements.insert(new BasicDBObject("name", "apples").append("price_id", "1"));
      supplements.insert(new BasicDBObject("name", "babanas").append("price_id", "2"));
      supplements.insert(new BasicDBObject("name", "carrots").append("price_id", "3"));

      supplementPrices.insert(new BasicDBObject("id", "1").append("value", "123.23"));
      supplementPrices.insert(new BasicDBObject("id", "2").append("value", "234.54"));
      supplementPrices.insert(new BasicDBObject("id", "3").append("value", "345.65"));

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
