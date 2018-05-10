
Java Grinder Playstation 2 demo.
================================

Introduction
------------

Since Java Grinder is getting Playstation 2 support, I decided
to open a new repository for the demo.  Previously we've been
putting the demos in the samples directory of Java Grinder but
I since this one might have some bigger assets I wanted a
separate repository for it.  There is still a small sample
program with Java grinder, but it's more for showing examples
of how to use the API.

If the documentation in this README doesn't make sense, please
send me an email and I'll elaborate more.

Joining The Project
-------------------

I plan on inviting friends to help work on this demo... anyone
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

The Hardware
------------

It might help to learn about very low level Playstation 2 hardware,
so I created a page that explains it a bit along with links to all
the Playstation 2 PDF programming documents:

http://www.mikekohn.net/software/playstation2.php 

The API
-------

So first some things to know about the Playstation 2: The system was
mostly designed to run on a TV / CRT with an interlaced video beam.
So even though the drawing resolution is 640x448, the frame buffer is
only 640x224.  The video chip will draw 224 raster lines down the
screen and after a vsync the video beam will go back to the top
and go  down the screen to in fill in the space between the originalx
lines with more data.  Probably using this API no one will need
to think about this, but it might be good to know when dealing with
"contexts" (aka double buffering).  The two contexts allow the video
beam to read out of memory of context 1 to draw the even lines on the
screen, while this is happening a branch new image can be drawn on
context 2.  On the next pass of the video beam, context 2 can be
drawn to the computer screen while context 1 is cleared and a new
picture is created.

I have an example of this in the Java Grinder samples directory
samples/playstation2/PS2Demo.java.  The idea there is while
640x224 buffer used by context 0 is being drawn by the video beam,
the triangle and textured square can be drawing the next 640x224 frame
in a separate buffer.

All of the API calls are in the Java Grinder repository in:

```
java/net/mikekohn/java_grinder/Playstation2.java
java/net/mikekohn/java_grinder/Draw3D/*.java
```

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
(1000.0, 1000.0) to (1640.0, 1480.0)
```

Yeah, they are awkward numbers... the Playstation 2 display area is
defined as the X and Y being able to be from 0.0 to 4096.0.  I set
the offset to be 1000.0, 1000.0 in the initialization code.

Coding Style
------------

When writing code, the { } should line up (unless they fit on
one line so:

```
  for (i = 0; i < 100; i++)
  {
  }
```

Notice the space after "for" and the { } line up.  Also, no tabs..
use 2 spaces.





