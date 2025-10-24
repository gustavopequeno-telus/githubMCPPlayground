---
applyTo: '**'
description: 'Rules for creating a pull request from the current checked-out branch with a strict title and description format and without adding commits'
---

Purpose

- Ensure the assistant opens pull requests using the user's current checked-out workspace branch and that the PR contains only the commits already present on that branch (no extra or placeholder commits added by the assistant).

Rules

- Current branch selection
  - When asked to create a PR and no head/source branch is explicitly provided, the assistant MUST determine the current checked-out git branch in the workspace and use that branch as the PR head.
  - If the current branch cannot be determined from the workspace (for example, the workspace is not available or git fails), the assistant MUST fall back to the repository's default branch (for example `main`), and MUST explicitly inform the user which branch was used.

- No extra commits
  - The assistant MUST NOT create, amend, or otherwise add new commits to the branch when preparing or opening the PR. If the workspace branch is 2 commits ahead of the target branch, the PR must contain exactly those 2 commits.
  - The assistant MUST NOT create a placeholder remote commit to satisfy a remote branch; creating a placeholder commit is allowed only as a clearly temporary measure when explicitly approved by the user, and the assistant must state that local commits are not included and provide exact push instructions to sync them.

- Remote sync and verification (required)
  - Before creating the PR the assistant MUST ensure the remote (`origin`) branch contains the same commits as the local workspace branch.
    - Preferred flow: push the current workspace branch to `origin` so the remote branch contains all workspace commits.
    - After pushing (or if the branch already existed remotely), the assistant MUST compare commit SHAs between the local branch and the remote branch and confirm they match.
    - The assistant MUST list the last few commit SHAs that it verified are present on the remote in the assistant response.
  - If the assistant cannot push programmatically due to authentication, permission, or network errors, it MUST stop automated PR creation and report the exact error returned (copy/paste the git or API error). The assistant should then provide exact terminal commands and guidance the user can run to sync their branch.

- Target branch selection
  - By default the PR should target the repository's default branch (for example `main` or `master`), unless the user explicitly specifies a different target branch in the request.

- Title format (required)
  - The PR title MUST follow this format exactly: {Type}: ({Ticket}) - {Brief summary of the changes}
    - Type → Derived from the branch prefix (for example `story/`, `bugfix/`, `feature/`, `chore/`). The assistant should normalize the Type to Title case (e.g., `story` -> `Story`, `bugfix` -> `Bugfix`) unless the user specifies a different casing.
    - Ticket → Extracted from the branch identifier. Example: branch `story/JIRA-001-navigation-setup` → Ticket `JIRA-001`. The assistant should look for an identifier matching the common ticket pattern (e.g., `[A-Z]+-\d+`) in the branch name and use that as Ticket. If no ticket-like token is found, the assistant should leave the Ticket empty and still follow the formatting (e.g., `Story: () - ...`) but also inform the user it couldn't extract a ticket.
    - Summary → A concise, one-line description of the implemented modifications. Preferred sources in order:
      1. Explicit summary provided by the user in the create-PR request.
      2. The branch name segment after the ticket (for example `navigation-setup` in `story/JIRA-001-navigation-setup`), converted to a human-readable one-line phrase.
      3. The short summary of the most recent commit on the branch.

- Description (required)
  - The PR description MUST start with the ticket number on its own line (for example: `JIRA-001`).
  - After the ticket line, include a clear summary of what the PR achieves based on the actual changes on the branch. To produce this summary the assistant SHOULD inspect commit messages and the diff for the branch and synthesize a few concise paragraphs describing the scope, the key files or modules changed, any notable behavior, and any migration/upgrade notes.
  - If the assistant is unable to inspect the workspace diff (for example, remote-only operation), it MUST state that it could not inspect local changes and request permission to proceed or ask the user to provide a description.

- Metadata
  - The assistant MAY add optional labels or reviewers only if the user explicitly asks for them in the same create-PR request. Otherwise, do not attach labels, reviewers, or assignees automatically.

Verification

- After attempting to push the workspace branch to `origin`, the assistant MUST fetch the remote ref and compare commit SHAs. The assistant should list the last 3 commits' SHAs (or fewer if the branch has fewer commits) that it verified are present on the remote in its response.
- If the SHAs differ or the push fails, the assistant MUST stop and report the exact git/API error that occurred, and provide exact copy-paste terminal commands the user can run to push and verify their branch.

Examples

- Example branch: `story/JIRA-001-navigation-setup` with two local commits ahead of `main`:
  - Title -> `Story: (JIRA-001) - Navigation setup`
  - Description ->
    JIRA-001

    This PR adds navigation setup for the app. It includes: ... (summary derived from commits/diff)
  - The assistant will push `story/JIRA-001-navigation-setup` to `origin`, verify SHA list (e.g., `abc1234`, `def5678`) are present on remote, and then open the PR.

Maintenance

- To change behavior (for example allowing the assistant to create PRs without pushing local commits), update this file.


