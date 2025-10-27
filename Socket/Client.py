# -*- coding: utf-8 -*-
"""
Created on Mon Oct 27 09:36:12 2025

@author: sumap
"""

import socket

HOST = '127.0.0.1'   # IP server (localhost)
PORT = 65432         # Harus sama dengan server

# Buat socket
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect((HOST, PORT))

print(f"Terhubung ke server di {HOST}:{PORT}")

while True:
    message = input("Ketik pesan ('exit' untuk keluar): ")
    client_socket.sendall(message.encode('utf-8'))

    if message.lower() == 'exit':
        print("Koneksi ditutup.")
        break

    data = client_socket.recv(1024).decode('utf-8')
    print(f"Balasan dari server: {data}")

client_socket.close()