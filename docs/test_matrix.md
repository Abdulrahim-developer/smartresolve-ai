# SmartResolve AI — Automated Unit & Quality Assurance Validation Matrix

This matrix documents the comprehensive testing suite executed inside both our local integration environments and our live AWS production container stack to guarantee full-stack validation.

## 1. Automated Spring Boot Application Testing Context
We executed automated suite runs utilizing our integrated JUnit 5 test harness engine (`SmartresolveApplicationTests.java`). 

*   **Command Executed:** `mvn clean test` (Local validation loop)
*   **Context Verification Outcome:** `Build Success` (0 failures, 0 context drop exceptions)
*   **Core Systems Audited Natively:**
    *   `contextLoads()`: Successfully validated that the Java 21 JVM initializes the Spring Boot container engine context, injecting configuration parameters from `application.yml` seamlessly.
    *   `DataSource` Connection: Verified that the HikariCP connection pool successfully establishes network handshakes with PostgreSQL over port 5432.
    *   `RedisConnectionFactory`: Proved that the Lettuce driver successfully authenticates against the isolated Valkey memory cache cluster over port 6379.

---

## 2. Deployed Production QA Edge-Case Transaction Ledger
The following manual validation matrix tracks how our system behaves under production security constraints.

| Test ID | Architectural Scenario Evaluated | Execution Input Step Path | Expected System Outcome Status | Production Result |
| :--- | :--- | :--- | :--- | :--- |
| **QA-01** | Prohibited Privilege Elevation | Post register payload with `role: "ADMIN"` | API intercepts, overrides role to `REPORTER` | **PASS (Verified)** |
| **QA-02** | Cross-User Ticket Isolation | Reporter B attempts to GET Reporter A's ticket ID | Returns `403 Forbidden` access-denied | **PASS (Verified)** |
| **QA-03** | Malicious Input Overload | Post ticket with 2,000 character description | Returns `400 Bad Request` validation | **PASS (Verified)** |
| **QA-04** | Ambiguous Classification | Submit: "Nothing is working in main office" | Score delta falls below 0.55; routes to Admin Queue | **PASS (Verified)** |
| **QA-05** | Deterministic Workload Tie | Submit ticket to IT Support pool | Auto-assigns to IT Staff with lowest container backlog | **PASS (Verified)** |
| **QA-06** | Severe Safety Hazard Trigger | Submit ticket with `hazard_flag = true` | Displays immediate emergency call text guidance | **PASS (Verified)** |
| **QA-07** | Illegal Status Workflow Jump | IT Staff attempts to skip straight to `CLOSED` | Returns `400` constraint validation rule block | **PASS (Verified)** |
| **QA-08** | Incomplete Resolution Notes | IT Staff marks `RESOLVED` with 5-char text note | Returns `400` constraint validation warning | **PASS (Verified)** |
