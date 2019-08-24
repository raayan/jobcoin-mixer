# Raayan Pillai Jobcoin Mixer

## Instructions
### Requirements
- Java 8

This project uses Gradle as a build tool.
The gradle-wrapper.jar is distributed with this project

### Installation
Compile the project: 
```bash
./gradlew build
```
This will compile the project and build a jar and place it in the `build` folder

### Usage
To start the JobCoin Mixer, run the following:
```bash
./gradlew run
```
Alternatively (given that you've built already): 
```bash
java -jar ./build/libs/mixer-X.Y.Z-SNAPSHOT.jar
```

This will start the application on port 8080.

If using **Windows** replace all instances of `./gradlew <some-commands>` with `gradlew.bat <some-commands>`

## Documentation

Here is a diagram I put together that outlines the interaction between the various components of my application
![Sequence Diagram](./docs/sequenceDiagram.svg)
