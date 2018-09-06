package gui.chat;

import game.DefaultGameBuilder;
import game.Game;
import game.InvalidPlayerCountException;
import game.io.WerewolfIOHandler;
import game.notification.GameNotification;
import game.notification.PhaseConcludedNotification;
import game.notification.PhasePreparedNotification;
import game.notification.VoteCompletedNotification;
import gui.chat.models.Channel;
import phases.Day;
import phases.Phase;
import phases.SeerNight;
import phases.WerewolfNight;
import roles.Player;
import roles.Seer;
import roles.Werewolf;
import thinirc.client.MyConnectionBuilder;
import thinirc.client.ThinIrcConnection;
import thinirc.exceptions.BuildException;
import thinirc.exceptions.InvalidChannelException;
import thinirc.message.IrcCommand;
import thinirc.message.Message;
import thinirc.message.MyMessage;
import thinirc.message.MyMessageHeader;

import java.io.IOException;
import java.sql.Time;
import java.util.*;

import static thinirc.client.IrcChannelMode.INVITE;
import static thinirc.client.IrcChannelMode.PRIVATE;
import static thinirc.client.IrcChannelMode.QUIET;

public class WerewolfBot implements WerewolfIOHandler {
    private String username, password, nick, server, prefix;
    private int port;
    private boolean ssl;
    ThinIrcConnection ircConnection;
    Map<String, List<String>> playersInChannel = new HashMap<>();
    Map<String, String> votes = new HashMap<>();
    final Object mutex = true;

    Map<String, Channel> channels = new HashMap<>();

    public WerewolfBot(String username, String password, String nick, String server, String prefix, int port, boolean ssl) {
        this.username = username;
        this.password = password;
        this.nick = nick;
        this.server = server;
        this.port = port;
        this.ssl = ssl;
        this.prefix = prefix;

        connect();

        joinChannel("Lobby");
        joinChannel("Day");
        joinChannel("Seer");
        joinChannel("Werewolf");
        synchronized (mutex){
            playersInChannel.put(prefix + "Lobby", new ArrayList<>());
            playersInChannel.put(prefix + "Day", new ArrayList<>());
            playersInChannel.put(prefix + "Seer", new ArrayList<>());
            playersInChannel.put(prefix + "Werewolf", new ArrayList<>());
        }

        ircConnection.addMode(prefix+"Seer", QUIET);
        ircConnection.addMode(prefix+"Seer", INVITE);
        ircConnection.addMode(prefix+"Seer", PRIVATE);

        ircConnection.addMode(prefix+"Werewolf", QUIET);
        ircConnection.addMode(prefix+"Werewolf", INVITE);
        ircConnection.addMode(prefix+"Werewolf", PRIVATE);

        ircConnection.addMode(prefix+"Day", INVITE);
        ircConnection.addMode(prefix+"Day", PRIVATE);

        ircConnection.registerMsgRec(new MsgFilter(this));
    }

