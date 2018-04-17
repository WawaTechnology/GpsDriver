package com.example.unsan.gpsdriver;

/**
 * Created by Unsan on 17/4/18.
 */

public class Employee {
    int age;
    String name;

    public Employee(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public boolean equals(Object obj)
    {
        if(this==obj)
            return true;
        if(!(obj instanceof Employee))
        {
            return false;
        }
        Employee e1=(Employee)obj;
        return this.getName()==e1.getName()&&this.getAge()==e1.getAge();
    }
    @Override
    public int hashCode()
    {
        int result=2;
        result=result*4+age;
        result=result*2+(name!=null?name.hashCode():0);
        return result;
    }
}
