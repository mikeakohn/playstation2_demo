#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

struct _triangle
{
  float normal[3];
  float vertex_1[0];
  float vertex_2[0];
  float vertex_3[0];
  uint16_t attributes;
};

static uint32_t read_uint32(FILE *fp)
{
  uint32_t n;

  n = getc(fp) |
     (getc(fp) << 8) |
     (getc(fp) << 16) |
     (getc(fp) << 24);

  return n;
}

static uint16_t read_uint16(FILE *fp)
{
  uint16_t n;

  n = getc(fp) | (getc(fp) << 8);

  return n;
}

static float read_real32(FILE *fp)
{
  int n;
  float *f;

  n = getc(fp) |
     (getc(fp) << 8) |
     (getc(fp) << 16) |
     (getc(fp) << 24);

  f = (float *)&n;

  return *f;
}

static int read_header(FILE *fp)
{
  char buffer[80];
  int n;

  if (fread(buffer, 1, 80, fp) != 80) { return -1; }

  for (n = 0; n < 80; n++)
  {
    if (buffer[n] >= ' ' && buffer[n] <= 'z')
    {
      printf("%c", buffer[n]);
    }
  }

  printf("\n");

  return 0;
}

static int read_vector(FILE *fp, float *vector, float scale)
{
  int i;

  for (i = 0; i < 3; i++)
  {
    vector[i] = read_real32(fp) * scale;
  }

  return 0;
}

static int read_triangle(FILE *fp, struct _triangle *triangle, float scale)
{
  if (read_vector(fp, triangle->normal, scale) != 0) { return -1; }
  if (read_vector(fp, triangle->vertex_1, scale) != 0) { return -1; }
  if (read_vector(fp, triangle->vertex_2, scale) != 0) { return -1; }
  if (read_vector(fp, triangle->vertex_3, scale) != 0) { return -1; }

  triangle->attributes = read_uint16(fp);

  return 0;
}

static int print_vector(float *vector)
{
   printf("    { %0.3f, %0.3f, %0.3f },\n",
     vector[0], vector[1], vector[2]);

  return 0;
}

static int print_triangles(struct _triangle *triangle)
{
  printf("  normal: ");
  print_vector(triangle->normal);
  printf("  vertex_1: ");
  print_vector(triangle->vertex_1);
  printf("  vertex_2: ");
  print_vector(triangle->vertex_2);
  printf("  vertex_3: ");
  print_vector(triangle->vertex_3);
  printf("  attributes: %d\n", triangle->attributes);

  return 0;
}

static int print_triangles_as_code(struct _triangle *triangle)
{
  print_vector(triangle->vertex_1);
  print_vector(triangle->vertex_2);
  print_vector(triangle->vertex_3);

  return 0;
}

int main(int argc, char *argv[])
{
  FILE *fp;
  uint32_t triangles;
  uint32_t n;
  struct _triangle triangle;
  float scale = 1.0f;

  if (argc != 2 && argc != 3)
  {
    printf("Usage: %s <filename> <scale as real32>\n", argv[0]);
    exit(0);
  }

  fp = fopen(argv[1], "rb");

  if (fp == NULL)
  {
    printf("File not found %s\n", argv[1]);
    exit(1);
  }

  if (argc == 3)
  {
    scale = atof(argv[2]);
  }

  do
  {
    if (read_header(fp) != 0) { break; }

    triangles = read_uint32(fp);

    printf("Triangles: %d\n", triangles);

    for (n = 0; n < triangles; n++)
    {
      printf("-- Triangle %d --\n", n);
      if (read_triangle(fp, &triangle, scale) != 0) { break; }

      print_triangles(&triangle);
    }

    printf("  static float[] object_points =\n");
    printf("  {\n");

    for (n = 0; n < triangles; n++)
    {
      print_triangles_as_code(&triangle);
    }

    printf("  };\n");

  } while(0);

  fclose(fp);

  return 0;
}

