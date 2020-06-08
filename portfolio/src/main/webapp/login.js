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

function getLoginInfo() {
    let loginContainer = document.getElementById("login-container");
    fetch('/login-data').then(response => response.json()).then(data => {
        let loggedIn = data.loggedIn;
        if(loggedIn) {
            loginContainer.innerText = ("You are already logged in.");
            let logoutLink = document.createElement("a");
            logoutLink.setAttribute("href", data.logoutUrl);
            logoutLink.innerText = "Log out";
            loginContainer.appendChild(logoutLink);
        } else {
            loginContainer.innerText = ("You have not yet logged in.");
            let loginLink = document.createElement("a");
            loginLink.setAttribute("href", data.loginUrl);
            loginLink.innerText = "Log in";
            loginContainer.appendChild(loginLink);
        }
    });
}