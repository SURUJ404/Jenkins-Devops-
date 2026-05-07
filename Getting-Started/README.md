Jenkins Installation and Overview Guide

This guide provides an overview of Jenkins, its features, and step-by-step instructions to install Jenkins on a Debian-based system. The content is designed to help users understand Jenkins and set it up effectively. 

## Install Java JDK 17 on Windows

### 1. Install JDK 17

Run the following command in PowerShell (Administrator):

```powershell
winget install EclipseAdoptium.Temurin.17.JDK


2. Verify Installation Directory

Check the installed JDK folder: 

dir "C:\Program Files\Eclipse Adoptium" 

Example output: 

jdk-17.0.19.10-hotspot

Add Java to PATH (Temporary)

Run: 

$env:Path += ";C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot\bin" 

4. Verify Java Installation

Check Java version:

java -version
javac -version


openjdk version "17.x.x"
javac 17.x.x


Add Java to PATH Permanently

Run the following command in PowerShell (Administrator): 

[System.Environment]::SetEnvironmentVariable(
"Path",
$env:Path + ";C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot\bin",
[System.EnvironmentVariableTarget]::Machine
)


6. Restart PowerShell

Close and reopen PowerShell, then verify again: 

java -version
javac -version