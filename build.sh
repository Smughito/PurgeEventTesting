#!/bin/bash

echo "Building PurgeEvent Plugin..."
echo ""

if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed!"
    echo "Please install Java 21 or higher and try again."
    echo ""
    echo "Download Java from: https://adoptium.net/"
    exit 1
fi

java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)

if [ "$java_version" -lt 21 ]; then
    echo "Error: Java 21 or higher is required!"
    echo "Current version: Java $java_version"
    echo ""
    echo "Download Java 21 from: https://adoptium.net/"
    exit 1
fi

echo "Java version check passed: Java $java_version"
echo ""

chmod +x gradlew

echo "Running Gradle build..."
./gradlew clean build

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================="
    echo "Build successful!"
    echo "========================================="
    echo ""
    echo "Plugin JAR location:"
    echo "  build/libs/PurgeEvent-1.0.0.jar"
    echo ""
    echo "Installation instructions:"
    echo "  1. Copy the JAR file to your server's plugins/ folder"
    echo "  2. Make sure WorldGuard is installed"
    echo "  3. Restart your server"
    echo "  4. Configure plugins/PurgeEvent/config.yml"
    echo ""
else
    echo ""
    echo "Build failed! Please check the error messages above."
    exit 1
fi
