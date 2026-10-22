# SmartResolve AI — Relational Schema Data Blueprint

Our persistence layer uses PostgreSQL to store application credentials, transactional grievance records, and machine learning validation test datasets.

### 1. `users` Table Ledger
*   `id`: `BIGINT` (Primary Key / Serialized Auto-increment)
*   `email`: `VARCHAR(150)` (Unique Index Constraint for authentication lookups)
*   `password_hash`: `VARCHAR(255)` (Salted bcrypt string hash)
*   `role`: `VARCHAR(30)` (Enum: `REPORTER`, `STAFF`, `ADMIN`)
*   `department_id`: `VARCHAR(50)` (`null` for students; maps to `IT_SUPPORT`, `FACILITIES`, `ADMINISTRATION` for privileged staff resolution teams)

### 2. `grievances` Table Ledger
*   `id`: `VARCHAR(20)` (Format: `SR-000124` Unique Identifier Index)
*   `reporter_id`: `BIGINT` (Foreign Key referencing `users.id`)
*   `title`: `VARCHAR(120)` | `description`: `TEXT`
*   `urgency`: `VARCHAR(20)` (`LOW`, `MEDIUM`, `HIGH`)
*   `affected_count`: `INTEGER` (Constraint validation range: 1 to 500)
*   `hazard_flag`: `BOOLEAN` (Triggers safety guidance metrics when true)
*   `assigned_department`: `VARCHAR(50)` (Dynamically mapped by Naive Bayes or manually updated by Admin)
*   `assignee_id`: `BIGINT` (Foreign Key referencing `users.id` tracking balanced workload auto-routing)
*   `status`: `VARCHAR(30)` (`OPEN`, `ASSIGNED`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`)
*   `created_at` / `updated_at`: `TIMESTAMP` (Generated natively on cloud server host clocks)

### 3. `grievance_samples` Table Ledger (Machine Learning Baseline Core)
*   `id`: `BIGINT` (Primary Key / Serialized Auto-increment)
*   `title`: `VARCHAR(120)` | `description`: `TEXT` (Sample string corpus text profiles)
*   `true_category`: `VARCHAR(50)` (Ground truth evaluation label: `IT_SUPPORT`, `FACILITIES`, `ADMINISTRATION`)
*   `is_test_sample`: `BOOLEAN` (Flags row distribution: `false` for 60 training rows, `true` for 30 held-out validation cases)
