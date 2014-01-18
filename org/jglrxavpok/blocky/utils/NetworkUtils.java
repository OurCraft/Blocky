package org.jglrxavpok.blocky.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class NetworkUtils
{

    public static String post(String adress,List<String> keys,List<String> values) throws IOException
    {
        String result = "";
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        try  
        {
            String data="";
            if(keys != null && values != null)
            for(int i=0;i<keys.size();i++)
            {
                if (i!=0) data += "&";
                data +=URLEncoder.encode(keys.get(i), "UTF-8")+"="+URLEncoder.encode(values.get(i), "UTF-8");
            }
            URL url = new URL(adress);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String ligne;
            while ((ligne = reader.readLine()) != null)
            {
                result+=ligne;
            }
        }
        catch (IOException e) 
        {
            throw e;
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch(Exception e)
            {
                ;
            }
            try
            {
                reader.close();
            }
            catch(Exception e)
            {
                ;
            }
        }
        return result;
    }

}
