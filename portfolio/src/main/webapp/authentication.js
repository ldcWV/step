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
        if(data.loggedIn) {
            loginContainer.appendChild(makeParagraph("You are already logged in."));
            
            let username = data.username;
            if(username == null) {
                loginContainer.appendChild(makeParagraph("You have not set a username"));
                loginContainer.appendChild(makeLink("Create username", "/username.html"));
                loginContainer.appendChild(document.createElement("br"));
            } else {
                loginContainer.appendChild(makeParagraph("You are signed in as " + username));
            }
            loginContainer.appendChild(makeLink("Log out", data.logoutUrl));
        } else {
            loginContainer.appendChild(makeParagraph("You are not logged in."));
            loginContainer.appendChild(makeLink("Log in", data.loginUrl));
        }
    });
}

function makeParagraph(s) {
    let res = document.createElement("p");
    res.innerText = s;
    return res;
}

function makeLink(text, url) {
    let res = document.createElement("a");
    res.setAttribute("href", url);
    res.innerText = text;
    return res;
}