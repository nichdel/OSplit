OSplit
======

OSplit is an open source cross-platform SplitTimer primarily for usage by Speedrunners.

Current Features
================

OSplit is cross-platform (it should work on Mac, Windows, and Linux*) open source splitFile timer that provides the ability to start a timer and record segment times through hotkeys.

OSplit records splitFile times in .csv files, which are a standard file format for trial data. These .csv's can be imported into most spreadsheet software with ease.

*More specifically, X11 environments, which constitutes nearly all Linux distros.

Graphical Edition
=================

The GUI is still lacking features, but very much useable. It can be opened via the command line with "java -jar OSplit.jar" but your Operating System of choice likely will open Java programs with a double click.

The Timing page uses shift+s to start the timer, splitFile, and save the timings (after the timer has stopped).

The File page allows you to open and save files as well as deleting splits and editing the headers. Note that changes made to a file are not remembered on other pages until you save the file.

Terminal Edition
================

So far there exists only a terminal edition. The terminal edition has received sparse testing on Windows and Linux and is by no means complete. The terminal edition is not particularly user friendly and is meant largely for testing purposes when the GUI interface is undesired (or not complete, as is the current case).

The terminal edition is used with the following invocation:

    java -jar OSplitTE.jar SplitFileName
    
SplitFileName corresponds to either an existing SplitFileName.csv or one created by OSplit. When prompted to provide headers (which correspond to segments), they should be provided in the form:

    PartOne,PartTwo,PartThree
    
I'm unsure of how many special symbols work, but spaces do.

Currently, OSplitTE uses crtl+0 as the only hotkey. When first pressed, it starts the timer. Every press after that records a segment. After the last segment is recorded, the final press will print the segment times (in nanoseconds) and the program will close.
The .csv created will record times in nanoseconds from the time the timer started. PB and BS are reported when running with an existing splitfile, in seconds per segment.

Statistical Features
====================

OSplit hopes to provide additional utility over other splitFile timers by offering the convenience of meaningful statistics.

Currently, OSplit can report Personal Best, Best Segments (AKA IL), and Averages. Some measure of variance or spread will be available soon. Such measures enable you to see which segments are inconsistent (perhaps heavily RNG based or something you've not yet mastered) and which are consistent (something you do well, or where RNG is unimportant). This allows you to find points of potential improvement with ease.

I also plan to add the ability to compare two SplitFiles, receiving side-by-side statistics for each as well as statistics for the two files combined. This should allow two runners to compare, or allow an individual to compare two separate strategies.

Roadmap
=======

    [ ] To-Do [o] Started [x] Completed

    1.0

    [o] File I/O
        [x] Open and Close Files
        [x] Create Files
        [x] Append Splits to Files
        [x] Read Files
    [x] Terminal Edition
        [x] Open/Create files
        [x] Start and advance timer
        [x] Show statistics upon opening an existing file
    [o] Graphical Edition
        [x] Open/Create/Edit files
        [x] Start/Advance timer
        [ ] Stop/Reset
        [ ] Ability to see statistical summaries
            [ ] Ability to compare two SplitFiles
        [ ] Highly Customizable Interface
            [ ] Comparison Choices
            [ ] Pictures, Words, System Variables
            [ ] Some Interface to allow others to easily make components
            [ ] Color, Font, Hotkey control variables
    [o] Statistical
        [x] Basic SplitTimer Statistics (Personal Best, Best of Segments, Averages)
        [ ] Advanced Statistics (Improvement over time, segment IQR and variance)
        [ ] Ability to generate a .csv with statistical summaries
        [ ] Run predictor

    1.X

    [ ] Dreams (AKA, Don't get your hopes up)
        [ ] OBS-Studio Plugin
        [ ] SpeedRunsLive Integration
    [ ] Compatibility
        [ ] Import splits from other SplitTimers
        [ ] Import splits from websites?
        [ ] Export to other formats (if wanted)
        [ ] ASUP Protocol Support