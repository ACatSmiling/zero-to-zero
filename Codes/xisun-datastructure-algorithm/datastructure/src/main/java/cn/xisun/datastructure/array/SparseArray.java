package cn.xisun.datastructure.array;

/**
 * @author XiSun
 * @since 2024/1/10 23:06
 * <p>
 * 稀疏数组
 */
public class SparseArray {
    public static void main(String[] args) {
        // 原始棋盘

        // 1.创建一个原始的二维数组11 * 11，模拟棋盘
        int[][] chessArray = new int[11][11];

        // 2.棋盘已有的棋子，0: 表示没有棋子，1：表示黑子，2：表示蓝子
        chessArray[1][2] = 1;
        chessArray[2][3] = 2;
        chessArray[4][5] = 2;

        // 3.输出原始的二维数组
        System.out.println("原始的二维数组：");
        for (int[] row : chessArray) {
            for (int data : row) {
                System.out.print(data + "\t");
            }
            System.out.println();
        }

        // 将二维数组转换成稀疏数组

        // 1.先遍历二维数组，得到非0数据的个数
        int sum = 0;
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (chessArray[i][j] != 0) {
                    sum++;
                }
            }
        }

        // 2.创建对应的稀疏数组
        int[][] sparseArray = new int[sum + 1][3];

        // 3.给稀疏数组赋值
        sparseArray[0][0] = 11;// 行数
        sparseArray[0][1] = 11;// 列数
        sparseArray[0][2] = sum;// 非0数据的个数

        // 遍历二维数组，将非0的值存放到sparseArray中
        int count = 0; //count 用于记录是第几个非0数据
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (chessArray[i][j] != 0) {
                    count++;
                    sparseArray[count][0] = i;
                    sparseArray[count][1] = j;
                    sparseArray[count][2] = chessArray[i][j];
                }
            }
        }

        // 4.输出稀疏数组
        System.out.println();
        System.out.println("得到稀疏数组为：");
        for (int[] ints : sparseArray) {
            System.out.print(ints[0] + "\t" + ints[1] + "\t" + ints[2] + "\n");
        }
        System.out.println();

        // 将稀疏数组恢复成原始的二维数组

        // 1.先读取稀疏数组的第一行，根据第一行的数据，创建原始的二维数组
        int[][] chessArray2 = new int[sparseArray[0][0]][sparseArray[0][1]];

        // 2.再读取稀疏数组后几行的数据(从第二行开始)，并赋给原始的二维数组
        for (int i = 1; i < sparseArray.length; i++) {
            chessArray2[sparseArray[i][0]][sparseArray[i][1]] = sparseArray[i][2];
        }

        // 3.输出恢复后的二维数组
        System.out.println("恢复后的二维数组：");
        for (int[] row : chessArray2) {
            for (int data : row) {
                System.out.print(data + "\t");
            }
            System.out.println();
        }
    }
}
