SE206 VAMIX Beta ReadMe
Alex Mercer

INSTALLATION
-For VAMIX to run correctly, the resources folder must be in the same directory as the .jar file.

-To launch the .jar file, open your console and navigate to the installed directory.
-Then, enter "java -jar SE206_VAMIX_beta_amer236.jar". The program will then launch.

-DO NOT launch the jar by applying the executable permissions.

-To launch from eclipse, add the src folder and the resources folder to an eclipse project. After adding
-the three jar files from the resources folder to the build path, the project will then compile and can be run.
-Note, the resources folder does not need to be a source folder.

OVERVIEW
-Upon running the program, the main video player and controls are located on the right of the window, 
-with 3 tabs on the left.
-The general tab contains file select and file download capabilities. The file select allows you 
-to import video files to an easy access list.
-From here, individual files can be loaded into the player.
-To download a file, simply enter a url and click download. The download progress is displayed 
-with a loading bar.
-The audio tab allows you to cut audio, video or both, to merge audio tracks, and to replace a video's audio track with another. 
-Each of these functions can be carried out at the same time, can be cancelled and show whether they are active.
-The video tab allows you to add a title screen and a credits scene to the current video, and to apply filters to the video.
-The top menu bar allows you to Save/Load state, as well as select some custom theme colours. 
-This readme can also be accessed the menu bar.

OVERLAY AUDIO
-To overlay audio onto video, you must cut out audio using the first audio tool. 
-Then merge this audio with your second track using the second tool. 
-Finally, this merged track can replace the video's audio track with the third tool.

FILE OUTPUT
-The output of most operations is the directory where the jar is located.

ADDING TEXT
-However, text adding is output to the /home/VAMIX directory. The user is required to copy their 
-output to another folder, as it may be erased from this directory.

SAVE STATE
-The save state file is located in the /home/Documents directory.