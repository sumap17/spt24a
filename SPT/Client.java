import java.io.*;
import java.net.*;

// Program Client Chat Room Sederhana
// Dibuat untuk tugas Sistem Paralel dan Terdistribusi
// Materi: Komunikasi antar proses menggunakan socket
public class Client {
    private static final String IP_SERVER = "127.0.0.1"; // bisa diganti IP server lain
    private static final int PORT_SERVER = 4444;

    public static void main(String[] args) {
        try (Socket soket = new Socket(IP_SERVER, PORT_SERVER)) {
            System.out.println("===================================");
            System.out.println("✅ Terhubung ke server chat spt!");
            System.out.println("Ketik 'EXIT' untuk keluar dari chat");
            System.out.println("===================================");

            // Jalankan thread penerima pesan
            new Thread(new TerimaPesan(soket)).start();

            PrintWriter penulis = new PrintWriter(soket.getOutputStream(), true);
            BufferedReader inputUser = new BufferedReader(new InputStreamReader(System.in));

            String teks;
            while ((teks = inputUser.readLine()) != null) {
                penulis.println(teks);
                if (teks.equalsIgnoreCase("EXIT")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Gagal terhubung ke server: " + e.getMessage());
        }
    }
}

// Thread untuk menerima pesan dari server
class TerimaPesan implements Runnable {
    private Socket soket;

    public TerimaPesan(Socket soket) {
        this.soket = soket;
    }

    public void run() {
        try (BufferedReader pembaca = new BufferedReader(new InputStreamReader(soket.getInputStream()))) {
            String pesan;
            while ((pesan = pembaca.readLine()) != null) {
                System.out.println(pesan);
            }
        } catch (IOException e) {
            System.out.println("⚠ Terputus dari server: " + e.getMessage());
        }
    }
}