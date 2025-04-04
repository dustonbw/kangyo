## Kangyo 干魚

Kangyo is a proof-of-concept localization of (parts of) the [Lox](https://github.com/munificent/craftinginterpreters) interpreter, as described in [Crafting interpreters](https://craftinginterpreters.com).

This is a very close derivitive of the upstream *lox* project, as described in
Nystrom's book.

It was created as a demonstration to accompany a technical report, submitted
for marks as part of *Technical Communication for Engineering Techonlogies*
(ENL2019T) at Algonquin College of Applied Arts and Technology.

As of writing, just the Scanner has been implemented (the first four chapters
in the book).

## Building and Running

On \*nix: clone this repository and run `make jar` to build a JAR archive. Then
`cd bin` and run `java -jar kangyo.jar`. The interpreter can be run as a REPL
or can be passed an input file to read: invoke as `java -jar kangyo.jar
<INPUT>`.

On Windows, you'll need to build manually; compile every file in the
`com/dustonbw/kangyo` directory under `src` (or open and run from an IDE). 
The entry point is in `com.dustonbw.kangyo.Kangyo`.

## Troubleshooting

If you are providing input which matches the keywords and identifier character
set, as listed in `Scanner.java` but only getting the following output from the
REPL:
```
[line 1] Error: Unexpected character '?'
```
You may need to try using a different terminal emulator. Windows Terminal (stock)
does not work, but GNOME Terminal does, for example.

This has been tested with OpenJDK 21.0.6 (the Red Hat build) on Fedora 41, and
with an OpenJDK 21 build on Windows 11.
