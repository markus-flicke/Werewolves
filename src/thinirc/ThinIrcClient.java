package thinirc;

import thinirc.client.MyConnectionBuilder;
import thinirc.client.ThinIrcConnection;
import thinirc.exceptions.BuildException;

public class ThinIrcClient {

    public static void main(String ... args) {
        try {
            String host = args[0];
            int port = Integer.valueOf(args[1]);
            String trustStorePath = args[2];

            ThinIrcConnection ircConnection = new MyConnectionBuilder()
                    .setServer(host)
                    .setPort(port)
                    .setTrustStore(trustStorePath)
                    .setUser("Testuser")
                    .setNick("testu")
                    .setPassword("propra2018")
                    .setSSL(true)
                    .build();

            ircConnection.registerMsgRec(new MsgPrinter());

        } catch (BuildException e) {
            e.printStackTrace();
        }

    }

}
