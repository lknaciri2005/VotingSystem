
package com.mycompany.votingsystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Scanner;

/**
 *
 * @author DELL
 */
public class CentralAggro {
    public static void main(String[] args) {
        int party1 = 0;
        int party2 = 0;
        int party3 = 0;
        
        try (var listener = new ServerSocket(59090)) {
            System.out.println("The date server is running...");
            int loop = 0;
            while (loop < 5) {
                try (var socket = listener.accept()) {
                    var out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("hi");
                    
                    var in = new Scanner(socket.getInputStream());
                    
                    party1 += Integer.parseInt(in.nextLine());
                    party2 += Integer.parseInt(in.nextLine());
                    party3 += Integer.parseInt(in.nextLine());
                    System.out.println("Total Number of Votes From The First Party:" + party1);
                    System.out.println("Total Number of Votes From The Second Party:" + party2);
                    System.out.println("Total Number of Votes From The Third Party:" + party3);
                    loop = loop + 1;
                }    
            }
            
            int Winner = Math.max(party1, Math.max(party2, party3));
            
            String sfirst = "Annnd The winner of this election is .... ";
            String sWin = null;
            String sSecond = " !! With an outstanding ";
            
            if (party1 == Winner) {
                sWin = "The First Party";
            }
            if (party2 == Winner) {
                sWin = "The Second Party";
            }
            if (party3 == Winner) {
                sWin = "The Third Party";
            }
            
            System.out.println(sfirst + sWin + sSecond + Winner + " Votes , Congratulations !! ");
            
        } catch (IOException ex) {
            System.getLogger(CentralAggro.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
    

