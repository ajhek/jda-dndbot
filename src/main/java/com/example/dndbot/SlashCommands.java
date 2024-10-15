package com.example.dndbot;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;

public class SlashCommands extends ListenerAdapter {
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) throws IndexOutOfBoundsException{
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
            event.deferReply().queue();
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

    public void onGuildReady(GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(
                Commands.slash("roll", "Roll to perform an action")
                        .addOptions(new OptionData(OptionType.STRING, "action", "Pick your action", true)
                                .addChoices(choiceArray))
                        .addOptions(new OptionData(OptionType.INTEGER, "modifier", "Add an additional modifier to your roll", false)),
                Commands.slash("addchar", "Add a character")
                        .addOptions(new OptionData(OptionType.STRING, "name", "Name your character", true))
        ).queue();
    }
    protected Dictionary<Long, Dictionary<String, Integer>> characters = new Hashtable<>();
    protected Dictionary<Long, String> charNames = new Hashtable<>();
    protected String[] npcs = {};
    protected Random rand = new Random();
}
