package com.radnoti.studentmanagementsystem.enums;

public enum SearchFilterEnum {

    ALL_USERS(1, SearchFilterEnum.Types.ALL_USERS),

    SUPERADMIN(2, SearchFilterEnum.Types.SUPERADMIN),
    STUDENT(3, SearchFilterEnum.Types.STUDENT),
    ADMIN(4, SearchFilterEnum.Types.ADMIN),
    WORKGROUP(5, SearchFilterEnum.Types.WORKGROUP),
    INSTITUTION(6, SearchFilterEnum.Types.INSTITUTION),
    DELETED_ALL_USERS(7, SearchFilterEnum.Types.DELETED_ALL_USERS),

    USERS_IN_WORKGROUP(6, SearchFilterEnum.Types.USERS_IN_WORKGROUP);

    public class Types {
        public static final String ALL_USERS = "users";

        public static final String SUPERADMIN = "super-admin";
        public static final String STUDENT = "student";
        public static final String ADMIN = "admin";
        public static final String WORKGROUP = "workgroup";
        public static final String INSTITUTION = "institution";
        public static final String DELETED_ALL_USERS = "deleted-users";
        public static final String USERS_IN_WORKGROUP = "users-in-workgroup";
        public static final String WORKGROUP_SCHEDULE = "workgroup-schedule";

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
