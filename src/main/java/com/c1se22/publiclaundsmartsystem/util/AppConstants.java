package com.c1se22.publiclaundsmartsystem.util;

import lombok.Getter;

public class AppConstants {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIR = "asc";
    public static final Integer TIME_TO_CANCEL_RESERVATION = 15;
    public static final Integer TIME_TO_NOTIFY_USER = 5;
    public static final Integer MAX_MONTHLY_CANCEL_RESERVATION = 3;
    public static final Integer MAX_BAN_BEFORE_DELETE = 3;
    public static final Integer[] BAN_DURATION = {1, 7, 14};

    @Getter
    public enum MachineStatus {
        AVAILABLE("Available"),
        IN_USE("In Use"),
        MAINTENANCE("Maintenance");

        private final String status;

        MachineStatus(String status) {
            this.status = status;
        }
    }
}
