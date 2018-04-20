/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package assembler;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Mustafa Acıkaraoğlu
 */
public class Assembler {

    /**
     * @param args the command line arguments
     */
    private final Map<String, String> opcode;
    private final Map<String, String> typeInstruction;
    private List<String> machineCode;

    private Assembler() {
        opcode = new HashMap<>();
        typeInstruction = new HashMap<>();
        machineCode = new ArrayList<>();
        opcode.put("add", "100000");
        typeInstruction.put("add", "R");
        opcode.put("sub", "100010");
        typeInstruction.put("sub", "R");
        opcode.put("mult", "011000");
        typeInstruction.put("mult", "R");
        opcode.put("and", "100100");
        typeInstruction.put("and", "R");
        opcode.put("or", "100101");
        typeInstruction.put("or", "R");
        opcode.put("addi", "001000");
        typeInstruction.put("addi", "I");
        opcode.put("sll", "000000");
        typeInstruction.put("sll", "R");
        opcode.put("slt", "101010");
        typeInstruction.put("slt", "R");
        opcode.put("mfhi", "010000");
        typeInstruction.put("mfhi", "R");
        opcode.put("mflo", "010010");
        typeInstruction.put("mflo", "R");
        opcode.put("lw", "100011");
        typeInstruction.put("lw", "I");
        opcode.put("sw", "101011");
        typeInstruction.put("sw", "I");
        opcode.put("beq", "000100");
        typeInstruction.put("beq", "I");
        opcode.put("blez", "000110");
        typeInstruction.put("blez", "I");
        opcode.put("j", "000010");
        typeInstruction.put("j", "J");
        opcode.put("addhi", "111111");
        typeInstruction.put("addhi", "R");
    }

    private void parseToMachineCode() {
        List<String> instruction = new ArrayList<>();
        String path = "test_program_asm.txt";
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            instruction = stream.collect(Collectors.toList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        for (String instr : instruction) {
            if(instr.contains("#")){
              if(instr.charAt(0) == '#'){
                continue;
              }
              instr = instr.substring(0,instr.indexOf("#"));
            }
            if(instr.isEmpty() || instr == null){
              continue;
            }
            String binaryCode = "";
            //System.out.println(instr.split(" ")[0]);
            String opCode = instr.split(" ")[0];
            String rest = instr.split(" ")[1];
            if ("R".equals(typeInstruction.get(opCode))) {
                if ("mult".equals(opCode)) {
                    String[] rsrt = rest.split(",");
                    String op = opcode.get(opCode);
                    String rs = signExtend(Integer.toBinaryString(Integer.parseInt(rsrt[0].substring(1))), 5 - (Integer.toBinaryString(Integer.parseInt(rsrt[0].substring(1)))).length());
                    String rt = signExtend(Integer.toBinaryString(Integer.parseInt(rsrt[1].substring(1))), 5 - (Integer.toBinaryString(Integer.parseInt(rsrt[1].substring(1)))).length());
                    String rd = "00000";
                    String shamt = "00000";
                    String func = "000000";
                    binaryCode += op += rs += rt += rd += shamt += func;
                    long decimal = Long.parseLong(binaryCode, 2);
                    String hexCode = Long.toString(decimal, 16);
                    machineCode.add(hexCode);
                } else if ("mflo".equals(opCode) || "mfhi".equals(opCode)) {
                    String op = opcode.get(opCode);
                    String rs = "00000";
                    String rt = "00000";
                    String rd = signExtend(Integer.toBinaryString(Integer.parseInt(rest.substring(1))), 5 - (Integer.toBinaryString(Integer.parseInt(rest.substring(1)))).length());
                    String shamt = "00000";
                    String func = "000000";
                    binaryCode += op += rs += rt += rd += shamt += func;
                    long decimal = Long.parseLong(binaryCode, 2);
                    String hexCode = Long.toString(decimal, 16);
                    machineCode.add(hexCode);
                } else {
                    String[] rsrtrd = rest.split(",");
                    String op = opcode.get(opCode);
                    String rs = signExtend(Integer.toBinaryString(Integer.parseInt(rsrtrd[1].substring(1))), 5 - (Integer.toBinaryString(Integer.parseInt(rsrtrd[1].substring(1)))).length());
                    String rt = signExtend(Integer.toBinaryString(Integer.parseInt(rsrtrd[2].substring(1))), 5 - (Integer.toBinaryString(Integer.parseInt(rsrtrd[2].substring(1)))).length());
                    String rd = signExtend(Integer.toBinaryString(Integer.parseInt(rsrtrd[0].substring(1))), 5 - (Integer.toBinaryString(Integer.parseInt(rsrtrd[0].substring(1)))).length());
                    String shamt = "00000";
                    String func = "000000";
                    binaryCode += op += rs += rt += rd += shamt += func;
                    long decimal = Long.parseLong(binaryCode, 2);
                    String hexCode = Long.toString(decimal, 16);
                    machineCode.add(hexCode);
                }
            } else if ("I".equals(typeInstruction.get(opCode))) {
                String[] rsrtI = rest.split(",");
                String op = opcode.get(opCode);
                String rt = signExtend(Integer.toBinaryString(Integer.parseInt(rsrtI[1].substring(1))), 5 - Integer.toBinaryString(Integer.parseInt(rsrtI[1].substring(1))).length());
                String rd = signExtend(Integer.toBinaryString(Integer.parseInt(rsrtI[0].substring(1))), 5 - Integer.toBinaryString(Integer.parseInt(rsrtI[0].substring(1))).length());
                String I = signExtend(Integer.toBinaryString(Integer.parseInt(rsrtI[2])), 16 - Integer.toBinaryString(Integer.parseInt(rsrtI[2])).length());
                binaryCode += op += rt += rd += I;
                long decimal = Long.parseLong(binaryCode, 2);
                String hexCode = Long.toString(decimal, 16);
                machineCode.add(hexCode);
            } else if ("J".equals(typeInstruction.get(opCode))) {
                String op = opcode.get(opCode);
                String J = signExtend(Integer.toBinaryString(Integer.parseInt(rest)), 26 - Integer.toBinaryString(Integer.parseInt(rest)).length());
                binaryCode += op += J;
                long decimal = Long.parseLong(binaryCode, 2);
                String hexCode = Long.toString(decimal, 16);
                hexCode = "0" + hexCode;
                machineCode.add(hexCode);
            }
        }
    }

    private String signExtend(String binary, int howMuch) {
        for (int i = 0; i < howMuch; i++) {
            binary = "0" + binary;
        }
        return binary;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        Assembler asm = new Assembler();
        asm.parseToMachineCode();
        try {
            FileWriter f = new FileWriter("test_program_code.txt");
            f.write("v2.0 raw" + System.lineSeparator());
            for (String code : asm.machineCode) {
                f.write(code + " ");
            }
            f.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

}
