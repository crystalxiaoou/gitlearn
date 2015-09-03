package com.crystal.books.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Created by hombre on 2015/9/3.
 *
 * @Email : hombre1218@gmail.com
 * @Version : 1.0
 * @Date : 2015/9/3 20:25
 *
 * Test Copying between channels.
 *
 */
public class ChannelCopy {

    private static final int CAPACITY = 16 * 1024;

    /**
     * This code copies data from stdin to stdout. Like the 'cat'
     * command, but without any useful options.
     * @param args
     */
    public static void main(String[] args) throws IOException{
        ReadableByteChannel source = Channels.newChannel(System.in);
        WritableByteChannel dest = Channels.newChannel(System.out);

//        channelCopy1(source, dest);

        // alternatively, call channelCopy2 (source, dest);
        channelCopy2 (source, dest);

        source.close();
        dest.close();
    }


    /**
     * Channel Copy method 1. This method copies data from the src channel and writes it
     * to the dest channel until EOF on src.
     * This implementations makes use of compact() on the temp buffer
     * to pack down the data if the buffer wasn't fully drained.
     * This may result in data copying, but minimizes system calls. It also
     * requires a cleanup loop to make sure all the data gets sent.
     * @param source
     * @param dest
     */
    private static void channelCopy1(ReadableByteChannel source, WritableByteChannel dest) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(CAPACITY);
        while(source.read(buffer) != -1){
            // Prepare the buffer to be drained
            buffer.flip();
            // Write to the channel; may block
            dest.write(buffer);
            // If partial transfer, shift remainder down
            // If buffer is empty, same as doing clear()
            buffer.compact();
        }

        // EOF will leave buffer in fill state
        buffer.flip();
        // Make sure that the buffer is fully drained
        while(buffer.hasRemaining()){
            dest.write(buffer);
        }
    }

    /**
     * Channel copy method 2. This method performs the same copy, but
     * assures the temp buffer is not empty before reading more data. This
     * never requires data copying but may result in more systems calls.
     * No post-loop cleanup is needed because the buffer will be empty
     * when the loop is exited.
     * @param source
     * @param dest
     */
    private static void channelCopy2(ReadableByteChannel source, WritableByteChannel dest) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(CAPACITY);
        while(source.read(buffer) != -1){
            // Prepare the buffer to be drained
            buffer.flip();
            // Make sure that the buffer was fully drained
            while(buffer.hasRemaining()){
                dest.write(buffer);
            }
            // Make the buffer empty, ready for filling
            buffer.clear(); //这一行非常重要，设置position为0, limit 为capacity的大小
        }
    }


}
