# GARAGE CLI

Si tratta di un semplice programma di esempio per la gestione di un'officina.

## PREREQUISITI

E' necessario che sia Java 8+ e maven siano installati sul sistema.

## COME COMPILARE ED ESEGUIRE

Clonare il codice sorgente:

```bash
$ git clone https://github.com/ziccardi/officina.git
```

Entrare nel folder 'officina' e digitare:
```bash
$ mvn clean package appassembler:assemble
```

Tale comando creera' due script direttamente eseguibili sia per windows che per unix.

Il programma e' direttamente eseguibile digitando:

```bash
./target/appassembler/bin/garage
```

Eseguendo il comando sopraindicato, si otterra' un errore, in quanto e' necessario specificare un file di configurazione contenente 
la configurazione delle piattaforme presenti nell'officina.
La struttura di tale file di configurazione (XML) e' molto semplice. Un esempio e' presente in [config.xml](./src/main/resources/config.xml).

Lanciare quindi il software con il comando:
```bash
./target/appassembler/bin/garage ./src/main/resources/config.xml 
```

Apparira' un menu' che permettera' di accedere a tutte le funzioni implementate:
```
1) List all the available platforms and operations
2) List the daily schedule for each platform
3) Schedule a new operation
4) Set an operation as completed
5) Print completed operations report
6) Exit

Please make your choice: 
```

E' possibile modificare il file config.xml per modificare le piattaforme presenti nell'officina.

# Un breve sguardo al codice

L'applicazione e' molto semplice ed e' composta dalle seguenti classi:
* Garage: la classe principale. Essa contiene una lista di 'piattaforme'
* Platform: si tratta della piattaforma vera e propria. Essa utilizza l'oggetto `Schedule` per organizzare la schedulazione delle operazioni
durante l'orario lavorativo (preconfigurato come 8-17, ma facilmente modificabile)/
* Schedule: mantiene una lista di oggetti `Workday`, ciascuno dei quali contiene la schedulazione delle operazioni per una singola giornata.
* Workday: mantiene la lista delle operazioni da effettuare in una singola giornata. Implementa l'interfaccia 'PropertyChangeListener' cosi' da poter 
ricevere notifiche dall'oggetto `Operation` quando un'operazione cambia stato (e' conclusa).
* Operation: rappresenta un'operazione eseguibile nell'officina. Notifica la lista dei listener registrati in caso di cambio stato.
* CompletedOperationStore: Mantiene la lista delle operazioni completate, registrando l'orario di fine operazioni. Anche questa classe si registra come
`listener` sulla classe `Operation` per riceverne la notifica di cambio stato.
* Config loader: una semplicissima classe che si occupa di leggere il file XML e configurare opportunamente la classe `Garage` aggiungendo in essa le 
piattaforme configurate.
* Main: si tratta della main class per la CLI.

# Limitazioni
* La gestione degli errori e' minimale. Non vengono in particolare effettuati controlli sulla correttezza del file XML di configurazione.
* Il sistema e' "case sensitive": quando si inserisce una nuova operazione occorre prestare attenzione a questo aspetto
* Non viene mantenuta una persistenza dei dati: riavviare l'applicazione corrisponde a ripartire da zero
* Quando si registra una nuova operazione, un codice viene assegnato a ciascuna operazione. Tale codice va di due in due anziche' di uno in uno 
in quanto il costruttore della classe `Operation` viene richiamato due volte, una volta per testare se l'operazione puo' essere aggiunta 
al giorno corrente e una volta per inserirla effettivamente. Trattandosi puramente di un esercizio ho ignorato tale issue.
* Il passare del tempo non e' considerato: ogni giornata (compresa oggi) inizia alle 8.00 e finisce alle 17.00. Se un'operazione viene 
marcata come 'completata', tutte le operaioni successive vengono 'shiftate' occupando l'orario non piu' occupato dall'operazione appena conclusa.
Cio' avviene pero' solo nel giorno corrente: le operazioni dei giorni successivi non sono impattate. 