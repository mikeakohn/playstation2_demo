import net.mikekohn.java_grinder.Playstation2;

public class Playstation2Demo
{
  static public void main(String args[])
  {
    BillionDevices.run();
    Logos.run();
    Stars.run();
    Mandelbrots.run();

    Playstation2.showContext(0);

    while(true)
    {
      Playstation2.waitVsync();
      Playstation2.clearContext(0);
    }
  }
}

