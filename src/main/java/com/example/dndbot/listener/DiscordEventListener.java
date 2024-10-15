package com.example.dndbot.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.example.dndbot.DnDBot;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public class DiscordEventListener extends ListenerAdapter {
    public DnDBot bot;
    public DiscordEventListener(DnDBot bot) {
        this.bot = bot;
    }

    public void onReady(@NotNull ReadyEvent event) {
        registerCommands(bot.getShardManager());
    }

    private void registerCommands(ShardManager jda) {
        Guild g = jda.getGuildById("838473776869015653"); // Replace this with the ID of your own server.
        if (g != null) {
            CommandListUpdateAction commands = g.updateCommands();
            commands.addCommands(
                    Commands.slash("hello", "Have the bot say hello to you in an ephemeral message!"),
                    Commands.slash("roll", "Roll to perform an action")
                            .addOptions(new OptionData(OptionType.STRING, "action", "Pick your action", true)
                                    .addChoices(choiceArray))
                            .addOptions(new OptionData(OptionType.INTEGER, "modifier", "Add an additional modifier to your roll", false)),
                    Commands.slash("addchar", "Add a character")
                            .addOptions(new OptionData(OptionType.STRING, "name", "Name your character", true)),
                    Commands.slash("modstat", "Modify a stat")
                                    .addOptions(new OptionData(OptionType.STRING, "stat", "Stat to modify", true)
                                            .addChoices(choiceArray))
                            .addOptions(new OptionData(OptionType.INTEGER, "amount", "New stat value", true)),
                    Commands.slash("delchar", "Delete your character permanently."))
                    .queue();
            System.out.println("Commands reg");
            // All slash commands must be added here. They follow a strict set of rules and are not as flexible as text commands.
            // Since we only need a simple command, we will only use a slash command without any arguments.
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        System.out.println("Command caught");
        if (event.getName().equals("hello")) { // Is the command name "hello"?
            event.reply("Hello " + event.getUser().getAsMention() + "!") // What will we reply with?
                    .setEphemeral(true) // Do we want the message hidden so only the user who ran the command can see it?
                    .queue(); // Queue the reply.
        }
        if (event.getName().equals("roll")) {
            Long userId = event.getUser().getIdLong();
            if(characters.get(userId) == null){
                event.reply("You must add a character first!").queue();
                return;
            }
            String action = event.getOption("action").getAsString();
            String charName = charNames.get(userId);
            Integer bonus = (characters.get(userId)).get(action);
            Integer roll = rand.nextInt(100) + 1;
            String rollmsg;
            if (event.getOption("modifier") == null){
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
            if(characters.get(userId) != null){
                event.reply("Character already exists. Please delete if you'd like to create a new character.").queue();
                return;
            }
            String charName = event.getOption("name").getAsString();
            Map<String, Integer> stats = new Hashtable<>();
            for (Command.Choice choice : choiceArray) {
                stats.put(choice.getName(), 0);
            }
            characters.put(userId, stats);
            charNames.put(userId, charName);
            System.out.println(characters.get(userId));
            System.out.println(charNames.get(userId));

            event.reply("Character " + charName + " added!").queue();
        }

        if (event.getName().equals("modstat")){
            Long userId = event.getUser().getIdLong();
            if(characters.get(userId) == null){
                event.reply("You must add a character first!").queue();
                return;
            }
            String stat = event.getOption("stat").getAsString();
            int oldStat = characters.get(userId).get(stat);
            int newStat = event.getOption("amount").getAsInt();
            characters.get(userId).put(stat, newStat);
            event.reply(stat + " changed from " + oldStat + " → " + newStat + ".").queue();
        }

        if (event.getName().equals("delchar")){
            Long userId = event.getUser().getIdLong();
            if(characters.get(userId) == null){
                event.reply("You do not currently have a character.").queue();
                return;
            }
            event.reply("Are you sure you want to delete " + charNames.get(userId) + "?")
                    .addActionRow(
                            Button.danger("ydelete", "Yes"),
                            Button.secondary("ndelete", "No"))
                    .queue();

        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if(event.getComponentId().equals("ydelete")){
            Long userId = event.getUser().getIdLong();
            characters.remove(userId);
            charNames.remove(userId);

            event.editMessage("Character has been deleted.").setActionRow(Button.danger("ydelete", "Yes").asDisabled(), Button.secondary("ndeles", "No").asDisabled())

                    .queue();
        }
        if(event.getComponentId().equals("ndelete")){
            event.editMessage("Character has not been deleted.").queue();
        }
    }

    private static final Command.Choice[] choiceArray = {
            new Command.Choice("ranged", "ranged"),
            new Command.Choice("melee", "melee"),
            new Command.Choice("CQC", "cqc"),
            new Command.Choice("dodge", "dodge"),
            new Command.Choice("block", "block"),
            new Command.Choice("throwables", "throwables"),
            new Command.Choice("perception", "perception"),
            new Command.Choice("bigbrain", "bigbrain"),
            new Command.Choice("speech", "speech"),
            new Command.Choice("stealth", "stealth")
    };

    protected Map<Long, Map<String, Integer>> characters = new Hashtable<>();
    protected Map<Long, String> charNames = new Hashtable<>();
    protected String[] npcs = {};
    protected Random rand = new Random();
}
