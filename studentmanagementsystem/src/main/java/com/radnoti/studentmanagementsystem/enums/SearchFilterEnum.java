package com.radnoti.studentmanagementsystem.enums;

public enum SearchFilterEnum {

    ALL_USERS(1, SearchFilterEnum.Types.ALL_USERS),
    SUPERADMIN(2, SearchFilterEnum.Types.SUPERADMIN),
    STUDENT(3, SearchFilterEnum.Types.STUDENT),
    ADMIN(4, SearchFilterEnum.Types.ADMIN),
    WORKGROUP(5, SearchFilterEnum.Types.WORKGROUP),
    INSTITUTION(6, SearchFilterEnum.Types.INSTITUTION);

    public class Types {
        public static final String ALL_USERS = "All users";
        public static final String SUPERADMIN = "Superadmin";
        public static final String STUDENT = "Student";
        public static final String ADMIN = "Admin";
        public static final String WORKGROUP = "Workgroup";
        public static final String INSTITUTION = "Institution";
    }
    private final Integer id;
    private final String label;



    private SearchFilterEnum(Integer id, String label) {
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