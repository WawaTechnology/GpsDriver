package com.example.unsan.gpsdriver;

import org.junit.Test;

/**
 * Created by Unsan on 17/4/18.
 */

public class ConstructorCallsOverride {

    abstract class Base {
        Base() {
            System.out.println("base called");
            overrideMe();
        }

        abstract void overrideMe();
    }

    class Child extends Base {
        final int x;

        Child(int x) {
            this.x = x;
        }

        @Override
        void overrideMe() {
            System.out.println(x);
        }
    }

    @Test
    public void testMe() {

        new Child(42);
}// prints "0"

}
