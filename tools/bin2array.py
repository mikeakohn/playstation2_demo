#!/usr/bin/env python

import sys

if len(sys.argv) != 2:
  print "Usage: bin2array.py <filename>"
  sys.exit(0)

print "  static byte[] array ="
print "  {"

count = 0
s = ""

fp = open(sys.argv[1], "rb")

while 1:
  data = fp.read(4096)
  if len(data) == 0: break

  for c in data:
    byte = ord(c)

    if byte > 127: byte = -((byte ^ 0xff) + 1)

    s += " %4d," % byte
    count += 1

    if (count % 8) == 0:
      print "    " + s
      count = 0
      s = ""

  if s != "": print "    " + s

fp.close()

print "  };"

