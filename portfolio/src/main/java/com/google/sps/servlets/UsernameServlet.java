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
import com.google.sps.data.UserInfo;
import com.google.sps.data.Utils;
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

@WebServlet("/username-data")
public class UsernameServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()) {
            response.sendRedirect("/login.html");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()) {
            response.sendRedirect("/profile.html");
            return;
        }

        String username = request.getParameter("username");
        String id = userService.getCurrentUser().getUserId();
        String userEmail = userService.getCurrentUser().getEmail();
        String urlToRedirectToAfterUserLogsOut = "/login.html";
        String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
        String comments = "";
        long upvotesReceived = 0;
        long downvotesReceived = 0;
        String profilePictureUrl = "images/defaultProfilePicture.jpg";

        // check if user has already made a username
        Entity tmp = Utils.getEntity(id);
        if(tmp != null) {
            response.sendRedirect("/profile.html");
            return;
        }

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity("UserInfo", id);
        entity.setProperty("id", id);
        entity.setProperty("email", userEmail);
        entity.setProperty("username", username);
        entity.setProperty("logouturl", logoutUrl);
        entity.setProperty("comments", comments);
        entity.setProperty("upvotesReceived", upvotesReceived);
        entity.setProperty("downvotesReceived", downvotesReceived);
        entity.setProperty("profilePictureUrl", profilePictureUrl);
        datastore.put(entity);

        response.sendRedirect("/profile.html");
    }
}
