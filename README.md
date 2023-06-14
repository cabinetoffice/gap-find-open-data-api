# API-Prototype
This repository is a API prototype for retrieving application form data from the admin database.

## Running the app (locally)
If you're using something like Intellij or Spring tool suite, there should be a green triangle somewhere or a "run" menu in the top nav bar. Either of these should allow you to run the application.

Alternatively you could navigate to the ApplicationRunnerApplication class in the package explorer -> right click -> run.

Or use a command to run the app by navigating to wherever you've installed the application, move inside the project directory and run the following command
`./mvnw spring-boot:run`

## Software Installation
To run this project locally you must install the following:
* OpenJDK 17
* Maven

To check whether you have the software installed already and which versions you currently have, you can use the following commands:
Check which versions of Java are installed (if any) `/usr/libexec/java_home -V`

Check which version of Java your computer is currently using (if any) `java -v or java --version`

Check if maven is installed `mvn -v`

### OpenJDK 17
Using Homebrew is an easiest way to install and manage software on a Mac so you want to use it then run through these installation instructions.

https://docs.brew.sh/Installation

Once Homebrew is installed, run the following commands:

Install OpenJDK 17 
`brew install openjdk@17`

Check where OpenJDK 17 has been installed to 
`/usr/libexec/java_home -V`

Set Java Home environment variable, add this to your PATH and add these to the bash profile. The OpenJDK location should come from the output of the command above 

`echo 'export JAVA_HOME=/usr/local/Cellar/openjdk@17/17.0.3/libexec/openjdk.jdk/Contents/Home' >> ~/.bash_profile`

`echo 'export PATH=$PATH:$JAVA_HOME/bin' >> ~/.bash_profile`

NOTE If you are using a shell other than bash then this step will be different for you. If you do not know which shell you are using then run the following command
`echo $0`

### Maven
If you’re using the Spring Tool Suite IDE then Maven will come pre-installed with this. It will also come pre-installed with Intellij IDEA. There is a Spring Tools plugin for VS Code made by the developers of Spring Boot, but I can’t confirm if Maven comes packaged with this.

To install maven using Homebrew you can run the following command:

brew install maven

## remove me
test
