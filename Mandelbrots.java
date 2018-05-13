
import net.mikekohn.java_grinder.Playstation2;
import net.mikekohn.java_grinder.Draw3D.Draw3DTriangleFanWithTexture;
import net.mikekohn.java_grinder.Draw3D.Draw3DTexture16;

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
    Draw3DTriangleFanWithTexture mandelbrot = new Draw3DTriangleFanWithTexture(4);
    Draw3DTexture16 image = new Draw3DTexture16(64, 64);

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

    float r_step = real_end - real_start / 64;
    float i_step = imaginary_end - imaginary_start / 64;

    float imaginary_add = i_step * 8;
    int y, n, ptr = 0;

    // Set up r_step and i_step in the VU0 params.  These values
    // shouldn't change for a single image.  Only need to set the
    // first value for the step, the VU0 code can copy the first
    // value to the other 3.  r0, r1, r2, r3 shouldn't change between
    // runs.
    vu0_params[0] = r_step;
    vu0_params[4] = i_step;
    vu0_params[5] = real_start;
    vu0_params[6] = real_start + r_step;
    vu0_params[7] = vu0_params[6] + r_step;
    vu0_params[8] = vu0_params[7] + r_step;
    vu0_params[9] = imaginary_start;
    vu0_params[10] = imaginary_start + i_step;
    vu0_params[11] = vu0_params[11] + i_step;
    vu0_params[12] = vu0_params[12] + i_step;

    for (y = 0; y < 8; y++)
    {
      // Upload data to draw the Mandelbrot and wait until it's done.
      // Then download the piece of the image so it can be transferred
      // to VU1 to be drawn as a texture.
      Playstation2.vu0UploadData(0, vu0_params);
      Playstation2.vu0Start();
      while(Playstation2.vu0IsRunning()) { }
      Playstation2.vu0DownloadData(0, vu0_data);

      for (n = 0; n < 64 * 8; n++)
      {
        image.setPixel(ptr, colors[vu0_data[n]]);
        ptr++;
      }

      vu0_params[9] = imaginary_add;
      vu0_params[10] = imaginary_add;
      vu0_params[11] = imaginary_add;
      vu0_params[12] = imaginary_add;
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

