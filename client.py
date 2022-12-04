import socket
import random

HOST = '172.15.0.144'
PORT = 6000
options = ["rock\n", "paper\n", "scissors\n", "lizard\n", "spock\n"]
serverwins = 0
clientwins = 0
intwin = 0
clientplay = ""
serverplay = ""
winner = ""
histServer = []
histClient = []
result = []
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST, PORT))
rounds = 0
def stringtonumber(s):
    if(s == "rock"):
        return 0
    if(s== "paper"):
        return 1
    if( s== "scissors"):
        return 2
    if(s == "lizard"):
        return 3
    if(s == "spock"):
        return 4
    
def numbertostring(s):
    if(s == 0):
        return "rock\n"
    if(s== 1):
        return "paper\n"
    if( s== 2):
        return "scissors\n"
    if(s == 3):
        return "lizard\n"
    if(s == 4):
        return "spock\n"
    
    
while (True):
    #client always chooses between lizard if he is winning or the last play from the server if he is losing
    if(clientwins <= serverwins):
        clientplay = random.choice(options)
    else:
        clientplay = histServer[len(histServer) - 1]   
    sock.sendall(str.encode(clientplay))
    data = sock.recv(1024)
    outServer = data.decode()
    serverplay = numbertostring(int(outServer[0]))
    intwin = int(outServer[1])
    if(intwin == 1):
        winner = "Server"
    else:
        if(intwin == 2):
            winner = "Client"
        else:
            winner = "Draw"
    histClient.append(clientplay)
    histServer.append(serverplay)
    result.append(winner)
    
    if(intwin == 1):
        serverwins = serverwins + 1
        rounds = rounds + 1
        print("Round "  + str(rounds) + ": " + clientplay + " (Client) x " + serverplay + " (Server) =  Server wins, client loses\n")
    else:
        if(intwin == 2):
            clientwins = clientwins + 1
            rounds = rounds + 1
            print("Round "  + str(rounds) + ": " + clientplay + " (Client) x " + serverplay + " (Server) =  Client wins, server loses\n")
        else:
            rounds = rounds + 1
            print("Round "  + str(rounds) + ": " + clientplay + " (Client) x " + serverplay + " (Server) =  its a draw\n")
    if(rounds == 15):
        break
    
sock.close()

if(clientwins > serverwins):
    print("\n****The final winner is the client****\n")
else:
    if(serverwins > clientwins):
        print("\n****The final winner is the server****\n")
    else:
        print("\n****The final result is a draw****\n")