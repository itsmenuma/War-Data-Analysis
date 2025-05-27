# Development Guide

This document provides guidance for developers working on the War Management System. It covers project structure, development environment setup, and best practices.

## Project Structure

```
warManagement/
├── src/                           # Source code
│   ├── com/
│   │   └── warManagementGUI/      # JavaFX GUI components
│   │       ├── DataAnalysis/      # JavaFX Charts and data visualization
│   │       ├── Equipment/         # Equipment management
│   │       ├── Mission/           # Mission management
│   │       ├── Personnel/         # Personnel management
│   │       ├── Supply/            # Supply management
│   │       ├── Units/             # Units management
│   │       ├── util/              # Utilities and helpers
│   │       ├── WarManagement.java # Controller class 
│   │       └── WarManagementApp.java # JavaFX Application main class
│   └── pics/                      # Images and icons
├── mission_predictor/             # Python-based mission prediction
│   ├── mission_predictor_api.py   # Flask API for predictions
│   ├── mock_mission_data.xlsx     # Sample data for training
│   ├── requirements.txt           # Python dependencies
│   └── train_ann_model.py         # Model training script
├── docs/                          # Documentation
├── Queries.sql                    # Database schema and queries
├── pom.xml                        # Maven project configuration with JavaFX dependencies
└── requirements.txt               # Python visualization dependencies
```

## Development Environment Setup

### 1. JavaFX Development

#### IDE Setup

We recommend using IntelliJ IDEA or Eclipse for JavaFX development.

**IntelliJ IDEA Setup**:

1. Open IntelliJ IDEA
2. Select "Open or Import"
3. Navigate to the warManagement directory
4. Select the pom.xml file and click "Open"
5. Choose "Open as Project"
6. Go to "File > Project Structure > Libraries" and ensure JavaFX libraries are properly added
7. Add VM Options for JavaFX modules if needed: `--module-path PATH_TO_FX --add-modules javafx.controls,javafx.fxml`

**Eclipse Setup**:

1. Open Eclipse
2. Select "File > Import"
3. Choose "Maven > Existing Maven Projects"
4. Navigate to the warManagement directory
5. Click "Finish"
6. Install the "e(fx)clipse" plugin if not already present
7. Configure build path to include JavaFX modules

#### JavaFX Coding Standards

- Follow standard Java naming conventions:
  - Classes: PascalCase (e.g., `PersonnelDetails`)
  - Methods and variables: camelCase (e.g., `getPersonnelDetails`) 
  - Constants: UPPER_SNAKE_CASE (e.g., `MAX_PERSONNEL_COUNT`)
  - FXML files: PascalCase matching controller class names (e.g., `PersonnelView.fxml`)
- Include JavaDoc comments for all public methods
- Use inheritance and interfaces appropriately
- Handle exceptions properly
- Use FXML for view definitions when possible, separating view from controller logic
- Follow the MVC (Model-View-Controller) pattern for JavaFX components
- Use CSS for styling JavaFX components instead of inline styles

### 2. Python Development

For Python development (mission predictor and visualization):

1. Set up a virtual environment:

   ```bash
   cd warManagement
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```

2. Install required packages:

   ```bash
   cd mission_predictor
   pip install -r requirements.txt
   cd ..
   pip install -r requirements.txt
   ```

3. Use a Python IDE such as PyCharm or VS Code with Python extensions.

#### Python Coding Standards

- Follow PEP 8 styling guidelines
- Use docstrings for functions and modules
- Handle exceptions appropriately
- Use type hints where possible

### 3. Database Development

For database development:

1. Install MySQL Workbench for easier database management
2. Connect to your local MySQL instance
3. Import the schema from `Queries.sql`
4. Add dummy values from `Data.sql`
5. Use MySQL Workbench for schema changes and query testing

## Testing

### JavaFX Testing

- Use JUnit for unit testing Java components
- For JavaFX UI components, use TestFX framework
- Create tests in the `src/test/java` directory
- Run tests using Maven:
  ```bash
  mvn test
  ```
- Use `Platform.runLater` when testing JavaFX UI components
- Reference the `ChartMigrationTest.java` file for examples of testing JavaFX charts

### Python Testing

- Use pytest for Python components
- Place tests in a `tests` directory
- Run tests with pytest:
  ```bash
  pytest
  ```

## Building and Deployment

### JavaFX Application

- Build the JavaFX application using Maven:
  ```bash
  mvn clean package
  ```
- Run the application using JavaFX Maven plugin:
  ```bash
  mvn javafx:run
  ```
- Alternatively, run the application with JavaFX modules explicitly:
  ```bash
  java --module-path PATH_TO_JAVAFX_MODULES --add-modules javafx.controls,javafx.fxml -jar target/warManagement-1.0.0-SNAPSHOT.jar
  ```

Note: Replace `PATH_TO_JAVAFX_MODULES` with the path to your JavaFX SDK lib directory.

### Python Components

- The Python components can be run directly:
  ```bash
  cd mission_predictor
  python mission_predictor_api.py
  ```
