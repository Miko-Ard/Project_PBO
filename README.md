# 🎮 Mini Mario Bros - Game OOP Project

Sebuah game platformer 2D yang dikembangkan dengan **Java Swing** sebagai implementasi konsep **Object-Oriented Programming (OOP)** dalam mata kuliah PBO (Pemrograman Berorientasi Objek).

## 🎯 Fitur Utama

✅ **Character Animation** - Animasi idle, run, jump, fall, dan hit  
✅ **Enemy AI** - Musuh bergerak dan bisa dikalahkan  
✅ **Collision Detection** - Deteksi tabrakan real-time  
✅ **Score System** - Sistem poin dan kill counter  
✅ **Lives System** - Nyawa pemain dengan display heart  
✅ **Audio System** - Sound effect untuk jump dan enemy hit  
✅ **Floating Text Effect** - Notifikasi poin yang floating  
✅ **Game Over Screen** - UI game over dengan tombol restart  

---

## 💻 Requirements

- **Java 8** atau lebih tinggi
- **RAM minimal** 512 MB
- **Display** 1920x1020 (dapat disesuaikan)

### Dependencies
- Java Swing (built-in dengan Java)
- Java Sound API (built-in dengan Java)

**Tidak perlu install library eksternal!** ✨

---

## 📦 Instalasi & Setup

### 1. Clone Repository
```bash
git clone https://github.com/yourusername/mini-mario-bros.git
cd mini-mario-bros
```

### 2. Compile Source Code
```bash
cd src
javac -d ../out/production/TestAkhir *.java
cd ../out/production/TestAkhir
```

### 3. Run Game
```bash
java GameFrame
```

**atau** compile + run sekaligus:
```bash
cd src
javac -d ../out/production/TestAkhir *.java && cd ../out/production/TestAkhir && java GameFrame
```

---

## 🎮 Cara Bermain

### Controls
| Tombol | Aksi |
|--------|------|
| **← / A** | Bergerak kiri |
| **→ / D** | Bergerak kanan |
| **SPACE / W / ↑** | Melompat |
| **Mouse Click** (Game Over) | Restart game |

### Objective
1. **Lompati musuh (Mushroom)** dari atas untuk mengalahkannya
2. **Hindari sentuhan musuh** dari samping atau bawah (kehilangan 1 nyawa)
3. **Kumpulkan poin** setiap kali mengalahkan musuh
4. **Pertahankan 3 nyawa** agar tidak Game Over
5. **Musuh akan respawn** otomatis setelah 1 detik

---

## 🎲 Game Mechanics

### Player Mechanics
- **Health:** 3 nyawa (ditampilkan sebagai hati)
- **Movement:** Kecepatan konstan ke kiri/kanan
- **Jump:** Hanya bisa melompat saat di ground
- **Gravity:** Simulasi gravitasi realistis
- **Animation:** Animasi berubah sesuai state (idle, run, jump, fall, hit)

### Enemy Mechanics
- **Behavior:** Bergerak bolak-balik di area level
- **Collision:** Collision detection dengan player
- **Death:** Mati jika dihit dari atas → efek death animation
- **Respawn:** Muncul lagi setelah 1000ms (1 detik)
- **Scoring:** +100 poin setiap mengalahkan musuh

### Scoring System
- **Enemy Kill:** +100 poin
- **Kill Counter:** Track jumlah musuh yang dikalahkan
- **Game Over:** Final score ditampilkan di layar

---

## 📁 Struktur Project

```
mini-mario-bros/
│
├── src/
│   ├── GameFrame.java          # Main class & Game loop
│   ├── Player.java             # Karakter pemain
│   ├── Enemy.java              # NPC musuh
│   ├── Level.java              # Manajemen ground/level
│   ├── Score.java              # Sistem scoring
│   ├── AudioManager.java       # Manajemen audio
│   ├── FloatingText.java       # Text effect
│   │
│   └── assets/
│       ├── bg.png              # Background
│       ├── Ground.png          # Ground texture
│       ├── idle.png            # Player idle sprite
│       ├── run.png             # Player run sprite
│       ├── Jump (32x32).png    # Player jump sprite
│       ├── fall.png            # Player fall sprite
│       ├── hit.png             # Player hit sprite
│       ├── Mushroom-Idle.png   # Enemy idle sprite
│       ├── Mushroom-Run.png    # Enemy run sprite
│       ├── Mushroom-Die.png    # Enemy death sprite
│       ├── frame.png           # Game over frame
│       ├── button.png          # Restart button
│       ├── hearth.png          # Heart icon
│       ├── enemy-hit.wav       # Sound effect enemy
│       ├── jump-se.wav         # Sound effect jump
│       └── PixelifySans-Medium.ttf  # Game font
│
├── out/                        # Compiled output
├── .git/                       # Git repository
├── .gitignore
├── TestAkhir.iml               # IDE project file
└── README.md                   # File ini
```

---

## 🛠️ Teknologi yang Digunakan

### Language & Framework
- **Language:** Java
- **GUI Framework:** Java Swing
- **Audio:** Java Sound API

### Key Classes & Concepts
| Class | Purpose | OOP Concept |
|-------|---------|-------------|
| `GameFrame` | Game controller utama | Inheritance, Composition |
| `Player` | Character implementation | Encapsulation, Polymorphism |
| `Enemy` | NPC implementation | Inheritance, Polymorphism |
| `Level` | Environment management | Encapsulation |
| `Score` | Score tracking | Encapsulation, Singleton |
| `AudioManager` | Audio playback | Singleton Pattern |
| `FloatingText` | Visual effects | Composition |

### OOP Principles yang Diimplementasikan
✅ **Inheritance** - Class GameFrame extends JPanel  
✅ **Encapsulation** - Private attributes, public methods  
✅ **Polymorphism** - Implements ActionListener, KeyListener  
✅ **Composition** - GameFrame contains Player, Enemy, dll  
✅ **Singleton** - AudioManager pattern  
✅ **Interface** - Implementation dari multiple interfaces  

---

## 👨‍💻 Author & Kontribusi

**Developed by:** M.Miko Ardiansyah (2400018056)
                  M. Akbar Riyan H (2400018052)
                  ⁠Bintang Alfathir (2400018054)
                  
**Institution:** Universitas Ahmad Dahlan 
**Course:** Pemrograman Berorientasi Objek (PBO)  
**Year:** 2026  

### Contributors
- @Miko-Ard
- Mhmmdakbarrh

---

## 📄 License

Project ini dibuat untuk keperluan akademis.

---

## 🎓 Pembelajaran & Resources

### Konsep yang Dipelajari
- Event-driven programming (ActionListener, KeyListener)
- Game loop implementation
- Collision detection algorithm
- Sprite animation management
- Audio playback system
- UI state management
- Error handling & resource management

### Referensi Belajar
- [Java Swing Documentation](https://docs.oracle.com/javase/tutorial/uiswing/)
- [Game Development Patterns](https://gameprogrammingpatterns.com/)
- [Java Sound API](https://docs.oracle.com/javase/tutorial/sound/)

---

## 📝 Notes

- Game resolution: **1920 x 1020** pixels
- Frame rate: **50 FPS** (Timer 20ms)
- Build tools: **Java Compiler (javac)**

---

## 🚀 Future Enhancements

- [ ] Multiple levels dengan difficulty progression
- [ ] Power-ups system (shield, speed boost, etc)
- [ ] Leaderboard system
- [ ] Particle effects
- [ ] Different enemy types
- [ ] Boss fight level
- [ ] Mobile version

---

**Selamat bermain! 🎮**

