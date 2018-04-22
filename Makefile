INCLUDE_PATH=../naken_asm/include/playstation2
JAVA_GRINDER=../java_grinder/java_grinder
NAKEN_ASM=../naken_asm/naken_asm
CLASSES= \
  Playstation2Demo.class \
  JavaGrinderScreen.class

default: $(CLASSES)
	$(JAVA_GRINDER) -v Playstation2Demo.class playstation2_demo.asm playstation2
	$(NAKEN_ASM) -I $(INCLUDE_PATH) -l -e -o playstation2_demo.elf playstation2_demo.asm

%.class: %.java
	javac -classpath ../java_grinder/build/JavaGrinder.jar:. $*.java

debug:
	$(NAKEN_ASM) -I $(INCLUDE_PATH) -l -e -o playstation2_demo.elf playstation2_demo.asm

clean:
	@rm -f *.elf *.o *.lst *.class playstation2_demo.asm
	@cd tools && make clean
	@echo "Clean!"

