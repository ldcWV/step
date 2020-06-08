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
import com.google.sps.data.LoginInfo;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login-data")
public class LoginServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        UserService userService = UserServiceFactory.getUserService();
        LoginInfo data = null;
        if (userService.isUserLoggedIn()) {
            String userEmail = userService.getCurrentUser().getEmail();
            String urlToRedirectToAfterUserLogsOut = "/login.html";
            String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
            String username = getUsername(userService.getCurrentUser().getUserId());
            if(username == null) {
                data = new LoginInfo(true, userEmail, null, null, logoutUrl);
            } else {
                data = new LoginInfo(true, userEmail, username, null, logoutUrl);
            }
        } else {
            String urlToRedirectToAfterUserLogsIn = "/login.html";
            String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
            data = new LoginInfo(false, null, null, loginUrl, null);
        }
        String json = new Gson().toJson(data);
        response.getWriter().println(json);
    }

    /** Returns the username of the user with id, or null if the user has not set a username. */
    private String getUsername(String id) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query =
            new Query("UserInfo")
                .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
        PreparedQuery results = datastore.prepare(query);
        Entity entity = results.asSingleEntity();
        if (entity == null) {
            return null;
        }
        String username = (String) entity.getProperty("username");
        return username;
    }
}