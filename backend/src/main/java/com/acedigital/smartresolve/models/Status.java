package com.acedigital.smartresolve.models;

public enum Status {
    OPEN,
    PENDING_HUMAN_REVIEW,  // Triggered automatically if the AI confidence margin is low
    RESOLVED
}
