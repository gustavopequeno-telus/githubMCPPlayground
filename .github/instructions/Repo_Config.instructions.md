---
applyTo: '**'
description: 'description'
---
Provide project context and coding guidelines that AI should follow when generating code, answering questions, or reviewing changes.

Default GitHub MCP repository

- default_mcp_repo: gustavopequeno-telus/githubMCPPlayground

Rules

- When the assistant receives a GitHub MCP command that does NOT explicitly specify an owner/repo (for example: list_issues, list_pull_requests, get_issue), use the repository `gustavopequeno-telus/githubMCPPlayground` as the default target.
- If the command explicitly includes an owner/repo, always use the provided owner/repo instead of the default.
- If accessing the default repo fails due to not found or permission errors, return the exact error and do not attempt other repositories automatically; prompt the user for guidance.
- In assistant responses, when the default repo is used implicitly, note that the default was applied (e.g., "Using default MCP repo gustavopequeno-telus/githubMCPPlayground").
- When the assistant receives a GitHub MCP command to create a pull request (for example: `create_pull_request`) that does NOT explicitly specify a head/source branch, prefer using the user's "current branch" (the branch checked out in the workspace's git repository) as the PR head. To determine the current branch, the assistant should attempt to use available workspace context (for example, running a git command in the workspace or reading the repository metadata). If the current branch cannot be determined or the workspace is not available, fall back to using the repository's default branch (e.g., `main`) and explicitly inform the user which branch was used. Always note in the assistant response when a fallback/default branch was applied.

Examples

- "list open issues"  -> interpreted as: list open issues in gustavopequeno-telus/githubMCPPlayground
- "count PRs for octocat/Hello-World" -> interpreted as: count PRs in octocat/Hello-World (explicit repo takes precedence)
- "create a PR" -> interpreted as: create a PR from the user's current checked-out branch in the workspace into the repository's default branch; if the current branch can't be determined, create from the default branch and say so.

Maintenance

- To change the default, update the `default_mcp_repo` value in this file.
