#!/bin/bash

echo "========================================"
echo "Java Thread Dump Analyzer 배포 스크립트"
echo "========================================"

echo ""
echo "1. Git 상태 확인..."
git status

echo ""
echo "2. 변경사항 커밋..."
git add .
git commit -m "Update: $(date)"

echo ""
echo "3. GitHub에 푸시..."
git push origin main

echo ""
echo "4. 애플리케이션 빌드..."
./gradlew build

echo ""
echo "5. 애플리케이션 실행..."
echo "서버가 http://localhost:38089 에서 시작됩니다."
java -jar build/libs/thread-dump-analyzer.jar
