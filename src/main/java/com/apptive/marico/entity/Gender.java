package com.apptive.marico.entity;

public enum Gender {
    MALE("남"), FEMALE("여");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Gender fromDisplayName(String displayName) {
        for (Gender gender : Gender.values()) {
            if (gender.getDisplayName().equals(displayName)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("적절하지 않은 성별 입니다. " + displayName);
    }
}
