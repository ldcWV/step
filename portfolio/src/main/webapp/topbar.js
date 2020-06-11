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

createTopBar();

/** Creates a navigation bar and adds it to the page. */
function createTopBar() {
  	let topbar = document.getElementsByClassName("topbar")[0];
    
    let name = document.createElement("div");
    name.setAttribute("class", "name");
    name.innerText = "Lawrence Chen";
    topbar.appendChild(name);

    let menu = document.createElement("span");
    menu.setAttribute("class", "menu");
    menu.appendChild(createLink("index.html", "Home"));
    menu.appendChild(createLink("aboutme.html", "About Me"));
    menu.appendChild(createLink("projects.html", "Projects"));
    menu.appendChild(createLink("https://www.linkedin.com/in/lawrence-chen-6ab742158/", "LinkedIn"));
    menu.appendChild(createLink("https://github.com/ldcWV", "GitHub"));
    menu.appendChild(createLink("resume.pdf", "Resume"));
    topbar.appendChild(menu);

    let otherlinks = document.createElement("span");
    otherlinks.setAttribute("class", "otherlinks");
    otherlinks.appendChild(createLink("login.html", "Log in"));
    otherlinks.appendChild(createLink("profile.html", "My Profile"));
    topbar.appendChild(otherlinks);
}

function createLink(url, text) {
    let res = document.createElement("a");
    res.setAttribute("href", url);
    res.innerText = text;
    return res;
}