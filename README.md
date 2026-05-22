# Akari (Light Up) Puzzle Game

## 💡 Overview
This is a Java based implementation of the classic logic puzzle game **Akari** (also known as *Light Up*). The application features an interactive menu system, fully playable maps, and a level editor.

## 🛠️ Prerequisites
To successfully build and run this project, you will need the following software installed on your system:
- **Java Development Kit (JDK):** Version 24 (or a compatible modern JDK) ensuring support for the compiler configurations in `pom.xml`.
- **Apache Maven:** Used for building the project, running tasks, and managing dependencies natively.

## 🏗️ Building the Project
1. Clone or extract the repository to your local machine.
2. Open a terminal and navigate to the project's root directory (where the `pom.xml` file is located).
3. Compile the source code using Maven by running:
   ```bash
   mvn compile
   ```

## ▶️ Running the Game
Once the project is successfully compiled, you can launch the application by running the generated main class (`edu.hazi.Main`). Maven handles this via the exec plugin. 
Run the following command:
```bash
mvn exec:java
```

## 🧪 Running Tests
This project includes automated JUnit tests to ensure map validity and core game logic. You can execute all tests by running:
```bash
mvn test
```

## 📁 Project Structure
- `src/main/java/edu/hazi/`: Contains the core source code for the game logic, distinct UI menus, and map management.
- `src/main/resources/maps/`: Includes the predefined puzzle levels for the game.
- `src/test/java/`: Contains unit tests validating game interactions and rules.
