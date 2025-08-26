package yace.yace;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for identifying and parsing code blocks from source code.
 */
public class BlockParser {
    
    private static final Pattern FUNCTION_PATTERN = Pattern.compile(
        "\\b(?:public|private|protected|static|final|abstract|synchronized)?\\s*\\w+\\s+\\w+\\s*\\([^)]*\\)\\s*\\{");
    
    private static final Pattern CLASS_PATTERN = Pattern.compile(
        "\\b(?:public|private|protected|abstract|final)?\\s*class\\s+\\w+\\s*(?:extends\\s+\\w+)?\\s*(?:implements\\s+[\\w,\\s]+)?\\s*\\{");
    
    private static final Pattern IF_PATTERN = Pattern.compile("\\bif\\s*\\([^)]+\\)\\s*\\{");
    private static final Pattern ELSE_PATTERN = Pattern.compile("\\belse\\s*\\{");
    private static final Pattern LOOP_PATTERN = Pattern.compile("\\b(?:for|while)\\s*\\([^)]*\\)\\s*\\{");
    private static final Pattern TRY_PATTERN = Pattern.compile("\\btry\\s*\\{");
    private static final Pattern CATCH_PATTERN = Pattern.compile("\\bcatch\\s*\\([^)]+\\)\\s*\\{");
    private static final Pattern FINALLY_PATTERN = Pattern.compile("\\bfinally\\s*\\{");
    
    /**
     * Parses the given source code and returns a list of identified blocks.
     * 
     * @param sourceCode The source code to parse
     * @return List of identified blocks
     */
    public List<Block> parseBlocks(String sourceCode) {
        if (sourceCode == null || sourceCode.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String[] lines = sourceCode.split("\\n");
        List<Block> blocks = new ArrayList<>();
        Stack<Block> blockStack = new Stack<>();
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            
            // Skip empty lines and comments
            if (line.isEmpty() || line.startsWith("//") || line.startsWith("/*")) {
                continue;
            }
            
            // Check for block end (closing brace) first
            if (line.contains("}") && !blockStack.isEmpty()) {
                Block completedBlock = blockStack.pop();
                Block updatedBlock = new Block(completedBlock.getType(), 
                                             completedBlock.getContent(), 
                                             completedBlock.getStartLine(), 
                                             i + 1);
                
                // Copy children from the original block
                for (Block child : completedBlock.getChildren()) {
                    updatedBlock.addChild(child);
                }
                
                if (blockStack.isEmpty()) {
                    blocks.add(updatedBlock);
                } else {
                    blockStack.peek().addChild(updatedBlock);
                }
            }
            
            // Check for block start patterns (after handling closing brace)
            Block.BlockType blockType = getBlockType(line);
            if (blockType != null) {
                Block block = new Block(blockType, line, i + 1, -1);
                blockStack.push(block);
            }
        }
        
        return blocks;
    }
    
    /**
     * Determines the block type based on the line content.
     * 
     * @param line The line to analyze
     * @return The block type or null if no block pattern is found
     */
    private Block.BlockType getBlockType(String line) {
        if (CLASS_PATTERN.matcher(line).find()) {
            return Block.BlockType.CLASS;
        }
        if (FUNCTION_PATTERN.matcher(line).find()) {
            return Block.BlockType.FUNCTION;
        }
        if (IF_PATTERN.matcher(line).find()) {
            return Block.BlockType.IF;
        }
        if (ELSE_PATTERN.matcher(line).find()) {
            return Block.BlockType.ELSE;
        }
        if (LOOP_PATTERN.matcher(line).find()) {
            return Block.BlockType.LOOP;
        }
        if (TRY_PATTERN.matcher(line).find()) {
            return Block.BlockType.TRY;
        }
        if (CATCH_PATTERN.matcher(line).find()) {
            return Block.BlockType.CATCH;
        }
        if (FINALLY_PATTERN.matcher(line).find()) {
            return Block.BlockType.FINALLY;
        }
        if (line.contains("{")) {
            return Block.BlockType.SCOPE;
        }
        
        return null;
    }
    
    /**
     * Finds all blocks of a specific type in the given source code.
     * 
     * @param sourceCode The source code to search
     * @param blockType The type of blocks to find
     * @return List of blocks matching the specified type
     */
    public List<Block> findBlocksByType(String sourceCode, Block.BlockType blockType) {
        List<Block> allBlocks = parseBlocks(sourceCode);
        List<Block> matchingBlocks = new ArrayList<>();
        
        findBlocksByTypeRecursive(allBlocks, blockType, matchingBlocks);
        
        return matchingBlocks;
    }
    
    private void findBlocksByTypeRecursive(List<Block> blocks, Block.BlockType targetType, List<Block> result) {
        for (Block block : blocks) {
            if (block.getType() == targetType) {
                result.add(block);
            }
            findBlocksByTypeRecursive(block.getChildren(), targetType, result);
        }
    }
}