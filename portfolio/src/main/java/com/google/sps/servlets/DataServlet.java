// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.sps.data.Comment;
import com.google.sps.data.CommentList;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.QueryResultList;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/comment-data")
public class DataServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ArrayList<Comment> comments = new ArrayList<>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        int lim = getNumComments(request);

        FetchOptions fetchOptions = FetchOptions.Builder.withLimit(lim);
        Query query = new Query("Comment").addSort("time", SortDirection.DESCENDING);
        PreparedQuery pq = datastore.prepare(query);
        QueryResultList<Entity> results;
        try {
            results = pq.asQueryResultList(fetchOptions);
        } catch(Exception e) {
            return;
        }
        for (Entity entity : results) {
            String username = (String)entity.getProperty("username");
            String comment = (String)entity.getProperty("comment");
            long time = (long)entity.getProperty("time");
            long id = entity.getKey().getId();
            long upvotes = (long)entity.getProperty("upvotes");
            long downvotes = (long)entity.getProperty("downvotes");
            comments.add(new Comment(username, comment, System.currentTimeMillis()-time, id, upvotes, downvotes));
        }

        CommentList data = new CommentList(comments, pq.countEntities(FetchOptions.Builder.withLimit(100000)));
        String json = new Gson().toJson(data);
        response.getWriter().println(json);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("username", getClientUsername(request));
        commentEntity.setProperty("comment", getClientComment(request));
        commentEntity.setProperty("time", System.currentTimeMillis());
        commentEntity.setProperty("upvotes", 0);
        commentEntity.setProperty("downvotes", 0);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);

        response.sendRedirect("/index.html");
    }
    
    private String getClientUsername(HttpServletRequest request) {
        return request.getParameter("username");
    }

    private String getClientComment(HttpServletRequest request) {
        return request.getParameter("comment");
    }

    private int getNumComments(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("numcomments"));
    }
}
