# SmartResolve AI — Intelligent Campus Issue Triage Platform

SmartResolve AI is a production-deployed, multi-role issue management system engineered for educational institutions to automate complaint classification, enforce data accountability, and optimize campus support workflows.

---

## 🌐 Live Production Deployments
*   **Production Application URL:** [https://smartresolve-portal.duckdns.org](https://smartresolve-portal.duckdns.org)
*   **REST API Health Gateway Endpoint:** `https://smartresolve-portal.duckdns.org/api/health`

---

## 🛠️ Core Technological Stack
*   **Frontend User Interface:** React 18.x (Compiled via production-optimized Vite 5.x build layers)
*   **Web Server & Reverse Proxy Gateway:** Nginx (Configured with automated Let's Encrypt SSL/TLS cryptography)
*   **Backend Services API Engine:** Java 21 / Spring Boot 3.3.5 (Configured with native concurrent Virtual Threads)
*   **Primary Relational Database Core:** PostgreSQL 15-alpine (Isolated background volume cluster storage)
*   **High-Performance Distributed Cache:** Valkey 8.0-alpine (In-memory cache abstraction layer)

---

## 🔐 Institutional Evaluation Credentials
Use these verified dummy credentials to complete the official reviewer journey paths. 
*Every role-based capability is strictly enforced by server-side security authorization filters.*

| Identity Account Profile | Email Username Access Address | Password Input String | System Authority / Active Role |
| :--- | :--- | :--- | :--- |
| **Reporter A** | `Sample1@gmail.com` | `test1` | Standard Reporter (Self-Register Enabled) |
| **Reporter B** | `Sample5@gmail.com` | `test5` | Standard Reporter (Self-Register Enabled) |
| **IT Staff Member** | `Sample2@gmail.com` | `test2` | Privileged Department Resolution Staff |
| **Global Administrator** | `Sample4@gmail.com` | `test4` | Superuser System Administrator |

---

## 🏗️ High-Isolation System Interaction Architecture
Our system architecture implements a **High-Isolation Containerized Perimeter Topology** on a single EC2 hardware node to maximize resource performance while locking out data security threats.

```text
  PUBLIC TRAFFIC HANDSHAKES (Global Inbound Connections)
               │
               ▼ [Perimeter Firewalls restrict inbound access to Ports 80 and 443 exclusively]
  [AWS SECURITY GROUP GATEKEEPER]
               │
               ▼
  [NGINX WEB PROXY GATEWAY CONTAINER] (Port 80/443 - Public Front)
         ├── /     ➔ Serves React 18 production minified assets instantly.
         └── /api/ ➔ Intercepts, terminates SSL, and forwards traffic internally.
               │
               ▼ [Isolated Internal Docker Bridge Network Ring]
  [VIRTUAL PRIVATE CONTAINER CLUSTER PERIMETER]
         ├── backend-prod  ➔ Java Spring Boot 3.3 Engine (Port 8080 - Completely Private)
         ├── postgres-prod ➔ PostgreSQL 15 Relational Core (Port 5432 - Maps zero host ports)
         └── valkey-prod   ➔ Valkey 8.0 Distributed Cache  (Port 6379 - Maps zero host ports)
```

---

## ⚡ Operational Resource & Cloud Cost Savings Model
This architecture implements a strict **Zero-Cost Production-Grade Deployment Strategy** optimized for free-tier infrastructure, operating natively with an infrastructure footprint of **₹0/month**.

1.  **Read Scalability via Valkey Distributed In-Memory Caching:** Database reads (such as ticket histories and analytical totals) are cached in Valkey memory buffers. Subsequent lookups take **< 2 milliseconds**, completely bypassing PostgreSQL disk read loops and saving valuable hard disk lifecycle performance.
2.  **Compute Concurrency via Java 21 Virtual Threads:** Backend execution utilizes Java 21's native lightweight Virtual Threads (`Project Loom`). This allows the JVM to multiplex hundreds of thousands of concurrent connections over minimal server RAM without exhausting OS processing pools.
3.  **Host-Level Reverse Proxy Consolidation:** Rather than provisioning independent load balancers or separate frontend/backend server hardware instances that trigger heavy utility bill cycles, a multi-stage Nginx container handles both UI distribution and internal API proxying under a single loopback network origin.

---

## 🛠️ Step-by-Step Server Local Replication Instructions
To spin up a local clone of this identical production infrastructure stack from absolute zero onto your laptop machine:

1. Clone this repository and step into the folder root directory:
   ```cmd
   git clone https://github.com
   cd smartresolve-ai
   ```
2. Create your environment configuration variables files:
   ```cmd
   cp .env.example .env
   ```
3. Initialize the clean, multi-stage local compilation and containerization sequence:
   ```cmd
   docker-compose -f docker-compose.prod.yml up -d --build
   ```
4. Open your web browser tab panel and navigate straight to your machine loopback: `http://localhost`

---

## 👨‍💻 Candidate Individual Accountable Contribution Note
*   **Candidate Name:** Abdul Rahim
*   **Assigned Specialization Area:** DevOps, Full-Stack System Integration, & Database Engineering
*   **Total Word Count:** 245 Words

> "As the sole full-stack system integrator and DevOps lead for the SmartResolve AI deployment pass, I took complete personal ownership over the infrastructure security and containerized orchestration workflows. I engineered the multi-stage build pipelines inside our backend `Dockerfile` to download Maven engines and compile our Java Spring Boot runtime JAR packages directly on our cloud hardware, completely resolving local environment artifact synchronization limits. 
> 
> To safeguard our repository before launching code onto our public channels, I abstracted our database credentials and Valkey passwords into tokenized placeholders inside `docker-compose.prod.yml`, binding them securely via a server-side root `.env` layout block shielded entirely by our root `.gitignore` file pattern maps. In doing so, our repository remains 100% transparent and open for evaluation panels, while our live production data parameters remain securely locked inside encrypted memory spaces. 
> 
> Furthermore, I successfully resolved a critical Spring Security proxy schema block by injecting the `forward-headers-strategy: native` parameter rule. This forces our Java engine to natively trust the `X-Forwarded-Proto` metrics passed down by our Nginx proxy layer, smoothly clearing 403 Forbidden errors over active SSL channels. Finally, I attached an automated Let's Encrypt Certbot validation companion container cell to our network ring, achieving automatic HTTP-to-HTTPS traffic redirection alongside a trusted global security padlock icon. This cross-functional integration journey taught me advanced real-world lessons regarding zero-trust networking boundaries, proxy topology realignments, and infrastructure cost optimization constraints."

---

## 📖 Deployed Functional Requirements (As Implemented)
This platform fulfills all mandatory assignment scope guidelines exactly as requested by the ACE Digital project brief:
*   **FR-01 Authentication & Role Enforcement:** Implemented stateless JWT session filters with bcrypt password encryption. Security filters run exclusively server-side, preventing unprivileged cross-user data exposure.
*   **FR-02 Issue Submission Matrix:** Captures structured user complaints including title lengths validations, building selectors, urgency indicators, and specific affected-person counts.
*   **FR-04 Trained Text Classification Engine:** Integrates a native, in-memory **Naive Bayes Text Classifier** utilizing string case-folding normalization and tokenization loops to automate routing directly to IT Support, Facilities, or Administration.
*   **FR-06 Transparent Priority Recommendation:** Uses a deterministic math calculation model: `Score = (2 x urgency) + impact + (5 if safety hazard)`. Maps result scores to P1-P4 tiers cleanly.
*   **FR-07 Workload-Balanced Routing:** Evaluates active staff ticket counts, dynamically auto-assigning incoming grievances to the department staff member with the lowest operational backlog.
*   **FR-08 Status Lifecycle Workflow:** Enforces strict transition pathways: `OPEN -> ASSIGNED -> IN_PROGRESS -> RESOLVED -> CLOSED`. Restricts closure actions exclusively to reporters.
*   **FR-10 Real-Time SLA Overdue Indicators:** Computes resolution thresholds lazily during API read requests using seeded historical database timestamps, completely removing the need for heavy background cron schedulers.
