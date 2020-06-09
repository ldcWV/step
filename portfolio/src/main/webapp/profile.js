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
google.charts.setOnLoadCallback(drawCharacterDistribution);

function loadProfile() {
    let heading = document.getElementById('heading');
    fetch('/profile-data').then(response => response.json()).then(userInfo => {
        heading.innerText = userInfo.loginInfo.username + "'s Profile";
        drawCharacterDistribution(userInfo.comments);
    });
}

function drawCharacterDistribution(comments) {
    console.log(comments);
    let letters = comments.toLowerCase();
    console.log(letters);
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
