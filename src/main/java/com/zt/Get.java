package com.zt;

import com.mongodb.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Endpoint;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringJoiner;


public class Get extends javax.servlet.http.HttpServlet {

    PrintWriter writer;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setHeader("Content-Type", "application/json");

        writer = resp.getWriter();

        String host = System.getProperty("mongo.host", "localhost");

        MongoClient mongoClient = new MongoClient(host, 27017);
        DB db = mongoClient.getDB("mydb");
        DBCollection supplements = db.getCollection("supplements");
        DBCollection supplementPrices = db.getCollection("supplement_prices");

        DBCursor cursor = supplements.find();
        try {
            StringJoiner sj = new StringJoiner(",", "[", "]");
            while (cursor.hasNext()) {
                DBObject supplement = cursor.next();
                Object priceId = supplement.get("price_id");

                BasicDBObject query = new BasicDBObject("id", new BasicDBObject("$eq", priceId));
                DBObject price = supplementPrices.findOne(query);

                String name = String.valueOf(supplement.get("name"));
                String value = String.valueOf(price.get("value"));

                String item = "{\"name\": \"" + name + "\", \"price\": \"" + value + "\"}";

                sj.add(item);
            }
            writer.write(sj.toString());
        } finally {
            cursor.close();
            writer.flush();
            writer.close();
        }
    }

}
