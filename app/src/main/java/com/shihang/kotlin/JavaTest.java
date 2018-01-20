package com.shihang.kotlin;


public class JavaTest {


    public static class Data{
        String name;
        Data(String name){
            this.name = name;
        }
    }

    public static void main(String[] args){
        Data data1 = new Data("data1");
        Data data2 = new Data("data2");
        Data data3 = new Data("data3");
        String outStr = "%s.hascode = %s ------ threadId:%s\n";
        System.out.print(String.format(outStr, data1.name, data1.hashCode(), Thread.currentThread().getId()));
        System.out.print(String.format(outStr, data2.name, data2.hashCode(), Thread.currentThread().getId()));
        System.out.print(String.format(outStr, data3.name, data3.hashCode(), Thread.currentThread().getId()));
        test(data1);
        test(data2);
        test(data3);
    }


    private static void test(final Data data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    System.out.print(String.format("%s.hascode = %s ------ threadId:%s\n",data.name, data.hashCode(), Thread.currentThread().getId()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
