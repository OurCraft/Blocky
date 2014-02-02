package org.jglrxavpok.blocky.server;

import java.io.Serializable;

import org.jglrxavpok.storage.TaggedStorageSystem;

public class OldPacket implements Serializable
{

    private static final long serialVersionUID = -8863066537098218591L;

    public static final TaggedStorageSystem tagSystem = new TaggedStorageSystem();
    
    public String name;
    public byte[] data;
    
    public OldPacket(String name, byte[] data)
    {
        this.name = name;
        this.data = data;
    }
}
