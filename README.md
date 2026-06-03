# Warung Wong Solo - POS Application

Proyek ini adalah aplikasi Point of Sales (POS) sederhana berbasis CLI (Command Line Interface) untuk mengelola antrian pesanan penjualan di Warung Makan "Wong Solo". Aplikasi ini dibangun menggunakan bahasa Java dengan mengimplementasikan struktur data **Linked List (Queue)** secara manual tanpa menggunakan Array untuk penyimpanan transaksinya.

## 📂 Struktur File dan Folder
- **Folder**: `final-Project-UAS-Sem2` merupakan folder utama (dan satu-satunya) yang menampung aplikasi ini.
- **File Utama**: `AppWarungWongSolo.java`. Seluruh logika, struktur data, dan antarmuka CLI berada dalam satu file ini agar struktur kodenya sangat simpel dan mudah untuk dijalankan (mirip dengan contoh referensi `appQueue.java`).

## 🚀 Fitur Utama
1. **Multi-User Level**: Terdapat 4 jenis pengguna (Pembeli Biasa, Member, Admin, dan Pemilik).
2. **Sistem Antrian (Queue)**: Transaksi diproses menggunakan prinsip FIFO (First In First Out) di mana pembeli pertama akan diproses terlebih dahulu.
3. **Diskon Member**: Pengguna berstatus Member mendapat potongan harga otomatis 5% dari total belanja.
4. **Laporan & Grafik**: Pemilik bisa melihat rangkuman harian, omset per barang, dan representasi grafik penjualan sederhana.

## 🔑 Data Login (Username & Password)
Untuk memudahkan demonstrasi, data login sudah disematkan (hardcoded) secara langsung di dalam kode:

- **MEMBER**
  - Akun 1 -> ID: `1`, Password: `1`
  - Akun 2 -> ID: `2`, Password: `2`
  - Akun 3 -> ID: `3`, Password: `3`
- **ADMIN**
  - Password: `admin`
- **PEMILIK**
  - Password: `pemilik`

## 🛠️ Cara Penggunaan (Compile & Run)
Buka terminal/CMD, arahkan ke folder project ini, lalu jalankan perintah berikut:
```bash
javac AppWarungWongSolo.java
java AppWarungWongSolo
```

## 🧩 Penjelasan Class & Alur Kode

Aplikasi ini sangat mengedepankan kesederhanaan dengan menggunakan sedikit kelas:

### 1. `class Barang`
Objek blueprint yang hanya menyimpan nama dan harga barang. Hanya ada 5 objek yang dideklarasikan secara statik di kelas utama (Ayam Bakar, Ayam Goreng, Nasi, Es Teh, Air Putih).

### 2. `class Transaksi`
Berfungsi sebagai **Node** dalam Linked List. Menyimpan:
- `kodeTransaksi`, `namaPembeli`, `barang`, `qty` (jumlah), `status` (0: Belum, 1: Diproses), dan tipe pembeli (`isMember`).
- `Transaksi next` (pointer ke transaksi berikutnya dalam antrian).

### 3. `class QueueTransaksi`
Struktur data **Linked List** khusus (Queue). Memiliki fungsi utama:
- `enqueue()`: Menambahkan order ke paling belakang antrian (Rear).
- `appendQueue()`: Menyatukan (menggabungkan) keranjang belanja lokal pembeli ke Antrian Total toko ketika pembeli melakukan *Checkout*.

### 4. `class AppWarungWongSolo` (Main Class)
Tempat berkumpulnya semua variabel global, inisialisasi barang, serta alur (flow) menu. Memiliki beberapa fungsi:
- `main()`: Loop utama aplikasi yang menampilkan pilihan Level Pengguna (Pembeli, Member, Admin, Pemilik).
- `menuBelanja()`: Fungsi untuk pembeli/member memilih barang. Menggunakan `QueueTransaksi` lokal, yang baru di-gabungkan ke antrian utama jika memilih **"Selesai (Checkout)"**.
- `menuAdmin()`: Membaca antrian dari yang paling depan (`front`) yang statusnya masih `0` (belum diproses), lalu mengubahnya menjadi `1` (diproses) serta mengakumulasi total uang ke variabel pendapatan.
- `menuPemilik()`: Mengakses semua variabel pendapatan statik, iterasi ke seluruh daftar pesanan untuk mencari total yang diproses dan belum diproses, serta mengubah harga pada objek `Barang`.
