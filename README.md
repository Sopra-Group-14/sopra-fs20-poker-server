# SoPra FS20 Group 14 Poker - Server

## Introduction

This is the server of a poker game implemented for SOPRA FS20 at UZH.
In this game users can play Poker with four different limits (none, fixed, split, pot) as well as watch other games live. They can also chat with other players and spectators. 

## Technologies
Java - The programming language of the backend

Gradle - Used for building and wrapping

SpringBoot - Used for running the server

SonarCube - Code Analysis

Heroku - Deployment


## High-level components: 
Controllers ([GameController](src/main/java/ch/uzh/ifi/seal/soprafs20/controller/GameController.java), [UserController](src/main/java/ch/uzh/ifi/seal/soprafs20/controller/UserController.java), [ChatController](src/main/java/ch/uzh/ifi/seal/soprafs20/controller/ChatController.java)) - These interact with the frontend using a REST-Interface

Services ([GameService](src/main/java/ch/uzh/ifi/seal/soprafs20/service/GameService.java), [UserService](src/main/java/ch/uzh/ifi/seal/soprafs20/service/UserService.java), [ChatService](src/main/java/ch/uzh/ifi/seal/soprafs20/service/ChatService.java)) - These command the specific object of these types (Game, User, Chat)

[Game](src/main/java/ch/uzh/ifi/seal/soprafs20/entity/Game.java) - The main gameflow is in the Game class

[CardAnalyser](src/main/java/ch/uzh/ifi/seal/soprafs20/cards/CardAnalyser.java)/[WinnerCalculator](src/main/java/ch/uzh/ifi/seal/soprafs20/cards/WinnerCalculator.java) - These are used to check the players' cards and determine who has won a round


## Launch and Deployment.
To launch this project locally:
Go into the sopra-fs20-group-14-server folder in your directory and open two command windows. In the first one, run "gradlew.bat build" to build the project; add the "--continuous" keyword in the end if you want it to recompile every time you make a change or the "--xtest" keyword if you don't want to execute any tests. In the second one, run "gradlew.bat bootRun" to start the local server.

To deploy the project:
The project automatically depoys onto Heroku (https://dashboard.heroku.com/apps/sopra-fs20-group-14-server) once pushed to Github.

## Roadmap
A friends list and an out-of-game chat could be implemented so that people can easily find and talk to other players they enjoyed playing with.

Different rulesets of Poker could be added. 

Paid topping up of an account outside of the 24-hour cooldown could be implemented.

## Authors and acknowledgment
Csanad Erdei-Griff, Kevin Kindler, Konstantin Moser, Lara Fried, Andy Aidoo, Dimitri Kohler (Coach)

## License
MIT License

Copyright (c) 2020 Sopra group 14

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.





