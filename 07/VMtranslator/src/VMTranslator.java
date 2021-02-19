/*
 * @Description: 
 * @Author: notplus
 * @Date: 2021-02-18 22:20:09
 * @LastEditors: notplus
 * @LastEditTime: 2021-02-19 21:25:56
 */

public class VMTranslator {
    public static void main(String[] args) throws Exception {
        String filename = args[0];
        if (filename.endsWith(".vm"))
        {
            Parser parser = new Parser(filename);
            String outputPath = filename.substring(0, filename.lastIndexOf(".")) + ".asm";
            CodeWriter codewriter = new CodeWriter(outputPath);
            
            while (parser.hasMoreCommands()) {
                parser.advance();
                switch (parser.commandType()) {
                    case C_ARITHMETIC:
                        codewriter.writeArithmetic(parser.arg1());
                        break;
                    case C_PUSH:
                    case C_POP:
                        codewriter.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
                        break;
    
                    default:
                        break;
                }
            }
            codewriter.close();
            System.out.println("Successfully translated!");
        }
        else
        {
            System.out.println("Input file error, please check vm file path!");
        }
        
    }
}
