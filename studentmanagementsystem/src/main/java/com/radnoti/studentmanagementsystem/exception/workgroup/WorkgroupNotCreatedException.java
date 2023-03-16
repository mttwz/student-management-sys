package com.radnoti.studentmanagementsystem.exception.workgroup;

public class WorkgroupNotCreatedException extends RuntimeException{
    public WorkgroupNotCreatedException() {
        super("Workgroup does not created");
    }
}
