package com.radnoti.studentmanagementsystem.exception.workgroup;

public class WorkgroupAlreadyDeletedException extends RuntimeException{
    public WorkgroupAlreadyDeletedException() {
        super("Workgroup is already deleted");
    }
}
