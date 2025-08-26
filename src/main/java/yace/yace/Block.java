package yace.yace;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a code block with type, content, and location information.
 */
public class Block {
    public enum BlockType {
        FUNCTION,
        CLASS,
        IF,
        ELSE,
        LOOP,
        TRY,
        CATCH,
        FINALLY,
        SCOPE
    }
    
    private final BlockType type;
    private final String content;
    private final int startLine;
    private final int endLine;
    private final List<Block> children;
    
    public Block(BlockType type, String content, int startLine, int endLine) {
        this.type = type;
        this.content = content;
        this.startLine = startLine;
        this.endLine = endLine;
        this.children = new ArrayList<>();
    }
    
    public BlockType getType() {
        return type;
    }
    
    public String getContent() {
        return content;
    }
    
    public int getStartLine() {
        return startLine;
    }
    
    public int getEndLine() {
        return endLine;
    }
    
    public List<Block> getChildren() {
        return children;
    }
    
    public void addChild(Block child) {
        children.add(child);
    }
    
    @Override
    public String toString() {
        return String.format("Block{type=%s, lines=%d-%d, children=%d}", 
                            type, startLine, endLine, children.size());
    }
}