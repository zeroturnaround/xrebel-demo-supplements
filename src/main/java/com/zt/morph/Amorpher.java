package com.zt.morph;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Amorpher extends javax.servlet.http.HttpServlet {


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Morphia morphia = new Morphia();
        Datastore ds = morphia.map(Repository.class).
                createDatastore(new MongoClient("localhost", 27017), "mydb");

        Organization org = new Organization("MegaOrg");
        Repository repository = new Repository(org, "MegaRepository");
        repository.owner = new GithubUser("Antonius");
        repository.owner.fullName = "Anton";
        repository.owner.repositories = Arrays.asList(repository);
        ds.save(repository);

        PrintWriter writer = resp.getWriter();
        writer.write("<html>\n" +
                "<head><title>Amorpher</title></head>\n" +
                "<body>\n" +
                "<p>Hello from Amorpher!</p>\n" +
                "</body>\n" +
                "</html>");

        writer.flush();
        writer.close();
    }

}
