package com.example.admin.myapplication;

public class Abc {
    public static int count=0;
    public static void main(String args[]){
        (new Thread(){
            @Override
            public void run() {
                System.out.println("hello world one!");
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
               printCar();
                }
            }
        }).start();

        (new Thread(){
            @Override
            public void run(){
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                 countCar();
                }

            }
        }).start();
    }
    synchronized static void printCar() {
        if(count!=0){
            System.out.println("=>"+count);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count=0;
        }
    }
    synchronized static void countCar(){
        count++;
        System.out.println("+1");
    }
}
