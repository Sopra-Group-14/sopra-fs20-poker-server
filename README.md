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
Controllers (GameController, UserController, ChatController) - These interact with the frontend using a REST-Interface

Services (GameService, UserService, ChatService) - These command the specific object of these types (Game, User, Chat)

Game - The main gameflow is in the Game class

CardAnalyser/WinnerCalculator - These are used to check the players' cards and determine who has won a round


## Launch and Deployment.
To launch this project locally:
Go into the sopra-fs20-group-14-server folder in your directory and open two command windows. In the first one, run "gradlew.bat build" to build the project; add the "--continuous" keyword in the end if you want it to recompile every time you make a change or the "--xtest" keyword if you don't want to execute any tests. In the second one, run "gradlew.bat bootRun" to start the local server.

To deploy the project:
The project automatically depoys onto Heroku (https://dashboard.heroku.com/apps/sopra-fs20-group-14-server) once pushed to Github.

## Illustrations
To play poker the user needs to register or log in. Then he can create or join a pokergame. The user has an account where he can top up his credit once every 24h. As a not logged in (as well as a logged in) user it is possible to join a game as a spectator and see the hand cards of the different Players as well as some winning odds. 


## Roadmap
We could imagine adding sound effects, card animations as well as a
friend list or private messages to stay in contact with people you liked to play with. 

## Authors and acknowledgment
Csanad Erdei-Griff, Kevin Kindler, Konstantin Moser, Lara Fried, Andy Aidoo

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





