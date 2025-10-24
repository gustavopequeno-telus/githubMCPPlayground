# GitHub MCP Rule

## Repository
gustavopequeno-telus/githubMCPPlayground

## Description
This rule defines the **Pull Request (PR) creation workflow** for the GitHub MCP Playground repository when using Gemini within Android Studio.  
It ensures that Pull Requests are consistently generated with properly formatted titles and the correct source/target branch setup.

## Rule Name
Pull Request Creation Workflow

## Rule Definition
When this rule is triggered, the Gemini agent must:

1. **Identify the current local branch** (the branch currently checked out).
2. **Ensure the branch exists remotely**:
    - If it does not exist on GitHub, create it before proceeding.
3. **Analyze the full code diff of the current branch** to generate a non-technical, product-oriented summary of the changes.
4. **Create a Pull Request** using the following configuration:
    - **Source Branch** → The current checked-out branch.
    - **Target Branch** → `main`
    - **Title Format** →  
      `{Type}: ({Ticket}) - {Brief summary of the changes}`
        - **Type** → Derived from the branch prefix (e.g., `story/`, `bugfix/`, `feature/`).
        - **Ticket** → Extracted from the branch identifier (e.g., `JIRA-001` from `story/JIRA-001-navigation-setup`).
        - **Summary** → A concise, one-line description of the implemented modifications.
    - **Description** → The generated product-oriented summary.
5. **Do not merge or rebase automatically** — the PR should remain open for review.
6. **Apply this rule only to the repository:**  
   `gustavopequeno-telus/githubMCPPlayground`

## Example Pull Request Title
Story: (JIRA-001) - setup navigation and coordinator flow

## Notes
- The PR title must remain short and descriptive (one line only).
- The rule applies only to the **current working branch**.
- Gemini should confirm successful **PR creation** once completed.
