import net.mikekohn.java_grinder.Playstation2;
import net.mikekohn.java_grinder.Draw3D.Draw3DTriangleFanWithTexture;
import net.mikekohn.java_grinder.Draw3D.Draw3DTexture16;

public class Logos
{
  static float[] points =
  {
    -64.0f,  32.0f, 0.f,
     64.0f,  32.0f, 0.f,
     64.0f, -32.0f, 0.f,
    -64.0f, -32.0f, 0.f,
  };

  static float[] points_square =
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

  static void run()
  {
    Draw3DTexture16 texture_ps2 = new Draw3DTexture16(128, 64);
    Draw3DTexture16 texture_naken_asm = new Draw3DTexture16(128, 64);
    Draw3DTexture16 texture_java_grinder = new Draw3DTexture16(128, 64);
    Draw3DTexture16 texture_java = new Draw3DTexture16(64, 64);
    Draw3DTriangleFanWithTexture ps2 = new Draw3DTriangleFanWithTexture(4);
    Draw3DTriangleFanWithTexture naken_asm = new Draw3DTriangleFanWithTexture(4);
    Draw3DTriangleFanWithTexture java_grinder = new Draw3DTriangleFanWithTexture(4);
    Draw3DTriangleFanWithTexture java = new Draw3DTriangleFanWithTexture(4);

    float distance_x = 230;
    float distance_y = 170;
    float z = 2048.0f;
    float dx = 1.0f;
    float dy = 2.0f;

    int i;

    for (i = 0; i < 4; i++)
    {
      ps2.setPointColor(i, 0x80808080);
      naken_asm.setPointColor(i, 0x80808080);
      java_grinder.setPointColor(i, 0x80808080);
      java.setPointColor(i, 0x80808080);
    }

    texture_ps2.setPixelsRLE16(0, ImageLogoPS2.image);
    texture_ps2.enableTransparencyOnBlack();

    texture_naken_asm.setPixelsRLE16(0, ImageLogoNakenAsm.image);
    texture_naken_asm.enableTransparencyOnBlack();

    texture_java_grinder.setPixelsRLE16(0, ImageLogoJavaGrinder.image);
    texture_java_grinder.enableTransparencyOnBlack();

    texture_java.setPixelsRLE16(0, ImageLogoJava.image);
    texture_java.enableTransparencyOnBlack();

    ps2.setPoints(points);
    ps2.setTextureCoords(texture_coords);
    //ps2.setPosition(1100.f, 1050.0f, 2048.0f);
    ps2.setPosition(1320.0f - distance_x, 1218.0f - distance_y, z);
    ps2.enableAlphaBlending();
    ps2.setContext(0);

    naken_asm.setPoints(points);
    naken_asm.setTextureCoords(texture_coords);
    //naken_asm.setPosition(1100.0f, 1350.0f, z);
    naken_asm.setPosition(1320.0f - distance_x, 1218.0f + distance_y, z);
    naken_asm.enableAlphaBlending();
    naken_asm.setContext(0);

    java_grinder.setPoints(points);
    java_grinder.setTextureCoords(texture_coords);
    //java_grinder.setPosition(1550.0f, 1050.0f, z);
    java_grinder.setPosition(1320.0f + distance_x, 1218.0f - distance_y, z);
    java_grinder.enableAlphaBlending();
    java_grinder.setContext(0);

    java.setPoints(points_square);
    java.setTextureCoords(texture_coords);
    //java.setPosition(1550.0f, 1350.0f, z);
    java.setPosition(1320.0f + distance_x, 1218.0f + distance_y, z);
    java.enableAlphaBlending();
    java.setContext(0);

    Playstation2.showContext(0);

    for (i = 0; i < 60 * 3; i++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(0);

      texture_ps2.upload();
      ps2.draw();

      texture_naken_asm.upload();
      naken_asm.draw();

      texture_java_grinder.upload();
      java_grinder.draw();

      texture_java.upload();
      java.draw();
    }

    for (i = 0; i < 47; i++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(0);

      texture_ps2.upload();
      ps2.setPosition(1320.0f - distance_x, 1218.0f - distance_y, z);
      ps2.draw();

      texture_naken_asm.upload();
      naken_asm.setPosition(1320.0f - distance_x, 1218.0f + distance_y, z);
      naken_asm.draw();

      texture_java_grinder.upload();
      java_grinder.setPosition(1320.0f + distance_x, 1218.0f - distance_y, z);
      java_grinder.draw();

      texture_java.upload();
      java.setPosition(1320.0f + distance_x, 1218.0f + distance_y, z);
      java.draw();

      distance_x -= dx;
      distance_y -= dy;
      z += 70.0f;
      dx += 0.19f;
      dy += 0.08f;
    }

    Playstation2.clearContext(0);
  }
}

