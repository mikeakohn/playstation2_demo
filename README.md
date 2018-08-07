
Java Grinder Playstation 2 demo.
================================

Introduction
------------

Since Java Grinder is getting Playstation 2 support, I decided
to open a new repository for the demo.  Previously we've been
putting the demos in the samples directory of Java Grinder but
I since this one might have some bigger assets I wanted a
separate repository for it.  There is still a small sample
program with Java Grinder, but it's more for showing examples
of how to use the API.

If the documentation in this README doesn't make sense, please
send me an email and I'll elaborate more.

http://www.mikekohn.net/micro/playstation2_java.php

Building The Project
-------------------

Three things are required:

* This repository
* Java Grinder
* naken_asm

```
git clone https://github.com/mikeakohn/playstation2_demo.git
git clone https://github.com/mikeakohn/java_grinder.git
git clone https://github.com/mikeakohn/naken_asm.git
```

To make the process easier, put all 3 of these repositories in the
same directory.  The Makefile in the demo will find java_grinder and
naken_asm by prefixing the executable with ../  All 3 repos have
very simple Makefiles, with the exception that naken_asm will require
running ./configure first.

