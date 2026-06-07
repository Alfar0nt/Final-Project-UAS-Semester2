# Struktur Dokumentasi Project

## Desain Kelas (Class Design)

Berikut merupakan kelas‑kelas utama yang ada dalam proyek **AppWarungWongSolo** beserta relasi dan kegunaannya.

| Kelas | Deskripsi | Relasi | Kegunaan Utama |
|------|-----------|--------|----------------|
| `Barang` | Representasi sebuah produk (nama & harga). | Tidak memiliki relasi ke kelas lain, namun objek `Barang` digunakan oleh `Transaksi`. | Menyimpan data produk yang dapat dibeli. |
| `Transaksi` | Menyimpan informasi satu baris pembelian (kode, pembeli, barang, qty, status, member flag). | Memiliki referensi ke `Barang` dan pointer `next` ke `Transaksi` berikutnya (membentuk linked‑list). | Menjadi elemen dalam antrian transaksi. |
| `QueueTransaksi` | Struktur antrian berbasis linked‑list untuk menampung objek `Transaksi`. | Menyimpan referensi `front` dan `rear` ke objek `Transaksi`. | Menangani penambahan (`enqueue`) dan penggabungan antrian (`appendQueue`). |
| `AppWarungWongSolo` | Kelas utama yang berisi `main` serta semua menu UI console. | Menggunakan semua kelas di atas (`Barang`, `Transaksi`, `QueueTransaksi`). | Mengatur alur aplikasi: login, belanja, admin, pemilik, laporan. |

### Relasi antar Kelas
- `AppWarungWongSolo` **membuat** instance statik `Barang` (ayamBakar, ayamGoreng, …) yang digunakan oleh `Transaksi`.
- Setiap kali pengguna menambah barang, sebuah objek `Transaksi` baru dibuat dan **dimasukkan** ke dalam `QueueTransaksi` melalui method `enqueue`.
- `QueueTransaksi` dapat **menggabungkan** antrian belanjaan sementara (`belanjaan`) ke antrian utama `transaksiTotal` menggunakan `appendQueue`.
- Pada proses admin, objek `Transaksi` dari `transaksiTotal` di‑iterasi untuk mengubah status, menghitung pendapatan, dan memperbarui total belanja member.

## Penjelasan Program (Program Explanation)

Berikut potongan‑potongan kode penting yang mewakili tiap submenu / proses dalam aplikasi.

### 1. `main` – Pilihan Level
```java
public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int level = 0;
    do {
        System.out.println("\n=== APP WARUNG WONG SOLO ===");
        System.out.println("1. Pembeli\n2. Member\n3. Admin\n4. Pemilik\n5. Keluar");
        System.out.print("Pilih Level = ");
        level = sc.nextInt();
        switch (level) {
            case 1: // Pembeli biasa
                System.out.print("Nama Pembeli: ");
                String nama = sc.next();
                menuBelanja(sc, nama, false, "");
                break;
            case 2: // Login member
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
```

### 2. `menuBelanja` – Proses Belanja Pembeli/Member
```java
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
        if (pilihan == 1) { // tambah barang
            // ... tampilkan daftar barang, pilih, input qty
            // buat Transaksi baru dan enqueue
            belanjaan.enqueue(new Transaksi(kode, nama, dipilih, q, isMember));
        } else if (pilihan == 2) { // hapus semua
            belanjaan.front = belanjaan.rear = null;
            System.out.println("Belanjaan Dihapus!");
        } else if (pilihan == 3) { // lihat daftar order
            int total = 0;
            for (Transaksi t = belanjaan.front; t != null; t = t.next) {
                int sub = t.qty * t.barang.harga;
                total += sub;
                System.out.println(t.barang.nama + " x" + t.qty + " = " + sub);
            }
            if (isMember) {
                int diskon = (int) (total * 0.05);
                System.out.println("Diskon 5%: -" + diskon);
                total -= diskon;
            }
            System.out.println("Total: Rp" + total);
        } else if (pilihan == 4) { // checkout
            transaksiTotal.appendQueue(belanjaan);
            System.out.println("Checkout Sukses dengan kode " + kode);
            break;
        } else if (pilihan == 5 && isMember) { // ubah password member
            System.out.print("Password Baru: ");
            String newPass = sc.next();
            if (idMember.equals(idM1)) passM1 = newPass;
            else if (idMember.equals(idM2)) passM2 = newPass;
            else if (idMember.equals(idM3)) passM3 = newPass;
            System.out.println("Password Diubah!");
        }
    } while (true);
}
```

