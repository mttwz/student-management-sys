package com.radnoti.studentmanagementsystem.exception.workgroupSchedule;

public class WorkgroupScheduleNotExistException extends RuntimeException{
    public WorkgroupScheduleNotExistException() {
        super("Workgroup schedule does not exist");
    }
}
