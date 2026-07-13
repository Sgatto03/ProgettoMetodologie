# ⚔️ Arena of Glory — Gladiator RPG

Un gioco di ruolo in Java incentrato sulla carriera di un gladiatore che combatte in diverse arene dell'antica Roma per ottenere fama e ricchezza.

**Progetto MDP & MGC — A.A. 2025/26**
**Matricola**: 123297
**Package**: `it.unicam.cs.mpgc.rpg123297`

---

## 🎮 Funzionalità

- **Creazione del personaggio**: scegli tra 4 classi di gladiatore (Murmillo, Retiarius, Thraex, Secutor), ognuna con statistiche e abilità speciali uniche
- **Combattimento a turni**: sistema strategico con attacco, difesa, abilità speciale e uso di consumabili
- **5 arene progressive**: dalla Fossa dei Reietti all'Arena degli Dei, sbloccabili con la fama
- **Sistema di equipaggiamento**: armi, armature e accessori con 5 livelli di rarità
- **Negozio**: compra e vendi oggetti con prezzi influenzati dal Carisma del gladiatore
- **Progressione**: livelli, esperienza, fama e ranghi (da Novizio a Dio dell'Arena)
- **IA nemica**: 3 strategie comportamentali (Aggressiva, Difensiva, Bilanciata)
- **Persistenza**: salva e carica la partita in formato JSON
- **GUI JavaFX**: interfaccia grafica completa con tema romano

---

## 🚀 Come Eseguire

### Prerequisiti
- **Java 21** (o superiore)
- **Gradle** (il wrapper è incluso nel progetto)

### Compilazione e Avvio

```bash
# Clona il repository
git clone <url-repo>
cd ProgettoMetodologie

# Compila e avvia
./gradlew run
```

Su Windows:
```cmd
gradlew.bat run
```

### Build
```bash
./gradlew build
```

---

## 🏗️ Architettura

Il progetto segue un'architettura **MVC** (Model-View-Controller) con separazione netta tra i tre livelli. Il model è completamente indipendente da JavaFX, permettendo futuri port su Android o Web.

### Struttura dei Package

```
it.unicam.cs.mpgc.rpg123297/
├── model/           — Logica di dominio (38 classi)
│   ├── character/   — Personaggi: Gladiator, Enemy, CharacterStats, EnemyFactory
│   ├── combat/      — Combattimento: CombatEngine, CombatAction, AI strategies
│   ├── item/        — Oggetti: Weapon, Armor, Consumable, Equipment, Inventory
│   ├── arena/       — Arene: Arena, ArenaTier, ArenaChallenge, ArenaManager
│   ├── progression/ — Progressione: ProgressionManager, FameRank
│   └── shop/        — Negozio: Shop (interface), ArenaShop, Transaction
├── controller/      — Controller (3 classi): GameController, CombatController, ShopController
├── view/            — Viste (14 classi): GameView, ViewFactory + schermate JavaFX
├── event/           — Sistema eventi (3 classi): GameEventBus, GameEvent, EventType
├── persistence/     — Persistenza (4 classi): GameRepository, JsonGameRepository, SaveData, GameDataMapper
└── Main.java        — Entry-point
```

### Design Patterns Utilizzati

| Pattern | Applicazione | File principali |
|---------|-------------|-----------------|
| **Strategy** | Azioni di combattimento e IA nemica | `CombatAction`, `EnemyAI` |
| **Factory** | Creazione nemici e oggetti | `EnemyFactory`, `ItemFactory` |
| **Observer** | Sistema eventi model↔view | `GameEventBus` |
| **Template Method** | Flusso combattimento | `CombatEngine` |
| **Repository** | Astrazione persistenza | `GameRepository` → `JsonGameRepository` |
| **Abstract Factory** | Creazione viste multi-piattaforma | `ViewFactory` → `FxViewFactory` |
| **Facade** | Orchestrazione controller | `GameController` |

### Principi SOLID

| Principio | Come è applicato |
|-----------|-----------------|
| **SRP** | Ogni classe ha una singola responsabilità (es. `CombatEngine` gestisce solo il flusso di combattimento) |
| **OCP** | Nuove azioni, strategie IA, oggetti e viste si aggiungono implementando interfacce, senza modificare codice esistente |
| **LSP** | `Gladiator` e `Enemy` sono intercambiabili tramite `GameCharacter` |
| **ISP** | Interfacce piccole e specifiche: `Item`, `Shop`, `CombatAction`, `EnemyAI`, `GameView` |
| **DIP** | I controller dipendono da astrazioni (`GameRepository`, `ViewFactory`), non da implementazioni concrete |

---

## ⚔️ Sistema di Combattimento

### Punti Ferita (HP)
- **Formula HP massimi**: `HP = 50 + (Resistenza × 10)`
- **Danno effettivo**: `max(1, danno_attacco - difesa_bersaglio)`
- **Difesa attiva**: dimezza il danno ricevuto nel turno
- **Reset automatico**: gli HP vengono ripristinati al massimo prima di ogni combattimento

### Azioni disponibili
1. **Attacco**: danno base + bonus arma, con probabilità di critico (×2 danno)
2. **Difesa**: dimezza il danno ricevuto, senza infliggere danno
3. **Abilità speciale**: unica per classe, con cooldown di 3 turni
4. **Usa oggetto**: consuma un consumabile dall'inventario

### Esito del combattimento
- **Vittoria**: il gladiatore riceve oro, XP e fama; il nemico è sconfitto
- **Sconfitta**: gli HP del gladiatore arrivano a 0 → Game Over (la partita termina)
- **Iniziativa**: determinata dall'Agilità — chi ha più Agilità attacca per primo

---

## 🛡️ Classi di Gladiatore

| Classe | STR | AGI | END | DEX | CHA | Abilità Speciale |
|--------|-----|-----|-----|-----|-----|-----------------|
| **Murmillo** | 8 | 4 | 7 | 4 | 2 | Colpo di Scudo — danno + stordimento nemico |
| **Retiarius** | 4 | 8 | 3 | 7 | 3 | Trappola di Rete — riduce l'agilità nemica |
| **Thraex** | 6 | 6 | 5 | 5 | 3 | Frenesia — doppio attacco |
| **Secutor** | 6 | 3 | 9 | 3 | 4 | Fortezza — blocca danni + cura 15% HP |

---

## 🏟️ Arene

| Arena | Tier | Fama richiesta | Descrizione |
|-------|------|---------------|-------------|
| Fossa dei Reietti | ⭐ | 0 | Combattimenti tra disperati |
| Arena di Provincia | ⭐⭐ | 100 | Allenamento giovani gladiatori |
| Arena Imperiale | ⭐⭐⭐ | 300 | Scontri epici nella capitale |
| Il Colosseo | ⭐⭐⭐⭐ | 600 | Davanti all'Imperatore |
| Arena degli Dei | ⭐⭐⭐⭐⭐ | 1000 | Solo i più grandi |

---

## 📦 Estensibilità

L'architettura è progettata per supportare future estensioni senza modificare il codice esistente:

| Estensione | Come implementare |
|-----------|-------------------|
| Nuove piattaforme (Android, Web) | Implementare `ViewFactory` |
| Nuovi backend (SQLite, Cloud) | Implementare `GameRepository` |
| Nuove classi gladiatore | Aggiungere valori a `GladiatorClass` |
| Nuove arene | Aggiungere valori a `ArenaTier` |
| Nuove azioni di combattimento | Implementare `CombatAction` |
| Nuove strategie IA | Implementare `EnemyAI` |
| Nuovi tipi di oggetto | Estendere `AbstractItem` |

---

---

## 📝 Dichiarazione sull'uso di strumenti AI

Nella realizzazione di questo progetto sono stati utilizzati strumenti di intelligenza artificiale (GitHub Copilot / Gemini) 
- Come supporto alla scrittura del codice atraverso esempi che poi sono stati adattati e implementati dallo studente.
- Come supporto alla documentazione revisionata e corretta dallo studente.
- Per la realizazione del comportamento dei nemici in base all'arena di apparteneza.

Tutto il codice è stato revisionato e validato dallo studente.
