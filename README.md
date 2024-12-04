# **Java Swing Chess Application**

## **Overview**
This project is a fully functional chess game developed using **Java Swing**. It features robust gameplay mechanics and advanced features like checkmate detection, castling, undo functionality, and more. The application supports both **local play** and **networked multiplayer mode** using a custom-built networking framework.

## **Features**
### **Core Features**
- **Standard Chess Rules**: Implements all standard chess rules, including piece movement, check, and checkmate.
- **Highlight Valid Tiles**: Displays valid moves for the selected piece.

### **Advanced Features**
- **Checkmate Detection**: Automatically determines when a game ends in checkmate.
- **Castling**: Fully functional with validation.
- **No Self-Check Rule**: Ensures no piece move leaves the king in check.
- **Undo Functionality**: Allows players to revert their moves.
- **Pawn Promotion**: Players can choose the promotion piece via a custom dialog.

### **Networking Support**
- **Custom Networking Framework**: Allows two players to connect over a network and play seamlessly.
- **Dynamic Matchmaking**: Matches two players based on their connection order using UUIDs.
- **Real-Time Updates**: Ensures moves are synchronized between clients.
- **Disconnect Handling**: Tracks and manages client disconnections.

---

## **Requirements**
- **Java Development Kit (JDK)**: Version 8 or higher.
- **Networking Framework**: Clone and build the [NFrameworkserver](https://GitHub.com/Mohammeddaniyal/NetworkFramework.git).
- **Gson Library**: Ensure the Gson library is available in your classpath for serialization/deserialization.

---

## **Setup and Usage**

### **Compilation**

#### **For Server**
1. Compile with the required dependencies:
   ```bash
   javac -classpath c:/NFramework/server/build/libs/server.jar;c:/NFramework/common/build/libs/common.jar;gson-jar-path ServerChessFrame.java ServerChessUpdater.java
(compile both)
#### **For Client**
```bash
   javac -classpath c:/NFramework/client/build/libs/client.jar;c:/NFramework/common/build/libs/common.jar;gson-jar-path Chess.java
```
### **Compilation**

#### **For Server**
```bash
   java -classpath c:/NFramework/server/build/libs/server.jar;c:/NFramework/common/build/libs/common.jar;gson-jar-path --add-opens java.base/java.util=ALL-UNNAMED -classpath <compiled-classes-and-jar-paths> ServerChessFrame
```
   #### **For Client**
```bash
java  -classpath c:/NFramework/client/build/libs/client.jar;c:/NFramework/common/build/libs/common.jar;gson-jar-path --add-opens java.base/java.util=ALL-UNNAMED -classpath <compiled-classes-and-jar-paths> Chess "player name"
```
### **Validation files**
#### Compile
```bash
javac *.java
```
This files are needed to be compiled, before executing the actual application.
