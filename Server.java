import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.*;
import java.lang.*;

public class Server {
	private static Integer PORT = 6000;
	private static LinkedList<String> options = new LinkedList<String>();
	private static Integer[][] comp = new Integer[5][5];
	private static LinkedList<String> histServer = new LinkedList<String>();
	private static LinkedList<String> histClient = new LinkedList<String>();
	private static LinkedList<String> result = new LinkedList<String>();
	
	public static void update_comp() {
		comp[0][0] = 0; comp[0][1] = -1; comp[0][2] = 1; comp[0][3] = 1; comp[0][4] = -1;
		comp[1][0] = 1; comp[1][1] = 0; comp[1][2] = -1; comp[1][3] = -1; comp[1][4] = 1;
		comp[2][0] = -1; comp[2][1] = 1; comp[2][2] = 0; comp[2][3] = 1; comp[2][4] = -1;
		comp[3][0] = -1; comp[3][1] = 1; comp[3][2] = -1; comp[3][3] = 0; comp[3][4] = 1;
		comp[4][0] = 1; comp[4][1] = -1; comp[4][2] = 1; comp[4][3] = -1; comp[4][4] = 0;
	}
	public static void update_options() {
		options.add("rock"); options.add("paper"); options.add("scissors");
		options.add("lizard"); options.add("spock");
	}
	public static int stringtonumber (String op) {
		if(op.equals("rock"))
			return 0;
		else if(op.equals("paper"))
			return 1;
		else if(op.equals("scissors"))
			return 2;
		else if(op.equals("lizard"))
			return 3;
		else if(op.equals("spock"))
			return 4;
		else 
			return 0;
	}
	public static String numbertostring (int op) {
		String ans = "";
		if(op == 0)
			ans = "rock";
		if(op == 1)
			ans = "paper";
		if(op == 2)
			ans = "scissors";
		if(op == 3)
			ans = "lizard";
		if(op == 4)
			ans = "spock";
		return ans;
	}
	
	public static void main(String args[]) throws Exception {
		update_comp();
		update_options();
		String inputClient;
		String inputServer;
		String resClient = "";
		int rounds = 0;
		int serverwins = 0;
		int clientwins = 0;
		Integer intClient = 0;
		Integer intServer = 0;
		
		//teste 
		ServerSocket welcomeSocket = new ServerSocket(Server.PORT);
		System.out.println("Waiting for connection...\n");
		Socket client = welcomeSocket.accept();
		if(client.isConnected()) {
			System.out.println("\nPlayer (" + (client.getLocalAddress().toString()).substring(1) + ":"
					+ client.getLocalPort() + ") connected\n");
		}
		while(!welcomeSocket.isClosed() & (rounds < 15)) {
			
			PrintWriter out = new PrintWriter(client.getOutputStream(),true);
			BufferedReader inClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			inputClient = inClient.readLine();
//			System.out.println(inputClient);
			intClient = stringtonumber(inputClient);
//			System.out.println(intClient);
			
			if(serverwins >= clientwins) {
				inputServer = "spock";
			}
			else {
				inputServer = options.poll(); //jogada do servidor
				options.add(inputServer);
			}
			intServer = stringtonumber(inputServer);
//			System.out.println(intServer);
			
			if(comp[intServer][intClient] > 0) {
				rounds = rounds + 1;
				resClient = intServer.toString() + "1";
				//resClient = "Round " + (rounds) + ": " + inputClient + " (Client) x " + inputServer + " (Server) =  Client loses, server wins\n";
				System.out.println("This round:" + inputClient + " (Client) x " + inputServer + " (Server) = Client loses, server wins\n");
				serverwins = serverwins + 1;
				histServer.add(inputServer);
				histClient.add(inputClient);
				result.add("Server");
			}
			else {
				if(comp[intServer][intClient] < 0) {
					rounds = rounds + 1;
					resClient = intServer.toString() + "2";
					//resClient = "Round " + rounds + ": "+ inputClient + " (Client) x " + inputServer + " (Server) = Client wins, server loses\n";
					System.out.println("This round:" + inputClient + " (Client) x " + inputServer + " (Server) =  Client wins, server loses\n");
					clientwins = clientwins + 1;
					histServer.add(inputServer);
					histClient.add(inputClient);
					result.add("Client");
				}
				else {
					rounds = rounds + 1;
					//resClient = inputClient + " (Client) x " + inputServer + " (Server) = its a draw\n";
					resClient = intServer.toString() + "3";
					System.out.println("This round:" + inputClient + " (Client) x " + inputServer + " (Server) = its a draw\n");
					histServer.add(inputServer);
					histClient.add(inputClient);
					result.add("Draw");
					//rounds = rounds + 1;
				}
			}
			out.println(resClient);
			//lets print the rounds history
			System.out.println("History: \nServer wins: " + serverwins + "\nClient wins: " + clientwins);
			for(int i = 0; i < result.size(); i++) {
				if(result.get(i).equals("Draw")) {
					System.out.println("Round " + (i+1) + ": " + histClient.get(i) + " (Client) x " + histServer.get(i) + " (Server) = " + result.get(i) + "\n");
				}
				else 
					System.out.println("Round " + (i+1) + ": " + histClient.get(i) + " (Client) x " + histServer.get(i) + " (Server) = " + result.get(i) + " wins\n");
			}
			System.out.println("\n");
			if(rounds == 15)
				welcomeSocket.close();
		}
		if(serverwins > clientwins) {
			String end = "\n****The final winner is the server****\n";
			System.out.println(end);
		}
		else {
			if(clientwins > serverwins) {
				String end = "\n****The final winner is the client****\n";
				System.out.println(end);
			}
			else {
				String end = "\n****The final result is a draw****\n";
				System.out.println(end);
			}
		}
		welcomeSocket.close();

	}
}