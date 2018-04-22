use std::env;
use std::fs::File;
use std::io::prelude::*;
use std::process;

fn get_int16(buf: &[u8], index: usize) -> u32
{
  return (buf[index] as u32) | ((buf[index + 1] as u32) << 8);
}

fn get_int32(buf: &[u8], index: usize) -> u32
{
  return (buf[index] as u32) | 
         ((buf[index + 1] as u32) << 8) |
         ((buf[index + 2] as u32) << 16) |
         ((buf[index + 3] as u32) << 24);
}

fn main()
{
  let args: Vec<String> = env::args().collect();

  if args.len() != 3
  {
    println!("Usage: bmp2texture <filename> <16/24>");
    process::exit(0);
  }

  let filename = &args[1];
  let bits_per_pixel = &args[2].parse::<i32>().unwrap();

  println!("      filename: {}", filename);
  println!("bits_per_pixel: {}", bits_per_pixel);

  let mut file = File::open(filename).expect("File not found");
  let mut buf = [0u8; 128 * 1024];
  let bytes_read = file.read(&mut buf).unwrap();

  let width = get_int32(&buf, 18);
  let height = get_int32(&buf, 22);
  let bmp_bits_per_pixel = get_int16(&buf, 28);
  let image_offset = get_int32(&buf, 10) as usize;
  let image_size = get_int32(&buf, 34) as usize;

  println!("Bytes read {}", bytes_read);
  println!(" ---------- BMP File Header ----------");
  println!("          header: {}{}", buf[0] as char, buf[1] as char);
  println!("            size: {}", get_int32(&buf, 2));
  println!("        reserved: {}", get_int16(&buf, 6));
  println!("        reserved: {}", get_int16(&buf, 8));
  println!("          offset: {}", image_offset);
  println!(" ---------- BMP Info Header ----------");
  println!("            size: {}", get_int32(&buf, 14));
  println!("           width: {}", width);
  println!("          height: {}", height);
  println!("    color planes: {}", get_int16(&buf, 26));
  println!("  bits per pixel: {}", bmp_bits_per_pixel);
  println!("     compression: {}", get_int32(&buf, 30));
  println!("      image size: {}", get_int32(&buf, 34));
  println!("       horiz res: {}", get_int32(&buf, 38));
  println!("        vert res: {}", get_int32(&buf, 42));
  println!("  paletta colors: {}", get_int32(&buf, 46));
  println!("important colors: {}", get_int32(&buf, 50));

  if (width % 64) != 0
  {
    println!("Width of image must be a multiple of 64\n");
    process::exit(1);
  }

  if width.leading_zeros() + width.trailing_zeros() != 31
  {
    println!("Width of image must be either 64, 128, 256, 512 pixels.\n");
    process::exit(1);
  }

  if height.leading_zeros() + height.trailing_zeros() != 31
  {
    println!("Height of image must be either 2, 4, 8, 16, 32 64, 128, 256, 512 pixels.\n");
    process::exit(1);
  }

  if bmp_bits_per_pixel != 24
  {
    println!("Only bits per pixel of 24 is supported by this app right now.");
    process::exit(1);
  }

  let mut n = 0;

  if bits_per_pixel == &16
  {
    println!("  static short[] texture =");
    println!("  {{");

    while (n as usize) < image_size 
    {
      let mut out = String::from("   ");

      for _ in 0..8
      {
        let r = (buf[image_offset + n + 0] as u32) >> 3;
        let g = (buf[image_offset + n + 1] as u32) >> 3;
        let b = (buf[image_offset + n + 2] as u32) >> 3;

        let pixel = r | (g << 5) | (b << 10);
        let mut next = format!(" 0x{:04x},", pixel as u32);

        out.push_str(&mut next);

        n = n + 3;
      }

      println!("{}", out);
    }

    println!("  }};");
  }
  else
  {
    println!("  static int[] texture =");
    println!("  {{");

    while (n as usize) < image_size 
    {
      let mut out = String::from("   ");

      for _ in 0..6
      {
        let pixel = (buf[image_offset + n + 0] as u32) |
                    ((buf[image_offset + n + 1] as u32) << 8) |
                    ((buf[image_offset + n + 2] as u32) << 16);
        let mut next = format!(" 0x{:08x},", pixel as u32);

        out.push_str(&mut next);

        n = n + 3;
      }

      println!("{}", out);
    }

    println!("  }};");
  }
}

