Build Bives-SBML 
=================

When you've cloned the source code:

```sh
git clone git@github.com:SemsProject/BiVeS-SBML.git
```

There are two supported options to build this project:

* [Build with Maven](#build-with-maven)
* [Build with Ant](#build-with-ant)

Build with Maven 
-----------------

[Maven](https://maven.apache.org/) is a build automation tool. We ship a `pom.xml` together with the sources which tells maven about versions and dependencies. Thus, maven is able to resolve everything on its own and, in order to create the library, all you need to call is `mvn package`:

```sh
usr@srv $ mvn package

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running de.unirostock.sems.TestParser
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.085 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

mvn -q package  10.32s user 0.26s system 176% cpu 5.997 total
```

That done, you'll find the binaries in the `target` directory.

Build with Ant 
---------------

[Ant](https://ant.apache.org/) is an Apache tool for automating software build processes. There is a `build.xml` file included in the source code that tells ant what to do. Since ant is not able to resolve the dependencies you need to create a directory `lib` containing the following libraries:

* [BiVeS-Core](http://sems.uni-rostock.de/trac/bives-core/wiki) (downloaded latest binary from <http://bin.sems.uni-rostock.de> or [build BiVeS core](http://sems.uni-rostock.de/trac/bives-core/wiki/BuildBivesCore))

We defined multiple targets in the `build.xml`. They can be displayed by calling `ant -p`:

```sh
usr@srv $ ant -p
Buildfile: /path/to/BiVeS-SBML/build.xml

        BiVeS - BioModel Version Control System
        This package provides SBML integration for BiVeS
    
Main targets:

 clean    clean up
 compile  compile the source
 dist     generate the distribution
 init     initialize workspace
 sign     sign a dist
Default target: dist
```

* `clean up` will delete all compiled files and produced libraries
* `compile` compiles the source code
* `dist` bundles all compiled binaries into a jar library

For example, to create the jar library just run `ant dist`:

```sh
usr@srv $ ant dist
Buildfile: /path/to/BiVeS-SBML/build.xml

init:
    [mkdir] Created dir: /path/to/BiVeS-SBML/build
    [mkdir] Created dir: /path/to/BiVeS-SBML/dist

compile:
    [javac] Compiling 39 source files to /path/to/BiVeS-SBML/build

dist:
      [jar] Building jar: /path/to/BiVeS-SBML/dist/BiVeS-SBML-1.2.5.jar
      [jar] Building jar: /path/to/BiVeS-SBML/dist/BiVeS-SBML-1.2.5-fat.jar

BUILD SUCCESSFUL
Total time: 2 seconds
```

