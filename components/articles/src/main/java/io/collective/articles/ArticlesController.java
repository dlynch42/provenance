package io.collective.articles;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.collective.restsupport.BasicHandler;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

public class ArticlesController extends BasicHandler {
    private final ArticleDataGateway gateway;

    public ArticlesController(ObjectMapper mapper, ArticleDataGateway gateway) {
        super(mapper);
        this.gateway = gateway;
    }

    // Handles incoming HTTP request for specified endpoint
    @Override
    public void handle(String target, Request request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        // Articles endpoint
        get("/articles", List.of("application/json", "text/html"), request, servletResponse, () -> {

            { // todo - query the articles gateway for *all* articles, map record to infos, and send back a collection of article infos
                // Create a list to store ArticleInfo objects
                List<ArticleInfo> articles = new ArrayList<>();

                // Iterate through all article records
                gateway.findAll().forEach(article -> 
                    // Map each record to an articleInfo object
                    articles.add(new ArticleInfo(article.getId(), article.getTitle())));
                
                // Write the articleInfo list using the given JSON response body
                writeJsonBody(servletResponse, articles);
            }
        });

        // Available endpoint; pretty much the same as above with a different endpoint and only looking for available articles
        get("/available", List.of("application/json"), request, servletResponse, () -> {

            { // todo - query the articles gateway for *available* articles, map records to infos, and send back a collection of article infos
                // Create a list to store ArticleInfo objects
                List<ArticleInfo> articles = new ArrayList<>();

                // Iterate through all article records
                gateway.findAvailable().forEach(article -> // Changed function from findAll to findAvailable
                    // Map each record to an articleInfo object
                    articles.add(new ArticleInfo(article.getId(), article.getTitle())));
                
                // Write the articleInfo list using the given JSON response body
                writeJsonBody(servletResponse, articles);
            }
        });
    }
}
