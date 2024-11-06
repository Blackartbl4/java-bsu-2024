package controlWork;

import java.io.*;
import java.io.FileOutputStream;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<LightAuto> list1;
        try(FileInputStream fin=new FileInputStream("input1.txt"))
        {
            int i;
            while((i=fin.read())!=-1){

                System.out.print((char)i);
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        System.out.println();
        try(FileInputStream fin=new FileInputStream("input2.txt"))
        {
            int i;
            while((i=fin.read())!=-1){

                System.out.print((char)i);
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    /*static List<AbstractAuto> Metod1(List<AbstractAuto> list) {
        List<AbstractAuto> lis = list.stream().sorted(Comparator.comparing())
    }*/

    static int Metod2(List<AbstractAuto> list, int color) {
        int k = 0;
        for (var i : list) {
            if (i.color_ == color) {
                k++;
            }
        }
        return k;
    }

    static boolean Metod3(List<AbstractAuto> list, String name, int color, int rating) throws RuntimeException{
        List<AbstractAuto> auto = list.stream().filter(a -> a.color_ == color && a.name_ == name && a.rating_ == rating).toList();
        return !auto.isEmpty();
    }
}

