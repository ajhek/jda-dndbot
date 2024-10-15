package com.example.dndbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;


import javax.security.auth.login.LoginException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;

public class Bot extends ListenerAdapter{
    public static void main(String[] arguments) throws LoginException {
        JDA api = JDABuilder.createDefault(System.getenv("DISCORD_TOKEN")).enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        //CommandListUpdateAction commands = api.updateCommands();
        api.updateCommands();

        //api.updateCommands().addCommands();

        api.addEventListener(new SlashCommands());
        //commands.queue();
        System.out.println("init");
    }

    /*public void onMessageReceived(MessageReceivedEvent event)
    {
        System.out.println("Text command");
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
    }*/


    /*public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        System.out.println("Command Received");
        if (event.getName().equals("roll")) {
            Long userId = event.getUser().getIdLong();
            String action = event.getOption("action").getAsString();
            String charName = charNames.get(userId);
            int bonus = characters.get(userId).get(action);
            int roll = rand.nextInt(100) + 1;
            String rollmsg;
            if (event.getOption("modifier").equals(false)){
                rollmsg = (charName + " rolls " + action + ".\n` " + (roll+bonus) + "` ⟵ [**" + roll + "**] d100 + " + bonus);
            }
            else{
                int modifier = event.getOption("modifier").getAsInt();
                rollmsg = (charName + " rolls " + action + ".\n` " + (roll+bonus+modifier) + "` ⟵ [**" + roll + "**] d100 + " + bonus + " + " + modifier);
            }
            event.reply(rollmsg).queue();
        }
        if (event.getName().equals("addchar")) {
            Long userId = event.getUser().getIdLong();
            String charName = event.getOption("name").getAsString();
            Dictionary<String, Integer> stats = new Hashtable<>();
            for (Command.Choice choice : choiceArray) {
                stats.put(choice.getName(), 0);
            }
            characters.put(userId, stats);
            charNames.put(userId, charName);

            event.reply("Character " + charName + " added!").queue();
        }
    }*/


}