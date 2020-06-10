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
import com.google.gson.Gson;
import com.google.sps.data.Utils;
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

@WebServlet("/profile-data")
public class ProfileServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        UserService userService = UserServiceFactory.getUserService();

        // email and username must be set up
        String id = userService.getCurrentUser().getUserId();
        Entity userInfoEntity = Utils.getEntity(id);
        String userEmail = (String) userInfoEntity.getProperty("email");
        String username = (String) userInfoEntity.getProperty("username");
        String logoutUrl = (String) userInfoEntity.getProperty("logouturl");
        String comments = (String) userInfoEntity.getProperty("comments");
        long upvotesReceived = (long) userInfoEntity.getProperty("upvotesReceived");
        long downvotesReceived = (long) userInfoEntity.getProperty("downvotesReceived");
        
        LoginInfo loginInfo = new LoginInfo(true, userEmail, username, null, logoutUrl);
        UserInfo userInfo = new UserInfo(loginInfo, comments, upvotesReceived, downvotesReceived);

        String json = new Gson().toJson(userInfo);
        response.getWriter().println(json);
    }
}
