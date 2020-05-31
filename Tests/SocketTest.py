import socket
import numpy as np
HOST = "192.168.1.1"
PORT = 8080

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST, PORT))

sock.sendall("Connected\r\n".encode())
data = sock.recv(1024).decode()

while True:
    data = sock.recv(1024).decode()
    print(data)