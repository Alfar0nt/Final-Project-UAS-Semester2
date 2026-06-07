# Dokumentasi Struktur Program

Proyek *AppWarungWongSolo* merupakan aplikasi konsol sederhana yang mensimulasikan sistem point‑of‑sale untuk sebuah warung. Program terdiri dari empat kelas utama: **Barang**, **Transaksi**, **QueueTransaksi**, dan **AppWarungWongSolo**. Kelas **Barang** berfungsi sebagai model data untuk setiap produk yang dijual, menyimpan nama dan harga. Kelas **Transaksi** merepresentasikan satu baris pembelian; ia menyimpan kode transaksi, nama pembeli, referensi ke objek **Barang**, jumlah barang (qty), status proses, serta flag apakah pembeli merupakan member. Setiap objek **Transaksi** juga memiliki pointer `next` yang memungkinkan pembentukan linked‑list. Kelas **QueueTransaksi** mengimplementasikan antrian berbasis linked‑list untuk menampung objek **Transaksi**. Ia menyediakan metode `enqueue` untuk menambah transaksi ke akhir antrian dan `appendQueue` untuk menggabungkan dua antrian. Kelas **AppWarungWongSolo** berperan sebagai kelas utama yang menginisialisasi objek‑objek **Barang** secara statik, menyimpan kredensial admin, pemilik, serta password member. Kelas ini juga memuat struktur data global seperti `transaksiTotal`, `nomorUrut`, dan akumulator pendapatan per produk. Semua fungsi menu (belanja, admin, pemilik) berada di dalam kelas ini dan menggunakan objek‑objek yang telah dideklarasikan.

Program dimulai pada metode `main`, dimana pengguna diminta memilih level akses: Pembeli, Member, Admin, atau Pemilik. Untuk pembeli dan member, program memanggil `menuBelanja`. Pada menu ini pengguna dapat menambah barang ke keranjang (dengan membuat objek **Transaksi** baru dan menambahkannya ke antrian sementara), menghapus seluruh keranjang, menampilkan daftar order beserta total harga (dengan diskon 5 % bagi member), atau melakukan checkout yang menggabungkan antrian sementara ke antrian utama `transaksiTotal`. Member yang berhasil login juga dapat mengubah password melalui pilihan kelima.

Bagian **Admin** diakses melalui `menuAdmin`. Admin dapat menampilkan semua transaksi yang belum diproses, memproses satu transaksi pada satu waktu, serta memperbarui pendapatan per jenis barang dan total belanja masing‑masing member. Proses transaksi mengubah status, menghitung pembayaran (dengan potongan 5 % bila member), dan menambah nilai ke variabel akumulator seperti `incomeAyamBakar` atau `belanjaM1`.

Bagian **Pemilik** diwakili oleh `menuPemilik`. Pemilik dapat melihat ringkasan total order yang sudah dan belum diproses, mengubah harga masing‑masing barang, menampilkan laporan harian per barang, menampilkan total belanja per member, serta menampilkan grafik penjualan sederhana. Grafik dibuat oleh metode `cetakGrafik`, yang menghasilkan baris teks berisi huruf ‘X’ berulang sesuai skala pendapatan (setiap 10 000 rupiah) dan menampilkan nilai numerik di sampingnya.

Secara keseluruhan, alur program mengikuti pola berikut: pengguna memilih level, melakukan operasi pada antrian transaksi sesuai peran, kemudian data transaksi diproses oleh admin atau dilaporkan oleh pemilik. Semua kelas saling berinteraksi melalui referensi objek **Barang** dan **Transaksi**, sementara **QueueTransaksi** menyediakan mekanisme antrian yang memudahkan penambahan, penggabungan, dan iterasi transaksi.

*Catatan*: Seluruh kode berada dalam satu file **AppWarungWongSolo.java** di direktori proyek.

Dokumen ini menjelaskan secara singkat dan formal tentang desain kelas serta alur program pada proyek **AppWarungWongSolo**.

**Desain Kelas**

