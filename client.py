import socket
import random

HOST = '172.15.0.144'
PORT = 6000
options = ["rock\n", "paper\n", "scissors\n", "lizard\n", "spock\n"]
serverwins = 0
clientwins = 0
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

sock.connect((HOST, PORT))
ans = "Round xx"
while (True):
    messageclient = random.choice(options)
    sock.sendall(str.encode(messageclient))
    print("client: ", messageclient)
    winner = sock.recv(1024)
    whowins = winner.decode()
    if (whowins == "Server"):
        serverwins = serverwins + 1
    else:
        clientwins = clientwins + 1
    data = sock.recv(1024)
    outServer = data.decode()
    print(outServer)
    ans = outServer[0:8]
    if(ans == "Round 15"):
        break

    
sock.close()