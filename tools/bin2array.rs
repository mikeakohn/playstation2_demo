use std::env;
use std::fs::File;
use std::io::prelude::*;

fn main()
{
  let args: Vec<String> = env::args().collect();
  let filename = &args[1];

  let mut file = File::open(filename).expect("File not found");

  let mut buf = [0u8; 65536];
  let bytes_read = file.read(&mut buf).unwrap();

  println!("  static byte[] array =");
  println!("  {{");

  let mut i = 0;

  while i < bytes_read
  {
    let mut out = String::from("    ");
    let mut count = bytes_read - i;

    if count > 8 { count = 8; }

    for n in 0..count
    {
      let mut next = format!(" {:4.0},", buf[i + n] as i8);
      out.push_str(&mut next);
    }

    println!("{}", out);

    i += 8
  }

  println!("  }};")
}

