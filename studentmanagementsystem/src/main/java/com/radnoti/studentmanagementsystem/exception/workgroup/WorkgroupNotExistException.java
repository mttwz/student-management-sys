package com.radnoti.studentmanagementsystem.exception.workgroup;

public class WorkgroupNotExistException extends RuntimeException{
    public WorkgroupNotExistException() {
        super("Workgroup does not exist");
    }
}
