import net.mikekohn.java_grinder.Playstation2;
import net.mikekohn.java_grinder.Memory;
import net.mikekohn.java_grinder.draw3d.TriangleFanWithTexture;
import net.mikekohn.java_grinder.draw3d.Texture16;

public class Credits
{
  static float[] points_1 =
  {
    -150.0f,  100.0f, 0.f,
     150.0f,  100.0f, 0.f,
     150.0f, -100.0f, 0.f,
    -150.0f, -100.0f, 0.f,
  };

  static float[] points_2 =
  {
    -300.0f,  100.0f, 0.f,
     300.0f,  100.0f, 0.f,
     300.0f, -100.0f, 0.f,
    -300.0f, -100.0f, 0.f,
  };

  static float[] points_3 =
  {
    -100.0f,  50.0f, 0.f,
     100.0f,  50.0f, 0.f,
     100.0f, -50.0f, 0.f,
    -100.0f, -50.0f, 0.f,
  };

  static float[] texture_coords =
  {
    0.0f, 1.0f,
    1.0f, 1.0f,
    1.0f, 0.0f,
    0.0f, 0.0f,
  };

  static void run()
  {
    Texture16 texture_michael_kohn = new Texture16(128, 64);
    Texture16 texture_mikekohn_net = new Texture16(256, 64);
    Texture16 texture_year = new Texture16(128, 64);
    TriangleFanWithTexture michael_kohn = new TriangleFanWithTexture(4);
    TriangleFanWithTexture mikekohn_net = new TriangleFanWithTexture(4);
    TriangleFanWithTexture year = new TriangleFanWithTexture(4);
    byte[] image_michael_kohn = Memory.preloadByteArray("assets/michael_kohn.trle16");
    byte[] image_mikekohn_net = Memory.preloadByteArray("assets/mikekohn_net.trle16");
    byte[] image_year = Memory.preloadByteArray("assets/2018.trle16");
    int i;

    for (i = 0; i < 4; i++)
    {
      michael_kohn.setPointColor(i, 0x80808080);
      mikekohn_net.setPointColor(i, 0x80808080);
      year.setPointColor(i, 0x80808080);
    }

    texture_michael_kohn.setPixelsRLE16(0, image_michael_kohn);
    texture_mikekohn_net.setPixelsRLE16(0, image_mikekohn_net);
    texture_year.setPixelsRLE16(0, image_year);

    michael_kohn.setPoints(points_1);
    michael_kohn.setTextureCoords(texture_coords);
    michael_kohn.setPosition(1320.f, 1100.0f, 2048.0f);
    michael_kohn.setContext(0);

    mikekohn_net.setPoints(points_2);
    mikekohn_net.setTextureCoords(texture_coords);
    mikekohn_net.setPosition(1320.f, 1380.0f, 2048.0f);
    mikekohn_net.setContext(0);

    year.setPoints(points_3);
    year.setTextureCoords(texture_coords);
    year.setPosition(1320.f, 1240.0f, 2048.0f);
    year.setContext(0);

    for (i = 0; i < 60 * 10; i++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(0);

      texture_michael_kohn.upload();
      michael_kohn.draw();

      texture_mikekohn_net.upload();
      mikekohn_net.draw();

      texture_year.upload();
      year.draw();
    }

    Playstation2.clearContext(0);
  }
}

