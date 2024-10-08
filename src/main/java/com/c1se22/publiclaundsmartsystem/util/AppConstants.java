package com.c1se22.publiclaundsmartsystem.util;

import lombok.Getter;

public class AppConstants {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIR = "asc";

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
