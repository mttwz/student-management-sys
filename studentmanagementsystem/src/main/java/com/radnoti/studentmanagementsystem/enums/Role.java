package com.radnoti.studentmanagementsystem.enums;

public enum Role {
    SUPERADMIN(Types.SUPERADMIN),
    ADMIN(Types.ADMIN),
    STUDENT(Types.STUDENT);
    public class Types {
        public static final String SUPERADMIN = "SUPERADMIN";
        public static final String ADMIN = "ADMIN";
        public static final String STUDENT = "STUDENT";
    }

    private final String label;

    private Role(String label) {
        this.label = label;
    }

    public String toString() {
        return this.label;
    }



}
