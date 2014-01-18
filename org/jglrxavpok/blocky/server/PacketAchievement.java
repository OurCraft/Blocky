package org.jglrxavpok.blocky.server;

import org.jglrxavpok.blocky.achievements.Achievement;

public class PacketAchievement extends Packet
{

    private static final long serialVersionUID = -959041276311702428L;

    public PacketAchievement(Achievement a)
    {
        super("AchievementPacket", a.getID().getBytes());
    }

}
