[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](#)
[![Coverage](https://img.shields.io/badge/coverage-85%25-yellow.svg)](#)
]\(LICENSE)

# AeroAdmin

> ⚙️ A cutting-edge Java CLI for seamless airline administration with flexible persistence engines (JDBC or JPA).

---

## 📋 Table of Contents

* [🚀 Features](#-features)
* [🛠️ Tech Stack](#-tech-stack)
* [📦 Getting Started](#-getting-started)
* [⚙️ Configuration](#-configuration)
* [▶️ Usage](#-usage)
* [📂 Project Structure](#-project-structure)
* [📝 Version History](#-version-history)
* [👥 Contributing](#-contributing)
* [📄 License](#-license)

---

## 🚀 Features

* 🔄 **Dual Persistence**: Switch between JDBC and JPA (Hibernate) at startup.
* ✈️ **Full CRUD**: Manage Aircraft, Pilots, Airlines, and Flights.
* 🖥️ **Interactive CLI**: User-friendly menu with validation and ASCII tables.
* 📦 **Modular Architecture**: Clear separation of concerns (model, repositories, console).
* 🔄 **Auto Schema Update**: Hibernate `hbm2ddl.auto = update` for swift development.
* 📊 **ASCII UI**: Console tables powered by `ascii-table`.

---

## 🛠️ Tech Stack

| Component           | Technology              |
| ------------------- | ----------------------- |
| **Language**        | Java 21                 |
| **Build Tool**      | Gradle (Kotlin DSL)     |
| **Persistence**     | JDBC, JPA (Hibernate 7) |
| **Database**        | MySQL 8 (Docker)        |
| **Console UI**      | ascii-table             |
| **Dependency Mgmt** | Maven Central           |

---

## 📦 Getting Started

### Prerequisites

* Java JDK 21
* Docker & Docker Compose
* Git

### Quick Setup

```bash
# Clone repo
git clone https://github.com/yourusername/aeroadmin.git
cd aeroadmin

# Launch MySQL + phpMyAdmin
docker-compose up -d

# Build and run
./gradlew clean build
java -cp "$(find . -type d | grep -E 'build/(classes/java/main|resources/main)' | tr '\n' ':')" \
    cat.uvic.teknos.dam.aeroadmin.console.App
```

**Select persistence strategy:**

```text
1) JDBC
2) JPA
```

---

## ⚙️ Configuration

### `di.properties` (app/src/main/resources)

```properties
repository_factory.jdbc=cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.JdbcRepositoryFactory
repository_factory.jpa=cat.uvic.teknos.dam.aeroadmin.jpa.repositories.JpaRepositoryFactory
model_factory=cat.uvic.teknos.dam.aeroadmin.jpa.model.JpaModelFactory
```

### `persistence.xml` (app/src/main/resources/META-INF)

```xml
<property name="jakarta.persistence.jdbc.url"
          value="jdbc:mysql://127.0.0.1:3306/AVIATION_SYSTEM?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"/>
<property name="jakarta.persistence.jdbc.user" value="root"/>
<property name="jakarta.persistence.jdbc.password" value="1qaz2wsx3edc"/>
```

---

## ▶️ Usage

1. **Start app** and choose persistence engine.
2. **Navigate menus** to list, create, update, or delete entities.
3. **Exit** by selecting `0` in any manager.

Example flow:

```text
> 1 (JDBC)
> 1 (Manage Aircraft)
> 3 (Create new Aircraft)
> Enter details...
> 0 (Back)
> 0 (Exit)
```

---

## 📂 Project Structure

```bash
aeroadmin/
├─ app/           # Console application
├─ jdbc/          # JDBC repositories
├─ jpa/           # JPA repositories
├─ model/         # Domain models and factories
├─ repositories/  # Repository interfaces
├─ utilities/     # Shared utilities (DataSource)
└─ docker-compose.yml
```

---

## 📝 Version History

| Version   | Description                          |
| --------- | ------------------------------------ |
| **0.1.0** | Initial import                       |
| **0.1.1** | Added README                         |
| **0.2.0** | Added model project                  |
| **0.3.0** | Created repository module            |
| **0.4.0** | Partial JDBC implementation          |
| **1.0.0** | Complete JDBC implementation + tests |
| **1.1.0** | Complete JPA implementation + tests  |
| **1.1.1** | Final JPA corrections                |
