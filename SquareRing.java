import net.mikekohn.java_grinder.Math;
import net.mikekohn.java_grinder.Playstation2;
import net.mikekohn.java_grinder.Draw3D.Triangle;
import net.mikekohn.java_grinder.Draw3D.TriangleFan;

public class SquareRing
{
  static float[] points =
  {
    -12.000f, -12.000f, 0.000f,
    -12.000f,  12.000f, 0.000f,
     12.000f,  12.000f, 0.000f,
     12.000f, -12.000f, 0.000f,
  };

  static int[] colors =
  {
    0x80ff0000,
    0x80ff0000,
    0x80ff0000,
    0x80ff0000,
  };

  static void drawRing(TriangleFan square, int radius, int step, int color, float depth, int rotation)
  {
    float x, y;
    int n;

    square.setPointColor(0, color);
    square.setPointColor(1, color);
    color = color & 0x007f7f7f;
    square.setPointColor(2, color);
    square.setPointColor(3, color);

    for (n = 0; n < 512; n += step)
    {
      x = radius * Math.cos512(n + rotation);
      y = radius * Math.sin512(n + rotation);

      square.setPosition(1320.0f + x, 1224.0f + y, 2048.0f + depth);
      square.draw();
    }
  }

  static void run()
  {
    TriangleFan square = new TriangleFan(4);
    Triangle pyramid = new Triangle(ObjectPyramid.points.length / 3);

    pyramid.setPoints(ObjectPyramid.points);
    pyramid.setPointColors(ObjectPyramid.colors);
    //pyramid.disableGouraudShading();

    int n, i, rotation, ring_rotation;
    float x, y, z;

    square.setPoints(points);
    square.setPosition(1320.0f, 1224.0f, 2048.0f);
    square.setContext(0);

    Playstation2.showContext(0);

    rotation = 0;

    // Single squares rotating
    for (n = 0; n < (60 * 8) + 30; n++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();
      Playstation2.showContext(n + 1);

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(n);

      square.rotateX512(rotation << 4);
      square.rotateY512(rotation << 2);
      rotation++;

      square.setContext(n);
      drawRing(square, 160, 20, 0x00ff0000,   0.0f, 0);

      if (n > (60 * 2) + 00)
      {
        drawRing(square, 140, 32, 0x000000ff,  40.0f, 0);
      }

      if (n > (60 * 4) + 20)
      {
        drawRing(square, 120, 51, 0x0000ff00,  80.0f, 0);
      }

      if (n > (60 * 6) + 40)
      {
        drawRing(square, 100, 64, 0x00ff00ff, 120.0f, 0);
      }
    }

    x = 0; y = 0; z = -2000;
    ring_rotation = 0;

    // Single squares rotating with pyramid sliding in.
    for (n = 0; n < (60 * 5); n++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();
      Playstation2.showContext(n + 1);

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(n);

      square.rotateX512(rotation << 4);
      square.rotateY512(rotation << 2);
      pyramid.rotateY512(rotation << 1);
      pyramid.rotateZ512(rotation << 2);
      rotation++;

      square.setContext(n);
      drawRing(square, 160, 20, 0x00ff0000,   0.0f,  ring_rotation);
      drawRing(square, 140, 32, 0x000000ff,  40.0f, -ring_rotation);
      drawRing(square, 120, 51, 0x0000ff00,  80.0f,  ring_rotation);
      drawRing(square, 100, 64, 0x00ff00ff, 120.0f, -ring_rotation);
      ring_rotation++;

      pyramid.setContext(n);
      pyramid.setPosition(1320.0f, 1224.0f, 2048.0f + z);
      pyramid.draw();

      z = z + 6.6f;
    }

    int pyramid_rotation = 0;

    // Single squares rotating with pyramid rotating around ring.
    for (n = 0; n < (60 * 35); n++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();
      Playstation2.showContext(n + 1);

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(n);

      square.rotateX512(rotation << 4);
      square.rotateY512(rotation << 2);
      pyramid.rotateY512(rotation << 1);
      pyramid.rotateZ512(rotation << 2);
      rotation++;

      square.setContext(n);
      drawRing(square, 160, 20, 0x00ff0000,   0.0f,  ring_rotation);
      drawRing(square, 140, 32, 0x000000ff,  40.0f, -ring_rotation);
      drawRing(square, 120, 51, 0x0000ff00,  80.0f,  ring_rotation);
      drawRing(square, 100, 64, 0x00ff00ff, 120.0f, -ring_rotation);
      ring_rotation++;

      pyramid.setContext(n);

      x = 120 * Math.cos512(pyramid_rotation);
      z = 120 * Math.sin512(pyramid_rotation);

      pyramid.setPosition(1195.0f + x, 1224.0f, 2048.0f + z);
      pyramid.draw();

      if (n > 60 * 10)
      {
        pyramid.setPosition(1320.0f, 1224.0f + x, 2048.0f + z);
        pyramid.draw();
      }

      if (n > 60 * 15)
      {
        pyramid.setPosition(1320.0f, 1224.0f - x, 2048.0f + z);
        pyramid.draw();
      }

      if (n > 60 * 17)
      {
        pyramid.setPosition(1195.0f - x, 1224.0f, 2048.0f - z);
        pyramid.draw();
      }

      if (n > 60 * 20)
      {
        pyramid.setPosition(1195.0f - x, 1224.0f + x, 2048.0f - z);
        pyramid.draw();
      }

      if (n > 60 * 23)
      {
        pyramid.setPosition(1195.0f - x, 1224.0f + x, 2048.0f + z);
        pyramid.draw();
      }

      if (n > 60 * 26)
      {
        pyramid.setPosition(1195.0f - x, 1224.0f - x, 2048.0f + z);
        pyramid.draw();
      }

      if (n > 60 * 29)
      {
        pyramid.setPosition(1195.0f + x, 1224.0f - z, 2048.0f + z);
        pyramid.draw();
      }

      if (n > 60 * 29)
      {
        pyramid.setPosition(1195.0f + x, 1224.0f - z, 2048.0f);
        pyramid.draw();
      }

      pyramid_rotation += 4;
    }

    Playstation2.clearContext(0);
  }
}

