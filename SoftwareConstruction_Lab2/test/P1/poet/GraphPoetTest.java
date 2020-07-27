/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }


    /**
     *测试poem方法是否能根据要求插入bridge
     * 设置多个bridge word，选择边之和权值较大的bridge word进行插入
     * 输入word划分：存在大写字母、不存在大写字母
     */
    @Test
    public void testPoem() throws IOException{
        GraphPoet graphPoetInstance = new GraphPoet(new File("src/P1/src/poet/mugar-omni-theater.txt"));
        String expectedOutputString = "Test insert the system.";
        assertEquals(expectedOutputString,graphPoetInstance.poem("Test the system."));
    }

    /**
     * 测试是否能正确检测输入字符串符合word的要求
     * 输入划分：符合要求的字符串（由数字、标点、字母构成）、不符合要求
     * @throws IOException
     */
    @Test
    public void testIsLegalWord () throws IOException{
        GraphPoet graphPoetInstance = new GraphPoet(new File("src/P1/src/poet/mugar-omni-theater.txt"));
        assertTrue(graphPoetInstance.isLegalWord("legalword!.123"));
        assertFalse(graphPoetInstance.isLegalWord("illegal word"));
    }

    /**
     * 测试是否能正确检测输入字符符合word字符的要求
     * 输入划分：符合要求（数字、标点、字母）、不符合要求
     * @throws IOException
     */
    @Test
    public void testIsLegalChar () throws IOException {
        GraphPoet graphPoetInstance = new GraphPoet(new File("src/P1/src/poet/mugar-omni-theater.txt"));
        assertTrue(graphPoetInstance.isLegalChar('a'));
        assertTrue(graphPoetInstance.isLegalChar('S'));
        assertTrue(graphPoetInstance.isLegalChar('5'));
        assertTrue(graphPoetInstance.isLegalChar('!'));
        assertFalse(graphPoetInstance.isLegalChar('~'));
    }

    /**
     * 测试是否能将输入字符串中的大写字母转化成小写字母
     * 字符串中存在的字符划分：大写字母、小写字母、数字、标点
     */
    @Test
    public void testStringToLowerCase() throws IOException {
        GraphPoet graphPoetInstance = new GraphPoet(new File("src/P1/src/poet/mugar-omni-theater.txt"));
        String inputString = "AbcD123.";
        String expectedOutputString = "abcd123.";
        assertEquals(expectedOutputString, graphPoetInstance.stringToLowerCase(inputString));
    }

}
