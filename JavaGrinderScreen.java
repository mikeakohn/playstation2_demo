import net.mikekohn.java_grinder.Playstation2;
import net.mikekohn.java_grinder.Draw3D.Draw3DTriangleFan;
import net.mikekohn.java_grinder.Draw3D.Draw3DTriangleFanWithTexture;
import net.mikekohn.java_grinder.Draw3D.Draw3DTexture16;

public class JavaGrinderScreen
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
    Draw3DTexture16 billion_devices_texture = new Draw3DTexture16(128, 64);
    Draw3DTriangleFanWithTexture billion_devices = new Draw3DTriangleFanWithTexture(4);
    int i;

    for (i = 0; i < 4; i++)
    {
      billion_devices.setPointColor(i, 0x80808080);
    }

    billion_devices_texture.setPixelsRLE16(0, ImageBillionDevices.image);

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

      billion_devices_texture.upload();
      billion_devices.draw();
    }

    for (i = 0; i < 120; i++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(0);

      billion_devices_texture.upload();
      billion_devices.rotateY512(i * 16);
      billion_devices.draw();
    }

    Playstation2.clearContext(0);
  }
}

