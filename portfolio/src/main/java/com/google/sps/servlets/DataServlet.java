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
import com.google.sps.data.Comment;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
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
        ArrayList<Comment> coms = new ArrayList<>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        int lim = getCommentLimit(request);
        int cnt = 0;

        Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
        PreparedQuery results = datastore.prepare(query);
        for (Entity entity : results.asIterable()) {
            String username = (String)entity.getProperty("username");
            String comment = (String)entity.getProperty("comment");
            coms.add(new Comment(username, comment));
            cnt++;
            if(cnt == lim) break;
        }

        String json = new Gson().toJson(coms);
        response.getWriter().println(json);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("username", getClientUsername(request));
        commentEntity.setProperty("comment", getClientComment(request));
        commentEntity.setProperty("timestamp", System.currentTimeMillis());

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

    private int getCommentLimit(HttpServletRequest request) {
        String s = request.getQueryString();
        if(s == null) {
            return 10;
        } else {
            return Integer.parseInt(s.substring(8,s.length()));
        }
    }
}
