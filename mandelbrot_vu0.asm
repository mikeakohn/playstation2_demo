.ps2_ee_vu0

  ; Calculate 64 pixels by 8 lines.

  ; struct _mandel_data
  ; {
  ;   float r_step, r_step, r_step, r_step;
  ;   float i_step, i_step, i_step, i_step;
  ;   float r0, r1, r2, r3;
  ;   float i0, i1, i2, i3;
  ; };

start:
  ; vf05 = [ r_step, r_step, r_step, r_step ]
  ; vf06 = [ i_step, i_step, i_step, i_step ]
  ; vf07 = [ r0, r1, r2, r3 ]
  ; vf08 = [ i0, i1, i2, i3 ]
  ; vf16 = [ -32768.0, -32768.0, -32768.0, -32768.0 ]
  ; vf04 = [ 4.0, 4.0, 4.0, 4.0 ]
  ; vf02 = [ 2.0, 2.0, 2.0, 2.0 ]
  ; vf15 = [ 1.0, 1.0, 1.0, 1.0 ]
  ; vf01 = [ 0.0, 0.0, 0.0, 0.0 ]
  sub.xyzw vf01, vf01, vf01   lq.xyzw vf05, 0(vi00)
  addw.xyzw vf15, vf01, vf00w lq.xyzw vf06, 1(vi00)
  add.xyzw vf02, vf15, vf15   lq.xyzw vf07, 2(vi00)
  add.xyzw vf04, vf02, vf02   lq.xyzw vf08, 3(vi00)
  nop                         iaddi vi01, vi00, -1
  nop                         mfir.xyzw vf16, vi01
  itof0 vf16, vf16            nop

  ; Copy vf05's x value to yzw
  ; Copy vf06's x value to yzw
  addx.yzw vf05, vf01, vf05    nop
  addx.yzw vf06, vf01, vf06    nop

  ; y = 8; as vi03
  ; while (y > 0)
  ; vi03 = 8
  nop                         iaddiu vi03, vi00, 8
for_y:
  ;; An optimization could be to move this lower an combine with upper instr.
  nop                         iaddi vi03, vi03, -1

  ; x = 64; as vi02
  ; while (x > 0) .. going to decrement vi02 by 4
  ; vi02 = 64
  nop                         iaddiu vi02, vi00, 64
for_x:
  ;; An optimization could be to move this lower an combine with upper instr.
  nop                         iaddi vi02, vi02, -4

  ; vf03 = [ 1.0, 1.0, 1.0, 1.0 ] = count_dec
  ; vf11 = [ 127.0, 127.0, 127.0, 127.0 ] = count
  addw.xyzw vf03, vf01, vf00w iaddiu vi04, vi00, 127
  nop                         mfir.xyzw vf11, vi04
  itof0.xyzw vf11, vf11       nop

  ; vf09 = zr = [ 0.0, 0.0, 0.0, 0.0 ]
  ; vf10 = zi = [ 0.0, 0.0, 0.0, 0.0 ]
  sub.xyzw vf09, vf09, vf09   nop
  sub.xyzw vf10, vf10, vf10   nop

next_iteration:
  ; z = z^2 + c
  ; z^2 = (x + yi) * (x + yi)
  ;     = x^2 + 2xyi - y^2
  ;     = (x^2 - y^2) + 2xyi

  ; vf12 = ti = (2 * zr * zi);
  mul.xyzw vf12, vf09, vf10   nop
  mul.xyzw vf12, vf12, vf02   nop

  ; vf13 = tr = ((zr * zr) - (zi * zi));
  mul.xyzw vf13, vf09, vf09   nop
  mul.xyzw vf14, vf10, vf10   nop
  sub.xyzw vf13, vf09, vf10   nop

  ; vf09 = zr = tr + r;
  ; vf10 = zi = ti + i;
  add.xyzw vf09, vf13, vf07   nop
  add.xyzw vf10, vf12, vf08   nop

  ; if ((zr * zr) + (zi * zi) > 4) break;
  mul.xyzw vf13, vf09, vf09   nop
  mul.xyzw vf14, vf10, vf10   nop
  add.xyzw vf13, vf13, vf14   nop

  ; Estimates if a float is less than 4 (output is 1) or more than 4
  ; output is 0.
  ; def less_than_4(num):
  ;   print num
  ;   a = min(4.0, num)
  ;   a = a - 4.0
  ;   a = a * -32768.0
  ;   a = min(a, 1)
  ;   print "result=" + str(int(a))

  ; if [ l0, l1, l2, l3 ] > 4, inc_count = 0
  mini.xyzw vf13, vf13, vf04  nop
  sub.xyzw vf13, vf13, vf04   nop
  mul.xyzw vf13, vf13, vf16   nop
  mini.xyzw vf13, vf13, vf15  nop

  ; vf03 = [ 1.0/0.0 1.0/0.0 1.0/0.0 1.0/0.0 ]
  mul.xyzw vf03, vf03, vf13   nop

  ; count = count - 1
  sub.xyzw vf11, vf11, vf03   nop

  ; if vf03 == [ 0, 0, 0, 0 ]; break
  ; VU0 doesn't have esum... awesome :(
  ftoi0 vf14, vf14            nop
  nop                         mtir vi05, vf14x
  nop                         mtir vi06, vf14y
  nop                         iadd vi05, vi05, vi06
  nop                         mtir vi06, vf14z
  nop                         iadd vi05, vi05, vi06
  nop                         mtir vi06, vf14w
  nop                         iadd vi05, vi05, vi06

  ; count = count - 1
  nop                         isubiu vi04, vi04, 1
  nop                         ibeq vi05, vi00, break_iteration
  nop                         ibne vi04, vi00, next_iteration

break_iteration:
  ; [ r0, r1, r2, r3 ] += rstep4
  add.xyzw vf20, vf20, vf11   nop

  nop                         ibne vi02, vi00, for_x

  ; [ i0, i1, i2, i3 ] += istep
  add.xyzw vf01, vf01, vf12   nop

  nop                         ibne vi03, vi00, for_y

  nop                         nop
  nop[E]                      nop
  nop                         nop

