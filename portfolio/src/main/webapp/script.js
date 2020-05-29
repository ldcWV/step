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

var clickCounter = 0;

/**
 * Increments the click counter.
 */
function countClick() {
    clickCounter++;
    const clickcounterContainer = document.getElementById('clickcounter-container');
    clickcounterContainer.innerText = clickCounter;
    const favnumberContainer = document.getElementById('favnumber-container');
    if(clickCounter == 24) {
        favnumberContainer.innerText = " is my favorite number!";
    } else {
        favnumberContainer.innerText = " is not my favorite number...";
    }
}