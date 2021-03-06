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

package com.google.sps.data;

// Represents the authentication data of a user
public class LoginInfo {
    public final boolean loggedIn;
    public final String email;
    public final String username;
    public final String loginUrl;
    public final String logoutUrl;
    
    public LoginInfo(boolean loggedIn, String email, String username, String loginUrl, String logoutUrl) {
        this.loggedIn = loggedIn;
        this.email = email;
        this.username = username;
        this.loginUrl = loginUrl;
        this.logoutUrl = logoutUrl;
    }
}
