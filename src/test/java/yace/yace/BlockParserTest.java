package yace.yace;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit tests for the BlockParser class.
 */
public class BlockParserTest {
    
    private BlockParser parser;
    
    @BeforeEach
    void setUp() {
        parser = new BlockParser();
    }
    
    @Test
    void testParseEmptyCode() {
        List<Block> blocks = parser.parseBlocks("");
        assertTrue(blocks.isEmpty(), "Empty code should return no blocks");
        
        blocks = parser.parseBlocks(null);
        assertTrue(blocks.isEmpty(), "Null code should return no blocks");
    }
    
    @Test
    void testParseSimpleFunction() {
        String code = "public void testMethod() {\n" +
                     "    System.out.println(\"Hello\");\n" +
                     "}";
        
        List<Block> blocks = parser.parseBlocks(code);
        assertEquals(1, blocks.size(), "Should find one function block");
        
        Block block = blocks.get(0);
        assertEquals(Block.BlockType.FUNCTION, block.getType());
        assertEquals(1, block.getStartLine());
        assertEquals(3, block.getEndLine());
        assertTrue(block.getContent().contains("testMethod"));
    }
    
    @Test
    void testParseClass() {
        String code = "public class TestClass {\n" +
                     "    private int value;\n" +
                     "}";
        
        List<Block> blocks = parser.parseBlocks(code);
        assertEquals(1, blocks.size(), "Should find one class block");
        
        Block block = blocks.get(0);
        assertEquals(Block.BlockType.CLASS, block.getType());
        assertEquals(1, block.getStartLine());
        assertEquals(3, block.getEndLine());
        assertTrue(block.getContent().contains("TestClass"));
    }
    
    @Test
    void testParseIfStatement() {
        String code = "if (condition) {\n" +
                     "    doSomething();\n" +
                     "}";
        
        List<Block> blocks = parser.parseBlocks(code);
        assertEquals(1, blocks.size(), "Should find one if block");
        
        Block block = blocks.get(0);
        assertEquals(Block.BlockType.IF, block.getType());
        assertEquals(1, block.getStartLine());
        assertEquals(3, block.getEndLine());
    }
    
    @Test
    void testParseLoop() {
        String code = "for (int i = 0; i < 10; i++) {\n" +
                     "    process(i);\n" +
                     "}";
        
        List<Block> blocks = parser.parseBlocks(code);
        assertEquals(1, blocks.size(), "Should find one loop block");
        
        Block block = blocks.get(0);
        assertEquals(Block.BlockType.LOOP, block.getType());
        assertEquals(1, block.getStartLine());
        assertEquals(3, block.getEndLine());
    }
    
    @Test
    void testParseTryCatch() {
        String code = "try {\n" +
                     "    riskyOperation();\n" +
                     "} catch (Exception e) {\n" +
                     "    handleError(e);\n" +
                     "}";
        
        List<Block> blocks = parser.parseBlocks(code);
        assertEquals(2, blocks.size(), "Should find try and catch blocks");
        
        Block tryBlock = blocks.get(0);
        assertEquals(Block.BlockType.TRY, tryBlock.getType());
        assertEquals(1, tryBlock.getStartLine());
        assertEquals(3, tryBlock.getEndLine());
        
        Block catchBlock = blocks.get(1);
        assertEquals(Block.BlockType.CATCH, catchBlock.getType());
        assertEquals(3, catchBlock.getStartLine());
        assertEquals(5, catchBlock.getEndLine());
    }
    
    @Test
    void testParseNestedBlocks() {
        String code = "public class TestClass {\n" +
                     "    public void method() {\n" +
                     "        if (condition) {\n" +
                     "            doSomething();\n" +
                     "        }\n" +
                     "    }\n" +
                     "}";
        
        List<Block> blocks = parser.parseBlocks(code);
        assertEquals(1, blocks.size(), "Should find one top-level class block");
        
        Block classBlock = blocks.get(0);
        assertEquals(Block.BlockType.CLASS, classBlock.getType());
        assertEquals(1, classBlock.getChildren().size(), "Class should have one child method");
        
        Block methodBlock = classBlock.getChildren().get(0);
        assertEquals(Block.BlockType.FUNCTION, methodBlock.getType());
        assertEquals(1, methodBlock.getChildren().size(), "Method should have one child if block");
        
        Block ifBlock = methodBlock.getChildren().get(0);
        assertEquals(Block.BlockType.IF, ifBlock.getType());
    }
    
    @Test
    void testFindBlocksByType() {
        String code = "public class TestClass {\n" +
                     "    public void method1() {\n" +
                     "        if (condition) {\n" +
                     "            doSomething();\n" +
                     "        }\n" +
                     "    }\n" +
                     "    public void method2() {\n" +
                     "        for (int i = 0; i < 10; i++) {\n" +
                     "            process(i);\n" +
                     "        }\n" +
                     "    }\n" +
                     "}";
        
        List<Block> functions = parser.findBlocksByType(code, Block.BlockType.FUNCTION);
        assertEquals(2, functions.size(), "Should find two function blocks");
        
        List<Block> loops = parser.findBlocksByType(code, Block.BlockType.LOOP);
        assertEquals(1, loops.size(), "Should find one loop block");
        
        List<Block> classes = parser.findBlocksByType(code, Block.BlockType.CLASS);
        assertEquals(1, classes.size(), "Should find one class block");
    }
    
    @Test
    void testParseWithComments() {
        String code = "// This is a comment\n" +
                     "public void method() {\n" +
                     "    // Another comment\n" +
                     "    doSomething();\n" +
                     "}";
        
        List<Block> blocks = parser.parseBlocks(code);
        assertEquals(1, blocks.size(), "Should find one function block, ignoring comments");
        
        Block block = blocks.get(0);
        assertEquals(Block.BlockType.FUNCTION, block.getType());
        assertEquals(2, block.getStartLine()); // Comment line is skipped
    }
}