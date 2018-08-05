import net.mikekohn.java_grinder.Math;
import net.mikekohn.java_grinder.Playstation2;
import net.mikekohn.java_grinder.Draw3D.Triangle;

public class Cubes
{
  static float[] points =
  {
    -40.000f, -40.000f, 40.000f,
    40.000f, -40.000f, 40.000f,
    40.000f, 40.000f, 40.000f,
    40.000f, 40.000f, 40.000f,
    -40.000f, 40.000f, 40.000f,
    -40.000f, -40.000f, 40.000f,
    -40.000f, 40.000f, -40.000f,
    40.000f, 40.000f, -40.000f,
    40.000f, -40.000f, -40.000f,
    -40.000f, 40.000f, -40.000f,
    40.000f, -40.000f, -40.000f,
    -40.000f, -40.000f, -40.000f,
    -40.000f, -40.000f, -40.000f,
    40.000f, -40.000f, -40.000f,
    40.000f, -40.000f, 40.000f,
    40.000f, -40.000f, 40.000f,
    -40.000f, -40.000f, 40.000f,
    -40.000f, -40.000f, -40.000f,
    -40.000f, 40.000f, 40.000f,
    40.000f, 40.000f, 40.000f,
    40.000f, 40.000f, -40.000f,
    -40.000f, 40.000f, 40.000f,
    40.000f, 40.000f, -40.000f,
    -40.000f, 40.000f, -40.000f,
    40.000f, 40.000f, -40.000f,
    40.000f, 40.000f, 40.000f,
    40.000f, -40.000f, 40.000f,
    40.000f, 40.000f, -40.000f,
    40.000f, -40.000f, 40.000f,
    40.000f, -40.000f, -40.000f,
    -40.000f, -40.000f, -40.000f,
    -40.000f, -40.000f, 40.000f,
    -40.000f, 40.000f, 40.000f,
    -40.000f, 40.000f, 40.000f,
    -40.000f, 40.000f, -40.000f,
    -40.000f, -40.000f, -40.000f,
  };

  static int[] colors =
  {
    0x80ff0000,
    0x80ff0000,

    0x8000ff00,
    0x8000ff00,

    0x80ff00ff,
    0x80ff00ff,

    0x80ffff00,
    0x80ffff00,

    0x8000ffff,
    0x8000ffff,

    0x80ffffff,
    0x80ffffff,
  };

  static void run()
  {
    //Triangle cube = new Triangle(points.length / 3);
    Triangle cube = new Triangle(36);
    //Triangle cube = new Triangle(3);

    int n, i;

    cube.disableGouraudShading();

    i = 0;

    for (n = 0; n < 12; n++)
    //for (n = 0; n < 1; n++)
    {
      cube.setPointColor(i++, colors[n]);
      cube.setPointColor(i++, colors[n]);
      cube.setPointColor(i++, colors[n]);
    }

    cube.setPoints(points);
    cube.setPosition(1320.0f, 1200.0f, 2048.0f);
    //cube.setPointColors(colors);
    cube.setContext(0);

    Playstation2.showContext(0);

    // Single cube rotating
    for (n = 0; n < (60 * 3) + 30; n++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();
      Playstation2.showContext(n + 1);

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(n);

      // Draw the cube on the screen
      cube.setContext(n);
      cube.draw();

      cube.rotateX512(n << 4);
      cube.rotateY512(n << 2);
    }

    float ft = 0;
    int t = 0;
    int dt = 12;
    float x, y;

    // Four cubes rotating
    for (n = 0; n < (60 * 5) + 10; n++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();
      Playstation2.showContext(n + 1);

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(n);
      cube.setContext(n);

      t = (int)ft;

      x = 120 * Math.cos512(t);
      y = 120 * Math.sin512(t);

      cube.rotateX512(n << 4);
      cube.rotateY512(n << 2);
      //cube.setPosition(1200.0f, 1100.0f, 2048.0f);
      cube.setPosition(1300.0f + x, 1200.0f + y, 2048.0f);
      cube.draw();

      x = 120 * Math.cos512(t + 128);
      y = 120 * Math.sin512(t + 128);

      cube.rotateX512(n << 2);
      cube.rotateY512(n << 4);
      //cube.setPosition(1400.0f, 1100.0f, 2048.0f);
      cube.setPosition(1300.0f + x, 1200.0f + y, 2048.0f);
      cube.draw();

      x = 120 * Math.cos512(t + 256);
      y = 120 * Math.sin512(t + 256);

      cube.rotateZ512(n << 2);
      cube.rotateY512(n << 4);
      //cube.setPosition(1200.0f, 1300.0f, 2048.0f);
      cube.setPosition(1300.0f + x, 1200.0f + y, 2048.0f);
      cube.draw();

      x = 120 * Math.cos512(t + 384);
      y = 120 * Math.sin512(t + 384);

      cube.rotateZ512(n << 4);
      cube.rotateX512(n << 2);
      //cube.setPosition(1400.0f, 1300.0f, 2048.0f);
      cube.setPosition(1300.0f + x, 1200.0f + y, 2048.0f);
      cube.draw();

      ft = ft + (5.0f * Math.cos512(n << 2));
    }

    Playstation2.clearContext(0);
    Playstation2.clearContext(1);
  }
}

