
import net.mikekohn.java_grinder.Playstation2;
import net.mikekohn.java_grinder.Memory;
import net.mikekohn.java_grinder.draw3d.TriangleFanWithTexture;
import net.mikekohn.java_grinder.draw3d.Texture16;

public class Mandelbrots
{
  static short[] colors =
  {
    0x0000, // 0
    0x3000, // 1
    0x4c00, // 2
    0x5400, // 3
    0x54c0, // 4
    0x5580, // 5
    0x5660, // 6
    0x0266, // 7
    0x026c, // 8
    0x0273, // 9
    0x018c, // a
    0x00d5, // b
    0x0155, // c
    0x0159, // d
    0x00dd, // e
    0x001f, // f
  };

  static int[] texture_colors =
  {
    0x80ffffff,
    0x80ffffff,
    0x80ffffff,
    0x80ffffff,
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

  static void renderMandelbrot(int chunk, float real_start, float r_step, float imaginary_start, float i_step)
  {
    //float r_step = (real_end - real_start) / 64;
    //float i_step = (imaginary_end - imaginary_start) / 64;
    //float imaginary_add = i_step * 8;
    float offset = i_step * (chunk * 8);

    // Pass in 16 floats as paramters (described in the mandelbrot_vu0.asm
    // file) and expect 64 * 8 pixels back.
    float[] vu0_params = new float[16];

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
    vu0_params[12] = imaginary_start + offset;
    vu0_params[13] = vu0_params[12] + i_step;
    vu0_params[14] = vu0_params[13] + i_step;
    vu0_params[15] = vu0_params[14] + i_step;

    // Upload data to draw the Mandelbrot and wait until it's done.
    // Then download the piece of the image so it can be transferred
    // to VU1 to be drawn as a texture.
    Playstation2.vu0UploadData(0, vu0_params);
    Playstation2.vu0Start();

    //vu0_params[12] += imaginary_add;
    //vu0_params[13] += imaginary_add;
    //vu0_params[14] += imaginary_add;
    //vu0_params[15] += imaginary_add;
  }

  static void downloadMandelbrot(int chunk, Texture16 texture)
  {
    int[] vu0_data = new int[64 * 8];
    int ptr = chunk * (64 * 8);

    // Wait until chunk is finished rendering.
    while(Playstation2.vu0IsRunning()) { }

    // Download data from VU0 to main memory.
    Playstation2.vu0DownloadData(0, vu0_data);

    for (int n = 0; n < vu0_data.length; n++)
    {
      // FIXME: To Deal with issues in VU0.  Remove later.
      int data = vu0_data[n];

      if (data > 127 || data < 0) { data = 127; }
      texture.setPixel(ptr, colors[data >> 3]);
      ptr++;
    }
  }

  static void run()
  {
    float real_start, real_end;
    float imaginary_start, imaginary_end;
    float i_step, r_step;

    final float real_start_0 = 0.37f - 0.00f;
    final float real_end_0 = 0.37f + 0.04f;
    final float imaginary_start_0 = -0.2166f - 0.02f;
    final float imaginary_end_0 = -0.2166f + 0.02f;

    TriangleFanWithTexture mandelbrot = new TriangleFanWithTexture(4);
    TriangleFanWithTexture two_vector = new TriangleFanWithTexture(4);
    TriangleFanWithTexture one_mips = new TriangleFanWithTexture(4);

    Texture16[] texture_mandelbrot = new Texture16[2];
    texture_mandelbrot[0] = new Texture16(64, 64);
    texture_mandelbrot[1] = new Texture16(64, 64);
    Texture16 texture_two_vector = new Texture16(128, 64);
    Texture16 texture_one_mips = new Texture16(128, 64);

    texture_two_vector.enableTransparencyOnBlack();
    texture_one_mips.enableTransparencyOnBlack();

    two_vector.enableAlphaBlending();
    one_mips.enableAlphaBlending();

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

    // Starting values
    real_start = -2.00f;
    real_end = 1.00f;
    imaginary_start = -1.00f;
    imaginary_end = 1.00f;
    r_step = (real_end - real_start) / 64;
    i_step = (imaginary_end - imaginary_start) / 64;

    float drs = (real_start_0 - real_start) / 90;
    float dre = (real_end_0 - real_end) / 90;
    float dis = (imaginary_start_0 - imaginary_start) / 90;
    float die = (imaginary_end_0 - imaginary_end) / 90;

    // Upload the code to VU0 first.
    Playstation2.vu0UploadCode(MandelbrotsVU0.code);

    int y, n;
    float posx, posz;

    for (y = 0; y < 8; y++)
    {
      renderMandelbrot(y, real_start, r_step, imaginary_start, i_step);
      downloadMandelbrot(y, texture_mandelbrot[0]);
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

    int chunk = 8;
    int m = 1;

    // Generate Mandelbrots while animating other things.
    for (n = 0; n < 60 * 5; n++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();

      // Clear the entire context of where this is going to draw.
      Playstation2.showContext(n + 1);
      Playstation2.clearContext(n);

      if (chunk == 8)
      {
        chunk = 0;
        //m = (m + 1) & 1;
        m = m ^ 1;

        real_start += drs;
        real_end += dre;
        imaginary_start += dis;
        imaginary_end += die;
        r_step = (real_end - real_start) / 64;
        i_step = (imaginary_end - imaginary_start) / 64;
      }

      if (n > 60)
      {
        if (n < 120)
        {
          mandelbrot.rotateX512((n - 60) * 8);
        }
          else
        {
          mandelbrot.rotateX512(0);
          mandelbrot.rotateZ512((n - 60) * 8);
        }

        if (n > 120)
        {
          renderMandelbrot(chunk, real_start, r_step, imaginary_start, i_step);

          two_vector.rotateY512(-(n - 120) * 8);
          one_mips.rotateY512((n - 120) * 8);
        }
      }

      texture_mandelbrot[m].upload();

      mandelbrot.setContext(n);
      mandelbrot.draw();

      if (n > 120)
      {
        downloadMandelbrot(chunk, texture_mandelbrot[m ^ 1]);
        chunk++;
        renderMandelbrot(chunk, real_start, r_step, imaginary_start, i_step);
      }

      texture_two_vector.upload();

      two_vector.setContext(n);
      two_vector.draw();

      if (n > 120)
      {
        downloadMandelbrot(chunk, texture_mandelbrot[m ^ 1]);
        chunk++;
        renderMandelbrot(chunk, real_start, r_step, imaginary_start, i_step);
      }

      texture_one_mips.upload();

      one_mips.setContext(n);
      one_mips.draw();

      if (n > 120)
      {
        downloadMandelbrot(chunk, texture_mandelbrot[m ^ 1]);
        chunk++;
        renderMandelbrot(chunk, real_start, r_step, imaginary_start, i_step);
        downloadMandelbrot(chunk, texture_mandelbrot[m ^ 1]);
        chunk++;
      }
    }

    int start = n;
    int count = 0;
    int rotate_x = 0;
    float z = 1148.0f;

    // Show just the Mandelbrot.
    for (n = start; n < 60 * 10; n++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();

      // Clear the entire context of where this is going to draw.
      Playstation2.showContext(n + 1);
      Playstation2.clearContext(n);
      mandelbrot.rotateZ512((n - 60) * 8);

      if (n < 60 * 6)
      {
        rotate_x += 10;
        z = z - 10.0f;

        mandelbrot.rotateX512(rotate_x);
        mandelbrot.setPosition(1320.f, 1240.0f, z);
      }

      if (n > 60 * 9)
      {
        z = z + 60.0f;
        mandelbrot.setPosition(1320.f, 1240.0f, z);
      }

      if (chunk == 8)
      {
        chunk = 0;
        //m = (m + 1) & 1;
        m = m ^ 1;

        real_start += drs;
        real_end += dre;
        imaginary_start += dis;
        imaginary_end += die;
        r_step = (real_end - real_start) / 64;
        i_step = (imaginary_end - imaginary_start) / 64;

        if ((count & 127) == 0)
        {
          drs = -drs;
          dre = -dre;
          dis = -dis;
          die = -die;
        }

        count++;
      }

      texture_mandelbrot[m].upload();

      mandelbrot.setContext(n);
      mandelbrot.draw();

      for (y = 0; y < 4; y++)
      {
        renderMandelbrot(chunk, real_start, r_step, imaginary_start, i_step);
        downloadMandelbrot(chunk, texture_mandelbrot[m ^ 1]);
        chunk++;
      }
    }
  }
}

