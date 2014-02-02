package org.jglrxavpok.blocky.network.packets;

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
}
