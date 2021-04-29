package com.xlj.tools.juc;

/**
 * @author XLJ
 * @date 2020/10/24
 */
public class TransferValue {
    // 值传递
    public void transferValue1(int a) {
        a = 30;
    }

    // 对象引用传递
    public void transferValue2(PersonBo personBo) {
        personBo.setName("XLJ");
    }

    // String传递
    public void transferValue3(String str) {
        str = "XLJ";
    }

    public static void main(String[] args) {
        TransferValue transferValue = new TransferValue();

        // 值传递
        int a = 20;
        transferValue.transferValue1(a);
        System.out.println(a);

        // 对象引用传递
        PersonBo personBo = new PersonBo("xlj");
        transferValue.transferValue2(personBo);
        System.out.println(personBo.getName());

        // String传递
        String str = "xlj";
        transferValue.transferValue3(str);
        System.out.println(str);

    }

}
