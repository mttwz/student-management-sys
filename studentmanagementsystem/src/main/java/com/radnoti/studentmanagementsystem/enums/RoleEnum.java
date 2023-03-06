package com.radnoti.studentmanagementsystem.enums;

public enum RoleEnum {
    SUPERADMIN(1,Types.SUPERADMIN),
    ADMIN(2,Types.ADMIN),
    STUDENT(3,Types.STUDENT);
    public class Types {
        public static final String SUPERADMIN = "SUPERADMIN";
        public static final String ADMIN = "ADMIN";
        public static final String STUDENT = "STUDENT";
    }
    private final Integer id;
    private final String label;



    private RoleEnum(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public String toString() {
        return this.label;
    }


    public Integer getId() {
        return id;
    }
}
