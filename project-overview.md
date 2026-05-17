"Layer 1: The 30,000-Foot View"

╔══════════════════════════════════════════════════════════════════╗
║                        THE INTERNET                              ║
║                                                                  ║
║   User's Browser         Stripe Servers                         ║
║        │                      │                                  ║
╚════════╪══════════════════════╪══════════════════════════════════╝
         │                      │
         ▼                      ▼
┌─────────────────────────────────────────────────────────────────┐
│                     NGINX INGRESS                                │
│                                                                  │
│  codingshuttle.in  ──────────────────────► Frontend (React)     │
│  api.codingshuttle.in ───────────────────► API Gateway          │
│  *.previews.codingshuttle.in ────────────► Preview Proxy        │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                       API GATEWAY                                │
│                  (Single entry point)                            │
│                                                                  │
│   ✅ Validates JWT token on EVERY request                        │
│   ✅ Routes to the correct service                               │
│   ❌ Rejected → 401 Unauthorized (never reaches services)        │
└──────────┬──────────────────────────────────────────────────────┘
           │
           │ Routes to one of these:
           ├──────────────────► account-service   (auth + billing)
           ├──────────────────► workspace-service (projects + files)
           └──────────────────► intelligence-service (AI + chat)
Mentor Note: The API Gateway is the bouncer at the club door. If your JWT is invalid or missing, you never get inside. Every other service trusts that anyone who reaches them has already been verified.

"Layer 2: The Infrastructure Backbone"
These are not services your users interact with. They are the plumbing.


┌────────────────────────────────────────────────────────────────────────┐
│                     INFRASTRUCTURE LAYER                               │
│                                                                        │
│   ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐   │
│   │  discovery-svc  │    │  config-svc      │    │   PostgreSQL    │   │
│   │  (Eureka)       │    │  (Config Server) │    │   (3 databases) │   │
│   │                 │    │                  │    │                 │   │
│   │  port: 8761     │    │  port: 8888      │    │  account_db     │   │
│   │                 │    │                  │    │  workspace_db   │   │
│   │  "Who is        │    │  "Reads config   │    │  intelligence_db│   │
│   │  account-svc?   │    │  from GitHub     │    │                 │   │
│   │  → 192.168.1.5" │    │  repo and serves │    └─────────────────┘   │
│   │                 │    │  to all svcs"    │                          │
│   └─────────────────┘    └─────────────────┘    ┌─────────────────┐   │
│                                                   │     Redis       │   │
│   ┌─────────────────┐    ┌─────────────────┐    │                 │   │
│   │     Kafka        │    │     MinIO        │    │  Key-Value store│   │
│   │  (Event Bus)    │    │  (File Storage)  │    │                 │   │
│   │                 │    │                  │    │  "route:proj-42 │   │
│   │  Topics:        │    │  Buckets:        │    │  → 10.0.0.5:   │   │
│   │  • file-storage │    │  • projects/     │    │    5173"        │   │
│   │    -request     │    │  • starter-      │    │                 │   │
│   │  • file-store   │    │    projects/     │    │  Used for       │   │
│   │    -responses   │    │                  │    │  preview routing│   │
│   │                 │    │  "Like S3 but    │    │                 │   │
│   └─────────────────┘    │   self-hosted"   │    └─────────────────┘   │
│                           └─────────────────┘                          │
└────────────────────────────────────────────────────────────────────────┘
Mentor Note: Think of Config Server like a .env file manager for all your services. Instead of each service having its own Config, they all ask the Config Server: "Give me my settings." The Config Server fetches them from a private GitHub repo. This means if you need to change the DB password, you change it in ONE place in GitHub.

"Layer 3: How Each Service Works Internally"
Every service follows the exact same internal pattern:


HTTP Request comes in
        │
        ▼
┌───────────────┐
│  Controller   │  ← "I receive HTTP. I don't do logic. I just call service."
└──────┬────────┘
       │
       ▼
┌───────────────┐
│   Service     │  ← "I contain ALL the business logic and rules."
│  (interface)  │
└──────┬────────┘
       │
       ▼
