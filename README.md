# Couch-Duell

Party-Spiel für den Fernseher mit Handys als Controller – jeden Abend ein neues Programm.

Die ganze App steckt in einer einzigen Datei, `index.html`; dazu kommen nur zwei Bibliotheken in `vendor/` (PeerJS für die Verbindung, qrcode für den QR-Code). Live unter <https://nicoboehringer.de/couch-duell/>.

## So wird gespielt

| Aufruf | Gerät | Was passiert |
|---|---|---|
| `index.html` (ohne Parameter) | Fernseher | Führt das Spiel und zeigt QR-Code und Raumcode. OK auf der Fernbedienung bzw. Enter startet und geht weiter. |
| `index.html?r=CODE` (oder `#CODE`) | Handy | Wird zum Controller für einen Spieler. Ohne Code fragt die Seite danach. |
| `index.html?demo` | ein Bildschirm | Prototyp: Fernseher und vier simulierte Handys nebeneinander, Tasten 1–4 drücken für die Spieler. |

- Mit dem Handy den QR-Code am Fernseher scannen, Namen eingeben – 2 bis 4 Spieler. Wer zuerst beitritt, ist Gastgeber 👑 und startet den Abend.
- Fernseher und Handys verbinden sich direkt per WebRTC. Der öffentliche PeerJS-Dienst stellt nur den Kontakt her; einen eigenen Server braucht es nicht.
- Lädt ein Handy neu oder war der Bildschirm gesperrt, landet es wieder auf seinem Platz (Kennung pro Browser-Tab).
- Lässt sich PeerJS nicht laden, startet die Seite automatisch im Demo-Modus.

## Fire TV

`firetv/` enthält eine schlanke Android-App, die die Live-Seite im Vollbild lädt. Gebaut wird ohne Gradle mit `firetv/build.sh` (braucht Android SDK, build-tools 35 und platform 34); das APK landet in `firetv/build/`. Da die App nur die Seite lädt, kommen neue Versionen ohne neues APK an.

## Jeden Abend neu

- Das Abendprogramm (Auswahl und Reihenfolge der Spiele) wird täglich aus dem Datum gemischt.
- Jeder Wochentag hat ein Motto, das die Themen der KI-Fragen vorgibt.
- Serie („x Abende in Folge“) und Dauerwertung werden im Browser gespeichert.
- Eingebaute Fragen wiederholen sich erst, wenn alle anderen dran waren.

## 13 Spielarten

Buzzer-Quiz, Wo liegt was?, Reaktion, Was wird gemalt?, Hit-Quiz, Schätzen, Sortieren, Tipp-Rennen, Stoppuhr, Wisch-Duell, Merk-Meister, Perfekter Kreis, Farb-Falle.

## Optional

- KI-Fragen über einen eigenen Gemini-API-Key (wird nur lokal im Browser gespeichert).
- Hit-Quiz mit 30-Sekunden-Hörproben von Deezer, sonst eingebaute Melodien.
