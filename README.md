## Word Counter by Bradley Boutcher

### Introduction
Word Counter is a simple Java application for aggregating words and their number of appearances from a list of file paths.

### Running
Word Counter is created using Maven, which is the recommended way to build and run. Within the directory, run:
> mvn clean install
> mvn package

To run in interactive mode, use:
> run.sh
or manually specify the JAR:
> java -jar target/wordcounter-1.0-SNAPSHOT.jar

To run in non-interactive mode, use: 
> java -jar target/wordcounter-1.0-SNAPSHOT.jar <File Paths...>

### Implementation
Word Counter is implemented in two ways: 'headless' and 'interactive'. In interactive mode, we use a combination of builder and state patterns for generating and managing any number of `WordCounter` objects. A `WordCounter` is a custom data structure that also provides an interface for reading files, with a local `FileReader`, which contains non-interactive methods for interacting with a  given file.

#### In the state manager, the following commands are supported:
1: Create a new Word Reader
2: Print a saved Word Reader
3: Save current Word Reader
4: Open a saved Word Reader
5: Delete a saved Word Reader
0: Exit

#### In the `WordCounter`s, the following commands are supported: 
1: Add a file path to this Word Reader 
2: Remove a file path from this Word Reader
3: Print the aggregate word count of this Word Reader
4: List file paths in this word reader
0: Exit

Additionally, the user can choose to use the command line or the native file explorer for their OS to select file paths.
> NOTE: When using the file explorer on Mac, you may see a warning message regarding the MacOS fileprovider, this can be ignored.

In headless mode, the user can pass in any number of file paths through the command line when running the JAR File, which are immediately processed, and the respective wordcount printed to the console.

### Asynchronicity 
The methods used within make use of the EA Async library, which is a simplification of traditional `CompletedFuture` chains, and makes code a bit more readable. This also allows us to break up the reading of files into a few stages without blocking:
1. Retrieve the word count from a single file
    a. Strip down invalid characters from each word
2. Merge the word count into the running total
    a. Increment or decrement each word from the running total, as needed
When using the interface, the user can add additional paths or remove paths, which retriggers this process.

### Debugging
Run the included bash file, `run.sh` with the parameter `debug` like so:
> run.sh debug
This will begin the process on port 1044, which you can attach to in your IDE of choice.

If you're curious, the word count for this README is:
{wordcounters=1, wordcount=1, select=1, saved=3, chains=1, interacting=1, objects=1, interactive=2, your=1, bit=1, when=3, without=1, number=3, path=2, java=1, merge=1, jar=1, state=2, if=1, you=2, wordcounter=2, using=2, in=8, pass=1, is=5, an=1, each=2, exit=2, printed=1, contains=1, traditional=1, as=1, following=2, explorer=2, invalid=1, word=18, begin=1, boutcher=1, be=1, methods=2, save=1, increment=1, immediately=1, two=1, local=1, mac=1, youre=1, appearances=1, filereader=1, into=2, characters=1, current=1, see=1, file=12, ways=1, library=1, are=3, native=1, blocking=1, by=1, builder=1, attach=1, so=1, runsh=2, macos=1, a=20, manager=1, may=1, within=1, break=1, more=1, words=1, makes=1, simplification=1, the=28, single=1, decrement=1, additionally=1, to=6, choice=1, open=1, supported=2, through=1, data=1, use=3, reader=9, simple=1, run=1, used=1, down=1, mode=2, that=1, strip=1, aggregating=1, asynchronicity=1, implemented=1, create=1, warning=1, few=1, from=5, up=1, ea=1, commands=2, us=1, combination=1, which=5, add=2, readable=1, given=1, new=1, ignored=1, curious=1, retriggers=1, like=1, patterns=1, this=9, count=4, noninteractive=1, ide=1, list=2, regarding=1, port=1, paths=6, completedfuture=1, files=2, retrieve=1, managing=1, note=1, code=1, line=2, needed=1, additional=1, for=6, their=2, generating=1, choose=1, respective=1, interface=2, delete=1, we=1, remove=2, aggregate=1, running=3, can=5, total=2, and=6, provides=1, of=10, parameter=1, fileprovider=1, debugging=1, make=1, included=1, introduction=1, on=2, allows=1, console=1, process=2, or=3, debug=2, os=1, will=1, implementation=1, custom=1, reading=2, also=2, counter=3, readme=1, message=1, any=2, structure=1, command=2, headless=2, with=3, processed=1, async=1, print=2, application=1, stages=1, bash=1, bradley=1, user=3}

