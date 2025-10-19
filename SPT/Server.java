import java.io.*;
import java.net.*;
import java.util.*;

// Program Server Chat Room Sederhana
// Dibuat untuk tugas Sistem Paralel dan Terdistribusi
// Materi: Socket Programming & Multi-threading
public class Server {
    private static final int PORT = 4444;
    private static Set<HandlerClient> daftarClient = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        System.out.println("===================================");
        System.out.println("💬 SERVER CHAT SPT AKTIF DI PORT " + PORT);
        System.out.println("Menunggu client terhubung...");
        System.out.println("===================================");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket soketClient = serverSocket.accept();
                System.out.println("🔗 Client baru terhubung: " + soketClient.getInetAddress());

                HandlerClient handler = new HandlerClient(soketClient);
                daftarClient.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.out.println("⚠ Terjadi kesalahan pada server: " + e.getMessage());
        }
    }

    // Kirim pesan ke semua client (broadcast)
    static void kirimKeSemua(String pesan, HandlerClient pengirim) {
        synchronized (daftarClient) {
            for (HandlerClient client : daftarClient) {
                if (client != pengirim) {
                    client.kirimPesan(pesan);
                }
            }
        }
    }

    // Hapus client dari daftar jika keluar
    static void hapusClient(HandlerClient client) {
        daftarClient.remove(client);
        System.out.println("❎ " + client.getNamaUser() + " keluar dari chat.");
    }
}

// Kelas untuk menangani client
class HandlerClient implements Runnable {
    private Socket soket;
    private PrintWriter penulis;
    private String namaUser;

    public HandlerClient(Socket soket) {
        this.soket = soket;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void run() {
        try (
            BufferedReader pembaca = new BufferedReader(new InputStreamReader(soket.getInputStream()))
        ) {
            penulis = new PrintWriter(soket.getOutputStream(), true);

            penulis.println("Masukkan nama Anda: ");
            namaUser = pembaca.readLine();

            System.out.println("👤 " + namaUser + " telah bergabung.");
            Server.kirimKeSemua("🔔 " + namaUser + " bergabung ke chat.", this);

            String pesan;
            while ((pesan = pembaca.readLine()) != null) {
                if (pesan.equalsIgnoreCase("EXIT")) {
                    break;
                }
                System.out.println(namaUser + ": " + pesan);
                Server.kirimKeSemua(namaUser + ": " + pesan, this);
            }

            Server.kirimKeSemua("🚪 " + namaUser + " telah keluar.", this);
            Server.hapusClient(this);
            soket.close();

        } catch (IOException e) {
            System.out.println("⚠ Koneksi error dengan " + namaUser + ": " + e.getMessage());
        }
    }

    void kirimPesan(String pesan) {
        penulis.println(pesan);
    }
}