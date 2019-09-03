## Word Counter by Bradley Boutcher

### Introduction
Word Counter is a simple Java application for aggregating words and their number of appearances from a list of file paths.

### Running
Word Counter is created using Maven, which is the recommended way to build and run. Within the directory, run:
> mvn clean install
> mvn package

To run in interactive mode, use:
> run.sh start

or manually specify the JAR:

> java -jar target/wordcounter-1.0-SNAPSHOT.jar

To run in non-interactive mode, use: 
> run.sh start <File Paths...>
> java -jar target/wordcounter-1.0-SNAPSHOT.jar <File Paths...>

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

### Debugging
Run the included bash file, `run.sh` with the parameter `debug` like so:
> run.sh debug

This will begin the process on port 1044, which you can attach to in your IDE of choice.

If you're curious, the word count for this README is:
saved: 4
interacting: 2
objects: 2
your: 2
without: 2
path: 3
java: 4
merge: 2
jar: 5
state: 3
you: 3
wordcounter: 3
using: 4
pass: 2
an: 2
each: 3
contains: 2
as: 2
be: 2
immediately: 2
two: 2
youre: 2
filereader: 2
into: 3
characters: 2
current: 2
see: 2
are: 4
blocking: 2
by: 2
builder: 2
attach: 2
so: 2
runsh: 4
a: 21
manager: 2
words: 2
makes: 2
simplification: 2
the: 32
single: 2
to: 10
open: 2
supported: 3
through: 2
reader: 10
run: 6
down: 2
aggregating: 2
asynchronicity: 2
warning: 2
up: 2
ea: 2
commands: 3
us: 2
which: 7
add: 3
given: 2
ignored: 2
patterns: 2
this: 10
count: 5
ide: 2
list: 3
regarding: 2
port: 2
paths: 8
completedfuture: 2
needed: 2
additional: 2
for: 7
generating: 2
choose: 2
interface: 3
directory: 1
we: 2
remove: 3
provides: 2
fileprovider: 2
debugging: 2
custom: 2
reading: 3
counter: 5
message: 2
way: 1
headless: 3
with: 4
print: 3
bash: 2
bradley: 2
wordcounters: 2
wordcount: 2
select: 2
chains: 2
interactive: 4
bit: 2
when: 4
number: 4
if: 2
in: 11
specify: 1
is: 8
exit: 3
printed: 2
traditional: 2
install: 1
following: 3
explorer: 3
invalid: 2
word: 20
begin: 2
boutcher: 2
methods: 3
save: 2
increment: 2
local: 2
mac: 2
appearances: 2
file: 14
ways: 2
library: 2
native: 2
macos: 2
may: 2
within: 3
break: 2
more: 2
decrement: 2
additionally: 2
choice: 2
data: 2
use: 6
simple: 2
used: 2
mode: 5
that: 2
strip: 2
implemented: 2
create: 2
few: 2
from: 6
combination: 2
readable: 2
new: 2
package: 1
manually: 1
curious: 2
retriggers: 2
like: 2
created: 1
maven: 1
noninteractive: 3
targetwordcountersnapshotjar: 2
files: 3
retrieve: 2
managing: 2
note: 2
code: 2
line: 3
their: 3
respective: 2
delete: 2
aggregate: 2
running: 5
can: 6
total: 3
and: 8
of: 11
parameter: 2
make: 2
included: 2
introduction: 2
on: 3
allows: 2
console: 2
process: 3
or: 5
debug: 3
mvn: 2
os: 2
will: 2
implementation: 2
also: 3
readme: 2
clean: 1
any: 3
structure: 2
command: 3
recommended: 1
processed: 2
async: 2
application: 2
build: 1
stages: 2
user: 4
