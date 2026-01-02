#!/bin/bash

echo "==================================="
echo "PurgeEvent Build Verification"
echo "==================================="
echo ""

echo "Checking Java installation..."
if command -v java &> /dev/null; then
    java_version=$(java -version 2>&1 | head -n 1)
    echo "✓ Java found: $java_version"
else
    echo "✗ Java not found. Please install Java 17 or higher."
    exit 1
fi

echo ""
echo "Checking Gradle installation..."
if command -v gradle &> /dev/null; then
    gradle_version=$(gradle --version | grep Gradle | head -n 1)
    echo "✓ Gradle found: $gradle_version"
else
    echo "✗ Gradle not found. Please install Gradle 7.0 or higher."
    echo "  Or use the Gradle wrapper: ./gradlew"
fi

echo ""
echo "Checking source files..."
java_files=$(find src/main/java -name "*.java" | wc -l)
echo "✓ Found $java_files Java source files"

if [ -f "src/main/resources/plugin.yml" ]; then
    echo "✓ plugin.yml exists"
else
    echo "✗ plugin.yml not found"
fi

if [ -f "src/main/resources/config.yml" ]; then
    echo "✓ config.yml exists"
else
    echo "✗ config.yml not found"
fi

echo ""
echo "==================================="
echo "Verification complete!"
echo ""
echo "To build the plugin, run:"
echo "  ./gradlew clean build"
echo ""
echo "Or with system Gradle:"
echo "  gradle clean build"
echo "==================================="
