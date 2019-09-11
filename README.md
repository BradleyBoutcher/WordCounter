## Word Counter by Bradley Boutcher

### Introduction
Word Counter is a simple Java application for aggregating words and their number of appearances from a list of file paths.
> Note: For the simplest understanding of the process, look at the HeadlessFileReader class in FileReader.java

### Running
Word Counter is created using Maven, which is the recommended way to build and run. Within the directory, run:
> mvn clean install
> mvn package

To run in interactive mode, use:
> run.sh start

or manually specify the JAR:

> java -jar target/wordcounter-1.0-SNAPSHOT.jar

To run in non-interactive mode, use: 
> java -jar target/wordcounter-1.0-SNAPSHOT.jar \< File Paths... \<

### Implementation
Word Counter is implemented in two ways: 'headless' and 'interactive'. In interactive mode, we use a combination of builder and state patterns for generating and managing any number of `WordCounter` objects. A `WordCounter` is a custom data structure that also provides an interface for reading files, with a local `FileReader`, which contains non-interactive methods for interacting with a  given file.

> NOTE: Words are split with any non-letter character. No space is put in place of the character. For example, "Bill.Likes.Dogs" would be interpreted as a single word, "BillLikeDogs".

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

## Additional thoughts
The data "flow" is as follows:

Convert each input path to a File -> Retrieve and Return the total word count for each individual file -> Merge each individual word count into the total

Alternatively, it would be simple enough to merge the word count into the total as we process each individual word, however this removes some flexibility. Such a process would look like this:

Turn each path into a file -> Retrieve and parse each word from the file ->  Add or put the word in our overall map

The goal was to break it into a reasonable amount of small pieces to be handled individually before returning as a whole, which can be done in many ways with relative simplicity.

### Debugging
Run the included bash file, `run.sh` with the parameter `debug` like so:
> run.sh debug

This will begin the process on port 1044, which you can attach to in your IDE of choice.

If you're curious, the word count for this README is:
saved: 4,
interacting: 2,
objects: 2,
your: 2,
without: 2,
path: 5,
character: 2,
java: 4,
would: 3,
merge: 4,
jar: 5,
state: 3,
you: 3,
wordcounter: 3,
using: 4,
pass: 2,
an: 2,
each: 9,
input: 1,
contains: 2,
as: 5,
nonletter: 1,
interpreted: 1,
be: 4,
turn: 1,
immediately: 2,
two: 2,
example: 1,
youre: 2,
filereader: 2,
into: 6,
characters: 2,
current: 2,
see: 2,
are: 5,
blocking: 2,
by: 2,
builder: 2,
attach: 2,
so: 2,
runsh: 4,
a: 25,
alternatively: 1,
manager: 2,
words: 3,
makes: 2,
simplification: 2,
the: 40,
single: 3,
such: 1,
to: 12,
open: 2,
supported: 3,
through: 2,
billlikesdogs: 1,
reader: 10,
run: 6,
down: 2,
aggregating: 2,
asynchronicity: 2,
warning: 2,
up: 2,
ea: 2,
commands: 3,
us: 2,
which: 7,
add: 4,
given: 2,
ignored: 2,
patterns: 2,
count: 8,
this: 12,
ide: 2,
list: 3,
look: 1,
regarding: 2,
port: 2,
paths: 8,
completedfuture: 2,
overall: 1,
billlikedogs: 1,
however: 1,
some: 1,
needed: 2,
additional: 2,
for: 9,
generating: 2,
choose: 2,
interface: 3,
directory: 2,
we: 3,
remove: 3,
provides: 2,
fileprovider: 2,
debugging: 2,
custom: 2,
start: 1,
reading: 3,
counter: 5,
parse: 1,
message: 2,
way: 2,
headless: 3,
with: 5,
print: 3,
bash: 2,
bradley: 2,
wordcounters: 2,
wordcount: 2,
select: 2,
chains: 2,
interactive: 4,
convert: 1,
bit: 2,
when: 4,
put: 2,
number: 4,
if: 2,
flow: 1,
in: 13,
specify: 2,
is: 10,
it: 1,
exit: 3,
printed: 2,
traditional: 2,
install: 2,
following: 3,
explorer: 3,
invalid: 2,
word: 27,
begin: 2,
boutcher: 2,
methods: 3,
save: 2,
increment: 2,
our: 1,
local: 2,
mac: 2,
appearances: 2,
flexibility: 1,
file: 18,
ways: 2,
library: 2,
native: 2,
place: 1,
map: 1,
macos: 2,
may: 2,
within: 3,
break: 2,
more: 2,
decrement: 2,
additionally: 2,
removes: 1,
choice: 2,
return: 1,
data: 3,
use: 6,
simple: 3,
used: 2,
space: 1,
mode: 5,
that: 2,
split: 1,
strip: 2,
implemented: 2,
create: 2,
few: 2,
from: 7,
combination: 2,
readable: 2,
new: 2,
package: 2,
manually: 2,
curious: 2,
individual: 3,
like: 3,
retriggers: 2,
created: 2,
maven: 2,
follows: 1,
noninteractive: 3,
targetwordcountersnapshotjar: 3,
files: 3,
retrieve: 4,
managing: 2,
note: 3,
no: 1,
code: 2,
line: 3,
their: 3,
respective: 2,
delete: 2,
aggregate: 2,
running: 5,
can: 6,
total: 6,
and: 10,
of: 12,
parameter: 2,
make: 2,
included: 2,
introduction: 2,
on: 3,
allows: 2,
console: 2,
process: 5,
or: 6,
debug: 3,
mvn: 3,
os: 2,
will: 2,
implementation: 2,
also: 3,
enough: 1,
readme: 2,
clean: 2,
any: 4,
structure: 2,
command: 3,
recommended: 2,
processed: 2,
async: 2,
application: 2,
build: 2,
stages: 2,
user: 4,
