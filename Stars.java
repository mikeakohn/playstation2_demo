
import net.mikekohn.java_grinder.Playstation2;
import net.mikekohn.java_grinder.Draw3D.Points;

public class Stars
{
  public static void animateStars(short[] stars, Points points)
  {
    int n;

    for (n = 0; n < stars.length; n += 4)
    {
      stars[n] += stars[n + 2];
      stars[n + 1] += stars[n + 3];

      // If star is beyond boundary, erase it and reset it
      if (stars[n] <= 0 || stars[n] >= 640 ||
          stars[n + 1] <= 0 || stars[n + 1] >= 448)
      {
        stars[n + 0] = (short)(stars_init[n + 0] << 1);
        stars[n + 1] = (short)(stars_init[n + 1] << 1);
      }

      // Draw a trail of stars
      int x = stars[n], y = stars[n + 1];

      for (int c = 0; c < 4; c++)
      {
        float fx = (float)(x + 1000);
        float fy = (float)(y + 1000);

        points.setPoint(n + c, fx, fy, 2048.0f);

        x -= stars[n + 2];
        y -= stars[n + 3];
      }
    }
  }

  public static void run()
  {
    int n;

    // Points will be number of stars * 4 since each star will be
    // 4 points (1 bright and 3 gradually less bright).
    short[] stars = new short[stars_init.length];
    Points points = new Points(stars_init.length);

    points.disableGouraudShading();

    for (n = 0; n < stars.length; n = n + 4)
    {
      stars[n + 0] = (short)(stars_init[n + 0] << 1);
      stars[n + 1] = (short)(stars_init[n + 1] << 1);
      stars[n + 2] = stars_init[n + 2];
      stars[n + 3] = stars_init[n + 3];

      points.setPointColor(n + 0, 0x00ffffff);
      points.setPointColor(n + 1, 0x00aaaaaa);
      points.setPointColor(n + 2, 0x00888888);
      points.setPointColor(n + 3, 0x00666666);
    }

    // Main part of method to animate the stars
    for (n = 0; n < 60 * 5; n++)
    {
      // Wait until the video beam is done drawing the last frame.
      // Then show the last drawn frame.
      Playstation2.waitVsync();
      Playstation2.showContext(n + 1);

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(n);

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

