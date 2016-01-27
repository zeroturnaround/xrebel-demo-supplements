package com.zt;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;


public class Get extends javax.servlet.http.HttpServlet {

  PrintWriter writer;

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      // Thread.sleep is here just to slow down the request on purpose
      Thread.sleep(5);
      resp.setHeader("Content-Type", "application/json");
      writer = resp.getWriter();
      String json = Init.connected ? fromMongoDB() : fromGitHub();
      writer.write(json);
      throw new RuntimeException("Data flow exception");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      writer.flush();
      writer.close();
    }

  }

  private String fromGitHub() {
    StringBuilder sb = new StringBuilder();
    try {
      String spec = "https://raw.githubusercontent.com/antonarhipov/supplements/master/data.json";

      URL url = new URL(spec);
      URLConnection urlConnection = url.openConnection();
      InputStream inputStream = urlConnection.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      while((line = bufferedReader.readLine()) != null) {
        sb.append(line);
      }
      bufferedReader.close();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }

  private String fromMongoDB() throws UnknownHostException, InterruptedException {
    // Thread.sleep is here just to slow down the request on purpose
    Thread.sleep(5);
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
    cursor.close();
    return sj.toString();
  }


}
