Magic9 GUI Tool
===
First Beta
---

Most of the functionality has been already implemented and visible in the GUI. Probably there are some tweaks and improvements that could be done, but for know that application is already functional. 

What can this software do right now?
---
All the planned features have been already implemented which includes:

* SD card preparation for each step.
* CRC Checksum to ensure the files have been correctly extracted/copied to the SD.
* Automatic download of required resources.

What will this software do?
---

This will be a graphical interface to automate the process of preparing an SD card for the installation of [arm9loaderhax](https://github.com/AuroraWright/arm9loaderhax) based on the instructions from [3ds.guide](http://3ds.guide). 

I have added a previous SD card backup step if the supplied SD card is not empty (and of course the idea is to clean up the whole thing). I've also added an SD Capacity verification, checking the filesystem is much more complicated.

I have added as well a middle-step to check the integrity and backup the NAND from SD (which have to be previously extracted from the device using Decrypt9). This enforces the users to backup their NANDs.
