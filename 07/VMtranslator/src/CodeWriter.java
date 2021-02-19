import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * @Description: 
 * @Author: notplus
 * @Date: 2021-02-18 22:35:27
 * @LastEditors: notplus
 * @LastEditTime: 2021-02-19 21:23:01
 */
public class CodeWriter {
    private BufferedWriter out;
    private String filename;
    private int labelIndex = 0;

    /**
     * @description: Opens the output file/stream and gets ready to write into it.
     * @param {String} filename
     * @return {*}
     */
    public CodeWriter(String path) {
        try {
            File writeName = new File(path);
            writeName.createNewFile();
            out = new BufferedWriter(new FileWriter(writeName));
            filename = writeName.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: Writes the assembly code that is the translation of the given
     *               arithmetic command.
     * @param {String} command
     * @return {*}
     */
    public void writeArithmetic(String command) {

        try {
            if (command.equals("neg") || command.equals("not")) {
                out.write("@SP\r\n");
                out.write("A=M-1\r\n");
                if (command.equals("neg"))
                    out.write("M=-M\r\n");
                else if (command.equals("not"))
                    out.write("M=!M\r\n");
            } else {
                out.write("@SP\r\n");
                out.write("AM=M-1\r\n");
                out.write("D=M\r\n");
                out.write("A=A-1\r\n");

                switch (command) {
                    case "add":
                        out.write("M=D+M\r\n");
                        break;

                    case "sub":
                        out.write("M=M-D\r\n");
                        break;
                    case "neg":
                        out.write("M=-D\r\n");
                        break;

                    case "eq":
                    case "gt":
                    case "lt":
                        out.write("D=M-D\r\n");
                        out.write("@SETTRUE" + labelIndex + "\r\n");
                        out.write("D;J" + command.toUpperCase() + "\r\n");
                        out.write("@SETFALSE" + labelIndex + "\r\n");
                        switch (command) {
                            case "eq":
                                out.write("D;JNE\r\n");
                                break;
                            case "gt":
                                out.write("D;JLE\r\n");
                                break;
                            case "lt":
                                out.write("D;JGE\r\n");
                                break;
                            default:
                                break;
                        }
                        out.write("(SETTRUE" + labelIndex + ")\r\n");
                        out.write("@SP\r\n");
                        out.write("A=M-1\r\n");
                        out.write("M=-1\r\n");
                        out.write("@STOP" + labelIndex + "\r\n");
                        out.write("0;JMP\r\n");
                        out.write("(SETFALSE" + labelIndex + ")\r\n");
                        out.write("@SP\r\n");
                        out.write("A=M-1\r\n");
                        out.write("M=0\r\n");
                        out.write("@STOP" + labelIndex + "\r\n");
                        out.write("0;JMP\r\n");
                        out.write("(STOP" + labelIndex + ")\r\n");
                        labelIndex++;
                        break;

                    case "and":
                        out.write("M=D&M\r\n");
                        break;

                    case "or":
                        out.write("M=D|M\r\n");
                        break;

                    case "not":
                        out.write("M=!D\r\n");
                        break;

                    default:
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: Writes the assembly code that is the translation of the given
     *               command, where command is either C_PUSH or C_POP.
     * @param {C_TYPE} command
     * @param {String} segment
     * @param {int}    index
     * @return {*}
     */
    public void writePushPop(C_TYPE command, String segment, int index) {
        try {
            switch (command) {
                case C_PUSH:
                    switch (segment) {
                        case "constant":
                            out.write("@" + index + "\r\n");
                            out.write("D=A\r\n");
                            break;
                        case "temp":
                            out.write("@" + (index + 5) + "\r\n");
                            out.write("D=M\r\n");
                            break;
                        case "static":
                            out.write("@" + filename + '.' + index + "\r\n");
                            out.write("D=M\r\n");
                            break;
                        case "pointer":
                            if (index == 0)
                                out.write("@THIS\r\n");
                            else if (index == 1)
                                out.write("@THAT\r\n");
                            out.write("D=M\r\n");
                            break;
                        default:
                            switch (segment) {
                                case "local":
                                    out.write("@LCL\r\n");
                                    break;
                                case "argument":
                                    out.write("@ARG\r\n");
                                    break;
                                case "this":
                                    out.write("@THIS\r\n");
                                    break;
                                case "that":
                                    out.write("@THAT\r\n");
                                    break;
                                default:
                                    break;
                            }
                            out.write("D=M\r\n");
                            out.write("@" + index + "\r\n");
                            out.write("A=A+D\r\n");
                            out.write("D=M\r\n");

                    }
                    out.write("@SP\r\n");
                    out.write("AM=M+1\r\n");
                    out.write("A=A-1\r\n");
                    out.write("M=D\r\n");
                    break;
                case C_POP:
                    switch (segment) {
                        case "static":
                            out.write("@SP\r\n");
                            out.write("AM=M-1\r\n");
                            out.write("D=M\r\n");
                            out.write("@" + filename + '.' + index + "\r\n");
                            out.write("M=D\r\n");
                            break;
                        case "pointer":
                            out.write("@SP\r\n");
                            out.write("AM=M-1\r\n");
                            out.write("D=M\r\n");
                            if (index == 0)
                                out.write("@THIS\r\n");
                            else if (index == 1)
                                out.write("@THAT\r\n");
                            out.write("M=D\r\n");
                            break;
                        case "temp":
                            out.write("@SP\r\n");
                            out.write("AM=M-1\r\n");
                            out.write("D=M\r\n");
                            out.write("@" + (index + 5) + "\r\n");
                            out.write("M=D\r\n");
                            break;
                        default:
                            switch (segment) {
                                case "local":
                                    out.write("@LCL\r\n");
                                    break;
                                case "argument":
                                    out.write("@ARG\r\n");
                                    break;
                                case "this":
                                    out.write("@THIS\r\n");
                                    break;
                                case "that":
                                    out.write("@THAT\r\n");
                                    break;
                                default:
                                    break;
                            }
                            out.write("D=M\r\n");
                            out.write("@" + index + "\r\n");
                            out.write("D=D+A\r\n");
                            out.write("@R13\r\n");
                            out.write("M=D\r\n");
                            out.write("@SP\r\n");
                            out.write("AM=M-1\r\n");
                            out.write("D=M\r\n");
                            out.write("@R13\r\n");
                            out.write("A=M\r\n");
                            out.write("M=D\r\n");
                            break;
                    }
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: Closes the output file.
     * @param {*}
     * @return {*}
     */
    public void close() {
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
