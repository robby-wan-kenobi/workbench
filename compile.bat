mkdir bin
dir /s /B *.java > sources.txt
javac -d bin -cp .\lib\*;.\src @sources.txt
dir /s /B bin\*.class > classes.txt
del sources.txt