┌───────────────┐
│  ServiceImpl  │  ← "I am the actual implementation."
│               │     Talks to repository, calls other services,
│               │     publishes Kafka events, etc.
└──────┬────────┘
       │
       ▼
┌───────────────┐
│  Repository   │  ← "I talk to PostgreSQL. I am auto-generated by Spring Data."
└──────┬────────┘
       │
       ▼
┌───────────────┐
│  PostgreSQL   │  ← "I store the data permanently."
└───────────────┘
"Layer 4: The 5 Core User Journeys"
Journey 1: Signup / Login

User                  API Gateway           account-service
 │                        │                      │
 │── POST /auth/signup ──►│                      │
 │                        │ (public route,        │
 │                        │  no JWT needed)       │
 │                        │──────────────────────►│
 │                        │                       │ 1. Check username not taken
 │                        │                       │ 2. BCrypt hash the password
 │                        │                       │ 3. Save User to account_db
 │                        │                       │ 4. Generate JWT token
 │                        │                       │    (contains userId + name)
 │◄── { token: "ey..." } ─┤◄──────────────────────│
 │                        │                       │
 │  (stores token in      │                       │
 │   browser/localStorage)│                       │
Mentor Note: After this, EVERY request the user makes includes Authorization: Bearer eyJhbGc... in the header. That token IS the user's identity for all other services.

Journey 2: Creating a Project

User            API Gateway      workspace-service     account-service
 │                   │                  │                    │
 │─ POST /projects ─►│                  │                    │
 │                   │ ✅ JWT valid      │                    │
 │                   │─────────────────►│                    │
 │                   │                  │                    │
 │                   │                  │ 1. Who am I?        │
 │                   │                  │    (read userId     │
 │                   │                  │     from JWT)       │
 │                   │                  │                    │
 │                   │                  │ 2. Can I create?   │
 │                   │                  │────────────────────►│
 │                   │                  │   GET /internal/    │
 │                   │                  │   billing/current-  │
 │                   │                  │   plan              │
 │                   │                  │◄────────────────────│
 │                   │                  │   { maxProjects:5 } │
 │                   │                  │                    │
 │                   │                  │ 3. Count my projects│
 │                   │                  │    (I own 2, max 5) │
 │                   │                  │    ✅ Can create!   │
 │                   │                  │                    │
 │                   │                  │ 4. Save Project     │
 │                   │                  │    to workspace_db  │
 │                   │                  │                    │
 │                   │                  │ 5. Add me as OWNER  │
 │                   │                  │    in project_      │
 │                   │                  │    members table    │
 │                   │                  │                    │
 │                   │                  │ 6. Copy template    │
 │                   │                  │    files from MinIO │
 │                   │                  │    starter-projects/│
 │                   │                  │    react-vite...    │
 │                   │                  │    → projects/42/   │
 │                   │                  │                    │
 │◄─ { projectId:42 }┤◄─────────────────│                    │
Mentor Note: Notice how workspace-service calls account-service to check the plan limit. It does not have its own copy of billing data. It asks. This is microservices talking to each other via Feign HTTP client.

Journey 3: AI Code Generation (The Most Important Flow)
This is the heart of the whole system. Read this carefully.


