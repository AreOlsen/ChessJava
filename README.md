# Chess

A Java-based chess application developed by **Are Olsen**.

## About

Chess is a two-player strategy board game played on an 8×8 board. Each player starts with sixteen pieces, one king, one queen, two rooks, two bishops, two knights, and eight pawns. The objective is to place the opponent's king in **checkmate**, meaning the king is under attack and there is no legal move that can remove the threat.

This project is a graphical implementation of chess focused on providing a simple and enjoyable local playing experience.

## Current Features

The current game mode is:

- **Player vs Player** — two players can play against each other on the same computer.
- Standard chess board and pieces.
- Turn-based gameplay.
- Legal piece movement and basic chess rules.
- **Player vs AI** - Player can play against an ai.

## Screenshots

### Menu

<img width="1070" height="1019" alt="image" src="https://github.com/user-attachments/assets/6b8e2104-d19d-4a5c-8a13-8c4a28bb40cd" />

### Gameplay

<img width="1070" height="1019" alt="image" src="https://github.com/user-attachments/assets/79ed7533-a78b-45fd-9f2a-574597a92806" />

### Game Over

<img width="1070" height="1019" alt="image" src="https://github.com/user-attachments/assets/804492af-f04f-4b11-b044-c28ed54bb7fd" />

## Keybinds.
Current key-binds are:
- "R" for resetting the board.

## Planned Features

The project is still under development. Planned improvements include:

- **Multithreading of Minimax chess AI**
  - Explore a multithreaded implementation to improve search performance.

- **Settings page**
  - Add configurable application settings.
  - Provide options for things such as board appearance and gameplay preferences.

- **Keybinds page**
  - Display available keyboard shortcuts.
  - Allow keybinds to be customized where appropriate.

- **Additional chess rules**
  - Implement **en passant**.
  - Implement **castling**.

- **Online multiplayer**
  - Explore the possibility of adding an online multiplayer mode.
  - Allow players to play against other people over the internet.

## Running the Game

There are two ways to run the application.

(Files may be found under "Deployments").

### Java

The application can be run using the provided `.jar` file.

**Requirements:**

- Java 25 or newer

Run the JAR with:

```bash
java -jar chess-0.1.0.jar
```

### Windows

Windows users can run the provided .exe file directly.
Simply double-click the executable to launch the game.

## Author.
Author: Are Olsen.

## Licence.
The application's licence is specified under the `LICENCE` file.
