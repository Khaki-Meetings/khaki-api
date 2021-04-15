package com.getkhaki.api.bff.web.models;

public enum GoalMeasureDte {
    AttendeesPerMeeting("AttendeesPerMeeting"),
    AverageMeetingLength("AverageMeetingLength"),
    StaffTimeInMeetings("StaffTimeInMeetings"),
    MeetingPercentageThreshold("MeetingPercentageThreshold"),
    EmployeeDailyMeetingTime("EmployeeDailyMeetingTime"),
    EmployeeMeetingsPerDay("EmployeeMeetingsPerDay"),
    AverageStaffTimePerMeeting("AverageStaffTimePerMeeting");

    public final String label;

    private GoalMeasureDte(String label) {
        this.label = label;
    }
}