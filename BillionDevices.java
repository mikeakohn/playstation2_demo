import net.mikekohn.java_grinder.Playstation2;
import net.mikekohn.java_grinder.Memory;
import net.mikekohn.java_grinder.Draw3D.TriangleFanWithTexture;
import net.mikekohn.java_grinder.Draw3D.Texture16;

public class BillionDevices
{
  static float[] points =
  {
    -150.0f,  100.0f, 0.f,
     150.0f,  100.0f, 0.f,
     150.0f, -100.0f, 0.f,
    -150.0f, -100.0f, 0.f,
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
    Texture16 texture_billion_devices = new Texture16(256, 128);
    TriangleFanWithTexture billion_devices = new TriangleFanWithTexture(4);
    byte[] image = Memory.preloadByteArray("assets/java_3_billion_4.trle16");
    int i;

    for (i = 0; i < 4; i++)
    {
      billion_devices.setPointColor(i, 0x80808080);
    }

    texture_billion_devices.setPixelsRLE16(0, image);

    billion_devices.setPoints(points);
    billion_devices.setTextureCoords(texture_coords);
    billion_devices.setPosition(1320.f, 1220.0f, 1148.0f);

    billion_devices.setContext(0);
    Playstation2.showContext(0);

    for (i = 0; i < 60 * 3; i++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(0);

      texture_billion_devices.upload();
      billion_devices.draw();
    }

    for (i = 0; i < 120; i++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(0);

      texture_billion_devices.upload();
      billion_devices.rotateY512(i * 16);
      billion_devices.draw();
    }

    Playstation2.clearContext(0);
  }
}

