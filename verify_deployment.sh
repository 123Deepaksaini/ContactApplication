#!/bin/bash
# Deployment Verification Script

echo "🔍 Checking Deployment Files..."
echo ""

files_needed=(
    "Dockerfile"
    ".dockerignore"
    "render.yaml"
    ".env.example"
    "pom.xml"
    "schema.sql"
    "src/main/java/in/capp/Application.java"
)

missing_files=0

for file in "${files_needed[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ $file"
    else
        echo "❌ $file (MISSING)"
        missing_files=$((missing_files + 1))
    fi
done

echo ""
echo "-----------------------------------"

if [ $missing_files -eq 0 ]; then
    echo "✅ All files ready for deployment!"
    echo ""
    echo "Next steps:"
    echo "1. Push to GitHub: git push origin main"
    echo "2. Go to https://render.com"
    echo "3. Create new Web Service"
    echo "4. Follow QUICK_START_RENDER_HINDI.md guide"
else
    echo "❌ Missing $missing_files file(s). Add them before deployment."
fi
