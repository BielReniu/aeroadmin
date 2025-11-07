[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](#)
[![Coverage](https://img.shields.io/badge/coverage-85%25-yellow.svg)](#)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

# 🛫 AeroAdmin

> ⚙️ Aplicació Java per a la gestió d’aerolínies, amb una **CLI autònoma**, **arquitectura client/servidor** i **mòduls de persistència flexibles** (JDBC o JPA).

---

## 📋 Taula de Continguts

- [🚀 Funcionalitats](#-funcionalitats)
- [🛠️ Stack Tecnològic](#-stack-tecnològic)
- [📦 Primers Passos](#-primers-passos)
- [⚙️ Configuració](#-configuració)
- [▶️ Ús](#-ús)
- [📂 Estructura del Projecte](#-estructura-del-projecte)
- [📝 Historial de Versions](#-historial-de-versions)
- [👥 Contribucions](#-contribucions)
- [📄 Llicència](#-llicència)

---

## 🚀 Funcionalitats

- 🔄 **Persistència Dual:** Canvia fàcilment entre JDBC i JPA (Hibernate) en iniciar la CLI.  
- ✈️ **CRUD Complet:** Gestió d’Aeronaus, Pilots, Aerolínies i Vols.  
- 🖥️ **CLI Interactiva:** Menús intuïtius amb validació i taules ASCII.  
- 📦 **Arquitectura Modular:** Separació clara entre model, repositoris i consola.  
- 🌐 **Client/Servidor:** Inclou servidor HTTP lleuger (`rawhttp`) i client de consola remot.  
- 🔧 **Actualització Automàtica d’Esquema:** Hibernate `hbm2ddl.auto=update` per a un desenvolupament àgil.  
- 📊 **Interfície ASCII:** Visualització estructurada amb `ascii-table`.

---

## 🛠️ Stack Tecnològic

| Component | Tecnologia |
|------------|-------------|
| **Llenguatge** | Java 21 |
| **Eina de Build** | Gradle (Kotlin DSL) |
| **Persistència** | JDBC, JPA (Hibernate 7) |
| **Base de Dades** | MySQL 8 (Docker) |
| **UI de Consola** | `ascii-table` |
| **Comunicació Xarxa** | `rawhttp-core`, `Gson` |
| **Gestió de Dependències** | Maven Central |

---

## 📦 Primers Passos

### 🔧 Prerequisits

- Java JDK 21  
- Docker i Docker Compose  
- Git  

### ⚡ Instal·lació Ràpida (CLI Autònoma)

```bash
# Clonar el repositori
git clone https://github.com/yourusername/aeroadmin.git
cd aeroadmin

# Engegar MySQL + phpMyAdmin
docker-compose up -d

# Compilar i executar la consola principal
./gradlew :app:run
```

En iniciar, selecciona el motor de persistència:

```text
1) JDBC
2) JPA
```

---

## ⚙️ Configuració

### 🧩 `di.properties` (app/src/main/resources)

```properties
repository_factory.jdbc=cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.JdbcRepositoryFactory
repository_factory.jpa=cat.uvic.teknos.dam.aeroadmin.jpa.repositories.JpaRepositoryFactory
model_factory=cat.uvic.teknos.dam.aeroadmin.jpa.model.JpaModelFactory
```

### 🗃️ `persistence.xml` (jpa/src/main/resources/META-INF)

```xml
<property name="jakarta.persistence.jdbc.url"
          value="jdbc:mysql://127.0.0.1:3306/AVIATION_SYSTEM?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"/>
<property name="jakarta.persistence.jdbc.user" value="root"/>
<property name="jakarta.persistence.jdbc.password" value="1qaz2wsx3edc"/>
```

---

## ▶️ Ús

1. Executa l’aplicació amb `./gradlew :app:run`.  
2. Tria l’estratègia de persistència (`JDBC` o `JPA`).  
3. Navega pels menús per **llistar**, **crear**, **editar** o **eliminar** entitats.  
4. Prem `0` per sortir en qualsevol moment.

**Exemple de sessió:**

```text
> 1 (JDBC)
> 1 (Manage Aircraft)
> 3 (Create new Aircraft)
> Enter details...
> 0 (Back)
> 0 (Exit)
```

---

## 📂 Estructura del Projecte

```bash
aeroadmin/
├─ app/              # Aplicació principal (CLI autònoma)
├─ clients/console/  # Client de consola remot
├─ jdbc/             # Implementació JDBC
├─ jpa/              # Implementació JPA
├─ model/            # Models de domini i fàbriques
├─ repositories/     # Interfícies de repositori
├─ server/           # Servidor HTTP lleuger
└─ docker-compose.yml
```

---

## 📝 Historial de Versions

| Versió | Descripció |
|---------|-------------|
| 0.1.0 | Importació inicial |
| 0.1.1 | Afegit README |
| 0.2.0 | Afegit mòdul `model` |
| 0.3.0 | Creat mòdul `repositories` |
| 0.4.0 | Implementació parcial JDBC |
| 1.0.0 | Implementació completa JDBC + tests |
| 1.1.0 | Implementació completa JPA + tests |
| 1.1.1 | Correccions finals JPA |
| 1.2.0 | Afegida arquitectura Client/Servidor (`server` i `clients/console`) |
| **2.1.0** | **Servidor concurrent, fil dimoni i gestió d'inactivitat (Tasca 2.1)** |

---

## 👥 Contribucions

Les contribucions són benvingudes!  
Si vols millorar el projecte, fes un *fork*, crea una branca i envia un *pull request*.

---

## 📄 Llicència

Aquest projecte està sota la llicència [MIT](LICENSE).

---

🔗 [Repositori Oficial](https://github.com/BielReniu/aeroadmin)
