import net.mikekohn.java_grinder.Playstation2;
import net.mikekohn.java_grinder.Draw3D.Draw3DTriangleFan;

public class JavaGrinderScreen
{
  static float[] white_points =
  {
    -300.0f,  150.0f, 0.f,
     300.0f,  150.0f, 0.f,
     300.0f, -150.0f, 0.f,
    -300.0f, -150.0f, 0.f,
  };
 
  static float[] red_points =
  {
    -300.0f,  50.0f, 0.f,
     300.0f,  50.0f, 0.f,
     300.0f, -50.0f, 0.f,
    -300.0f, -50.0f, 0.f,
  };

  static void run()
  {
    Draw3DTriangleFan white_square = new Draw3DTriangleFan(4);
    Draw3DTriangleFan red_square = new Draw3DTriangleFan(4);
    int white_rotate = 0;
    int red_rotate = 0;
    float white_z = 2048.0f;
    float red_z = 2048.0f;
    int i;

    for (i = 0; i < 4; i++)
    {
      white_square.setPointColor(i, 0x00ffffff);
      red_square.setPointColor(i, 0x000000ff);
    }

    white_square.setPoints(white_points);
    white_square.setPosition(1320.f, 1170.0f, 2048.0f);

    red_square.setPoints(red_points);
    red_square.setPosition(1320.f, 1370.0f, 2048.0f);

    white_square.setContext(0);
    red_square.setContext(0);
    Playstation2.showContext(0);

    for (i = 0; i < 60 * 3; i++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(0);

      white_square.draw();
      red_square.draw();
    }

    for (i = 0; i < 120; i++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(0);

      white_z += 10.0f;
      white_rotate -= 1;
      white_square.setPosition(1320.f, 1170.0f, white_z);
      white_square.rotateX512(white_rotate);

      if (i >= 60)
      {
        red_z += 10.0f;
        red_rotate -= 1;
        red_square.setPosition(1320.f, 1370.0f, red_z);
        red_square.rotateX512(white_rotate);
      }

      white_square.draw();
      red_square.draw();
    }

    Playstation2.clearContext(0);
  }
}

