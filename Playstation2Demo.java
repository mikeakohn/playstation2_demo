import net.mikekohn.java_grinder.Memory;
import net.mikekohn.java_grinder.Playstation2;

public class Playstation2Demo
{
  static public void main(String args[])
  {
    int n;
    byte[] song_shoebox = Memory.preloadByteArray("assets/song_shoebox.adpcm");
    byte[] song_fretless = Memory.preloadByteArray("assets/song_fretless.adpcm");

    Playstation2.clearContext(0);
    Playstation2.clearContext(1);

    BillionDevices.run();
    Logos.run();

    Playstation2.spuInit();
    Playstation2.spuUploadSoundData(song_shoebox);
    Playstation2.spuSetVolume(0, 0x3fff);
    Playstation2.spuSetMasterVolume(0x3fff);
    Playstation2.spuSetPitch(0, 0x0eb3);
    Playstation2.spuKeyOn(0);

    Stars.run();
    Mandelbrots.run();
    Cubes.run();

    for (n = 0; n < 60; n++) { Playstation2.waitVsync(); }

    Playstation2.spuKeyOff(0);
    Playstation2.spuUploadSoundData(song_fretless);
    Playstation2.spuKeyOn(0);

    for (n = 0; n < 40; n++) { Playstation2.waitVsync(); }

    SquareRing.run();

    Playstation2.showContext(0);

    while(true)
    {
      Playstation2.waitVsync();
      Playstation2.clearContext(0);
    }
  }
}

