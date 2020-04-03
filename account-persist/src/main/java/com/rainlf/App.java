package com.rainlf;

import java.io.File;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        String a = "123/123\\123";
        System.out.println(a.replace("/", File.separator).replace("\\", File.separator));
    }
}
