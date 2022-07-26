package com.xlj.tools.leetcode;


import java.util.Arrays;

/**
 * 排序算法
 *
 * @author legend xu
 * @date 2022/7/25
 */
public class SortAlgorithm {

    public static void main(String[] args) throws Exception {
        int[] sourceArray = {5, 1, 8, 4, 3, 7, 6, 2, 0, 9};
        SortAlgorithm sortAlgorithm = new SortAlgorithm();
        System.out.println(Arrays.toString(sortAlgorithm.quickSortMain(sourceArray)));
        System.out.println(Arrays.toString(sortAlgorithm.bubbleSort(sourceArray)));
        System.out.println(Arrays.toString(sortAlgorithm.mergeSort(sourceArray)));
    }

    /**
     * 冒泡排序：
     * - 平均时间复杂度：O(n2)；空间复杂度：O(1)
     * - 步骤
     * 1.比较相邻的元素。如果第一个比第二个大，就交换他们两个。
     * 2.对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。这步做完后，最后的元素会是最大的数。
     * 3.针对所有的元素重复以上的步骤，除了最后一个。
     * 4.持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。
     *
     * @param sourceArray
     * @return
     */
    public int[] bubbleSort(int[] sourceArray) throws Exception {
        // 对原数组做深拷贝
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);
        for (int i = 1; i < arr.length; i++) {
            // 设定一个标志位，若当前循环没有交换，则排序已完成
            boolean flag = true;
            for (int j = 0; j < arr.length - i; j++) {
                // 若第一个数比第二个数大，则交换
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    flag = false;
                }
            }
            if (flag) {
                break;
            }
        }
        return arr;
    }

    /**
     * 快速排序：使用分治法策略来把一个串行分为两个子串行；本质应该算是在冒泡排序的基础上的递归分治法
     * - 平均时间复杂度：O(n log n)；空间复杂度：O(log n)
     * - 步骤：
     * 1.从数列中挑出一个元素，称为 "基准"（pivot）
     * 2.重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区操作
     * 3.递归地把小于基准值元素的子数列和大于基准值元素的子数列排序
     *
     * @param sourceArray
     * @return
     */
    public int[] quickSortMain(int[] sourceArray) throws Exception {
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);
        return quickSort(arr, 0, arr.length - 1);
    }

    public int[] quickSort(int[] arr, int left, int right) {
        if (left < right) {
            // 记录交换次数
            int index = left + 1;
            for (int i = index; i <= right; i++) {
                // 冒泡排序，以 arr[left] 为基准，在分区内数值作比较，比基准值大放右边，小放左边（交换）
                if (arr[i] < arr[left]) {
                    int temp = arr[i];
                    arr[i] = arr[index];
                    arr[index] = temp;
                    index++;
                }
            }
            // 基准值交换
            int temp = arr[left];
            arr[left] = arr[index - 1];
            arr[index - 1] = temp;
            // 分区索引值
            int partitionIndex = index - 1;
            // 递归左边分区进行排序
            quickSort(arr, left, partitionIndex - 1);
            // 递归右边分区进行排序
            quickSort(arr, partitionIndex + 1, right);
        }
        return arr;
    }

    /**
     * 归并排序
     *
     * @param sourceArray
     * @return
     */
    public int[] mergeSort(int[] sourceArray) {
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);
        if (arr.length < 2) {
            return arr;
        }
        int middle = (int) Math.floor(arr.length >> 1);

        int[] left = Arrays.copyOfRange(arr, 0, middle);
        int[] right = Arrays.copyOfRange(arr, middle, arr.length);

        return merge(mergeSort(left), mergeSort(right));
    }

    public int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0;
        while (left.length > 0 && right.length > 0) {
            if (left[0] <= right[0]) {
                result[i++] = left[0];
                left = Arrays.copyOfRange(left, 1, left.length);
            } else {
                result[i++] = right[0];
                right = Arrays.copyOfRange(right, 1, right.length);
            }
        }
        while (left.length > 0) {
            result[i++] = left[0];
            left = Arrays.copyOfRange(left, 1, left.length);
        }

        while (right.length > 0) {
            result[i++] = right[0];
            right = Arrays.copyOfRange(right, 1, right.length);
        }
        return result;
    }
}