┌──────────┐   ┌───────────┐   ┌──────────────────┐   ┌──────────────────┐
│  Browser │   │ API Gateway│   │intelligence-svc  │   │ workspace-svc    │
└────┬─────┘   └─────┬─────┘   └────────┬─────────┘   └────────┬─────────┘
     │               │                   │                       │
     │ POST /chat/stream                 │                       │
     │ { message: "add dark mode toggle" │                       │
     │   projectId: 42 }                 │                       │
     │──────────────►│                   │                       │
     │               │ ✅ JWT valid       │                       │
     │               │──────────────────►│                       │
     │               │                   │                       │
     │               │                   │ BEFORE calling LLM:   │
     │               │                   │ FileTreeContextAdvisor│
     │               │                   │──────────────────────►│
     │               │                   │  GET /internal/       │
     │               │                   │  projects/42/files/tree
     │               │                   │◄──────────────────────│
     │               │                   │  [src/App.tsx,        │
     │               │                   │   src/main.tsx, ...]  │
     │               │                   │                       │
     │               │                   │ Now call the LLM with:│
     │               │                   │ • System prompt       │
     │               │                   │ • File tree context   │
     │               │                   │ • User's message      │
     │               │                   │                       │
     │               │                   │ ┌────────────────────┐│
     │               │                   │ │     LLM (AI)       ││
     │               │                   │ │                    ││
     │               │                   │ │ "I need to read    ││
     │               │                   │ │  App.tsx first"    ││
     │               │                   │ │                    ││
     │               │                   │ │ TOOL CALL:         ││
     │               │                   │ │ read_files(        ││
     │               │                   │ │  ["src/App.tsx"])  ││
     │               │                   │ └────────────────────┘│
     │               │                   │                       │
     │               │                   │ CodeGenerationTools   │
     │               │                   │ handles the tool call:│
     │               │                   │──────────────────────►│
     │               │                   │  GET /internal/       │
     │               │                   │  projects/42/files/   │
     │               │                   │  content?path=src/App.tsx
     │               │                   │◄──────────────────────│
     │               │                   │  "import React..."    │
     │               │                   │                       │
     │               │                   │ Tool result fed back  │
     │               │                   │ to LLM.               │
     │               │                   │                       │
     │               │                   │ ┌────────────────────┐│
     │               │                   │ │ LLM now generates: ││
     │               │                   │ │                    ││
     │◄──────────────┤◄──────────────────│ │ <message>I'll add..││ ← streamed live
     │  chunk 1      │ SSE stream        │ │ <tool args=...>... ││ ← streamed live
     │◄──────────────┤◄──────────────────│ │ <file path="src/   ││ ← streamed live
     │  chunk 2      │                   │ │  App.tsx">         ││
     │◄──────────────┤◄──────────────────│ │  ...new content... ││ ← streamed live
     │  chunk 3...   │                   │ │ </file>            ││
     │               │                   │ │ <message>Done!</mes││ ← streamed live
     │               │                   │ └────────────────────┘│
     │               │                   │                       │
     │               │                   │ Stream COMPLETE.       │
     │               │                   │ Now save everything:  │
After streaming ends, the async saving happens (user already saw the response):


intelligence-svc                  Kafka                workspace-svc
     │                              │                       │
     │ 1. Parse LLM response        │                       │
     │    Find all <file> tags      │                       │
     │                              │                       │
     │ 2. Save to DB:               │                       │
     │    ChatSession ─────────────►│                       │
     │    ChatMessage (USER)        │                       │
     │    ChatMessage (ASSISTANT)   │                       │
     │    ChatEvents                │                       │
     │      - THOUGHT (confirmed)   │                       │
     │      - TOOL_LOG (confirmed)  │                       │
     │      - MESSAGE (confirmed)   │                       │
     │      - FILE_EDIT (PENDING ⏳)│                       │
     │                              │                       │
     │ 3. For each FILE_EDIT:       │                       │
     │    Generate sagaId = UUID    │                       │
     │    Publish event ───────────►│                       │
     │    { projectId: 42,          │                       │
     │      sagaId: "abc-123",      │                       │
     │      filePath: "src/App.tsx",│                       │
     │      content: "..." }        │                       │
     │                              │──────────────────────►│
     │                              │                       │ 4. Idempotency check
     │                              │                       │    (seen this sagaId?)
     │                              │                       │    No → proceed
     │                              │                       │
     │                              │                       │ 5. Upload to MinIO:
     │                              │                       │    projects/42/src/App.tsx
     │                              │                       │
     │                              │                       │ 6. Update ProjectFile
     │                              │                       │    in workspace_db
     │                              │                       │
     │                              │◄──────────────────────│
     │                              │  { sagaId: "abc-123", │
     │                              │    success: true }    │
     │◄─────────────────────────────│                       │
     │                              │                       │
     │ 7. Find ChatEvent            │                       │
     │    by sagaId "abc-123"       │                       │
     │    Set status:               │                       │
     │    PENDING → CONFIRMED ✅    │                       │
