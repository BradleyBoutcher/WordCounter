## Word Counter by Bradley Boutcher

### Introduction
Word Counter is a simple Java application for aggregating words and their number of appearances from a list of file paths.

### Implementation
Word Counter is implemented in two ways: headless and interactive. In interactive mode, we use a combination of builder and state patterns for generating and managing any number of `WordCounter` objects. A `WordCounter` is a custom data structure that also provides an interface for reading files, with a local `FileReader`, which contains non-interactive methods for interacting with a  given file.

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
