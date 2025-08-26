package yace.yace;

/**
 * Demo class to test block parsing functionality manually.
 */
public class BlockParsingDemo {
    
    public static void main(String[] args) {
        BlockParser parser = new BlockParser();
        
        String sampleCode = 
            "public class Example {\n" +
            "    private int value;\n" +
            "    \n" +
            "    public void processData() {\n" +
            "        try {\n" +
            "            validateInput();\n" +
            "            if (value > 0) {\n" +
            "                doPositiveWork();\n" +
            "            } else {\n" +
            "                doNegativeWork();\n" +
            "            }\n" +
            "        } catch (Exception e) {\n" +
            "            handleError(e);\n" +
            "        }\n" +
            "    }\n" +
            "    \n" +
            "    public void anotherMethod() {\n" +
            "        for (int i = 0; i < 10; i++) {\n" +
            "            process(i);\n" +
            "        }\n" +
            "    }\n" +
            "}";
        
        System.out.println("=== Block Parsing Demo ===");
        System.out.println("Source code:");
        System.out.println(sampleCode);
        System.out.println("\n=== Parsed Blocks ===");
        
        var blocks = parser.parseBlocks(sampleCode);
        printBlocks(blocks, 0);
        
        System.out.println("\n=== Functions Only ===");
        var functions = parser.findBlocksByType(sampleCode, Block.BlockType.FUNCTION);
        for (Block function : functions) {
            System.out.println("Function at lines " + function.getStartLine() + "-" + function.getEndLine() + 
                             ": " + function.getContent().trim());
        }
    }
    
    private static void printBlocks(java.util.List<Block> blocks, int indent) {
        String indentStr = "  ".repeat(indent);
        for (Block block : blocks) {
            System.out.println(indentStr + block);
            if (!block.getChildren().isEmpty()) {
                printBlocks(block.getChildren(), indent + 1);
            }
        }
    }
}