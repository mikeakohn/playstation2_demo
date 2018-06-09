
import net.mikekohn.java_grinder.Playstation2;
import net.mikekohn.java_grinder.Draw3D.Points;
import net.mikekohn.java_grinder.Draw3D.TriangleFanWithTexture;
import net.mikekohn.java_grinder.Draw3D.Texture16;
import net.mikekohn.java_grinder.Math;
import net.mikekohn.java_grinder.Memory;

public class Stars
{
  static float[] object_points =
  {
    -64.0f,  64.0f, 0.f,
     64.0f,  64.0f, 0.f,
     64.0f, -64.0f, 0.f,
    -64.0f, -64.0f, 0.f,
  };

  static float[] texture_coords =
  {
    0.0f, 1.0f,
    1.0f, 1.0f,
    1.0f, 0.0f,
    0.0f, 0.0f,
  };

  public static void animateStars(short[] stars, Points points)
  {
    int n;

    for (n = 0; n < stars.length; n += 4)
    {
      stars[n + 0] += stars[n + 2];
      stars[n + 1] += stars[n + 3];

      // If star is beyond boundary, erase it and reset it
      if (stars[n + 0] <= 0 || stars[n + 0] >= 640 ||
          stars[n + 1] <= 0 || stars[n + 1] >= 448)
      {
        stars[n + 0] = (short)(stars_init[n + 0] << 1);
        stars[n + 1] = (short)(stars_init[n + 1] << 1);
      }

      // Draw a trail of stars
      int x = stars[n], y = stars[n + 1];

      for (int c = 0; c < 4; c++)
      {
        float fx = (float)(x - 320);
        float fy = (float)(y - 224);

        points.setPoint(n + c, fx, fy, 0.0f);

        x -= stars[n + 2];
        y -= stars[n + 3];
      }
    }

      points.setPosition(1320.0f, 1224.0f, 2048.0f);
      points.draw();
      points.setPosition(1321.0f, 1224.0f, 2048.0f);
      points.draw();
      points.setPosition(1320.0f, 1225.0f, 2048.0f);
      points.draw();
      points.setPosition(1321.0f, 1225.0f, 2048.0f);
      points.draw();
  }

  public static void run()
  {
    int n;
    int state;

    // Points will be number of stars * 4 since each star will be
    // 4 points (1 bright and 3 gradually less bright).
    short[] stars = new short[stars_init.length];
    Points points = new Points(stars_init.length);
    Texture16 texture_java_grinder = new Texture16(256, 64);
    TriangleFanWithTexture java_grinder = new TriangleFanWithTexture(4);
    byte[] image_java_grinder = Memory.preloadByteArray("assets/java_grinder_title.trle16");
    byte[] image_on_playstation2 = Memory.preloadByteArray("assets/on_playstation2.trle16");

    texture_java_grinder.setPixelsRLE16(0, image_java_grinder);
    texture_java_grinder.enableTransparencyOnBlack();
    java_grinder.enableAlphaBlending();

    for (n = 0; n < 4; n++)
    {
      java_grinder.setPointColor(n, 0x80808080);
    }

    java_grinder.setPoints(object_points);
    java_grinder.setTextureCoords(texture_coords);

    for (n = 0; n < stars.length; n = n + 4)
    {
      stars[n + 0] = (short)(stars_init[n + 0] << 1);
      stars[n + 1] = (short)(stars_init[n + 1] << 1);
      stars[n + 2] = stars_init[n + 2];
      stars[n + 3] = stars_init[n + 3];

      points.setPointColor(n + 0, 0x80ffffff);
      points.setPointColor(n + 1, 0x80aaaaaa);
      points.setPointColor(n + 2, 0x80888888);
      points.setPointColor(n + 3, 0x80666666);
    }

    points.setPosition(1320.0f, 1224.0f, 2048.0f);

    // Main part of method to animate the stars
    for (n = 0; n < 60 * 2; n++)
    {
      // Wait until the video beam is done drawing the last frame.
      // Then show the last drawn frame.
      Playstation2.waitVsync();
      Playstation2.showContext(n + 1);

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(n);

      points.setContext(n);
      java_grinder.setContext(n);

      animateStars(stars, points);
    }

    for (state = 0; state < 2; state++)
    {
      float y = 1120.0f;
      float distance = 4000.0f;

      if (state == 1)
      {
        texture_java_grinder.setPixelsRLE16(0, image_on_playstation2);
      }

      texture_java_grinder.upload();
      java_grinder.rotateY512(0);

      for (n = 0; n < 60 * 3; n++)
      {
        // Wait until the video beam is done drawing the last frame.
        // Then show the last drawn frame.
        Playstation2.waitVsync();
        Playstation2.showContext(n + 1);

        // Clear the entire context of where this is going to draw.
        Playstation2.clearContext(n);

        points.setContext(n);
        java_grinder.setContext(n);

        animateStars(stars, points);

        java_grinder.setPosition(1320.0f, y, distance);
        java_grinder.draw();

        if (distance > 1000.0f)
        {
          y = 1120.0f + (Math.sin512(n * 3) * 200);

          distance = distance - 40;
        }
      }

      for (n = 0; n < 90; n++)
      {
        // Wait until the video beam is done drawing the last frame.
        // Then show the last drawn frame.
        Playstation2.waitVsync();
        Playstation2.showContext(n + 1);

        // Clear the entire context of where this is going to draw.
        Playstation2.clearContext(n);

        points.setContext(n);
        java_grinder.setContext(n);

        animateStars(stars, points);

        java_grinder.rotateY512(n * 30);
        java_grinder.setPosition(1320.0f, y, distance);
        java_grinder.draw();

        if (n > 20) { distance += 100; }
      }
    }

    // Rotate stars and disappear.
    for (n = 0; n < 60 * 2; n++)
    {
      // Wait until the video beam is done drawing the last frame.
      // Then show the last drawn frame.
      Playstation2.waitVsync();
      Playstation2.showContext(n + 1);

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(n);

      points.setContext(n);
      points.rotateZ512(n * 2);
      java_grinder.setContext(n);

      animateStars(stars, points);
    }
  }

  public static short[] stars_init =
  {
    // X0,  Y0,  DX,  DY   (160,112) is center
      140,  110, -1,  -1,
      180,   80,  1,  -1,
      170,  120,  1,   2,
      190,  130,  2,   1,
      150,  130, -2,   1,
      150,  135, -2,   2,
      150,   90, -1,  -2,
      170,   91,  1,  -2,
      165,  112,  2,   0,
      155,  110, -1,   0,
      140,  110, -2,  -2,
      180,   80,  2,  -2,
      170,  120,  2,   3,
      190,  130,  3,   2,
      150,  130, -3,   2,
      150,  135, -3,   3,
      150,   90, -2,  -3,
      170,   91,  2,  -3,
      165,  112,  3,   1,
      155,  110, -2,   1,
  };
}

