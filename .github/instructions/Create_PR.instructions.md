---
applyTo: '**'
description: 'Rules for creating a pull request from the current checked-out branch with a strict title and description format and without adding commits'
---

Purpose

- Ensure the assistant opens pull requests using the user's current checked-out workspace branch and that the PR contains only the commits already present on that branch (no extra or placeholder commits added by the assistant).

Rules

- Shorthand command behavior
  - When a user issues the short command `Create a PR` (or equivalent terse request) without additional parameters, the assistant MUST interpret this as a request to create or update a pull request using the current checked-out branch as the PR head and the repository's default branch as the target, and MUST follow all rules in this document.
  - If an open pull request already exists for the current branch, the assistant MUST push any local commits to `origin` and then update the existing PR body by programmatically populating the repository's PR template (see Description rules). The assistant must report any push or update errors and stop automated updates if push fails.
  - The shorthand behavior is a convenience that supplements the existing `Current branch selection` and `Existing PRs and local commits` rules; it does NOT bypass the Remote sync and verification rules, Title format, Description population, or Metadata rules described elsewhere in this file.
  - If the current branch cannot be determined, or if the workspace is unavailable, the assistant MUST fall back to the repository default branch and explicitly inform the user it used the fallback branch before proceeding.
  - The assistant MUST NOT add labels, reviewers, or any other metadata unless the user explicitly asked for them in the same shorthand request.

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
    - The assistant MUST NOT include raw SHA lists in the PR description. If the assistant reports verified SHAs for transparency, those SHAs MUST appear in the assistant's response outside the PR body (for example as part of the assistant's message to the user) and not inside the PR description itself.
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
  - The PR description MUST be generated by reading the repository's pull request template file at `.github/PULL_REQUEST_TEMPLATE.md` (or an alternative repo-configured template location) at the time the PR is created. The assistant MUST programmatically inspect that template and populate its fields rather than composing a free-form body. Concretely:
    1. The assistant MUST populate the template's ticket field (for example the first line or ticket placeholder) with the extracted ticket identifier (see Title / Ticket rules). If the template lacks a ticket placeholder, the assistant MUST still include the ticket as the first non-empty line of the PR body.
    2. The assistant MUST populate the `## Description` section (or equivalent textual description section in the template) with a concise summary synthesized from commit messages and the diff for the branch. The summary should explain scope, notable behavior, key files/modules changed, and any migration/upgrade notes when applicable.
    3. For media sections (for example `## Screenshot / Video`), the assistant MUST include the header and either attach/embed media URLs if provided by the user or leave the section empty / set to `N/A` if no media is applicable. If the template's media section has a specific placeholder token, replace it accordingly.
    4. If the template contains additional required headings or placeholders, the assistant MUST populate them where possible (using branch metadata, commit messages, or asking the user when necessary). If the template requires content the assistant cannot infer, it MUST pause and ask the user for the missing content rather than inserting placeholder or filler content.
    5. The assistant MUST read and use the repository's current PR template at the time the PR is created; if the repository's PR template is updated later, the assistant MUST use the updated template for future PRs and adapt its field population accordingly.
  - The PR description MUST NOT include sections titled "Key changes", "Notes", or a section listing "Verified remote commit SHAs." These sections are forbidden in the PR body. Verification details such as commit SHAs may be reported by the assistant in its external message to the user but must not be placed inside the PR description.
  - The PR description MUST NOT include a dedicated "Affected files" section or any explicit list of changed files. Do NOT include lists of file paths (for example `Affected files: ...`) in the PR body. If file-level evidence is required for troubleshooting, provide that information only outside the PR body in the assistant's external response when necessary.
  - When a pull request is created successfully, the assistant's final information to the user MUST be only a short confirmation that the PR was created and the PR URL (for example: "PR created: <url>"). The assistant MUST NOT append verification details (SHAs, file lists, or other evidence) after that final confirmation when the PR creation succeeds. If PR creation fails, the assistant MUST provide verification details and evidence (for example remote push errors, differing SHAs, or other diagnostics) in order to help diagnose and fix the failure.
  - Existing PRs and local commits: If a pull request already exists for the branch being used as the PR head and the local workspace contains commits that are not yet present on the remote branch, the assistant MUST:
    1. Push the local branch to `origin` so the remote branch contains all workspace commits. The assistant must capture and report any push errors and stop automated updates if the push fails.
    2. After pushing, verify that the remote branch's last commits match the local branch (compare SHAs). The assistant MUST NOT put raw SHAs into the PR description; verification SHAs may be reported in the assistant's external response only.
    3. Update the existing pull request body by programmatically populating the repository's PR template (see Description rules above). The assistant MUST synthesize a concise `## Description` from the new commits/diff and must NOT include file lists, raw SHAs, or a dedicated "Affected files" section in the PR body.
    4. If the assistant cannot infer required template fields from commits/diff, it MUST ask the user for the missing content rather than inserting placeholders.
    5. On successful PR update (after push and body update), the assistant's final message to the user MUST be only a short confirmation that the PR was updated and the PR URL (for example: "PR updated: <url>"). If any step fails (push, verification mismatch, or PR update), the assistant MUST provide full verification details and exact commands/errors to help the user resolve the issue.

- Metadata
  - The assistant MAY add optional labels or reviewers only if the user explicitly asks for them in the same create-PR request. Otherwise, do not attach labels, reviewers, or assignees automatically.

Verification

- After attempting to push the workspace branch to `origin`, the assistant MUST fetch the remote ref and compare commit SHAs. The assistant should verify that the last 3 commits' SHAs (or fewer if the branch has fewer commits) on the remote match the local branch.
- The assistant MUST NOT place raw SHA lists or a "Verified remote commit SHAs" section into the PR description. It may, however, report the verified SHAs in the assistant response (outside the PR body) so the user can confirm the verification.
- If the SHAs differ or the push fails, the assistant MUST stop and report the exact git/API error that occurred, and provide exact copy-paste terminal commands the user can run to push and verify their branch.

Examples

- Example branch: `story/JIRA-001-navigation-setup` with two local commits ahead of `main`:
  - Title -> `Story: (JIRA-001) - Navigation setup`
  - Description ->
    JIRA-001

    This PR adds navigation setup for the app. It includes: ... (summary derived from commits/diff)
  - The assistant will push `story/JIRA-001-navigation-setup` to `origin`, verify SHA list (e.g., `abc1234`, `def5678`) are present on remote (the SHA list must not be included in the PR body), and then open the PR.

Maintenance

- To change behavior (for example allowing the assistant to create PRs without pushing local commits), update this file.