Mentor Note: See why Kafka is used here? The LLM streaming must be fast — the user is watching it in real time. You do not want to slow down streaming by waiting for MinIO to save files. So intelligence-service fires an event ("save this file") and immediately returns. workspace-service saves it in the background. Then it fires back ("saved!"). intelligence-service updates the status. The user's UI can poll and see the file status change from ⏳ to ✅.

Journey 4: Project Preview / Deploy

User                workspace-service            Kubernetes Cluster
 │                        │                             │
 │─ POST /projects/42/deploy                            │
 │────────────────────────►│                            │
 │                         │                            │
 │                         │ 1. Is there already a      │
 │                         │    BUSY pod for project 42?│
 │                         │────────────────────────────►
 │                         │   kubectl get pods          │
 │                         │   --label project-id=42    │
 │                         │   --label status=busy       │
 │                         │◄────────────────────────────
 │                         │   (none found)              │
 │                         │                             │
 │                         │ 2. Grab an IDLE pod         │
 │                         │────────────────────────────►
 │                         │   kubectl get pods          │
 │                         │   --label status=idle       │
 │                         │◄────────────────────────────
 │                         │   "runner-pool-abc" found   │
 │                         │                             │
 │                         │ 3. Claim it (relabel)       │
 │                         │────────────────────────────►
 │                         │   kubectl label pod         │
 │                         │   runner-pool-abc           │
 │                         │   status=busy               │
 │                         │   project-id=42             │
 │                         │◄────────────────────────────
 │                         │                             │
 │                         │ 4. Sync files from MinIO    │
 │                         │────────────────────────────►
 │                         │   kubectl exec              │
 │                         │   runner-pool-abc           │
 │                         │   -c syncer                 │
 │                         │   mc mirror myminio/        │
 │                         │   projects/42/ /app/        │
 │                         │◄────────────────────────────
 │                         │   (files now in /app/)      │
 │                         │                             │
 │                         │ 5. Start watching MinIO     │
 │                         │    (live sync, background)  │
 │                         │────────────────────────────►
 │                         │   mc mirror --watch ...&    │
 │                         │                             │
 │                         │ 6. Start Vite dev server    │
 │                         │────────────────────────────►
 │                         │   kubectl exec              │
 │                         │   -c runner                 │
 │                         │   npm install               │
 │                         │   && npm run dev            │
 │                         │◄────────────────────────────
 │                         │                             │
 │                         │ 7. Register route in Redis  │
 │                         │   "route:project-42.        │
 │                         │    previews.domain.com"     │
 │                         │   → "10.0.1.5:5173"         │
 │                         │                             │
 │◄── { url: "http://project-42.previews.domain.com" } ──│
 │                         │                             │


Now user opens the preview URL:

User Browser             NGINX Ingress          Preview Proxy      Runner Pod
     │                        │                      │                  │
     │── GET project-42.previews.domain.com          │                  │
     │───────────────────────►│                      │                  │
     │                        │ *.previews.* route   │                  │
     │                        │─────────────────────►│                  │
     │                        │                      │ Redis lookup:    │
     │                        │                      │ "route:project-42│
     │                        │                      │  .previews..."   │
     │                        │                      │ → 10.0.1.5:5173  │
     │                        │                      │                  │
     │                        │                      │─────────────────►│
     │                        │                      │  proxy request   │
     │◄───────────────────────┤◄─────────────────────┤◄─────────────────│
     │   React app running!   │                      │  Vite dev server │
Mentor Note: The pod pool is the clever part. Starting a new pod from scratch takes 30-60 seconds. By keeping pods "warm and idle," claiming one takes milliseconds. The syncer sidecar copies the latest files from MinIO into /app/ which is shared with the runner via an emptyDir Kubernetes volume — like a shared folder between two containers.

Journey 5: Stripe Billing

