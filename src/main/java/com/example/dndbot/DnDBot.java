package com.example.dndbot;

import com.example.dndbot.listener.DiscordEventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;


import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class DnDBot extends ListenerAdapter{
    protected static DnDBot selfBot;
    private ShardManager shardManager = null;
    public DnDBot(String token) {
        try {
            shardManager = buildShardManager(token);
        } catch (LoginException | SQLException e) {
            System.out.println("Failed to start bot! Please check the console for any errors.");
            System.exit(0);
        }
    }

    private ShardManager buildShardManager(String token) throws LoginException, SQLException {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createLight(token)
                .addEventListeners(new DiscordEventListener(this));
        return builder.build();
    }

    public ShardManager getShardManager() {
        return shardManager;
    }
}