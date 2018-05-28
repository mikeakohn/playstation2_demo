INCLUDE_PATH=../naken_asm/include/playstation2
JAVA_GRINDER=../java_grinder/java_grinder
NAKEN_ASM=../naken_asm/naken_asm
CLASSES= \
  BillionDevices.class \
  ImageBillionDevices.class \
  ImageLogoJava.class \
  ImageLogoJavaGrinder.class \
  ImageLogoNakenAsm.class \
  ImageLogoPS2.class \
  Logos.class \
  Mandelbrots.class \
  MandelbrotsVU0.class \
  Playstation2Demo.class

default: $(CLASSES)
	$(JAVA_GRINDER) -v Playstation2Demo.class playstation2_demo.asm playstation2
	$(NAKEN_ASM) -I $(INCLUDE_PATH) -l -e -o playstation2_demo.elf playstation2_demo.asm

%.class: %.java
	javac -classpath ../java_grinder/build/JavaGrinder.jar:. $*.java

debug:
	$(NAKEN_ASM) -I $(INCLUDE_PATH) -l -e -dump_symbols -o playstation2_demo.elf playstation2_demo.asm

mandel:
	$(NAKEN_ASM) -I $(INCLUDE_PATH) -l -b -dump_symbols -o mandelbrot_vu0.bin mandelbrot_vu0.asm
	@echo "public class MandelbrotsVU0" > MandelbrotsVU0.java
	@echo "{" >> MandelbrotsVU0.java
	@tools/bin2array mandelbrot_vu0.bin  | sed s/array/code/ >> MandelbrotsVU0.java
	@echo "}\n" >> MandelbrotsVU0.java

test:
	$(NAKEN_ASM) -I $(INCLUDE_PATH) -l -b -dump_symbols -o test_vu0.bin test_vu0.asm
	@echo "public class MandelbrotsVU0" > MandelbrotsVU0.java
	@echo "{" >> MandelbrotsVU0.java
	@tools/bin2array test_vu0.bin  | sed s/array/code/ >> MandelbrotsVU0.java
	@echo "}\n" >> MandelbrotsVU0.java

clean:
	@rm -f *.elf *.bin *.o *.lst *.class playstation2_demo.asm
	@cd tools && make clean
	@echo "Clean!"

