
import net.mikekohn.java_grinder.Playstation2;
import net.mikekohn.java_grinder.Memory;
import net.mikekohn.java_grinder.Draw3D.TriangleFanWithTexture;
import net.mikekohn.java_grinder.Draw3D.Texture16;

public class Mandelbrots
{
  static short[] colors =
  {
    0x0000,  // 0
    0x1c00,  // 1
    0x3c00,  // 2
    0x7c00,  // 3
    0x00e0,  // 4
    0x01e0,  // 5
    0x03e0,  // 6
    0x07e0,  // 7
    0x03e0,  // 8
    0x01e3,  // 9
    0x00e3,  // a
    0x0067,  // b
    0x004f,  // c
    0x002f,  // d
    0x000f,  // e
    0x001f,  // f
  };

  static int[] texture_colors =
  {
    0x00ffffff,
    0x00ffffff,
    0x00ffffff,
    0x00ffffff,
  };

  static float[] points_64 =
  {
    -32.0f,  32.0f, 0.f,
     32.0f,  32.0f, 0.f,
     32.0f, -32.0f, 0.f,
    -32.0f, -32.0f, 0.f,
  };

  static float[] points_128_64 =
  {
    -64.0f,  32.0f, 0.f,
     64.0f,  32.0f, 0.f,
     64.0f, -32.0f, 0.f,
    -64.0f, -32.0f, 0.f,
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
    TriangleFanWithTexture mandelbrot = new TriangleFanWithTexture(4);
    TriangleFanWithTexture two_vector = new TriangleFanWithTexture(4);
    TriangleFanWithTexture one_mips = new TriangleFanWithTexture(4);

    Texture16 texture_mandelbrot = new Texture16(64, 64);
    Texture16 texture_two_vector = new Texture16(128, 64);
    Texture16 texture_one_mips = new Texture16(128, 64);

    byte[] image_two_vector = Memory.preloadByteArray("assets/two_vector.trle16");
    byte[] image_one_mips = Memory.preloadByteArray("assets/one_mips.trle16");

    mandelbrot.setPointColors(texture_colors);
    mandelbrot.setPoints(points_64);
    mandelbrot.setTextureCoords(texture_coords);
    mandelbrot.setPosition(1320.f, 1240.0f, 1148.0f);

    texture_two_vector.setPixelsRLE16(0, image_two_vector);
    two_vector.setPointColors(texture_colors);
    two_vector.setPoints(points_128_64);
    two_vector.setTextureCoords(texture_coords);

    texture_one_mips.setPixelsRLE16(0, image_one_mips);
    one_mips.setPointColors(texture_colors);
    one_mips.setPoints(points_128_64);
    one_mips.setTextureCoords(texture_coords);

    // Pass in 16 floats as paramters (described in the mandelbrot_vu0.asm
    // file) and expect 64 * 8 pixels back.
    float[] vu0_params = new float[16];
    int[] vu0_data = new int[64 * 8];

    // Starting values
    float real_start = -2.00f;
    float real_end = 1.00f;
    float imaginary_start = -1.00f;
    float imaginary_end = 1.00f;

    // Upload the code to VU0 first.
    Playstation2.vu0UploadCode(MandelbrotsVU0.code);

    float r_step = (real_end - real_start) / 64;
    float i_step = (imaginary_end - imaginary_start) / 64;

    float imaginary_add = i_step * 8;
    int y, n, ptr;
    float posx, posz;

    // DEBUG DEBUG DEBUG
    //for (n = 0; n < vu0_data.length; n++) { vu0_data[n] = 127; }
    //Playstation2.vu0UploadData(0, vu0_data);
    // DEBUG DEBUG DEBUG

    // Set up r_step and i_step in the VU0 params.  These values
    // shouldn't change for a single image.  Only need to set the
    // first value for the step, the VU0 code can copy the first
    // value to the other 3.  r0, r1, r2, r3 shouldn't change between
    // runs.
    vu0_params[0] = r_step;
    //vu0_params[1] = 0;
    //vu0_params[2] = 0;
    //vu0_params[3] = 0;
    vu0_params[4] = i_step;
    //vu0_params[5] = 0;
    //vu0_params[6] = 0;
    //vu0_params[7] = 0;
    vu0_params[8] = real_start;
    vu0_params[9] = real_start + r_step;
    vu0_params[10] = vu0_params[9] + r_step;
    vu0_params[11] = vu0_params[10] + r_step;
    vu0_params[12] = imaginary_start;
    vu0_params[13] = imaginary_start + i_step;
    vu0_params[14] = vu0_params[13] + i_step;
    vu0_params[15] = vu0_params[14] + i_step;

    ptr = 0;

    for (y = 0; y < 8; y++)
    {
      // Upload data to draw the Mandelbrot and wait until it's done.
      // Then download the piece of the image so it can be transferred
      // to VU1 to be drawn as a texture.
      Playstation2.vu0UploadData(0, vu0_params);
      Playstation2.vu0Start();
      while(Playstation2.vu0IsRunning()) { }
      Playstation2.vu0DownloadData(0, vu0_data);

      for (n = 0; n < vu0_data.length; n++)
      {
        // FIXME: To Deal with issues in VU0.  Remove later.
        int data = vu0_data[n];

        if (data > 127 || data < 0) { data = 127; }
        texture_mandelbrot.setPixel(ptr, colors[data >> 3]);
        ptr++;
      }

      vu0_params[12] += imaginary_add;
      vu0_params[13] += imaginary_add;
      vu0_params[14] += imaginary_add;
      vu0_params[15] += imaginary_add;
    }

    posx = 1000.0f;
    posz = 3000.0f;

    // Two vector units image
    for (n = 0; n < 61; n++)
    {
      // Wait until the video beam is done drawing the last frame.
      // Then show the last drawn frame.
      Playstation2.waitVsync();
      Playstation2.showContext(n + 1);

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(n);

      texture_two_vector.upload();

      two_vector.setContext(n);
      two_vector.setPosition(posx, 1100.0f, posz);
      two_vector.rotateZ512((n - 60) * 8);
      two_vector.draw();

      posx += 5.0f;
      posz -= 35.0f;
    }

    posx = 1000.0f;
    posz = 3000.0f;

    // One MIPS image
    for (n = 0; n < 61; n++)
    {
      // Wait until the video beam is done drawing the last frame.
      // Then show the last drawn frame.
      Playstation2.waitVsync();
      Playstation2.showContext(n + 1);

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(n);

      texture_two_vector.upload();

      two_vector.setContext(n);
      two_vector.draw();

      texture_one_mips.upload();

      one_mips.setContext(n);
      one_mips.setPosition(posx, 1380.0f, posz);
      one_mips.rotateZ512((n - 60) * 8);
      one_mips.draw();

      posx += 5.0f;
      posz -= 35.0f;
    }

    for (n = 0; n < 61 * 3; n++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();

      // Clear the entire context of where this is going to draw.
      Playstation2.showContext(n + 1);
      Playstation2.clearContext(n);

      texture_mandelbrot.upload();

      mandelbrot.setContext(n);
      mandelbrot.draw();

      texture_two_vector.upload();

      two_vector.setContext(n);
      two_vector.draw();

      texture_one_mips.upload();

      one_mips.setContext(n);
      one_mips.draw();
    }
  }
}

