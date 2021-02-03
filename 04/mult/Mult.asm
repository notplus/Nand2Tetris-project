// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Put your code here.

    @R1
    D=M
    @n
    M=D // n = R1
    @i
    M=1 // i = 1
    @sum
    M=0 // sum = 0

    @R0
    D=M
    @STOP
    D;JEQ // if R0 = 0 goto STOP
  
  (LOOP)
    @i
    D=M
    @n
    D=D-M
    @STOP
    D;JGT // if i > n goto STOP

    @R0
    D=M
    @sum
    M=M+D // sum = sum + R0
    @i
    M=M+1 // i = i + 1
    @LOOP
    0;JMP

  (STOP)
    @sum
    D=M
    @R2
    M=D