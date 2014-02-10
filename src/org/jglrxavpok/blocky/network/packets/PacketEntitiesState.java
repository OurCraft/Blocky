package org.jglrxavpok.blocky.network.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.utils.IO;
import org.jglrxavpok.blocky.utils.Reflect;
import org.jglrxavpok.storage.TaggedStorageChunk;

public class PacketEntitiesState extends Packet
{

    PacketEntitiesState(){}
    
    public PacketEntitiesState(List<Entity> entities, Class<?>[] exclude)
    {
        super("EntitiesState", null);
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream output = new DataOutputStream(baos);
            output.writeInt(entities.size());
            label1 : for(int i = 0;i<entities.size();i++)
            {
                Entity e = entities.get(i);
                for(Class<?> c : exclude)
                {
                    if(Reflect.isInstanceof(e, c))
                        continue label1;
                }
                output.writeUTF(e.getClass().getCanonicalName());
                output.writeUTF(new String(Base64.encodeBase64(BlockyMain.saveSystem.writeChunk(e.writeTaggedStorageChunk(i)))));
            }
            output.flush();
            output.close();
            baos.close();
            data = baos.toByteArray();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public List<?>[] getStates()
    {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ArrayList<TaggedStorageChunk> chunks = new ArrayList<TaggedStorageChunk>();
        try
        {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
            int size = in.readInt();
            for(int i = 0;i<size;i++)
            {
                try
                {
                    String entityClass = in.readUTF();
                    InputStream in1 = new ByteArrayInputStream(Base64.decodeBase64(in.readUTF()));
                    byte[] bytes = IO.read(in1);
                    in1.close();
                    TaggedStorageChunk chunk = BlockyMain.saveSystem.readChunk(bytes);
                    String s = chunk.getChunkName().replaceFirst("Entity_", "");
                    ids.add(Integer.parseInt(s.substring(s.lastIndexOf("_")+1)));
                    chunks.add(chunk);
                }
                catch(Exception e)
                {
                    if(e instanceof EOFException)
                    {
                        
                    }
                    else
                        e.printStackTrace();
                }
            }
            in.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return new List[]{ids, chunks};
    }
}
