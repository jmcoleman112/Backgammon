# Backgammon Game

### By Group 33: Jack Coleman (21207103) and Naoise Golden (21376026)
GITHUB ID: jmcoleman112, NaoiseG

## Overview
This project is an implementation of the classic board game Backgammon. It allows two players to play the game either interactively or by using commands from a file.

## Features
- Interactive gameplay with user input
- File-based gameplay for automated testing
- Display of the game board and pip counts
- Support for various game commands (e.g., roll, double, hint, quit)
- Score tracking and match management

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- IntelliJ IDEA or any other Java IDE

### Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/jmcoleman112/backgammon.git
    ```
2. Open the project in your IDE.

### Running the Game

#### Using the IDE
To run the game, execute the `main` method in the `Backgammon` class. You can start the game in two modes:
- **Interactive Mode**: Run the `main` method without any arguments.

#### Using the Release JAR
1. Download the release JAR file from the [releases page](https://github.com/jmcoleman112/backgammon/releases).
2. Open a terminal and navigate to the directory where the JAR file is located.
3. Run the game using the following command:
    ```sh
    java -jar backgammon.jar
    ```
   
### NOTE: Release Jar
Note that this project utilizes UTF-8. If you are on a windows machine and have not enabled UTF-8 already, you
need you to run the following command in the terminal before running the jar file:
```sh
chcp 65001
```

Users on Windows and Unix can likely ignore this.

### Commands
- `roll` or `dice`: Roll the dice.
- `double`: Double the stakes.
- `hint`: Display a list of allowed commands.
- `board`: Display the current game board.
- `pip`: Display the pip count for both players.
- `q`: Quit the game.

## Classes

### `Backgammon`
The main class that initializes and manages the game. It handles the game loop, processes player turns, and manages the transition between different game states.

### `Board`
Represents the game board and manages the state of the points, bar, and end zones. It includes methods to check for game winners and to update the board state based on player moves.

### `Players`
Manages the players and their turns. It keeps track of the current player, switches turns, and provides player-related information such as player names.

### `Match`
Tracks the score and manages the match state. It includes methods to handle the doubling cube and to determine the owner of the doubling cube.

### `Display`
Handles the display of the game board and other game-related messages. It includes methods to print the game board, display player changes, and show hints for allowed commands.

### `InputHandler`
Handles user input and validates commands. It includes methods to read input from the console, check for specific commands, and validate the format of commands.