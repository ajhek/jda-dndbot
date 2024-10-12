package com.example.dndbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class Bot extends ListenerAdapter{
    public static void main(String[] arguments) throws Exception {
        JDA api = JDABuilder.createDefault(System.getenv("DISCORD_TOKEN")).enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new Bot())
                .build();
        CommandListUpdateAction commands = api.updateCommands();

        commands.addCommands(
                Commands.slash("roll", "Roll to perform an action")
                        .addOptions(new OptionData(OptionType.STRING, "action", "Pick your action", true)
                                .addChoices(choiceArray))
                        .addOptions(new OptionData(OptionType.INTEGER, "modifier", "Add an additional modifier to your roll", false))
        );

        commands.queue();

    }
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;
        // We don't want to respond to other bot accounts, including ourselves
        Message message = event.getMessage();
        String content = message.getContentRaw();
        // getContentRaw() is an atomic getter
        // getContentDisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)
        if (content.equals("!ping"))
        {
            event.getChannel().sendMessage("pong!").queue();
        }
    }

    private static final Command.Choice[] choiceArray = {
            new Command.Choice("ranged", "Attack with a ranged weapon"),
            new Command.Choice("melee", "Attack with a melee weapon"),
            new Command.Choice("CQC", "Attack with your fists"),
            new Command.Choice("dodge", "Dodge an incoming attack"),
            new Command.Choice("block", "Withstand an incoming attack"),
            new Command.Choice("throwables", "Throw an object at a target"),
            new Command.Choice("perception", "Spot an incoming attack or hidden enemy"),
            new Command.Choice("bigbrain", "Determine if an action will have an intended effect"),
            new Command.Choice("speech", "Convince a target of something"),
            new Command.Choice("stealth", "Hide yourself or sneak up on a target")
    };

}