Proyek ini terdiri dari empat kelas utama. Kelas `Barang` merepresentasikan sebuah produk dengan atribut `nama` dan `harga`; objek‑objek `Barang` ini tidak memiliki relasi ke kelas lain tetapi dipakai oleh kelas `Transaksi` untuk menyimpan informasi barang yang dibeli. Kelas `Transaksi` menyimpan satu baris pembelian, mencakup kode transaksi, nama pembeli, referensi ke objek `Barang`, jumlah (`qty`), status proses, serta flag apakah pembeli merupakan member; selain itu setiap `Transaksi` memiliki pointer `next` yang membentuk linked‑list. Kelas `QueueTransaksi` merupakan struktur antrian berbasis linked‑list yang menyimpan referensi `front` dan `rear` ke objek `Transaksi`; ia menyediakan metode `enqueue` untuk menambahkan transaksi baru dan `appendQueue` untuk menggabungkan dua antrian. Terakhir, kelas `AppWarungWongSolo` adalah kelas utama yang berisi metode `main` serta semua menu antarmuka console (pembeli, member, admin, dan pemilik). Kelas ini membuat instance statik dari semua objek `Barang`, menyimpan kredensial member, admin, dan pemilik, serta menghitung pendapatan per barang dan total belanja member.

**Relasi antar Kelas**

`AppWarungWongSolo` membuat objek `Barang` statik yang kemudian digunakan oleh objek `Transaksi` ketika seorang pembeli menambah barang ke keranjang. Setiap kali barang ditambahkan, sebuah objek `Transaksi` baru dibuat dan dimasukkan ke dalam antrian `QueueTransaksi` melalui metode `enqueue`. Antrian sementara yang disebut `belanjaan` dapat digabungkan ke antrian utama `transaksiTotal` dengan memanggil `appendQueue`. Pada proses admin, objek‑objek `Transaksi` dalam `transaksiTotal` di‑iterasi untuk memperbarui status, menghitung pembayaran, dan menambah pendapatan per barang serta total belanja tiap member.

**Penjelasan Program**

Metode `main` menampilkan menu level (Pembeli, Member, Admin, Pemilik, Keluar) dan memanggil submenu yang sesuai. Pada level 1 (Pembeli) program meminta nama pembeli dan masuk ke `menuBelanja`. Pada level 2 (Member) pengguna memasukkan ID dan password; bila cocok, program memanggil `menuBelanja` dengan flag member aktif. Level 3 meminta password admin dan menjalankan `menuAdmin`, sedangkan level 4 meminta password pemilik dan menjalankan `menuPemilik`.

`menuBelanja` membuat antrian transaksi sementara, menghasilkan kode transaksi unik, dan menampilkan sub‑menu yang memungkinkan penambahan barang, penghapusan seluruh keranjang, melihat daftar order, checkout, serta (untuk member) mengubah password. Penambahan barang menghasilkan objek `Transaksi` yang langsung di‑enqueue ke antrian sementara. Pada checkout, antrian sementara digabungkan ke antrian utama `transaksiTotal` dan proses kembali ke menu utama.

`menuAdmin` menyediakan dua pilihan: menampilkan semua transaksi yang belum diproses, serta memproses satu transaksi yang belum selesai. Saat memproses, status transaksi diubah menjadi selesai, pembayaran dihitung (dengan diskon 5 % untuk member), dan pendapatan per barang serta total belanja tiap member diperbarui.

`menuPemilik` menawarkan lima fungsi: menampilkan total order (yang sudah dan belum diproses), mengubah harga barang, menampilkan laporan harian pendapatan per barang, menampilkan total belanja tiap member, dan menampilkan grafik penjualan sederhana. Grafik dibuat dengan memanggil metode `cetakGrafik` untuk setiap jenis barang.

Metode `cetakGrafik` menerima nama barang dan nilai pendapatan, lalu membangun string berisi karakter `X` sebanyak `income/10000` dan mencetaknya bersama nama barang serta nilai pendapatan. Grafik ini memberikan visualisasi sederhana berupa bar horizontal.

**Catatan**

Seluruh kode program berada dalam satu berkas sumber **AppWarungWongSolo.java** yang terletak di direktori proyek. Dokumen ini memberikan gambaran umum tentang struktur kelas dan alur program sehingga memudahkan pemahaman serta penulisan laporan tugas.
