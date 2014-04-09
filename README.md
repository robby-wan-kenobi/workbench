workbench
=========

As a programmer, I wanted an application that would have all my utilities combined in one. This is still a work in progress, so it doesn't include everything that I want yet and there are parts that need to be refactored, but it's a start.

Running the code
----------------
The easiest way to get this to work is to import the Eclipse project and create the runnable jar.

I haven't been able to succesfully create the executable jar from the command line yet, 
so I added a few batch files for compiling and running. They assume the JDK commands (java and javac) 
are available on the command line.
 - compile.bat = just compiles the code
 - run.bat = runs the workbench (assumes that the code is already compiled)
 - compileAndRun.bat = compiles the code and runs the workbench

Note
----
This project uses several external libraries. I do not claim any ownership of them. A lot of these are in the code, but aren't actually used yet (as of 4/8/2014). They include the following:

google-gson
~~~~~~~~~~~
gson-2.2.4.jar

JZoom
~~~~~
jaad-0.8.4.jar

java-audio-player
~~~~~~~~~~~~~~~~~
JavaMediaPlayer.jar

Java Music Player
~~~~~~~~~~~~~~~~~
jl1.0.1.jar

Apache Log4j
~~~~~~~~~~~~
log4j.jar

Sigar
~~~~~
.sigar_shellrc
sigar-amd64-winnt.dll
sigar-x86-winnt.dll
sigar-x86-winnt.lib
sigar.jar
