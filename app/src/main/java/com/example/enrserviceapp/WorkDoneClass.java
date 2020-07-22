package com.example.enrserviceapp;

class WorkDoneClass{
    private String workerId;
    private String customerId;
    private String startingTime, endTime;
    private long breakTime;
    private String workType;
    private String startingDate, endingDate;
    private String title;
    WorkDoneClass(){}
    public WorkDoneClass(String workerId, String customerId, String startingTime, String endTime, long breakTime, String workType, String startingDate, String endingDate, String title) {
        this.workerId = workerId;
        this.customerId = customerId;
        this.startingTime = startingTime;
        this.endTime = endTime;
        this.breakTime = breakTime;
        this.workType = workType;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.title = title;
    }
    public String getStartingTime() {
        return startingTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public String getTitle() {
        return title;
    }
    public String getStartingDate() {
        return startingDate;
    }
    public String getEndingDate() {
        return endingDate;
    }
    public String getWorkType() {
        return workType;
    }
    public String getWorkerId() {
        return workerId;
    }
    public String getCustomerId() {
        return customerId;
    }
    public long getBreakTime() {
        return breakTime;
    }
    public String startTime(){return getStartingTime();}
    public String endingTime(){return getEndTime();}
}
