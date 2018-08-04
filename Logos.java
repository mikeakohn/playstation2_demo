import net.mikekohn.java_grinder.Playstation2;
import net.mikekohn.java_grinder.Draw3D.TriangleFanWithTexture;
import net.mikekohn.java_grinder.Draw3D.Texture16;

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
    Texture16 texture_ps2 = new Texture16(128, 64);
    Texture16 texture_naken_asm = new Texture16(128, 64);
    Texture16 texture_java_grinder = new Texture16(128, 64);
    Texture16 texture_java = new Texture16(64, 64);
    TriangleFanWithTexture ps2 = new TriangleFanWithTexture(4);
    TriangleFanWithTexture naken_asm = new TriangleFanWithTexture(4);
    TriangleFanWithTexture java_grinder = new TriangleFanWithTexture(4);
    TriangleFanWithTexture java = new TriangleFanWithTexture(4);

    float distance_x = 230;
    float distance_y = 170;
    float z = 2048.0f;
    float dx = 1.0f;
    float dy = 2.0f;

    int n;

    for (n = 0; n < 4; n++)
    {
      ps2.setPointColor(n, 0x80808080);
      naken_asm.setPointColor(n, 0x80808080);
      java_grinder.setPointColor(n, 0x80808080);
      java.setPointColor(n, 0x80808080);
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
    ps2.setPosition(1320.0f - distance_x, 1218.0f - distance_y, z);
    ps2.enableAlphaBlending();
    ps2.setContext(0);

    naken_asm.setPoints(points);
    naken_asm.setTextureCoords(texture_coords);
    naken_asm.setPosition(1320.0f - distance_x, 1218.0f + distance_y, z);
    naken_asm.enableAlphaBlending();
    naken_asm.setContext(0);

    java_grinder.setPoints(points);
    java_grinder.setTextureCoords(texture_coords);
    java_grinder.setPosition(1320.0f + distance_x, 1218.0f - distance_y, z);
    java_grinder.enableAlphaBlending();
    java_grinder.setContext(0);

    java.setPoints(points_square);
    java.setTextureCoords(texture_coords);
    java.setPosition(1320.0f + distance_x, 1218.0f + distance_y, z);
    java.enableAlphaBlending();
    java.setContext(0);

    Playstation2.showContext(0);

    for (n = 0; n < 60 * 3; n++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();
      Playstation2.showContext(n + 1);

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(n);

      texture_ps2.upload();
      ps2.setContext(n);
      ps2.draw();

      texture_naken_asm.upload();
      naken_asm.setContext(n);
      naken_asm.draw();

      texture_java_grinder.upload();
      java_grinder.setContext(n);
      java_grinder.draw();

      texture_java.upload();
      java.setContext(n);
      java.draw();
    }

    for (n = 0; n < 47; n++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();
      Playstation2.showContext(n + 1);

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(n);

      texture_ps2.upload();
      ps2.setContext(n);
      ps2.setPosition(1320.0f - distance_x, 1218.0f - distance_y, z);
      ps2.draw();

      texture_naken_asm.upload();
      naken_asm.setContext(n);
      naken_asm.setPosition(1320.0f - distance_x, 1218.0f + distance_y, z);
      naken_asm.draw();

      texture_java_grinder.upload();
      java_grinder.setContext(n);
      java_grinder.setPosition(1320.0f + distance_x, 1218.0f - distance_y, z);
      java_grinder.draw();

      texture_java.upload();
      java.setContext(n);
      java.setPosition(1320.0f + distance_x, 1218.0f + distance_y, z);
      java.draw();

      distance_x -= dx;
      distance_y -= dy;
      z += 70.0f;
      dx += 0.19f;
      dy += 0.08f;
    }

    Playstation2.clearContext(1);
  }
}

