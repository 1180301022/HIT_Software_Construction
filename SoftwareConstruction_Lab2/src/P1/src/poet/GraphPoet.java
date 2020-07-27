/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //   AF(graph) = 将输入语料库构成graph，并利用它完成诗句创作
    // Representation invariant:
    //   graph != null
    // Safety from rep exposure:
    //   成员域用private修饰
    //   返回值类型都是不可变类
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        BufferedReader fileReader = null;
        try{
            fileReader = new BufferedReader(new FileReader(corpus));
        }
        catch (IOException e){
            System.out.println("Fail to open target file.");
            e.printStackTrace();
        }
        String tempLine = fileReader.readLine();
        List<String> wordStore = new ArrayList<String>();
        //循环直到读到最后一行
        while (tempLine!=null){
            //*对单个行的处理*
            String[] lineToArray = tempLine.split(" ");
            //*对单个word的处理*
            for(String tempWord : lineToArray){
                //如果输入单词不满足定义则直接跳过
                if(!isLegalWord(tempWord)){
                    continue;
                }
                //满足定义，转化成小写，加入word集
                wordStore.add(stringToLowerCase(tempWord));
            }
            tempLine = fileReader.readLine();
        }
        //所有的word都存放在wordStore中
        for(int i=0  ; i<=wordStore.size()-2 ; i++){
            String tempWord = wordStore.get(i);
            String nextWord = wordStore.get(i+1);
            graph.add(tempWord);
            int formerWeight = graph.set(tempWord, nextWord, 1);
            if(formerWeight!=0){
                graph.set(tempWord, nextWord, formerWeight+1);
            }
        }
        checkRep();
    }

    private void checkRep(){
        assert (graph != null);
    }

    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        String[] inputToStringArray = input.split(" ");
        StringBuilder outputString = new StringBuilder("");
        for(int i=0 ; i<=inputToStringArray.length-2 ; i++){
            //首先加入本轮循环对应word，不改变大小写
            outputString.append(inputToStringArray[i]);
            outputString.append(" ");

            String lowerCaseTempString = stringToLowerCase(inputToStringArray[i]);
            String lowerCaseNextString = stringToLowerCase(inputToStringArray[i+1]);
            //如果图中不包含该点，或者不包含下一个点，直接跳过本轮循环
            if(!(graph.vertices().contains(lowerCaseTempString)) ||
                    !(graph.vertices().contains(lowerCaseNextString))){
                continue;
            }
            //图中包含该点且包含下一个点，查看是否存在bridge word
            int largerWeight = 0;
            String bridgeWord = "";
            //遍历两次targets寻找输入String中的下一个word
            for(String tempStringTargets : graph.targets(lowerCaseTempString).keySet()){
                //获取第一条边权值
                int weight1 = graph.targets(lowerCaseTempString).get(tempStringTargets);

                for(String nextStringTargets : graph.targets(tempStringTargets).keySet()){
                    if(nextStringTargets.equals(lowerCaseNextString)){
                        int weight2 = graph.targets(tempStringTargets).get(nextStringTargets);
                        if(weight1 + weight2 > largerWeight){
                            largerWeight = weight1 + weight2;
                            bridgeWord = tempStringTargets;
                        }
                    }
                }
            }
            //获取到bridge word，加入输出，不存在时加入空字符串
            outputString.append(bridgeWord);
            if(!(bridgeWord.equals(""))){
                outputString.append(" ");
            }
        }
        //对最后一个单词特殊处理
        outputString.append(inputToStringArray[inputToStringArray.length-1]);
        checkRep();
        return outputString.toString();
    }


    /**
     * 判断输入字符串是否符合word规定
     * @param inputString 输入的字符串
     * @return 返回输入是否符合word定义
     */
    public boolean isLegalWord(String inputString){
        char[] inputToArray = inputString.toCharArray();
        for(char tempChar : inputToArray){
            if(!isLegalChar(tempChar)){
                checkRep();
                return false;
            }
        }
        checkRep();
        return true;
    }

    /**
     * 判断输入字符是否符合word中的字符定义
     * @param inputChar 输入字符
     * @return 返回输入是否符合word中字符定义
     */
    public boolean isLegalChar (char inputChar){
        checkRep();
        return (inputChar==',' || inputChar=='!' || inputChar=='.' || (inputChar<='z'&&inputChar>='a') ||
                inputChar=='?' || (inputChar>='A'&&inputChar<='Z') || (inputChar<='9'&&inputChar>='0'));
    }

    /**
     * 将传入字符串中的大写转换成小写
     * @param inputString 输入的字符串
     * @return 大写转换成小写（针对大写字母）后的字符串，如果非大写字母则输出不变
     */
    public String stringToLowerCase(String inputString){
        StringBuilder stringToReturn = new StringBuilder("");
        char[] inputToChar = inputString.toCharArray();
        for(char tempChar : inputToChar){
            stringToReturn.append(Character.toLowerCase(tempChar));
        }
        checkRep();
        return stringToReturn.toString();
    }
    
}
