
import net.mikekohn.java_grinder.Playstation2;
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

  static int[] mandelbrot_colors =
  {
    0x00ffffff,
    0x00ffffff,
    0x00ffffff,
    0x00ffffff,
  };

  static float[] mandelbrot_points =
  {
    -30.0f,  30.0f, 0.f,
     30.0f,  30.0f, 0.f,
     30.0f, -30.0f, 0.f,
    -30.0f, -30.0f, 0.f,
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
    Texture16 image = new Texture16(64, 64);

    mandelbrot.setPointColors(mandelbrot_colors);
    mandelbrot.setPoints(mandelbrot_points);
    mandelbrot.setTextureCoords(texture_coords);
    mandelbrot.setPosition(1320.f, 1200.0f, 1148.0f);

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

    //for (ptr = 0; ptr < 64 * 64; ptr++) { image.setPixel(ptr, 0); }

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
        image.setPixel(ptr, colors[data >> 3]);
        //image.setPixel(ptr, colors[vu0_data[n] >> 3]);
        ptr++;
      }

      vu0_params[12] += imaginary_add;
      vu0_params[13] += imaginary_add;
      vu0_params[14] += imaginary_add;
      vu0_params[15] += imaginary_add;
    }

    Playstation2.showContext(0);

    for (n = 0; n < 60 * 3; n++)
    {
      // Wait until the video beam is done drawing the last frame.
      Playstation2.waitVsync();

      // Clear the entire context of where this is going to draw.
      Playstation2.clearContext(0);

      image.upload();
      mandelbrot.draw();
    }
  }
}

