# Lottery Web App Automation

This project automatically generates a modern HTML homepage from daily lottery JSON files using Java and GitHub Actions.

## Project Structure

- `src/LotteryHtmlGenerator.java` — Java code to read JSON and generate HTML
- `index.html` — The generated homepage (auto-updated)
- `template.html` — (Optional) HTML template for the homepage
- `*.json` — Daily lottery result files (e.g., `BT-8-2025-06-30.json`)
- `.github/workflows/generate-html.yml` — GitHub Actions workflow for automation

## How It Works

1. **Add a new daily JSON file** to the repo and push.
2. **GitHub Actions** runs the Java code automatically.
3. The Java code reads the latest JSON and generates/updates `index.html`.
4. The workflow commits and pushes the new HTML file back to the repo.

## How to Use

- Place your Java code in `src/LotteryHtmlGenerator.java`.
- Add your daily JSON files to the root or a `data/` folder.
- Push changes to GitHub — the workflow will handle the rest!

## Requirements
- Java 17+ (for local development)
- GitHub repository

## Customization
- Edit the Java code to change the HTML layout or add features.
- Update the workflow file to change triggers or outputs.

---

**Maintained by sandeveloper1** 