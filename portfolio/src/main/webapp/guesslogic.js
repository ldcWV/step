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

// Prevents page from refreshing when submit is pressed
function onAction(event) { event.preventDefault(); } 
document.getElementById("guesser").addEventListener('submit', onAction);

// Submits the number in the textbox and gives feedback.
function submitNum() {
    var guess = document.getElementById('guess').value;
    const guessContainer = document.getElementById('guess-value');
    const feedbackContainer = document.getElementById('guess-feedback');

    guessContainer.innerText = guess;

    if(guess > 24) {
        feedbackContainer.innerText = " is greater than my favorite number";
    } else if(guess < 24) {
        feedbackContainer.innerText = " is less than my favorite number";
    } else {
        feedbackContainer.innerText = " is my favorite number!";
    }
}
