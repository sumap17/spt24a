# -*- coding: utf-8 -*-
"""
Created on Mon Oct 27 09:33:46 2025

@author: sumap
"""

import socket

HOST = '127.0.0.1'   # Alamat localhost
PORT = 65432         # Port (bebas asal sama di client)

# Buat socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((HOST, PORT))
server_socket.listen()

print(f"Server berjalan di {HOST}:{PORT}")
print("Menunggu koneksi dari client...")

conn, addr = server_socket.accept()
print(f"Terhubung dengan client: {addr}")

while True:
    data = conn.recv(1024).decode('utf-8')
    if not data or data.lower() == 'exit':
        print("Client memutuskan koneksi.")
        break

    print(f"Pesan dari client: {data}")
    reply = f"Server menerima: {data}"
    conn.sendall(reply.encode('utf-8'))

conn.close()
server_socket.close()
print("Server dihentikan.")