# Project variables
SERVICES = idea file-processor file-status-processor file-uploader
CONFIG_SERVER = config-server
DOCKER_COMPOSE = docker-compose.yml

.PHONY: all build build-all clean start start-local stop restart logs logs-local status help

# Default target
all: build start

# Build all services
build-all:
	@echo "Building all services..."
	@for %%s in ($(SERVICES) $(CONFIG_SERVER)) do ( \
		echo Building %%s... & \
		if exist %%s ( \
			cd %%s && call gradlew build -x test && cd .. \
		) else ( \
			echo Service %%s not found! \
		) \
	)

# Build specific service
build-%:
	@echo "Building $*..."
	@if exist $* ( \
		cd $* && call gradlew build -x test \
	) else ( \
		echo Service $* not found! \
	)

# Build with tests
build-test-all:
	@echo "Building all services with tests..."
	@for %%s in ($(SERVICES) $(CONFIG_SERVER)) do ( \
		echo Building %%s with tests... & \
		if exist %%s ( \
			cd %%s && call gradlew build && cd .. \
		) else ( \
			echo Service %%s not found! \
		) \
	)

# Clean all services
clean:
	@echo "Cleaning all services..."
	@for %%s in ($(SERVICES) $(CONFIG_SERVER)) do ( \
		echo Cleaning %%s... & \
		if exist %%s ( \
			cd %%s && call gradlew clean && cd .. \
		) else ( \
			echo Service %%s not found! \
		) \
	)

# Start only config-server in Docker and local services
start:
	@echo "Starting config-server in Docker..."
	docker-compose -f $(DOCKER_COMPOSE) up -d --build
	@echo "Config-server started. Run 'make start-local' to start local services"

# Start local services (without config-server)
start-local:
	@echo "Starting local services..."
	@for %%s in ($(SERVICES)) do ( \
		echo Starting %%s locally... & \
		if exist %%s ( \
			cd %%s && start cmd /k "call gradlew bootRun" && cd .. & \
			timeout /t 3 /nobreak > nul \
		) else ( \
			echo Service %%s not found! \
		) \
	)

# Start specific local service
start-local-%:
	@echo "Starting $* locally..."
	@if exist $* ( \
		cd $* && start cmd /k "call gradlew bootRun" \
	) else ( \
		echo Service $* not found! \
	)

# Stop all services
stop:
	@echo "Stopping Docker services..."
	docker-compose -f $(DOCKER_COMPOSE) down
	@echo "Stopping local services..."
	@taskkill /f /im java.exe 2>nul || echo "No Java processes found or already stopped"

# Stop only Docker services
stop-docker:
	@echo "Stopping Docker services..."
	docker-compose -f $(DOCKER_COMPOSE) down

# Stop only local services
stop-local:
	@echo "Stopping local services..."
	@taskkill /f /im java.exe 2>nul || echo "No Java processes found or already stopped"

# Restart all services
restart: stop start

# View Docker logs
logs:
	@echo "Showing Docker logs..."
	docker-compose -f $(DOCKER_COMPOSE) logs -f

# View local services status
status:
	@echo "Docker service status:"
	docker-compose -f $(DOCKER_COMPOSE) ps
	@echo.
	@echo "Local Java processes:"
	@tasklist /fi "imagename eq java.exe" 2>nul || echo "No Java processes running"

# Remove all containers and volumes
clean-docker:
	@echo "Cleaning Docker containers and volumes..."
	docker-compose -f $(DOCKER_COMPOSE) down -v

# Run tests for all services
test-all:
	@echo "Running tests for all services..."
	@for %%s in ($(SERVICES) $(CONFIG_SERVER)) do ( \
		echo Testing %%s... & \
		if exist %%s ( \
			cd %%s && call gradlew test && cd .. \
		) else ( \
			echo Service %%s not found! \
		) \
	)

# Run tests for specific service
test-%:
	@echo "Testing $*..."
	@if exist $* ( \
		cd $* && call gradlew test \
	) else ( \
		echo Service $* not found! \
	)

# Help message
help:
	@echo "Available commands:"
	@echo "  make all              - Build and start all services"
	@echo "  make build-all        - Build all services without tests"
	@echo "  make build-<service>  - Build specific service"
	@echo "  make clean            - Clean all services"
	@echo "  make start            - Start config-server in Docker"
	@echo "  make start-local      - Start local services in separate windows"
	@echo "  make start-local-<svc>- Start specific local service"
	@echo "  make stop             - Stop all services"
	@echo "  make stop-docker      - Stop only Docker services"
	@echo "  make stop-local       - Stop only local services"
	@echo "  make restart          - Restart all services"
	@echo "  make logs             - View Docker logs"
	@echo "  make status           - View service status"
	@echo "  make clean-docker     - Remove Docker containers and volumes"
	@echo "  make test-all         - Run tests for all services"
	@echo "  make test-<service>   - Run tests for specific service"
	@echo "  make help             - Show this help message"
	@echo ""
	@echo "Local services: $(SERVICES)"
	@echo "Docker service: $(CONFIG_SERVER)"