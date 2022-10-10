package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ArrayList<Integer> l = new ArrayList<>();

        l.add(8);
        l.add(1);
        l.add(9);
        l.add(5);

        Comparator<Integer> comp = (p1, p2) -> (p1 < p2) ? -1 : ((p1 == p2) ? 0 : 1);

        Collections.sort(l, comp);

        System.out.println(l);

        System.out.println(compare(20, 20, comp));

    }

    public static int compare (int n1, int n2, Comparator<Integer> c) {
        return c.compare(n1, n2);
    }
}
