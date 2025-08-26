package yace.yace;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Block class.
 */
public class BlockTest {
    
    @Test
    void testBlockCreation() {
        Block block = new Block(Block.BlockType.FUNCTION, "public void test()", 1, 5);
        
        assertEquals(Block.BlockType.FUNCTION, block.getType());
        assertEquals("public void test()", block.getContent());
        assertEquals(1, block.getStartLine());
        assertEquals(5, block.getEndLine());
        assertTrue(block.getChildren().isEmpty());
    }
    
    @Test
    void testAddChild() {
        Block parent = new Block(Block.BlockType.CLASS, "public class Test", 1, 10);
        Block child = new Block(Block.BlockType.FUNCTION, "public void method()", 2, 8);
        
        parent.addChild(child);
        
        assertEquals(1, parent.getChildren().size());
        assertEquals(child, parent.getChildren().get(0));
    }
    
    @Test
    void testMultipleChildren() {
        Block parent = new Block(Block.BlockType.CLASS, "public class Test", 1, 20);
        Block child1 = new Block(Block.BlockType.FUNCTION, "public void method1()", 2, 8);
        Block child2 = new Block(Block.BlockType.FUNCTION, "public void method2()", 10, 18);
        
        parent.addChild(child1);
        parent.addChild(child2);
        
        assertEquals(2, parent.getChildren().size());
        assertEquals(child1, parent.getChildren().get(0));
        assertEquals(child2, parent.getChildren().get(1));
    }
    
    @Test
    void testToString() {
        Block block = new Block(Block.BlockType.IF, "if (condition)", 5, 10);
        block.addChild(new Block(Block.BlockType.SCOPE, "{", 6, 9));
        
        String result = block.toString();
        
        assertTrue(result.contains("IF"));
        assertTrue(result.contains("lines=5-10"));
        assertTrue(result.contains("children=1"));
    }
    
    @Test
    void testBlockTypes() {
        // Test all block types are available
        assertNotNull(Block.BlockType.FUNCTION);
        assertNotNull(Block.BlockType.CLASS);
        assertNotNull(Block.BlockType.IF);
        assertNotNull(Block.BlockType.ELSE);
        assertNotNull(Block.BlockType.LOOP);
        assertNotNull(Block.BlockType.TRY);
        assertNotNull(Block.BlockType.CATCH);
        assertNotNull(Block.BlockType.FINALLY);
        assertNotNull(Block.BlockType.SCOPE);
    }
}