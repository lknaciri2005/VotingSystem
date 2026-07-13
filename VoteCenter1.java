package com.mycompany.votingsystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
/**
 *
 * @author DELL
 */
public class VoteCenter1 {

    int voters = 1000;
    int[] votes = new int[voters];


    AtomicInteger Party1Votes = new AtomicInteger(0);
    AtomicInteger Party2Votes = new AtomicInteger(0);
    AtomicInteger Party3Votes = new AtomicInteger(0);

    Random r = new Random();

    public void VoteGenerate() {
        for (int i = 0; i < voters; i++) {
            votes[i] = r.nextInt(3) + 1;
        }
    }

    public void VoteCount() throws InterruptedException {

        int half = voters / 2;

        Thread counter1 = new Thread(() -> {
            for (int i = 0; i < half; i++) {
                switch (votes[i]) {
                    case 1 -> Party1Votes.incrementAndGet();
                    case 2 -> Party2Votes.incrementAndGet();
                    case 3 -> Party3Votes.incrementAndGet();
                }
            }
        });

        Thread counter2 = new Thread(() -> {
            for (int i = half; i < voters; i++) {
                switch (votes[i]) {
                    case 1 -> Party1Votes.incrementAndGet();
                    case 2 -> Party2Votes.incrementAndGet();
                    case 3 -> Party3Votes.incrementAndGet();
                }
            }
        });


        counter1.start();
        counter2.start();


        counter1.join();
        counter2.join();

        System.out.println("Party 1 has " + Party1Votes + " Votes");
        System.out.println("Party 2 has " + Party2Votes + " Votes");
        System.out.println("Party 3 has " + Party3Votes + " Votes");
    }

    public static void main(String[] args) {
        VoteCenter1 VC = new VoteCenter1();
        VC.VoteGenerate();

        try {
            VC.VoteCount();
        } catch (InterruptedException e) {
            System.out.println("Counting was interrupted");
        }

        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }

        try {
            var socket  = new Socket(args[0], 59090);
            var in      = new Scanner(socket.getInputStream());
            System.out.println("Server response: " + in.nextLine());

            var out = new PrintWriter(socket.getOutputStream(), true);
            out.println(VC.Party1Votes.get());
            out.println(VC.Party2Votes.get());
            out.println(VC.Party3Votes.get());

        } catch (IOException ex) {
            System.getLogger(VoteCenter1.class.getName()).log(
                System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}