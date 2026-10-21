package com.acedigital.smartresolve.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String displayName;
    private String email;
    private String password;
    private String role;        // Passed as a raw string from frontend ("REPORTER", "STAFF")
    private String department;  // Optional organizational parameter based on role bounds
}
