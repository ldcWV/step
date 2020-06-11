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

google.charts.load('current', {'packages':['corechart']});

function loadProfile() {
    let hasUsername = false;
    let loginContainer = document.getElementById("login-container");
    fetch('/login-data').then(response => response.json()).then(data => {
        if(data.loggedIn) {
            loginContainer.appendChild(makeParagraph("You are already logged in."));
            
            if(data.username == null) {
                loginContainer.appendChild(makeParagraph("You have not set a username"));
                loginContainer.appendChild(makeLink("Create username", "/username.html"));
                loginContainer.appendChild(document.createElement("br"));
                loginContainer.appendChild(makeLink("Log out", data.logoutUrl));
            } else {
                loginContainer.appendChild(makeParagraph("You are signed in as " + data.username));
                loginContainer.appendChild(makeLink("Log out", data.logoutUrl));
                hasUsername = true;
            }
        } else {
            loginContainer.appendChild(makeParagraph("You are not logged in."));
            loginContainer.appendChild(makeLink("Log in", data.loginUrl));
        }
        if(hasUsername) {
            let heading = document.getElementById('heading');
            fetch('/profile-data').then(response => response.json()).then(userInfo => {
                heading.innerText = userInfo.loginInfo.username + "'s Profile";
                drawCharacterDistribution(userInfo.comments);
                drawWordLengthDistribution(userInfo.comments);
                drawVotingPieChart(userInfo.upvotesReceived, userInfo.downvotesReceived);
            });
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

function drawCharacterDistribution(comments) {
    let letters = comments.toLowerCase();
    let alphabet = "abcdefghijklmnopqrstuvwxyz";
    let rows = [];
    for(i = 0; i < 26; i++) {
        rows.push([alphabet.charAt(i), 0]);
    }
    for(i = 0; i < letters.length; i++) {
        let c = letters.charAt(i);
        let idx = c.charCodeAt(0) - 'a'.charCodeAt(0);
        if(0 <= idx && idx < 26) {
            rows[idx][1]++;
        }
    }

    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Letter');
    data.addColumn('number', 'Count');
    data.addRows(rows);
    const options = {
        'title': 'Letter Frequency',
        'width':700,
        'height':800
    };
    const chart = new google.visualization.BarChart (
        document.getElementById('character-distribution'));
    chart.draw(data, options);
}

function drawWordLengthDistribution(comments) {
    let tosplit = ".,;:<>!@#$%^&*()\"/{}[]|\\`~?"
    let words = comments;
    for(i = 0; i < tosplit.length; i++) {
        words = words.split(tosplit.charAt(i));
        words = words.join(" ");
    }
    words = words.split(" ");
    let arr = [["Word Length"]];
    for(i = 0; i < words.length; i++) 
        if(words[i].length != 0) arr.push([words[i].length]);
    let data = google.visualization.arrayToDataTable(arr);

    const options = {
        title: 'Word Length Distribution',
        'width':700,
        'height':800,
        histogram: { lastBucketPercentile: 5 }
    };

    const chart = new google.visualization.Histogram(
        document.getElementById('wordlength-distribution'));
    chart.draw(data, options);
}

function drawVotingPieChart(upvotes, downvotes) {
    const data = new google.visualization.DataTable();
  	data.addColumn('string', 'Votes');
  	data.addColumn('number', 'Count');
    data.addRows([
        ['Upvotes received', upvotes],
        ['Downvotes received', downvotes]
    ]);

  	const options = {
    	'title': 'Votes received',
    	'width':700,
    	'height':800,
        slices: {
        	0: { color: 'green' },
            1: { color: 'red' }
        }
  	};

  	const chart = new google.visualization.PieChart(
      	document.getElementById('voting-piechart'));
  	chart.draw(data, options);
}
