package com.radnoti.studentmanagementsystem.exception.workgroup;

public class WorkgroupNotDeletedException extends RuntimeException{
    public WorkgroupNotDeletedException() {
        super("Workgroup is not deleted");
    }
}