    @Override
    public void handleNotification(GameNotification notification) {
        Phase p = notification.getPhase();
        if(notification instanceof PhasePreparedNotification){
            if(p instanceof SeerNight){
                initialiseVoting("Seer");
            }else if(p instanceof WerewolfNight){
                initialiseVoting("Werewolf");
            }else if(p instanceof Day){
                initialiseVoting("Day");
            }
        }else if(notification instanceof VoteCompletedNotification) {
            try {
                if (p instanceof SeerNight) {
                    ircConnection.sendTextToChannel(prefix + "Seer", notification.getMessage());
                } else if (p instanceof WerewolfNight) {
                    ircConnection.sendTextToChannel(prefix + "Werewolf", notification.getMessage());
                } else if (p instanceof Day) {
                    ircConnection.sendTextToChannel(prefix + "Day", notification.getMessage());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if(notification instanceof PhaseConcludedNotification){
            try {
                if (p instanceof SeerNight) {
                    ircConnection.sendTextToChannel(prefix + "Seer", notification.getMessage());
                    ircConnection.removeMode(prefix + "Seer", QUIET);
                } else if (p instanceof WerewolfNight) {
                    ircConnection.sendTextToChannel(prefix + "Werewolf", notification.getMessage());
                    ircConnection.removeMode(prefix + "Werewolf", QUIET);
                    String killedPlayer = ((PhaseConcludedNotification) notification).getPhaseResult().getMessage().split(".")[1].split(" ")[3];
                    ircConnection.kick("Day", killedPlayer);
                    ircConnection.kick("Werewolf", killedPlayer);
                    ircConnection.kick("Seer", killedPlayer);
                } else if (p instanceof Day) {
                    ircConnection.sendTextToChannel(prefix + "Day", notification.getMessage());
                    ircConnection.removeMode(prefix + "Day", QUIET);
                    String killedPlayer = ((PhaseConcludedNotification) notification).getPhaseResult().getMessage().split(".")[1].split(" ")[3];
                    ircConnection.kick("Day", killedPlayer);
                    ircConnection.kick("Werewolf", killedPlayer);
                    ircConnection.kick("Seer", killedPlayer);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void initialiseVoting(String channel){
        ircConnection.removeMode(prefix + channel, QUIET);
        votes = new HashMap<>();
        try {
            Thread.sleep(5000);
            ircConnection.sendTextToChannel(prefix + channel, "Voting begins! \nSyntax: !vote [name]");
            if(channel.equals("Day")){
                ircConnection.sendTextToChannel(prefix + channel, "30sec to vote");
                Thread.sleep(30*1000);
            }else{
                ircConnection.sendTextToChannel(prefix + channel, "30sec to vote");
                Thread.sleep(30*1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> gatherVotes() {
        return votes;
    }

    private void joinChannel(String channel){
        channels.put(prefix + channel, new Channel(prefix + channel));
        try{
            ircConnection.joinChannel(prefix + channel);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void connect(){
        MyConnectionBuilder myConnectionBuilder = new MyConnectionBuilder();
        myConnectionBuilder
                .setUser(username)
                .setPassword(password)
                .setPort(port)
                .setSSL(ssl)
                .setNick(nick)
                .setServer(server);

        try {
            ircConnection = myConnectionBuilder.build();
        } catch (BuildException e) {
            e.printStackTrace();
        }
    }

    public void waitUntilEnoughPlayers() {
        int players;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (mutex){
                players = playersInChannel.get(prefix + "Lobby").size();
            }
        }while (!(players > 5));
        try {
            ircConnection.sendTextToChannel(prefix + "Lobby", players + " players joined, commencing in 10sec");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidChannelException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            ircConnection.sendTextToChannel(prefix + "Lobby", players + " players joined in total. Starting game now");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidChannelException e) {
            e.printStackTrace();
        }
    }

    public void invitePlayers(Game game){
        List<Player> players = game.getPlayers();
        for(Player p: players){
            if(p instanceof Werewolf){
                try {
                    ircConnection.sendMessage(new MyMessage(new MyMessageHeader(IrcCommand.INVITE,p.getName()
                            + " " + prefix + "Werewolf"),""));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(p instanceof Seer){
                try {
                    ircConnection.sendMessage(new MyMessage(new MyMessageHeader(IrcCommand.INVITE,p.getName()
                            + " " + prefix + "Seer"),""));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                ircConnection.sendMessage(new MyMessage(new MyMessageHeader(IrcCommand.INVITE,p.getName()
                        + " " + prefix + "Day"),""));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args){
        WerewolfBot werewolfBot = new WerewolfBot("GameMaster"
                ,"propra2018"
                ,"GameMaster"
                ,"dbsvm.mathematik.uni-marburg.de"
                ,"#Flickem"
                ,7778
                , false);
        werewolfBot.waitUntilEnoughPlayers();
        try {
            Game game;
            synchronized (werewolfBot.mutex){
                game = new DefaultGameBuilder().buildGame(werewolfBot.playersInChannel.get(werewolfBot.prefix + "Lobby"), werewolfBot);
            }
            werewolfBot.invitePlayers(game);
            game.runGame();
        } catch (InvalidPlayerCountException e) {
            e.printStackTrace();
        }

    }
}