User                  account-service              Stripe Servers
 │                         │                            │
 │── POST /api/payments/checkout                        │
 │   { planId: 3 }         │                            │
 │────────────────────────►│                            │
 │                         │ 1. Load Plan (get stripePriceId)
 │                         │ 2. Load User (get stripeCustomerId)
 │                         │ 3. Build Checkout params:  │
 │                         │    - price: "price_xxx"    │
 │                         │    - metadata: {           │
 │                         │        user_id: "7",       │
 │                         │        plan_id: "3"        │
 │                         │      }                     │
 │                         │────────────────────────────►
 │                         │   Session.create(params)   │
 │                         │◄────────────────────────────
 │                         │   { url: "https://checkout │
 │                         │      .stripe.com/..." }    │
 │◄── { checkoutUrl } ─────│                            │
 │                         │                            │
 │                         │                            │
 │ (user completes payment on Stripe's page)            │
 │                         │                            │
 │                         │◄───── Webhook POST ─────────
 │                         │   event: "checkout.        │
 │                         │    session.completed"      │
 │                         │   metadata: {              │
 │                         │     user_id: "7",          │
 │                         │     plan_id: "3"           │
 │                         │   }                        │
 │                         │                            │
 │                         │ ✅ Verify Stripe-Signature │
 │                         │ 1. Save stripeCustomerId   │
 │                         │    on user                 │
 │                         │ 2. Create Subscription row │
 │                         │    status: INCOMPLETE      │
 │                         │                            │
 │                         │◄── Webhook: invoice.paid ──│
 │                         │ → status: ACTIVE ✅        │
 │                         │ → set period dates         │
"Layer 5: The Common-Lib — Shared DNA"
Think of common-lib as a toolbox that gets packed into every service's bag:


                    common-lib (shared library)
┌───────────────────────────────────────────────────────────────┐
│                                                               │
│  ┌─────────────────┐    Every service gets this for FREE      │
│  │   AuthUtil      │ ── generate JWT / verify JWT /           │
│  │                 │    getCurrentUserId()                     │
│  └─────────────────┘                                          │
│                                                               │
│  ┌─────────────────┐                                          │
│  │ JwtAuthFilter   │ ── Runs on every HTTP request.           │
│  │                 │    Puts user into SecurityContextHolder   │
│  └─────────────────┘                                          │
│                                                               │
│  ┌─────────────────┐                                          │
│  │ GlobalException │ ── Converts exceptions to JSON errors    │
│  │    Handler      │    automatically (400, 401, 403, 404)    │
│  └─────────────────┘                                          │
│                                                               │
│  ┌─────────────────┐                                          │
│  │ Feign JWT       │ ── Auto-forwards your JWT when calling   │
│  │ Interceptor     │    another service                       │
│  └─────────────────┘                                          │
│                                                               │
│  ┌─────────────────┐                                          │
│  │ Shared Enums    │ ── ChatEventType, ProjectRole,           │
│  │ & DTOs          │    SubscriptionStatus, UserDto, etc.     │
│  └─────────────────┘                                          │
│                                                               │
│  ┌─────────────────┐                                          │
│  │ Kafka Events    │ ── FileStoreRequestEvent                 │
│  │                 │    FileStoreResponseEvent                │
│  └─────────────────┘                                          │
└───────────────────────────────────────────────────────────────┘
         │               │               │
         ▼               ▼               ▼
  account-svc      workspace-svc   intelligence-svc
  (all use it)     (all use it)    (all use it)
"Layer 6: The Security Model"
How does a service know if you are allowed to do something?


Request arrives at workspace-service
            │
            ▼
    ┌───────────────────────────────────────────┐
    │          JwtAuthFilter (from common-lib)  │
    │                                           │
    │  Reads Authorization header               │
    │  Verifies JWT signature                   │
    │  Puts JwtUserPrincipal into               │
    │  SecurityContextHolder                    │
    │  { userId: 7, username: "rush@...",       │
    │    name: "Rushil" }                       │
    └──────────────────┬────────────────────────┘
                       │
                       ▼
    ┌───────────────────────────────────────────┐
    │        Route-Level Security               │
    │                                           │
    │  /auth/**  → permitAll (public)           │
    │  /internal/** → permitAll (internal only) │
    │  everything else → must be authenticated  │
    └──────────────────┬────────────────────────┘
                       │
                       ▼
    ┌───────────────────────────────────────────┐
    │        Method-Level Security              │
    │   @PreAuthorize("@security.canEdit(#id)") │
    │                                           │
    │  SecurityExpressions.canEdit(projectId)   │
    │    → query DB: does userId=7 have         │
    │      EDIT permission on project 42?       │
    │    → Yes ✅ / No → 403 Forbidden          │
    └───────────────────────────────────────────┘
The Full System on One Page

                        ┌──────────────────────────────────────┐
                        │         GITHUB ACTIONS CI/CD         │
                        │  push to master → build → Jib push  │
                        │  → kubectl set image → rollout       │
                        └──────────────────────────────────────┘

┌────────────────────────────────────────────────────────────────────────────┐
│                            KUBERNETES CLUSTER                              │
│                                                                            │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │                        lovable-core namespace                       │  │
│  │                                                                     │  │
│  │  [NGINX Ingress] ──► [api-gateway] ──► [account-svc]               │  │
│  │                                   └──► [workspace-svc] ──► [MinIO] │  │
│  │                                   └──► [intelligence-svc]          │  │
│  │                                                                     │  │
│  │  [config-svc] ◄── GitHub repo (all configs live here)              │  │
│  │  [discovery-svc] (Eureka - service registry)                       │  │
│  │                                                                     │  │
│  │  [PostgreSQL] ── account_db / workspace_db / intelligence_db       │  │
│  │  [Kafka]      ── file-storage-request-event / file-store-responses  │  │
│  │  [Redis]      ── preview route table (domain → podIP)              │  │
│  │  [MinIO]      ── projects/ bucket + starter-projects/ bucket       │  │
│  │                                                                     │  │
│  │  [Preview Proxy] ── reads Redis ──► routes to runner pods          │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                                                            │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │                      lovable-previews namespace                     │  │
│  │                                                                     │  │
│  │   [runner-pool-pod-1]         [runner-pool-pod-2]                  │  │
│  │   status: idle                status: busy (project-id: 42)        │  │
│  │                                                                     │  │
│  │   ┌──────────────┐            ┌──────────────┐                     │  │
│  │   │  runner      │            │  runner      │ ← npm run dev       │  │
│  │   │  (node:20)   │            │  (node:20)   │   port 5173         │  │
│  │   ├──────────────┤            ├──────────────┤                     │  │
│  │   │  syncer      │            │  syncer      │ ← mc mirror --watch │  │
│  │   │  (minio/mc)  │            │  (minio/mc)  │                     │  │
│  │   └──────────────┘            └──────────────┘                     │  │
│  │     shared /app volume          shared /app volume                 │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────────────────┘

                COMMUNICATION PATTERNS
                ──────────────────────

  Browser ──HTTP──► API Gateway ──HTTP──► Services         (synchronous)
  Services ──Feign HTTP──► Other Services                  (synchronous)
  intelligence-svc ──Kafka──► workspace-svc ──Kafka──►     (asynchronous)
  intelligence-svc                                         (saga pattern)
The Mental Model That Ties It All Together
Think of the system as a newspaper publishing house:

Analogy	Actual Component
The front desk	API Gateway
Employee ID cards	JWT tokens
HR department	account-service
Filing room	workspace-service + MinIO
Star journalist (AI)	intelligence-service + LLM
Pneumatic tube between floors	Kafka
Live printing press room	Kubernetes runner pod pool
Reception ledger	Redis route table
Head office rules book	Config Server (GitHub repo)
Staff directory	Eureka (Discovery Service)
When you send a message: the journalist (AI) writes the article, sends it via pneumatic tube to the filing room (file saved), which confirms back via the same tube. The printing press (runner pod) then prints the live version using the latest files from the filing room.
