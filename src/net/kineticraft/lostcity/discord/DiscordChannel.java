package net.kineticraft.lostcity.discord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.kineticraft.lostcity.Core;
import net.kineticraft.lostcity.utils.ServerUtils;

import java.util.Arrays;

/**
 * List of discord channels the code explicity needs to send messages too.
 * These are ones like Oryx, which will have a message sent when Core.warn is called.
 * Created by Kneesnap on 6/28/2017.
 */
@AllArgsConstructor
public enum DiscordChannel {

    ANNOUNCEMENTS(316722073567232001L, 453660221361881111L),
    REPORTS(346101298732597249L, 453660263963688967L),
    WARP_PROPOSALS(377673586749603841L, 453660290408644608L),
    INGAME(199294638467710976L, 453663004425453568L),
    STAFF_CHAT(199243469766656001L, 453660335522709528L),
    ORYX(329817943204560909L, 453660063152734219L);

    private final long channelId;
    private final long devChannelId;

    public long getChannelId() {
        return ServerUtils.isDevServer() ? devChannelId : channelId;
    }

    /**
     * Get the JDA discord channel.
     * @return channel.
     */
    public MessageChannel getChannel() {
        return DiscordAPI.isAlive() ? DiscordAPI.getBot().getBot().getTextChannelById(getChannelId()) : null;
    }

    /**
     * Get a custom discord channel from a JDA channel.
     * @param channel
     * @return channel
     */
    public static DiscordChannel getChannel(MessageChannel channel) {
        return DiscordAPI.isAlive() ?
                Arrays.stream(values()).filter(dc -> dc.getChannelId() == channel.getIdLong()).findAny().orElse(null) : null;
    }
}
