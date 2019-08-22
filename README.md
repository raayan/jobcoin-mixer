# jobcoin-mixer
JC Mixer


## Requirements

- Java 8

This project uses Gradle as a build tool
The gradle-wrapper.jar is distributed with this project

## Installation
Compile the project: 
```bash
./gradlew build
```
This will compile the project and build a jar and place it in the `build` folder

## Usage
To start the JobCoin Mixer, run the following:
```bash
./gradlew run
```
Alternatively: 
```bash
java -jar ./build/libs/mixer-X.Y.Z-SNAPSHOT.jar
```
This will start the application with an apache tomcat server on port 8080


## On Windows
Replace all instances of `./gradlew <some-commands>` with `gradlew.bat <some-commands>`
