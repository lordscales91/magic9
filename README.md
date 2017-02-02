Magic9 GUI Tool
===
Pre-Alpha
---

This is still a WIP on an very early stage, not even an alpha, but close enough. At this point the software is already runnable (but Maven's POM still needs to be updated to copy the required resources) and is capable of determined the required resources and download them. A Table has been used to show the download progress of each file.

What can this software do right now?
---
Determining the required resources to hack the device and download them to the specified folder.

Which features have been already implemented (regardless of GUI visibility)?
---
* SD card preparation for each step (except for Decrypt9's MSET hax version).
* CRC Checksum to ensure the files have been correctly extracted/copied to the SD.
* Automatic download of required resources.

What will this software do?
---

This will be a graphical interface to automate the process of preparing an SD card for the installation of [arm9loaderhax](https://github.com/AuroraWright/arm9loaderhax) based on the instructions of [3ds.guide](http://3ds.guide). 

I have planned to add a previous SD card backup step is the supplied SD card is not empty (and of course the idea is to clean up the whole thing), as well as some requirements verifications (SD capacity, format).

I will add as well a middle-step to check the integrity and backup the NAND from SD (which have to be previously extracted from the device using Decrypt9). This will enforce the users to backup their NANDs.

I will be quite busy until 17th of February (except weekends) but I will try to work on this as much as possible.