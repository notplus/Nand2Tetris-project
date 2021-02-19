/*
 * @Description: 
 * @Author: notplus
 * @Date: 2021-02-18 22:35:02
 * @LastEditors: notplus
 * @LastEditTime: 2021-02-19 21:25:05
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    private ArrayList<String> commands;
    private int index = 0;
    private String arg1;
    private int arg2;
    private C_TYPE commandType;

    /**
     * @description: Opens the input file/stream and gets ready to parse it.
     * @param {String} filename
     * @return {*}
     */
    public Parser(String filename) {
        commands = new ArrayList<>();
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader bf = new BufferedReader(fr);
            String str;

            while ((str = bf.readLine()) != null) {
                String cmd = str.split("//")[0].strip();
                if (cmd.isEmpty())
                    continue;
                commands.add(cmd.toLowerCase());
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @description: Are there more commands in the input?
     * @param {*}
     * @return {Boolean}
     */
    public Boolean hasMoreCommands() {
        if (index < commands.size())
            return true;
        else
            return false;
    }

    /**
     * @description: Reads the next command from the input and makes it the current
     *               command. Should be called only if hasMoreCommands() is true.
     * @param {*}
     * @return {*}
     */
    public void advance() {
        String cmdType = commands.get(index).split(" ")[0];

        switch (cmdType) {
            case "add":
            case "sub":
            case "neg":
            case "eq":
            case "gt":
            case "lt":
            case "and":
            case "or":
            case "not":
                commandType = C_TYPE.C_ARITHMETIC;
                arg1 = cmdType;
                break;

            case "push":
            case "pop":
                if (cmdType.equals("push"))
                    commandType = C_TYPE.C_PUSH;
                else
                    commandType = C_TYPE.C_POP;
                arg1 = commands.get(index).split(" ")[1];
                arg2 = Integer.parseInt(commands.get(index).split(" ")[2]);
                break;

            default:
                break;
        }

        index++;
    }

    /**
     * @description: Returns the type of the current VM command. C_ARITHMETIC is
     *               returned for all the arithmetic commands.
     * @param {*}
     * @return {C_TYPE}
     */
    public C_TYPE commandType() {
        return commandType;
    }

    /**
     * @description: Returns the first argument of the current command. In the case
     *               of C_ARITHMETIC, the command itself (add, sub, etc.) is
     *               returned. Should not be called if the current command is
     *               C_RETURN.
     * @param {*}
     * @return {String}
     */
    public String arg1() {
        return arg1;
    }

    /**
     * @description: Returns the second argument of the current command. Should be
     *               called only if the current command is C_PUSH, C_POP,
     *               C_FUNCTION, or C_CALL.
     * @param {*}
     * @return {*}
     */
    public int arg2() {
        return arg2;
    }

}

enum C_TYPE {
    C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF, C_FUNCTION, C_RETURN, C_CALL
}