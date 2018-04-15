
Java Grinder Playstation 2 demo.
================================

Introduction
------------

Since Java Grinder is getting Playstation 2 support, I decided
to open a new repository for the demo.  Previously we've been
putting the demos in the samples directory of Java Grinder but
I since this one might have some bigger assets I wanted a
separate repository for it.  There is still a small sample
program with Java grinder, but it show off as much as this one
should.

If the documentation in this README doesn't make sense, please
send me an email and I'll elaborate more.

Joining The Project
-------------------

I plan on inviting friends to help me write this demo... anyone
who would like to join this project please send me an email before
starting.

To join, 3 things are required:

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

As far as coding style, please keep the coding style as close to the
rest of the program as possible.  No tabs and no Egyptian brackets.
There are examples lower in this file.  Please don't check in any
large files into this repo also.  Really with the limited resources
of the Playstation 2 it shouldn't need any big files.

The API
-------

So first some things to know about the Playstation 2: The system was
mostly designed to run on a TV / CRT with an interlaced video beam.
So even though the resolution is 640x448, the frame buffer is only
640x224.  The video chip will draw 224 raster lines down the screen
and after a vsync the video beam will go back down the screen in fill
in the space between the original lines with more data.

Because of this the Playstation 2 gives the option to have 2 contexts.
This is a kind of double buffer thing.  So it's possible to draw the
entire display with only the 1st 640x224 sized buffer if there isn't
a lot of data, but if there is too much data both contexts will need
to be used.  I have an example of this in the Java Grinder samples
directory samples/playstation2/PS2Demo.java.  The idea there is while
640x224 buffer used by context 0 is being drawn by the video beam,
the triangle and textured square can be drawing the next 640x224 frame
in a separate buffer.

All of the API calls are in the Java Grinder repository in:

java/net/mikekohn/java_grinder/Playstation2.java
java/net/mikekohn/java_grinder/Draw3D/*.java

Some of the stuff in Math.java should work too (the sin() / cos()
stuff).

There are two vector units in the Playstation 2.  Vector Unit 1 (VU1)
is being used to do 3D rotations and projections.  However, there is
a Vector Unit 0 (VU0) which I did give access to.  There is an
example of this in the Java Grinder samples directory.  Using the
vector unit requires writing assembly language.. a kind of awkward
one since it's a vector unit where most of the instructions are FPU
based and it's VLIW (executes two instructions together at the same
time).  For documentation on it, there is good documentation here
in the VU User's Manual:

[http://hwdocs.webs.com/ps2](http://hwdocs.webs.com/ps2)

When setting the position of an object on the screen, something to
keep in mind is that the display area is:

```
(1728, 1936) to (2368, 2384)
```

Yeah, they are awkward numbers.  I got these from someone else's
Playstation 2 demo when I was learning about the system and never
changed it.  Not sure why they used these numbers.. I could probably
switch it to (1000, 1000) to (1640, 1448) .. maybe I'll do that
actually.

Coding Style
------------

