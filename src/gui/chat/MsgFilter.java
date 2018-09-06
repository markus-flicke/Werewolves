package gui.chat;

import thinirc.MsgReceiver;
import thinirc.message.IrcCommand;
import thinirc.message.Message;

import static thinirc.message.IrcCommand.*;

public class MsgFilter implements MsgReceiver{
    WerewolfBot werewolfBot;

    public MsgFilter(WerewolfBot werewolfBot) {
        this.werewolfBot = werewolfBot;
    }

    public void accept(Message m){
        IrcCommand cmd  = m.getHeader().getIrcCommand();
        if(cmd.equals(RPL_NAMREPLY)
                || cmd.equals(JOIN)
                || cmd.equals(PART)
                || (cmd.equals(PRIVMSG) && m.getPayload().startsWith("!vote"))){
            switch (cmd){
                case PRIVMSG:
                    System.out.println(m);
                    String sender = m.getPayload().split(" !")[0];
                    String nominee = m.getPayload().replace("!vote ", "");
                    werewolfBot.votes.put(sender, nominee);
                case JOIN:
                    System.out.println(m);
                    String nick = m.getHeader().getRessource().split("!")[0];
                    String channel = m.getHeader().getParams();
                    synchronized (werewolfBot.mutex){
                        werewolfBot.playersInChannel.get(channel).add(nick);
                    }
//                  TODO: Implement PART & QUIT
            }
        }
    }
}
