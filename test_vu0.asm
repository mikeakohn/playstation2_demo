.ps2_ee_vu0

  nop                         iaddiu vi02, vi00, 50 ; red
  nop                         iaddiu vi01, vi00, 128 ; count
  nop                         mfir.xyzw vf02, vi02
  nop                         iadd vi03, vi00, vi00

loop:
  nop                         isubiu vi01, vi01, 1
  nop                         sqi.xyzw vf02, (vi03++)
  nop                         ibne vi01, vi00, loop
  nop                         nop

  ;; nop[E] will stop the execution of VU0
  nop[E]                      nop
  nop                         nop

