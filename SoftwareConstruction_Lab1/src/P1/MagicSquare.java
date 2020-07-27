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
        //��ʱ�Ѿ������ö�����bfr

        int row=0;//��¼����
        int col=0;//��¼����
        char[] tempRowToChar;//��ǰ��ת�����ַ�����
        String[] tempRowSplit;//��ǰ�и����Ʊ��ת�����ַ�������
        int[][] numArray = new int [0][];//����ת���ɵľ���
        try{
            String tempRow = bfr.readLine();//ÿ�ζ���һ�У����Ե�ǰ�н��д���
            while(tempRow!=null)//temp��Ϊ��ʱ����ѭ��
            {
                row++;
                tempRowToChar = tempRow.toCharArray();

                //�������Ϸ���
                for(int i=0;i<tempRowToChar.length;i++)
                {
                    if((tempRowToChar[i]<'0'||tempRowToChar[i]>'9')&&tempRowToChar[i]!='\t'&&tempRowToChar[i]!='\n')
                    {
                        System.out.println(fileName+":Input Format Error!");
                        return false;
                    }
                }

                tempRowSplit=tempRow.split("\t");
                if(col==0)//�Ե�һ�в���ʱ
                {
                    col=tempRowSplit.length;//�Ե�һ��Ԫ�ظ�����Ϊ����ά��
                    numArray = new int[col][col];//��������
                    //System.out.println(col);
                }
                if(row>col||tempRowSplit.length!=col)//����Ǿ���ʱ��Ӧ���
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

            //��ɶ�ȡ ����numArray������б��ֵ
            int sum=0;
            int flag=0;
            //1.��
            for(int i=0;i<col;i++)
            {
                for(int j=0;j<col;j++)//���i�е�ֵ
                {
                    sum+=numArray[i][j];
                }

                if(i==0)//��¼����֮��Ϊ��׼
                {
                    flag = sum;
                }
                else//������������ж�
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
            //2.��
            //���ǵ��ֲ���ԭ�����������ÿ�еĺ�
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
            //3.б��
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

            //����MagicSquare��������
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
        //����Ϸ��Լ��
        if( n<=0 || n%2==0 )
        {
            //System.out.println("Input error!");
            return false;
        }

        //����MagicSquare
        int magic[][] = new int[n][n];//MagicSquare����ڶ�ά����magic�У�����nΪ�����ģ��������
        int row = 0, col = n / 2, i, j, square = n * n;//squareΪmagic��ŵ�Ԫ�ظ���
        for (i = 1; i <= square; i++) //�ܹ�����square��Ԫ�أ�����square��ѭ�����������ݴ�1��ʼ
        {
            magic[row][col] = i;//��Ӧλ������
            if (i % n == 0)//��i��n��ģΪ0ʱ������ǰ��ָ����һ��
                row++;
            else {
                if (row == 0)//��i��n��ģ��Ϊ0ʱ��������Ϊ0���򽫵�ǰ��ָ��n-1��
                    row = n - 1;
                else//��i��n��ģ��Ϊ0ʱ����������Ϊ0���򽫵�ǰ��ָ����һ��
                    row--;
                if (col == (n - 1))//��i��n��ģ��Ϊ0ʱ������ǰ�������һ�У��򽫵�ǰ��ָ���0��
                    col = 0;
                else//��i��n��ģ��Ϊ0ʱ������ǰ�в������һ�У��򽫵�ǰ��ָ����һ��
                    col++;
            }
        }

        //ʹ��Ƕ��ѭ����ӡ���ɵ�MagicSquare
        for (i = 0; i < n; i++)
        {
            for (j = 0; j < n; j++)
                System.out.print(magic[i][j] + "\t");
            System.out.println();
        }

        //�����д��6.txt
        FileWriter fw = null;
        try {
            File f = new File("src/P1/txt/6.txt");
            if(!f.exists())//�ļ�������ʱ����Ҫ����
            {
                f.createNewFile();
            }
            fw = new FileWriter(f);//����д����

            int x=0;
            int y=0;
            for(x=0;x<n;x++)
            {
                for(y=0;y<n-1;y++)
                {
                    fw.write(magic[x][y]+"\t");
                }
                //��������ÿ�е����һ��
                fw.write(magic[x][y]+"\n");
                y=0;
            }
            fw.close();//�ر�д����
        }
        catch (IOException e){
            System.out.println("IOException");
            e.printStackTrace();
        }
        //������д��ɹ�
        System.out.println("Generated and Writed Successfully!");
        return true;
    }

    public static void main(String argv[]) throws FileNotFoundException,IOException
    {
        MagicSquare m = new MagicSquare();
        //��֤����
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

        //��������
        boolean flag = false;
        Scanner s = new Scanner(System.in);
        while (!flag)
        {
            System.out.println("Please input a positive odd number:");
            int input = s.nextInt();
            flag = MagicSquare.generateMagicSquare(input);
            if (!flag)//û�в���MagicSquare
            {
                System.out.println("Input error! Try again.");
            }
        }
        String path6 = "src/P1/txt/6.txt";
        m.isLegalMagicSquare(path6);

    }
}