import sys
import argparse

class Parser:
    def __init__(self, asm_path):
        self.asm_path = asm_path

    def parseCommands(self):
        f = open(self.asm_path)
        all_commands = f.readlines()
        f.close()
        no_label_commands = []
        self.label_table = {}
        self.commands = []
        self.variable_table = {}
        self.predefined_symbols = {'SP': 0, 'LCL': 1, 'ARG': 2,
                                   'THIS': 3, 'THAT': 4, 'SCREEN': 16384, 'KBD': 24576}
        for i in range(16):
            self.predefined_symbols['R'+str(i)] = i
        self.comp_table = {'0': '0101010', '1': '0111111', '-1': '0111010',
                           'D': '0001100', 'A': '0110000', 'M': '1110000',
                           '!D': '0001101', '!A': '0110001', '!M': '1110001',
                           '-D': '0001111', '-A': '0110011', '-M': '1110011',
                           'D+1': '0011111', 'A+1': '0110111', 'M+1': '1110111',
                           'D-1': '0001110', 'A-1': '0110010', 'M-1': '1110010',
                           'D+A': '0000010', 'D+M': '1000010', 'D-A': '0010011',
                           'D-M': '1010011', 'A-D': '0000111', 'M-D': '1000111',
                           'D&A': '0000000', 'D&M': '1000000', 'D|A': '0010101',
                           'D|M': '1010101'}
        self.dest_table = {'null': '000', 'M': '001', 'D': '010', 'MD': '011',
                           'A': '100', 'AM': '101', 'AD': '110', 'AMD': '111'}
        self.jump_table = {'null': '000', 'JGT': '001', 'JEQ': '010', 'JGE': '011',
                           'JLT': '100', 'JNE': '101', 'JLE': '110', 'JMP': '111'}
        i = 0
        for line in all_commands:
            line = line.split('//')[0].strip()
            if line == '':
                continue
            if '(' in line:
                self.parseLabel(line, i)
                continue
            no_label_commands += [line]
            i += 1

        for line in no_label_commands:
            if '@' in line:
                self.parseAInstruction(line)
            elif '=' or ';' in line:
                self.parseCInstruction(line)

        # print(self.commands)

    def parseLabel(self, str, index):
        label = str[str.find('(')+1:str.find(')')]
        self.label_table[label] = index

    def parseAInstruction(self, str):
        command = ''
        variable = str[str.find('@')+1:]
        if variable in self.predefined_symbols:
            command = bin(self.predefined_symbols[variable])
        else:
            if variable in self.label_table:
                command = bin(self.label_table[variable])
            elif variable.isdigit():
                command = bin(int(variable))
            else:
                if variable not in self.variable_table:
                    mem = 16+len(self.variable_table)
                    self.variable_table[variable] = mem
                command = bin(self.variable_table[variable])

        command = command[command.find('b')+1:].rjust(16, '0')
        self.commands += [command]

    def parseCInstruction(self, str):
        command = '111' + self.getAndParseComp(
            str)+self.getAndParseDest(str)+self.getAndParseJump(str)
        self.commands += [command]

    def getAndParseComp(self, str):
        str = str.split(';')[0]
        if '=' in str:
            str = str.split('=')[1]
        comp = self.comp_table[str]
        return comp

    def getAndParseDest(self, str):
        if '=' in str:
            str = str.split('=')[0]
        else:
            str = 'null'
        dest = self.dest_table[str]
        return dest

    def getAndParseJump(self, str):
        if ';' in str:
            str = str.split(';')[1]
        else:
            str = 'null'
        jump = self.jump_table[str]
        return jump

    def saveResult(self):
        save_path = self.asm_path[:self.asm_path.rfind('.')]+'.hack'
        f = open(save_path, 'w')
        for line in self.commands:
            f.write(line+'\n')
        f.close()
        print('Successfully save hack file!')


def parse_args():
    """
       Parse input arguments
       """
    parser = argparse.ArgumentParser(description='Hack language assembler')
    parser.add_argument('--asm', dest='asm_path',
                        help='Asm file path',
                        default=None, type=str)

    if len(sys.argv) == 1:
        parser.print_help()
        sys.exit(1)

    args = parser.parse_args()
    return args


if __name__ == '__main__':
    args = parse_args()

    print('Called with args:')
    print(args)

    parser = Parser(args.asm_path)
    parser.parseCommands()
    parser.saveResult()
