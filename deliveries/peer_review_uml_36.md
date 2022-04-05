



# Peer-Review 1: UML

Martina Gianola, Giovanni Gaggero, Leonardo Fiore

Gruppo 36

Valutazione del diagramma UML delle classi del gruppo 35.

## Lati positivi
- Sono stati implementati già alcune funzioni aggiuntive come i 4 giocatori e le 12 carte personaggio
- Buona idea la divisione della gestione del Board tra versione base e versione esperti
- Buona idea usare un enum con i nomi per distinguere le carte personaggio
- Ottima gestione per quanto riguarda l attivazione di un effetto della carta personaggio 

## Lati negativi
- Non ha molto senso decorare ogni volta il game per ogni carta character , poichè teoricamente solo 4 characters modificano i metodi del modello non istantaneamente, ma solo quando succede un evento.
Gli altri characters modificano il modello istantaneamente e non serve decorare ogni volta perché è inutile creare nuove istanze per l'override
- Suddivisione ancora imprecisa del modello , vista e controller.

## Confronto tra le architetture
Confrontando le due architetture la divisione tra BoardGame e GameModel è utile per la futura implementazione del controller, infatti dovremo implementarla anche noi nella nostra versione successiva. Analogo per la gestione della versione esperti dove viene sfruttato bene il principio di overload, noi invece abbiamo implementato il tutto in un'unica classe con un attributo booleano per definire se la versione è per esperti o meno. Già da subito hanno deciso quali funzionalità aggiuntive implementare, ovvero versione per 4 giocatori e implementazione delle 12 carte personaggio, aggiungendole già nello schema. Noi faremo questa scelta più avanti nel corso del progetto (quindi forse dovremo rifare il UML nel caso implementassimo delle funzionalità riguardanti le regole del gioco). 




