package com.crystal.books.nio.buffers;

import java.nio.CharBuffer;

/**
 * Created by hombre on 2015/9/2.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/9/2 11:04
 *
 * Buffer fill/drain example. This code uses the simplest means of filling
 * and draining a buffer: one element at a time.
 *
 */
public class BufferFillDrain {

    private static final int CAPACITY = 100;

    private static int index = 0;

    private static String[] strings = {
            "A random string value",
            "The product of an infinite number of monkeys",
            "Hey hey we're the Monkees",
            "Opening act for the Monkees: Jimi Hendrix",
            "'Scuse me while I kiss this fly", // Sorry Jimi ;-)
            "Help Me! Help Me!",
    };

    public static void main(String[] args) throws Exception{
        CharBuffer buffer = CharBuffer.allocate(CAPACITY);
        while(fillBuffer(buffer)){
            buffer.flip();
            drainBuffer(buffer);
            buffer.clear();
        }
    }

    private static void drainBuffer(CharBuffer buffer){
        while(buffer.hasRemaining()){
            System.out.print(buffer.get());
        }
        System.out.println("");
    }
    private static boolean fillBuffer(CharBuffer buffer) {
        if (index >= strings.length){
            return (false);
        }
        String string = strings[index++];
        for(int i = 0; i < string.length(); i++){
            buffer.put(string.charAt(i));
        }
        return (true);
    }
}
