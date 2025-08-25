# Кроссплатформенный Makefile
SERVICES = file-uploader file-processor file-status-processor
LOGS_DIR = logs

# Определение ОС
ifeq ($(OS),Windows_NT)
    DETECTED_OS := Windows
    GRADLE_CMD := gradlew.bat
    SLEEP := timeout /t 3 /nobreak > nul
else
    DETECTED_OS := $(shell uname -s)
    GRADLE_CMD := ./gradlew
    SLEEP := sleep 3
endif

.PHONY: start-all stop clean status help os

os:
	@echo "Detected OS: $(DETECTED_OS)"
	@echo "Gradle command: $(GRADLE_CMD)"

start-all:
	@echo "Starting all services on $(DETECTED_OS)..."

ifeq ($(DETECTED_OS),Windows)
	@echo "Starting Windows services in separate windows..."
	cd file-uploader && start "File Uploader" cmd /k "$(GRADLE_CMD) bootRun"
	$(SLEEP)
	cd file-processor && start "File Processor" cmd /k "$(GRADLE_CMD) bootRun"
	$(SLEEP)
	cd file-status-processor && start "File Status Processor" cmd /k "$(GRADLE_CMD) bootRun"
else
	@echo "Starting Unix services in background..."
	cd file-uploader && $(GRADLE_CMD) bootRun > ../$(LOGS_DIR)/file-uploader.log 2>&1 &
	$(SLEEP)
	cd file-processor && $(GRADLE_CMD) bootRun > ../$(LOGS_DIR)/file-processor.log 2>&1 &
	$(SLEEP)
	cd file-status-processor && $(GRADLE_CMD) bootRun > ../$(LOGS_DIR)/file-status-processor.log 2>&1 &
	@echo "Services started in background. Check logs in $(LOGS_DIR)/"
endif
	@echo "All services started!"

test-all:
	@echo "Running all tests in all services..."
	cd file-uploader && $(GRADLE_CMD) test
	cd file-processor && $(GRADLE_CMD) test
	cd file-status-processor && $(GRADLE_CMD) test
	@echo "All tests completed!"