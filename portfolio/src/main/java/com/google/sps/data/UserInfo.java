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

import java.util.ArrayList;

// Represents the information displayed on user's profile page
public class UserInfo {
    public final LoginInfo loginInfo;
    public String comments;
    public long upvotesReceived;
    public long downvotesReceived;
    public String uploadImageUrl;
    public String profilePictureUrl;
    
    public UserInfo(LoginInfo loginInfo, String comments, long upvotesReceived, long downvotesReceived, String uploadImageUrl, String profilePictureUrl) {
        this.loginInfo = loginInfo;
        this.comments = comments;
        this.upvotesReceived = upvotesReceived;
        this.downvotesReceived = downvotesReceived;
        this.uploadImageUrl = uploadImageUrl;
        this.profilePictureUrl = profilePictureUrl;
    }
}
