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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for upvoting/downvoting. */
@WebServlet("/vote-data")
public class VoteServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        long id = Long.parseLong(request.getParameter("id"));
        Key commentEntityKey = KeyFactory.createKey("Comment", id);
        int delta = Integer.parseInt(request.getParameter("delta"));
        Entity commentEntity = null;
        try {
            commentEntity = datastore.get(commentEntityKey);
        } catch(Exception e) {
            return;
        }

        if(delta == -1) {
            long prev = (long) commentEntity.getProperty("downvotes");
            commentEntity.setProperty("downvotes", prev+1);
            datastore.put(commentEntity);
        } else {
            long prev = (long) commentEntity.getProperty("upvotes");
            commentEntity.setProperty("upvotes", prev+1);
            datastore.put(commentEntity);
        }
    }
}