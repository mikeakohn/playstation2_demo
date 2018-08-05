import net.mikekohn.java_grinder.Math;
import net.mikekohn.java_grinder.Playstation2;
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

      square.setPosition(1320.0f + x, 1200.0f + y, 2048.0f + depth);
      square.draw();
    }
  }

  static void run()
  {
    TriangleFan square = new TriangleFan(4);

    int n, i;

    for (n = 0; n < 4; n++)
    {
      square.setPointColor(n, colors[n]);
    }

    square.setPoints(points);
    //square.setPosition(1320.0f, 1200.0f, 2048.0f);
    square.setPosition(1320.0f, 1200.0f, 2048.0f);
    square.setContext(0);

    Playstation2.showContext(0);

    // Single square rotating
    for (n = 0; n < (60 * 10); n++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();
      Playstation2.showContext(n + 1);

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(n);

      square.setContext(n);
      drawRing(square, 160, 20, 0x00ff0000,   0.0f, 0);
      if (n > 60 * 2) { drawRing(square, 140, 32, 0x000000ff,  40.0f, 0); }
      if (n > 60 * 5) { drawRing(square, 120, 51, 0x0000ff00,  80.0f, 0); }
      if (n > 60 * 7) { drawRing(square, 100, 64, 0x00ff00ff, 120.0f, 0); }

      square.rotateX512(n << 4);
      square.rotateY512(n << 2);
    }

    Playstation2.clearContext(0);
  }
}

