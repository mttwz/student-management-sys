package com.radnoti.studentmanagementsystem.exception.workgroupSchedule;

public class WorkgroupScheduleNotDeletedException extends RuntimeException{
    public WorkgroupScheduleNotDeletedException() {
        super("Workgroup schedule does not deleted");
    }
}
