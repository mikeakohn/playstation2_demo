#!/usr/bin/env python

import sys

def get_int16(data, index):
  return ord(data[index]) | (ord(data[index + 1]) << 8)

def get_int32(data, index):
  return ord(data[index]) | \
        (ord(data[index + 1]) << 8) | \
        (ord(data[index + 2]) << 16) | \
        (ord(data[index + 3]) << 24)

def leading_zeros(num):
  if num == 0: return 32
  count = 0

  while num != 0:
    if (num & 0x80000000) != 0: break
    num = num << 1
    count += 1

  return count

def trailing_zeros(num):
  if num == 0: return 32

  count = 0

  while num != 0:
    if (num & 1) != 0: break
    num = num >> 1
    count += 1

  return count

# ----------------------------- fold -----------------------------

if len(sys.argv) != 3:
  print "Usage: bmp2texture.py <filename> <8/16/rle16>"
  sys.exit(0)

filename_bmp = sys.argv[1]
filename_texture = filename_bmp.replace(".bmp", "")

bits_per_pixel = 0
compression = 0

if sys.argv[2] == "8":
  bits_per_pixel = 8
  filename_texture += ".t8"
elif sys.argv[2] == "16":
  bits_per_pixel = 16
  filename_texture += ".t16"
elif sys.argv[2] == "rle16":
  bits_per_pixel = 16
  compression = 1
  filename_texture += ".trle16"

print "    bmp filename: " + filename_bmp
print "texture filename: " + filename_texture
print "  bits_per_pixel: " + str(bits_per_pixel)
print "     compression: " + str(compression)

count = 0

fp = open(filename_bmp, "rb")
out = open(filename_texture, "wb")

data = fp.read(128 * 1024)
bytes_read = len(data)

width = get_int32(data, 18)
height = get_int32(data, 22)
bmp_bits_per_pixel = get_int16(data, 28)
image_offset = get_int32(data, 10)
image_size = get_int32(data, 34)

print "Bytes read " + str(bytes_read)
print " ---------- BMP File Header ----------"
print "          header: " + data[0] + data[1]
print "            size: " + str(get_int32(data, 2))
print "        reserved: " + str(get_int16(data, 6))
print "        reserved: " + str(get_int16(data, 8))
print "          offset: " + str(image_offset)
print " ---------- BMP Info Header ----------"
print "            size: " + str(get_int32(data, 14))
print "           width: " + str(width)
print "          height: " + str(height)
print "    color planes: " + str(get_int16(data, 26))
print "  bits per pixel: " + str(bmp_bits_per_pixel)
print "     compression: " + str(get_int32(data, 30))
print "      image size: " + str(get_int32(data, 34))
print "       horiz res: " + str(get_int32(data, 38))
print "        vert res: " + str(get_int32(data, 42))
print "  paletta colors: " + str(get_int32(data, 46))
print "important colors: " + str(get_int32(data, 50))

if (width % 64) != 0:
  print "Width of image must be a multiple of 64"
  sys.exit(1)

if leading_zeros(width) + trailing_zeros(width) != 31:
  print "Width of image must be either 64, 128, 256, 512 pixels."
  sys.exit(1)

if leading_zeros(height) + trailing_zeros(height) != 31:
  print "Height of image must be either 2, 4, 8, 16, 32 64, 128, 256, 512 pixels."
  sys.exit(1)

n = 0

if compression == 1:
  count = 0
  last = -1

  while n < image_size:
    r = ord(data[image_offset + n + 2]) >> 3
    g = ord(data[image_offset + n + 1]) >> 3
    b = ord(data[image_offset + n + 0]) >> 3

    pixel = (r | (g << 5) | (b << 10))

    if pixel != last or count == 255:

      if count != 0:
        out.write(chr(count))
        out.write(chr(last & 0xff))
        out.write(chr(last >> 8))

      count = 0
      last = pixel

    count += 1
    n = n + 3

  if count != 0:
    out.write(chr(count))
    out.write(chr(last & 0xff))
    out.write(chr(last >> 8))

elif bits_per_pixel == 16:

  while n < image_size:
    r = ord(data[image_offset + n + 2]) >> 3
    g = ord(data[image_offset + n + 1]) >> 3
    b = ord(data[image_offset + n + 0]) >> 3

    pixel = (r | (g << 5) | (b << 10))

    out.write(chr(pixel & 0xff))
    out.write(chr(pixel >> 8))

    n = n + 3

else:

  while n < image_size:
    out.write(data[image_offset + n + 2])
    out.write(data[image_offset + n + 1])
    out.write(data[image_offset + n + 0])
    out.write(chr(0))

    n = n + 3

fp.close()
out.close()

