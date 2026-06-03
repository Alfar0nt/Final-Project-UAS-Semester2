import java.util.Scanner;

class Barang {
    String nama;
    int harga;
    Barang(String n, int h) { nama = n; harga = h; }
}

class Transaksi {
    String kodeTransaksi;
    String namaPembeli;
    Barang barang;
    int qty;
    int status; 
    boolean isMember;
    Transaksi next;

    Transaksi(String kode, String nama, Barang b, int q, boolean member) {
        kodeTransaksi = kode;
        namaPembeli = nama;
        barang = b;
        qty = q;
        status = 0;
        isMember = member;
    }
}

class QueueTransaksi {
    Transaksi front, rear;

    void enqueue(Transaksi t) {
        if (rear == null) {
            front = rear = t;
        } else {
            rear.next = t;
            rear = t;
        }
    }

    void appendQueue(QueueTransaksi q) {
        if (q.front == null) return;
        if (rear == null) {
            front = q.front;
            rear = q.rear;
        } else {
            rear.next = q.front;
            rear = q.rear;
        }
    }
}

public class AppWarungWongSolo {
    
    // 5 Objek barang terpisah
    static Barang ayamBakar = new Barang("Ayam Bakar", 20000);
    static Barang ayamGoreng = new Barang("Ayam Goreng", 18000);
    static Barang nasi = new Barang("Nasi", 5000);
    static Barang esTeh = new Barang("Es Teh", 3000);
    static Barang airPutih = new Barang("Air Putih", 2000);

    // Hardcoded Member untuk kemudahan
    static String idM1 = "1", passM1 = "1";
    static String idM2 = "2", passM2 = "2";
    static String idM3 = "3", passM3 = "3";
    
    // Total Belanja Member
    static int belanjaM1 = 0;
    static int belanjaM2 = 0;
    static int belanjaM3 = 0;

    // Hardcoded Admin & Pemilik
    static String passAdmin = "admin";
    static String passPemilik = "pemilik";

    // Antrian Transaksi Total
    static QueueTransaksi transaksiTotal = new QueueTransaksi();
    static int nomorUrut = 1;

    // Variabel pendapatan harian tiap barang
    static int incomeAyamBakar = 0;
    static int incomeAyamGoreng = 0;
    static int incomeNasi = 0;
    static int incomeEsTeh = 0;
    static int incomeAirPutih = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int level = 0;

