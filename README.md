# Kineticraft v4 - The Lost City
The following is a custom implementation of many <a href="https://dev.bukkit.org/projects/essentials">Essentials</a> features and staff utilities. With the ability (and option) to expand and customize.

The Kineticraft core code is created by <a href="https://github.com/Kneesnap">Kneesnap</a> and can be found <a href="https://github.com/Kneesnap/Kineticraft">here</a>.
<br />
Repo insights and related details an be found <a href="https://github.com/Kneesnap/Kineticraft/graphs/contributors">here</a>.
<br />
Original ReadMe.md written by <a href="https://github.com/Egoscio">Egoscio</a>
<br />
Code maintenance/upkeep by Kineticraft developers.

# Implementing code to IDE
Before getting started, you will need:
1. IDE of your choice (We highly recommend using IntelliJ IDEA, Community or Professional version)
2. Java JDK 8u152, found <a href="http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html">here</a>

Please note that the following tutorial is for IntelliJ IDEA, assistance with other IDEs are availble upon request

## Instructions for IntelliJ IDEA:

1. Download the repository `source-master` by clicking on `Clone or Download` green button, then click on `Download ZIP`.
2. Open IntelliJ IDEA, `File` > `Open` > Select `source-master` repo folder > `Open`.
3. Configure Project Structure (`File` > `Project Structure`):
    1. Under `Project Settings` > `Project`:
        1. Set `Project SDK` to `1.8`.
        2. Set `Project language level` to `8 - Lambdas, type annotations etc`.
    2. Under `Project Settings` > `Modules` > `Kineticraft` > `Sources`:
        1. Mark the `src` directory as a `Source`.
    3. Under `Project Settings` > `Modules` > `Kineticraft` > `Paths`:
        1. Press the `Use module compile output path` radio button.
        2. Set `Output path` to a `/production/Kineticraft-master/build`.
        3. Set `Test output path` to `/test/Kineticraft-master/test`.
    4. Under `Project Settings` > `Libraries`:
        1. Press the `+` button in the top of the left panel and select `Java`.
        2. From the dropdown file browser, choose the `/libs/` directory.
        3. A window named `Choose Modules` will pop up, select the `Kineticraft` module and press `OK`.
        4. If there are any subdirectories in the `/libs/` directory (eg. Kotlin dependencies grouped into a directory), you need to manually add them using the `+` button in the bottom of the right panel.
    5. Under `Project Settings` > `Artifacts`:
        1. Press the `+` button, hover over `JAR` and select `Empty`.
        2. Name the new entry `Kineticraft`.
        3. Toggle the `Include in project build` checkmark.
        4. In the `Available Elements` panel, double click `'Kineticraft' compile output` to move it over to the left panel.
        5. Click `Kineticraft.jar` and at the bottom of the same window, press the `Use Exisiting Manifest` button, then select `MANIFEST.MF` within the `META-INF` folder.
4. Final Touches
    1. Enable annotation processing:
        1. `IntelliJ IDEA` > `Preferences` > `Build, Execution, Deployment` > `Compiler` > `Annotation Processors`.
        2. Check the `Enable annotation processing` box.
    2. Install `Lombok Plugin`:
        1. `IntelliJ IDEA` > `Preferences` > `Plugins`.
        2. Search and install `Lombok Plugin`.
