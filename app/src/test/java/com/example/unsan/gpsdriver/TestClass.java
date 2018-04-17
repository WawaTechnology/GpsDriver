package com.example.unsan.gpsdriver;

import org.junit.Test;

/**
 * Created by Unsan on 17/4/18.
 */

public class TestClass {

    @Test
    public void checkEmployee()
    {
        Employee e1=new Employee(31,"sampada");
        Employee e2=new Employee(31,"sampada");
        System.out.println(e1.equals(e2));
        System.out.println(e1.hashCode());
        System.out.println(e2.hashCode());
        String s="sampada";
        String s2=new String("sampada");
        System.out.println(s==s2);
        System.out.println(e1==e2);
        System.out.println(e1.name==e2.name);

    }
}
