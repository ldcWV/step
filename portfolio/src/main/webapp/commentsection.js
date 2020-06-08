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

function initComments() {
    getComments();
    checkUsername();
}

function getComments() {
    const commentsPerBlock = getCommentsPerBlock();
    document.getElementById('commentsPerBlock').value = parseInt(commentsPerBlock);
    fetch('/comment-data?numcomments='+(commentsPerBlock*numBlocks))
    .then(response => response.json()).then(data => {
        let comments = data.comments;
        const commentsContainer = document.getElementById("commentsection-container");
        commentsContainer.innerHTML = "";
        comments.forEach((line) => {
            commentsContainer.appendChild(createComment(line));
        });
        if(data.totalCommentCount <= commentsPerBlock * numBlocks) {
            let showmorebutton = document.getElementById("showmorebutton");
            showmorebutton.style.display = "none";
        }
    });
}

function checkUsername() {
    let form = document.getElementById('commentForm');
    let mustLogInToComment = document.getElementById('mustLogInToComment');

    fetch('/login-data').then(response => response.json()).then(data => {
        let canComment = data.loggedIn && data.username != null;
        if(canComment) {
            console.log("1");
            form.style.display = "inline";
            mustLogInToComment.display = "none";
        } else {
            console.log("2");
            form.style.display = "none";
            mustLogInToComment.display = "inline";
        }
    });
}

var numBlocks = 1;

function showMore() {
    numBlocks++;
    getComments();
}

function createComment(commentData) {
    let res = document.createElement("div");
    res.setAttribute("class", "single-comment");
    
    let voting = document.createElement("div");
    voting.setAttribute("class", "voting");

    let upvote = document.createElement("div");
    upvote.setAttribute("class", "upvote");
    upvote.onclick = function() {voteComment(commentData.id, 1)};
    let upvoteSymbol = document.createElement("div");
    upvoteSymbol.setAttribute("class", "upvoteSymbol");
    upvoteSymbol.innerText = "▲";
    let upvoteCount = document.createElement("div");
    upvoteCount.setAttribute("class", "upvoteCount");
    upvoteCount.innerText = commentData.upvotes;
    upvote.appendChild(upvoteSymbol);
    upvote.appendChild(upvoteCount);

    let downvote = document.createElement("div");
    downvote.setAttribute("class", "downvote");
    downvote.onclick = function() {voteComment(commentData.id, -1)};
    let downvoteSymbol = document.createElement("div");
    downvoteSymbol.setAttribute("class", "downvoteSymbol");
    downvoteSymbol.innerText = "▼";
    let downvoteCount = document.createElement("div");
    downvoteCount.setAttribute("class", "downvoteCount");
    downvoteCount.innerText = commentData.downvotes;
    downvote.appendChild(downvoteSymbol);
    downvote.appendChild(downvoteCount);

    voting.appendChild(upvote);
    voting.appendChild(downvote);

    let commentHeader = document.createElement("div");
    commentHeader.setAttribute("class", "comment-header");

    let username = document.createElement("span");
    username.setAttribute("class", "username");
    username.innerText = commentData.username;

    let timeText = "";
    let val = -1;
    let units = "";

    let time = commentData.elapsedTime;
    let secondInMs = 1000;
    let minuteInMs = 60*secondInMs;
    let hourInMs = 60*minuteInMs;
    let dayInMs = 24*hourInMs;
    let yearInMs = 365*dayInMs;
    if(time < minuteInMs) {
        val = Math.floor(time/secondInMs);
        units = "second";
    } else if(time < hourInMs) {
        val = Math.floor(time/minuteInMs);
        units = "minute";
    } else if(time < dayInMs) {
        val = Math.floor(time/hourInMs);
        units = "hour";
    } else if(time < yearInMs) {
        val = Math.floor(time/dayInMs);
        units = "day";
    } else {
        val = Math.floor(time/yearInMs);
        units = "year";
    }
    timeText = val + " " + units;
    if(val != 1) {
        timeText += "s";
    }
    timeText += " ago";
    username.innerText += " (" + timeText + ")";

    let deleteButton = document.createElement("button");
    deleteButton.innerText = "Delete";
    deleteButton.setAttribute("class", "deletebutton");
    deleteButton.onclick = function() {deleteComment(commentData.id)};

    let comment = document.createElement("div");
    comment.setAttribute("class", "comment-content");
    comment.innerText = commentData.comment;
    
    commentHeader.appendChild(username);
    commentHeader.appendChild(deleteButton);

    res.appendChild(commentHeader);
    res.appendChild(comment);
    res.appendChild(voting);
    return res;
}

function getCommentsPerBlock() {
    let tmp = (new URL(document.location)).searchParams;
    let res = tmp.get("numcomments");
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

function deleteComment(id) {
  const params = new URLSearchParams();
  params.append('id', id);
  fetch('/delete-data', {method: 'post', body: params}).then(response => {
      getComments();
  });
}

function voteComment(id, delta) {
    const params = new URLSearchParams();
    params.append('id', id);
    params.append('delta', delta);
    fetch('/vote-data', {method: 'post', body: params}).then(response => {
        getComments();
    });
}