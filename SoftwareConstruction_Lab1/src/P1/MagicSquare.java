package P1;

import java.io.*;
import java.util.Scanner;

public class MagicSquare
{
    boolean isLegalMagicSquare(String fileName) //throws FileNotFoundException,IOException
    {
        BufferedReader bfr = null;
        try {
            bfr = new BufferedReader(new FileReader(fileName));
        }
        catch (FileNotFoundException e){
            System.out.println("File Not Found Error!");
            e.printStackTrace();
            return false;
        }
        //此时已经创建好读缓冲bfr

        int row=0;//记录行数
        int col=0;//记录列数
        char[] tempRowToChar;//当前行转换成字符数组
        String[] tempRowSplit;//当前行根据制表符转换成字符串数组
        int[][] numArray = new int [0][];//最终转换成的矩阵
        try{
            String tempRow = bfr.readLine();//每次读入一行，并对当前行进行处理
            while(tempRow!=null)//temp不为空时进行循环
            {
                row++;
                tempRowToChar = tempRow.toCharArray();

                //检测输入合法性
                for(int i=0;i<tempRowToChar.length;i++)
                {
                    if((tempRowToChar[i]<'0'||tempRowToChar[i]>'9')&&tempRowToChar[i]!='\t'&&tempRowToChar[i]!='\n')
                    {
                        System.out.println(fileName+":Input Format Error!");
                        return false;
                    }
                }

                tempRowSplit=tempRow.split("\t");
                if(col==0)//对第一行操作时
                {
                    col=tempRowSplit.length;//以第一行元素个数作为矩阵维度
                    numArray = new int[col][col];//建立矩阵
                    //System.out.println(col);
                }
                if(row>col||tempRowSplit.length!=col)//输入非矩阵时对应情况
                {
                    System.out.println(fileName+":Not a Matrix!");
                    return false;
                }

                for(int i=0;i<col;i++)
                {
                    numArray[row-1][i] = Integer.valueOf(tempRowSplit[i]);
                }

                tempRow = bfr.readLine();
            }

            //完成读取 计算numArray各行列斜的值
            int sum=0;
            int flag=0;
            //1.行
            for(int i=0;i<col;i++)
            {
                for(int j=0;j<col;j++)//求第i行的值
                {
                    sum+=numArray[i][j];
                }

                if(i==0)//记录首行之和为基准
                {
                    flag = sum;
                }
                else//非首行则进行判断
                {
                    if(sum!=flag)
                    {
                        System.out.println(fileName+":Not a MagicSquare1!");
                        return false;
                    }
                }
                sum=0;
            }
            //System.out.println(flag);
            //2.列
            //考虑到局部性原理，设计数组存放每列的和
            int[] colSum = new int[col];
            for(int i=0;i<col;i++)
            {
                for(int j=0;j<col;j++)
                {
                    colSum[j] += numArray[i][j];
                }
            }
            for(int i=0;i<col;i++)
            {
                if(colSum[i]!= flag)
                {
                    System.out.println(fileName+":Not a MagicSquare2!");
                    return false;
                }
            }
            //3.斜边
            int leftSum=0;
            int rightSum=0;
            for(int i=0;i<col;i++)
            {
                leftSum+=numArray[i][i];
                rightSum+=numArray[i][col-i-1];
            }
            if(leftSum!=flag || rightSum!=flag)
            {
                System.out.println(fileName+":Not a MagicSquare3!");
                return false;
            }

            //符合MagicSquare所有条件
            System.out.println(fileName+":Input is MagicSquare! Summary of each line is "+flag);
            return true;
        }
        catch(IOException e){
            System.out.println("IO Exception");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean generateMagicSquare(int n)
    {
        //输入合法性检查
        if( n<=0 || n%2==0 )
        {
            //System.out.println("Input error!");
            return false;
        }

        //构建MagicSquare
        int magic[][] = new int[n][n];//MagicSquare存放在二维数组magic中，其中n为输入规模（奇数）
        int row = 0, col = n / 2, i, j, square = n * n;//square为magic存放的元素个数
        for (i = 1; i <= square; i++) //总共填入square个元素，进行square轮循环，填入数据从1开始
        {
            magic[row][col] = i;//对应位置填入
            if (i % n == 0)//当i对n求模为0时，将当前行指向下一行
                row++;
            else {
                if (row == 0)//当i对n求模不为0时，若行数为0，则将当前行指向n-1行
                    row = n - 1;
                else//当i对n求模不为0时，若行数不为0，则将当前行指向上一行
                    row--;
                if (col == (n - 1))//当i对n求模不为0时，若当前列是最后一列，则将当前列指向第0列
                    col = 0;
                else//当i对n求模不为0时，若当前列不是最后一列，则将当前列指向下一列
                    col++;
            }
        }

        //使用嵌套循环打印生成的MagicSquare
        for (i = 0; i < n; i++)
        {
            for (j = 0; j < n; j++)
                System.out.print(magic[i][j] + "\t");
            System.out.println();
        }

        //将结果写入6.txt
        FileWriter fw = null;
        try {
            File f = new File("src/P1/txt/6.txt");
            if(!f.exists())//文件不存在时，需要创建
            {
                f.createNewFile();
            }
            fw = new FileWriter(f);//创建写入器

            int x=0;
            int y=0;
            for(x=0;x<n;x++)
            {
                for(y=0;y<n-1;y++)
                {
                    fw.write(magic[x][y]+"\t");
                }
                //单独处理每行的最后一项
                fw.write(magic[x][y]+"\n");
                y=0;
            }
            fw.close();//关闭写入器
        }
        catch (IOException e){
            System.out.println("IOException");
            e.printStackTrace();
        }
        //创建并写入成功
        System.out.println("Generated and Writed Successfully!");
        return true;
    }

    public static void main(String argv[]) throws FileNotFoundException,IOException
    {
        MagicSquare m = new MagicSquare();
        //验证部分
        String path1 = "src/P1/txt/1.txt";
        String path2 = "src/P1/txt/2.txt";
        String path3 = "src/P1/txt/3.txt";
        String path4 = "src/P1/txt/4.txt";
        String path5 = "src/P1/txt/5.txt";
        m.isLegalMagicSquare(path1);
        m.isLegalMagicSquare(path2);
        m.isLegalMagicSquare(path3);
        m.isLegalMagicSquare(path4);
        m.isLegalMagicSquare(path5);

        //产生部分
        boolean flag = false;
        Scanner s = new Scanner(System.in);
        while (!flag)
        {
            System.out.println("Please input a positive odd number:");
            int input = s.nextInt();
            flag = MagicSquare.generateMagicSquare(input);
            if (!flag)//没有产生MagicSquare
            {
                System.out.println("Input error! Try again.");
            }
        }
        String path6 = "src/P1/txt/6.txt";
        m.isLegalMagicSquare(path6);

    }
}