OSplit
======

OSplit is an open source cross-platform SplitTimer primarily for usage by Speedrunners.

Current Features
================

OSplit is cross-platform (it should work on Mac, Windows, and Linux*) open source split timer that provides the ability to start a timer and record segment times through hotkeys.

OSplit records split times in .csv files, which are a standard file format for trial data. These .csv's can be imported into spreadsheet software or even R files

*More specifically, X11 environments, which constitutes nearly all Linux distros.

Graphical Edition
=================

Coming soon to a repository near you.

Terminal Edition
================

So far there exists only a terminal edition. The terminal edition has received sparse testing on Windows and Linux and is by no means complete. The terminal edition is not particularly user friendly and is meant for largely for testing purposes when the GUI interface is undesired (or not complete, as is the current case).

The terminal edition is used with the following invocation:

    java -jar OSplitTE.jar *SplitFileName*
    
SplitFileName corresponds to either an existing SplitFileName.csv or one created by OSplit. When prompted to provide headers (which correspond to segments), they should be provided in the form:

    PartOne,PartTwo,PartThree
    
Some special symbols work, but as of right now spaces do not.

Currently, OSplitTE uses crtl+0 as a hotkey to both start the timer and time a segment. The times reported and recorded to the .csv file are in nanoseconds since the timer started. The time of each segment can be calculated by subtracting its recorded time from the time of the preceeding segment.

Roadmap
=======

    Core
        [ ] Improve Header Specia Symbol Support
        [ ] Improve the code
        [ ] Handle errors gracefully
    Graphical Edition
        [ ] Open/Create files
        [ ] Start/Stop/Reset/Advance timer
        [ ] Highly Customizable Interface
            [ ] Comparison Choices
            [ ] Pictures, Words, System Variables
            [ ] Some Interface to allow others to easily make components
    Statistical
        [ ] Basic SplitTimer Statistics (Personal Best, Best of Segments, Averages)
        [ ] Advanced Statistics (Improvement over time, segment IQR and variance)
        [ ] Ability to generate a .csv with statistical summaries
    Dreams (AKA, Don't get your hopes up)
        [ ] OBS-Studio Plugin
        [ ] SpeedRunsLive Integration
