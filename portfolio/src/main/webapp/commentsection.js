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

function getComments() {
    const showNum = getCommentLimit();
    document.getElementById('shownum').value = parseInt(showNum);
    fetch('/comment-data?shownum='+showNum).then(response => response.json()).then((comments) => {
        const commentsContainer = document.getElementById("commentsection-container");
        commentsContainer.innerHTML = "";
        comments.forEach((line) => {
            commentsContainer.appendChild(createComment(line));
        });
    });
}

function createComment(commentData) {
    let res = document.createElement("div");
    res.setAttribute("class", "single-comment");
    
    let username = document.createElement("div");
    username.setAttribute("class", "username");
    username.innerText = commentData.username + ":";

    let comment = document.createElement("div");
    comment.setAttribute("class", "comment");
    comment.innerText = commentData.comment;
    
    res.appendChild(username);
    res.appendChild(comment);
    return res;
}

function getCommentLimit() {
    let tmp = (new URL(document.location)).searchParams;
    let res = tmp.get("shownum");
    if(!res || res.length === 0) {
        return "10";
    }
    return res;
}

function deleteAllComments() {
    fetch('/delete-data', {method: 'post'}).then(response => {
        getComments();
    });
}