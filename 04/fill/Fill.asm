// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

  (LOOP)
    @pixel
    M=0
    @KBD
    D=M
    @SET_PIXEL_N1
    D;JNE // if KBD != 0 goto SET_PIXEL_N1

  (PAINT)
    @SCREEN
    D=A
    @addr
    M=D // addr = SCREEN
    @i
    M=1 // i = 1
    @j
    M=1 // j = 1

  (LOOP_ROW)
    @i
    D=M
    @256
    D=D-A
    @STOP
    D;JGT // if i > 256 goto STOP

  (LOOP_COL)
    @j
    D=M
    @32
    D=D-A
    @STOP_COL
    D;JGT // if j > 32 goto STOP_COL

    @pixel
    D=M
    @addr
    A=M 
    M=D // RAM[addr] = pixel (0 / -1)

    @addr
    M=M+1 // addr = addr + 1
    @j
    M=M+1 // j = j + 1
    @LOOP_COL
    0;JMP

  (STOP_COL)
    @i
    M=M+1 // i = i + 1
    @j
    M=1 // j = 1
    @LOOP_ROW
    0;JMP

  (STOP)
    @LOOP
    0;JMP

  (SET_PIXEL_N1)
    @pixel
    M=-1
    @PAINT
    0;JMP // goto PAINT