        do {
            System.out.println("\n=== APP WARUNG WONG SOLO ===");
            System.out.println("1. Pembeli\n2. Member\n3. Admin\n4. Pemilik\n5. Keluar");
            System.out.print("Pilih Level = ");
            level = sc.nextInt();
            
            switch (level) {
                case 1:
                    System.out.print("Nama Pembeli: ");
                    String nama = sc.next();
                    menuBelanja(sc, nama, false, "");
                    break;
                case 2:
                    System.out.print("ID Member: ");
                    String id = sc.next();
                    System.out.print("Password: ");
                    String pass = sc.next();
                    
                    if ((id.equals(idM1) && pass.equals(passM1)) || 
                        (id.equals(idM2) && pass.equals(passM2)) || 
                        (id.equals(idM3) && pass.equals(passM3))) {
                        menuBelanja(sc, id, true, id);
                    } else {
                        System.out.println("Login Member Gagal!");
                    }
                    break;
                case 3:
                    System.out.print("Password Admin: ");
                    if (sc.next().equals(passAdmin)) {
                        menuAdmin(sc);
                    } else {
                        System.out.println("Password Salah!");
                    }
                    break;
                case 4:
                    System.out.print("Password Pemilik: ");
                    if (sc.next().equals(passPemilik)) {
                        menuPemilik(sc);
                    } else {
                        System.out.println("Password Salah!");
                    }
                    break;
            }
        } while (level != 5);
    }
    
    static void menuBelanja(Scanner sc, String nama, boolean isMember, String idMember) {
        QueueTransaksi belanjaan = new QueueTransaksi();
        String kode = "12-01-" + nomorUrut++; 
        
        int pilihan = 0;
        do {
            System.out.println("\n-- Belanja (" + nama + ") --");
            System.out.println("1. Tambah\n2. Hapus\n3. Lihat\n4. Selesai");
            if (isMember) System.out.println("5. Ubah Password");
            System.out.print("Pilih = ");
            pilihan = sc.nextInt();
            
            if (pilihan == 1) {
                System.out.println("1." + ayamBakar.nama + " (Rp" + ayamBakar.harga + ")");
                System.out.println("2." + ayamGoreng.nama + " (Rp" + ayamGoreng.harga + ")");
                System.out.println("3." + nasi.nama + " (Rp" + nasi.harga + ")");
                System.out.println("4." + esTeh.nama + " (Rp" + esTeh.harga + ")");
                System.out.println("5." + airPutih.nama + " (Rp" + airPutih.harga + ")");
                System.out.print("Pilih Barang: ");
                int b = sc.nextInt();
                System.out.print("Jumlah: ");
                int q = sc.nextInt();
                
                Barang dipilih = null;
                if (b == 1) dipilih = ayamBakar;
                else if (b == 2) dipilih = ayamGoreng;
                else if (b == 3) dipilih = nasi;
                else if (b == 4) dipilih = esTeh;
                else if (b == 5) dipilih = airPutih;
                
                if (dipilih != null) {
                    belanjaan.enqueue(new Transaksi(kode, nama, dipilih, q, isMember));
                }
            } else if (pilihan == 2) {
                belanjaan.front = belanjaan.rear = null;
                System.out.println("Belanjaan Dihapus!");
            } else if (pilihan == 3) {
                System.out.println("= Daftar Order =");
                int total = 0;
                for (Transaksi t = belanjaan.front; t != null; t = t.next) {
                    int sub = t.qty * t.barang.harga;
                    total += sub;
                    System.out.println(t.barang.nama + " x" + t.qty + " = " + sub);
                }
                if (isMember) {
                    int diskon = (int)(total * 0.05);
                    System.out.println("Diskon 5%: -" + diskon);
                    total -= diskon;
                }
                System.out.println("Total: Rp" + total);
            } else if (pilihan == 4) {
                transaksiTotal.appendQueue(belanjaan);
                System.out.println("Checkout Sukses dengan kode " + kode);
                break;
            } else if (pilihan == 5 && isMember) {
                System.out.print("Password Baru: ");
                String newPass = sc.next();
                if (idMember.equals(idM1)) passM1 = newPass;
                else if (idMember.equals(idM2)) passM2 = newPass;
                else if (idMember.equals(idM3)) passM3 = newPass;
                System.out.println("Password Diubah!");
            }
        } while (true);
    }
    
    static void menuAdmin(Scanner sc) {
        int pilihan = 0;
        do {
            System.out.println("\n-- Admin --");
            System.out.println("1. Tampil Belum Diproses\n2. Proses Transaksi\n3. Keluar");
            System.out.print("Pilih = ");
            pilihan = sc.nextInt();
            
            if (pilihan == 1) {
                int jumlah = 0;
                for (Transaksi t = transaksiTotal.front; t != null; t = t.next) {
                    if (t.status == 0) {
                        System.out.println("[" + t.kodeTransaksi + "] " + t.namaPembeli + " beli " + t.barang.nama + " x" + t.qty);
                        jumlah++;
                    }
                }
                System.out.println("Jumlah Transaksi Belum Diproses: " + jumlah);
            } else if (pilihan == 2) {
                Transaksi curr = transaksiTotal.front;
                while (curr != null && curr.status == 1) {
                    curr = curr.next;
                }
                if (curr != null) {
                    System.out.println("Transaksi: " + curr.namaPembeli + " beli " + curr.barang.nama);
                    System.out.print("1 (Proses) / 2 (Exit) = ");
                    if (sc.nextInt() == 1) {
                        curr.status = 1;
                        int bayar = curr.qty * curr.barang.harga;
                        if (curr.isMember) bayar -= (bayar * 0.05);
                        
                        if (curr.barang == ayamBakar) incomeAyamBakar += bayar;
                        else if (curr.barang == ayamGoreng) incomeAyamGoreng += bayar;
                        else if (curr.barang == nasi) incomeNasi += bayar;
                        else if (curr.barang == esTeh) incomeEsTeh += bayar;
                        else if (curr.barang == airPutih) incomeAirPutih += bayar;
                        
                        if (curr.isMember) {
                            if (curr.namaPembeli.equals(idM1)) belanjaM1 += bayar;
                            else if (curr.namaPembeli.equals(idM2)) belanjaM2 += bayar;
                            else if (curr.namaPembeli.equals(idM3)) belanjaM3 += bayar;
                        }
                        System.out.println("Berhasil diproses!");
                    }
                } else {
                    System.out.println("Semua transaksi sudah diproses.");
                }
            }
        } while (pilihan != 3);
    }
    
    static void menuPemilik(Scanner sc) {
        int pilihan = 0;
        do {
            System.out.println("\n-- Pemilik --");
            System.out.println("1. Total Order\n2. Ubah Harga\n3. Laporan Harian\n4. Belanja Member\n5. Grafik Penjualan\n6. Keluar");
            System.out.print("Pilih = ");
            pilihan = sc.nextInt();
            
            if (pilihan == 1) {
                int sudah = 0, belum = 0;
                for (Transaksi t = transaksiTotal.front; t != null; t = t.next) {
                    int sub = t.qty * t.barang.harga;
                    if (t.isMember) sub -= (sub * 0.05);
                    if (t.status == 1) sudah += sub;
                    else belum += sub;
                }
                System.out.println("Sudah Diproses: " + sudah);
                System.out.println("Belum Diproses: " + belum);
            } else if (pilihan == 2) {
                System.out.println("1.AyamBakar 2.AyamGoreng 3.Nasi 4.EsTeh 5.AirPutih");
                System.out.print("Pilih = ");
                int b = sc.nextInt();
                System.out.print("Harga Baru = ");
                int h = sc.nextInt();
                
                if (b == 1) ayamBakar.harga = h;
                else if (b == 2) ayamGoreng.harga = h;
                else if (b == 3) nasi.harga = h;
                else if (b == 4) esTeh.harga = h;
                else if (b == 5) airPutih.harga = h;
            } else if (pilihan == 3) {
                System.out.println("Ayam Bakar : " + incomeAyamBakar);
                System.out.println("Ayam Goreng: " + incomeAyamGoreng);
                System.out.println("Nasi       : " + incomeNasi);
                System.out.println("Es Teh     : " + incomeEsTeh);
                System.out.println("Air Putih  : " + incomeAirPutih);
            } else if (pilihan == 4) {
                System.out.println("Member " + idM1 + " : " + belanjaM1);
                System.out.println("Member " + idM2 + " : " + belanjaM2);
                System.out.println("Member " + idM3 + " : " + belanjaM3);
            } else if (pilihan == 5) {
                cetakGrafik("Ayam Bakar", incomeAyamBakar);
                cetakGrafik("Ayam Goreng", incomeAyamGoreng);
                cetakGrafik("Nasi", incomeNasi);
                cetakGrafik("Es Teh", incomeEsTeh);
                cetakGrafik("Air Putih", incomeAirPutih);
            }
        } while (pilihan != 6);
    }
    
    static void cetakGrafik(String nama, int income) {
        String xStr = "";
        for (int i = 0; i < (income / 10000); i++) {
            xStr += "X";
        }
        System.out.println(nama + " : " + xStr + " " + income);
    }
}