### 3. `menuAdmin` – Manajemen Transaksi Admin
```java
static void menuAdmin(Scanner sc) {
    int pilihan = 0;
    do {
        System.out.println("\n-- Admin --");
        System.out.println("1. Tampil Belum Diproses\n2. Proses Transaksi\n3. Keluar");
        System.out.print("Pilih = ");
        pilihan = sc.nextInt();
        if (pilihan == 1) { // tampil transaksi belum diproses
            int jumlah = 0;
            for (Transaksi t = transaksiTotal.front; t != null; t = t.next) {
                if (t.status == 0) {
                    System.out.println("[" + t.kodeTransaksi + "] " + t.namaPembeli + " beli " + t.barang.nama + " x" + t.qty);
                    jumlah++;
                }
            }
            System.out.println("Jumlah Transaksi Belum Diproses: " + jumlah);
        } else if (pilihan == 2) { // proses satu transaksi
            Transaksi curr = transaksiTotal.front;
            while (curr != null && curr.status == 1) { // lewati yang sudah selesai
                curr = curr.next;
            }
            if (curr != null) {
                System.out.println("Transaksi: " + curr.namaPembeli + " beli " + curr.barang.nama);
                System.out.print("1 (Proses) / 2 (Exit) = ");
                if (sc.nextInt() == 1) {
                    curr.status = 1;
                    int bayar = curr.qty * curr.barang.harga;
                    if (curr.isMember) bayar -= (bayar * 0.05);
                    // update pendapatan per barang
                    if (curr.barang == ayamBakar) incomeAyamBakar += bayar;
                    else if (curr.barang == ayamGoreng) incomeAyamGoreng += bayar;
                    else if (curr.barang == nasi) incomeNasi += bayar;
                    else if (curr.barang == esTeh) incomeEsTeh += bayar;
                    else if (curr.barang == airPutih) incomeAirPutih += bayar;
                    // update total belanja member
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
```

### 4. `menuPemilik` – Laporan & Pengaturan Pemilik
```java
static void menuPemilik(Scanner sc) {
    int pilihan = 0;
    do {
        System.out.println("\n-- Pemilik --");
        System.out.println(
                "1. Total Order\n2. Ubah Harga\n3. Laporan Harian\n4. Belanja Member\n5. Grafik Penjualan\n6. Keluar");
        System.out.print("Pilih = ");
        pilihan = sc.nextInt();
        if (pilihan == 1) { // total order (sudah vs belum diproses)
            int sudah = 0, belum = 0;
            for (Transaksi t = transaksiTotal.front; t != null; t = t.next) {
                int sub = t.qty * t.barang.harga;
                if (t.isMember) sub -= (sub * 0.05);
                if (t.status == 1) sudah += sub; else belum += sub;
            }
            System.out.println("Sudah Diproses: " + sudah);
            System.out.println("Belum Diproses: " + belum);
        } else if (pilihan == 2) { // ubah harga barang
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
        } else if (pilihan == 3) { // laporan harian per barang
            System.out.println("Ayam Bakar : " + incomeAyamBakar);
            System.out.println("Ayam Goreng: " + incomeAyamGoreng);
            System.out.println("Nasi       : " + incomeNasi);
            System.out.println("Es Teh     : " + incomeEsTeh);
            System.out.println("Air Putih  : " + incomeAirPutih);
        } else if (pilihan == 4) { // total belanja tiap member
            System.out.println("Member " + idM1 + " : " + belanjaM1);
            System.out.println("Member " + idM2 + " : " + belanjaM2);
            System.out.println("Member " + idM3 + " : " + belanjaM3);
        } else if (pilihan == 5) { // visualisasi simple grafik
            cetakGrafik("Ayam Bakar", incomeAyamBakar);
            cetakGrafik("Ayam Goreng", incomeAyamGoreng);
            cetakGrafik("Nasi", incomeNasi);
            cetakGrafik("Es Teh", incomeEsTeh);
            cetakGrafik("Air Putih", incomeAirPutih);
        }
    } while (pilihan != 6);
}
```

### 5. `cetakGrafik` – Grafik Penjualan Sederhana
```java
static void cetakGrafik(String nama, int income) {
    String xStr = "";
    for (int i = 0; i < (income / 10000); i++) {
        xStr += "X";
    }
    System.out.println(nama + " : " + xStr + " " + income);
}
```

---

**Catatan**: Semua kode berada dalam satu file `AppWarungWongSolo.java` yang berada di direktori proyek. Struktur di atas dapat membantu memahami alur program serta hubungan antar‑kelas.
