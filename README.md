# Project: Daily Routine Flow System

This project describes a day-long flow, from waking up to going to sleep. Developed using Angular, the application provides users with a detailed, interactive experience of their daily routines.

# ZenFlow Backend Java

This repository contains the backend for the ZenFlow application, built with Java and Spring Boot. It integrates external services, including Google Calendar API, Firebase Authentication, and Supabase for the database.

## Initial Setup

To run this project locally, follow the steps below.

### Prerequisites

- Java 17
- PostgreSQL
- Account on [Google Cloud Console](https://console.cloud.google.com/)
- Account on [Firebase Console](https://console.firebase.google.com/)
- Account on [Supabase](https://app.supabase.io/)

## Database Setup

1. Go to [Supabase](https://app.supabase.io/) and create a new project.
2. In the **Settings** tab, navigate to **Database** and configure the database name, user, and password.
3. Copy the connection details (URL, username, and password) and set up in the `.env` file.

Example `.env` configuration:

```plaintext
DB_URL=jdbc:postgresql://<supabase-url>:port/postgres
DB_USERNAME=<supabase-user>
DB_PASSWORD=<supabase-password>
```

## Configuring the credentials.json for Google Calendar

1. Go to Google Cloud Console.
2. Create a new project or select an existing project.
3. In the navigation panel, go to APIs & Services > OAuth consent screen and set up the consent screen.
4. Enable the Google Calendar API by navigating to APIs & Services > Library.
5. Go to APIs & Services > Credentials and click Create Credentials > Service Account.
6. Follow the prompts to create the service account and download the JSON key file.
7. Rename the file to credentials.json and place it in the src/main/resources/ directory.

### Example credentials.json

The file should have a structure similar to this:

```json
{
"type": "service_account",
"project_id": "your-project-id",
"private_key_id": "your-private-key-id",
"private_key": "-----BEGIN PRIVATE KEY-----\nYOUR-PRIVATE-KEY\n-----END PRIVATE KEY-----\n",
"client_email": "your-service-account-email@your-project-id.iam.gserviceaccount.com",
"client_id": "your-client-id",
"auth_uri": "https://accounts.google.com/o/oauth2/auth",
"token_uri": "https://oauth2.googleapis.com/token",
"auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
"client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/your-service-account-email%40your-project-id.iam.gserviceaccount.com"
}
```

## Configuring the firebase-adminsdk.json for Firebase Authentication

1. Go to Firebase Console.
2. Select your project or create a new one.
3. Navigate to Project Settings (click the gear icon) > Service accounts.
4. Click Generate new private key, and download the JSON file.
5. Rename the file to firebase-adminsdk.json and place it in the src/main/resources/ directory. 

### Example firebase-adminsdk.json

The file should have a structure similar to this:

```json
{
"type": "service_account",
"project_id": "your-project-id",
"private_key_id": "your-private-key-id",
"private_key": "-----BEGIN PRIVATE KEY-----\nYOUR-PRIVATE-KEY\n-----END PRIVATE KEY-----\n",
"client_email": "firebase-adminsdk-email@your-project-id.iam.gserviceaccount.com",
"client_id": "your-client-id",
"auth_uri": "https://accounts.google.com/o/oauth2/auth",
"token_uri": "https://oauth2.googleapis.com/token",
"auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
"client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-email%40your-project-id.iam.gserviceaccount.com"
}
```

## Running the Project
After completing the configuration above, you should be able to build and run the project:

Build the project:

```
./mvnw clean install
```

## Run the project:

```
./mvnw spring-boot:run
```

## Branching Strategy

To maintain a clean and effective development cycle, we follow a structured branching strategy with the following branches: `feature`, `dev`, `release`, and `prod`. Each branch serves a specific purpose, ensuring a stable and organized workflow.

### Branches

- **feature**: 
  - Purpose: Holds individual features in progress. Each new feature or bug fix is developed on a separate `feature` branch (e.g., `feature/feature-name`).
  - Workflow: Developers work on specific tasks here, ensuring isolated testing and development. Once ready, a Pull Request (PR) is created to merge changes into the `dev` branch.
  - Important: All `feature` branches must follow the naming convention `feature/feature-name`.

- **dev**:
  - Purpose: Integrates all new features under development. This branch acts as the primary working environment where all `feature` branches merge after review.
  - Workflow: Developers push features here only after PR review and approval. The `dev` branch is the testing environment, where features are validated collectively before a release.
  - Important: Before merging into `release`, this branch undergoes comprehensive testing.

- **release**:
  - Purpose: Prepares features for production. The `release` branch serves as the staging environment, holding features ready for deployment to production.
  - Workflow: Once features are finalized and tested in the `dev` branch, they merge here. Additional testing and quality checks occur, ensuring a stable and fully-functional build before moving to `prod`.
  - Important: Only tested features go into `release`, ensuring a stable staging area.

- **prod**:
  - Purpose: Holds the production-ready code deployed to the live environment. This is the final, user-facing branch, only containing stable, thoroughly tested code.
  - Workflow: After all tests and approvals in `release`, code is merged into `prod` for deployment.
  - Important: Only thoroughly tested and approved code reaches this branch, representing the final version available to users.

### Fixes and Hotfixes

Occasionally, critical issues arise in production that require immediate attention. For these cases, we use `fix` branches:
- **fix**: Temporary branches for critical bug fixes in `prod`. These branches, such as `fix/critical-bug`, are created from `prod` and merged back into both `prod` and `dev` to maintain consistency across environments.

## Workflow with Approvals and Pull Requests

To ensure quality and consistency, we use a pull request (PR) workflow with mandatory approvals:
1. **Feature Development**: Each developer creates a branch from `dev` named `feature/feature-name`.
2. **Pull Request**: Upon completing a feature, the developer submits a PR to merge the `feature` branch into `dev`.
3. **Approval Process**:
   - **Review**: At least one other team member reviews the PR for code quality, functionality, and adherence to guidelines.
   - **Comments**: If changes are required, reviewers comment on the PR, and the developer addresses these before resubmission.
4. **Testing in Dev**: After approval, the feature is merged into `dev` for integration testing.
5. **Release Preparation**: Once features in `dev` are stable and complete, a PR is made from `dev` to `release`. Further tests occur in `release` to prepare for production.
6. **Deployment**: After final approval, a PR merges `release` into `prod`, deploying the code to the live environment.

## Contribution Guidelines

- **Naming Conventions**: Follow branch naming conventions (`feature/feature-name`, `fix/bug-name`).
- **Commit Messages**: Use clear, concise commit messages summarizing changes.
- **PR Reviews**: All PRs require at least one review and approval.
- **Testing**: Ensure local testing before PR submission.

---
