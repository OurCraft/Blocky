package org.jglrxavpok.blocky.network.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.jglrxavpok.storage.TaggedStorageSystem;

public class Packet
{

    public static final TaggedStorageSystem tagSystem = new TaggedStorageSystem();
    
    public String name;
    public byte[] data;
    
    /**
     * For Kryo
     */
    Packet(){}
    
    public Packet(String name, byte[] data)
    {
        this.name = name;
        this.data = data;
    }

    public void decompressData()
    {
        if(data == null)
            return;
        try
        {
            data = decompress(data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (DataFormatException e)
        {
            e.printStackTrace();
        }
    }

    public void compressData()
    {
        if(data == null)
            return;
        try
        {
            data = compress(data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static byte[] compress(byte[] data) throws IOException
    {  
        Deflater deflater = new Deflater();  
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);  
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);   
            
        deflater.finish();  
        byte[] buffer = new byte[1024];   
        while (!deflater.finished()) {  
            int count = deflater.deflate(buffer); // returns the generated code... index  
            outputStream.write(buffer, 0, count);   
        }  
        outputStream.close();  
        byte[] output = outputStream.toByteArray();  
        return output;
    }
    
    public static byte[] decompress(byte[] data) throws IOException, DataFormatException 
    {  
        Inflater inflater = new Inflater(); 
        inflater.setInput(data);  
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
        byte[] buffer = new byte[1024];  
        while (!inflater.finished()) 
        {  
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }  
        outputStream.close();  
        byte[] output = outputStream.toByteArray();  
        return output;  
    }  
}
