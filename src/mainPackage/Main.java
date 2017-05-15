package mainPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    private static int nrTriunghiuri = 0;
    private static int nrElipse = 0;
    private static int nrDreptunghiuri = 0;
    private static InputStream is;
    private static int pointer = 0;
    private static HashMap<Position,Colour> pixelMap;

    public static void main(String [] args){
        try {
            File bmp = new File("C:\\Users\\Cristinaeviatamea\\Desktop\\Untitled.bmp"); // TODO: 13.05.2017 schimba cu args[0]
            pixelMap = new HashMap<>();
            is = new FileInputStream(bmp);

            System.out.println("Signature: " + readString(2));
            System.out.println("File size: " + readInt(4) + " bytes.");
            skip(4);
            final int bitmapDataOffset = readInt(4);
            System.out.println("Bitmap data offset: " + bitmapDataOffset);

            final int dibHeaderSize = readInt(4);
            System.out.println("\nDIB header size: " + dibHeaderSize);
            final int width = readInt(4);
            System.out.println("FRAME WIDTH: " + width);
            final int height = readInt(4);
            System.out.println("FRAME HEIGHT: " + height);
            System.out.println("Planes: " + readInt2Byte());
            System.out.println("Bits per pixel: " + readInt2Byte());
            System.out.println("Compression: " + readInt(4));
            System.out.println("Image size: " + readInt(4));
            System.out.println("X pixels per meter: " + readInt(4));
            System.out.println("Y pixels per meter: " + readInt(4));
            System.out.println("Colours in color table: " + readInt(4));
            System.out.println("Important colour count: " + readInt(4));
            //skip((bitmapDataOffset-pointer)-1);

            int padding;
            if(((width*3)%4) != 0){
                padding = 4-((width*3)%4);
            }else padding = 0;

            mapPixels(width,height,padding);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static String readString(int n) throws IOException {
        String result = "";
        for(int i = 1; i<=n;i++){
            result = result.concat(String.valueOf((char)is.read()));
        }
        pointer += n;
        return result;
    }

    private static int readInt(int n) throws IOException {
        byte[] b = new byte[n];
        for(int i=0;i<=n-1;i++){
            b[i] = readNextByteValue();
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(b);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return byteBuffer.getInt();
    }

    private static int readInt2Byte() throws IOException {
        byte[] b = new byte[2];
        b[0] = readNextByteValue();
        b[1] = readNextByteValue();
        return b[0] | b[1] << 8;
    }

    private static byte readNextByteValue() throws IOException {
        pointer +=1;
        return (byte)is.read();
    }

    private static void skip(int n) throws IOException {
        pointer += n;
        is.skip(n);
    }

    private static void mapPixels(final int width, final int height, final int padding) throws IOException {
        System.out.print("Mapping pixels...");
        for(int i = 0;i<=height-1;i++) {
            for(int j=0;j<=width-1;j++) {
                Colour temp = new Colour(is.read(),is.read(),is.read());
               // System.out.println("Registered Colour: " + temp);
                pixelMap.put(new Position(j,i), temp);
                pointer += 3;
            }
            skip(padding);
        }
        System.out.println("done");
        System.out.println("Pointer at " + pointer + ".");
    }

}

class Colour{
    private int r;
    private int g;
    private int b;

    Colour(int r, int b, int g){
        this.r = r;
        this.b = b;
        this.g = g;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    boolean equals(Colour colour){
        return (this.r == colour.r && this.g == colour.g && this.b == colour.b);
    }

    public String toString(){
        return "R: " + this.r + "; G: " + this.g + "; B: " + this.b + ";";
    }
}

class Position{
    private int x;
    private int y;

    Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    boolean equals(Position position){
        return (this.x==position.x && this.y==position.y);
    }
}
