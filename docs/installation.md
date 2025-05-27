# Installation Instructions

This document provides detailed step-by-step instructions for installing and setting up the War Management System.

## Windows Installation

### Prerequisites

1. **Java Development Kit (JDK) 21**:

   - Download from [Oracle's website](https://www.oracle.com/java/technologies/downloads/)
   - Run the installer and follow the instructions
   - Set JAVA_HOME environment variable:
     ```
     setx JAVA_HOME "C:\Program Files\Java\jdk-21"
     ```
   - Add Java to your PATH:
     ```
     setx PATH "%PATH%;%JAVA_HOME%\bin"
     ```

2. **JavaFX SDK** (Optional - Maven will download dependencies automatically):

   - Download JavaFX SDK from [Gluon's website](https://gluonhq.com/products/javafx/)
   - Extract to a directory (e.g., `C:\Program Files\JavaFX`)
   - Set PATH_TO_FX environment variable (useful for command line execution):
     ```
     setx PATH_TO_FX "C:\Program Files\JavaFX\lib"
     ```

3. **MySQL 8.0**:

   - Download MySQL Installer from [MySQL's website](https://dev.mysql.com/downloads/installer/)
   - Run the installer and select "MySQL Server" and "MySQL Workbench"
   - Follow the setup wizard instructions
   - Remember your root password!

4. **Maven**:

   - Download from [Maven's website](https://maven.apache.org/download.cgi)
   - Extract to a directory (e.g., `C:\Program Files\Maven`)
   - Set M2_HOME environment variable:
     ```
     setx M2_HOME "C:\Program Files\Maven"
     ```
   - Add Maven to your PATH:
     ```
     setx PATH "%PATH%;%M2_HOME%\bin"
     ```

5. **Python** (optional, for advanced features):
   - Download from [Python's website](https://www.python.org/downloads/)
   - Run the installer and check "Add Python to PATH"
   - Verify installation by running:
     ```
     python --version
     ```

## Linux/macOS Installation

### Prerequisites

1. **Java Development Kit (JDK) 21**:

   - Linux:
     ```bash
     sudo apt update
     sudo apt install openjdk-21-jdk
     ```
   - macOS (using Homebrew):
     ```bash
     brew install openjdk@21
     ```
   - Set JAVA_HOME:
     ```bash
     echo 'export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which javac))))' >> ~/.bashrc
     echo 'export PATH=$PATH:$JAVA_HOME/bin' >> ~/.bashrc
     source ~/.bashrc
     ```

2. **JavaFX SDK** (Optional - Maven will download dependencies automatically):

   - Linux:
     ```bash
     sudo apt install openjfx
     ```
   - macOS:
     ```bash
     brew install openjfx
     ```

3. **MySQL 8.0**:

   - Linux:
     ```bash
     sudo apt update
     sudo apt install mysql-server
     sudo mysql_secure_installation
     ```
   - macOS:
     ```bash
     brew install mysql
     brew services start mysql
     mysql_secure_installation
     ```

4. **Maven**:

   - Linux:
     ```bash
     sudo apt install maven
     ```
   - macOS:
     ```bash
     brew install maven
     ```

5. **Python** (optional):
   - Linux:
     ```bash
     sudo apt install python3 python3-pip python3-venv
     ```
   - macOS:
     ```bash
     brew install python
     ```
