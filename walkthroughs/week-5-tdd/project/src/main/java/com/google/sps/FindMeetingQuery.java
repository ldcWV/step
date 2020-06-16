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

package com.google.sps;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class FindMeetingQuery {
    public Collection<TimeRange> getComplement(ArrayList<TimeRange> timesToAvoid, int duration) {
        // get rid of intervals contained within other intervals
        Collections.sort(timesToAvoid, TimeRange.ORDER_BY_START);
        int latestSoFar = -1;
        ArrayList<TimeRange> filteredTimesToAvoid = new ArrayList<>();
        for(TimeRange tr: timesToAvoid) {
            if(tr.end() > latestSoFar) {
                latestSoFar = tr.end();
                filteredTimesToAvoid.add(tr);
            }
        }
        // get the times in between
        Collection<TimeRange> res = new ArrayList<>();
        int curStart = TimeRange.START_OF_DAY;
        for(TimeRange tr: filteredTimesToAvoid) {
            if(tr.start() - curStart >= duration) {
                res.add(TimeRange.fromStartEnd(curStart, tr.start(), false));
            }
            curStart = tr.end();
        }
        if(TimeRange.END_OF_DAY+1 - curStart >= duration)
            res.add(TimeRange.fromStartEnd(curStart, TimeRange.END_OF_DAY+1, false));
        return res;
    }

    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        Set<String> mandatoryAttendees = new HashSet<>();
        for(String attendee: request.getAttendees()) {
            mandatoryAttendees.add(attendee);
        }
        Set<String> optionalAttendees = new HashSet<>();
        for(String attendee: request.getOptionalAttendees()) {
            optionalAttendees.add(attendee);
        }
        int duration = (int)request.getDuration();
        if(mandatoryAttendees.size() == 0) {
            ArrayList<TimeRange> timesToAvoid = new ArrayList<>();
            for(Event event: events) {
                if(!Collections.disjoint(event.getAttendees(), optionalAttendees)) {
                    timesToAvoid.add(event.getWhen());
                }
            }
            return getComplement(timesToAvoid, duration);
        } else {
            ArrayList<TimeRange> timesToAvoid = new ArrayList<>();
            for(Event event: events) {
                if(!Collections.disjoint(event.getAttendees(), mandatoryAttendees) || !Collections.disjoint(event.getAttendees(), optionalAttendees)) {
                    timesToAvoid.add(event.getWhen());
                }
            }
            Collection<TimeRange> ans = getComplement(timesToAvoid, duration);
            if(ans.size() != 0) return ans;
            
            timesToAvoid = new ArrayList<>();
            for(Event event: events) {
                if(!Collections.disjoint(event.getAttendees(), mandatoryAttendees)) {
                    timesToAvoid.add(event.getWhen());
                }
            }
            return getComplement(timesToAvoid, duration);
        }
    }
}
