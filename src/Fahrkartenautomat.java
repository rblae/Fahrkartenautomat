import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

class Fahrkartenautomat {
    public record Muenze(int wert, String einheit) {
    }

    public static void begruessen() {
        LocalDate heute = LocalDate.now();
        DateTimeFormatter df;

        df = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        System.out.println("Herzlich willkommen\t\t\t" + heute.format(df) + "\n\n");
    }

    public static double fahrkartenbestellungErfassen(HashMap<String, Double> fahrkarten) {
        Scanner tastatur = new Scanner(System.in);
        int wahl = 0;
        double zuZahlenderBetrag = 0;
        int anzahl = 0;
        double ticketpreis = 0;

        fahrkarten.put("Einzelfahrschein Berlin AB", 2.9);
        fahrkarten.put("Einzelfahrschein Berlin BC", 3.3);
        fahrkarten.put("Einzelfahrschein Berlin ABC", 3.6);
        fahrkarten.put("Kurzstrecke", 1.9);
        fahrkarten.put("Tageskarte Berlin AB", 8.6);
        fahrkarten.put("Tageskarte Berlin BC", 9.0);
        fahrkarten.put("Tageskarte Berlin ABC", 9.6);
        fahrkarten.put("Kleingruppen-Tageskarte Berlin AB", 23.5);
        fahrkarten.put("Kleingruppen-Tageskarte Berlin BC", 24.3);
        fahrkarten.put("Kleingruppen-Tageskarte Berlin ABC", 24.9);


        while (true) {
            System.out.println("Waehlen Sie ihre Wunschfahrkarte fuer Berlin AB aus:");
            int i = 1;
            for (Map.Entry<String, Double> fahrkarte : fahrkarten.entrySet()) {
                System.out.printf("%2d: %-36s[%4.2f EURO]\n", i++, fahrkarte.getKey(), fahrkarte.getValue());
            }
            if (zuZahlenderBetrag > 0) {
                System.out.println("11: Bezahlen\n");
            } else System.out.println("11: Beenden\n");
            while (wahl < 1 || wahl > 11) {
                System.out.print("Ihre Wahl: ");
                wahl = tastatur.nextInt();
                if (wahl < 1 || wahl > 11) {
                    System.out.println("\n>>falsche Eingabe<<");
                }
            }

            if (wahl == 11) {
                return zuZahlenderBetrag;
            }

            int k = 1;
            for (Double value : fahrkarten.values()) {
                if (k++ == wahl) {
                    ticketpreis = value;
                    break;
                }
            }

            while (anzahl < 1 || anzahl > 10) {
                System.out.print("\nAnzahl der Tickets (1-10): ");
                anzahl = tastatur.nextInt();
                if (anzahl < 1 || anzahl > 10) {
                    System.out.println(">> Waehlen Sie bitte eine Anzahl von 1 bis 10 Tickets aus.");
                }
            }
            zuZahlenderBetrag += ticketpreis * anzahl;
            System.out.printf("\nZwischensumme: %4.2f EURO\n\n", zuZahlenderBetrag);
            warte(2000);
            anzahl = 0;
            wahl = 0;
        }
    }

    public static double fahrkartenBezahlen(double zuZahlenderBetrag) {
        // Geldeinwurf
        // -----------
        Scanner tastatur = new Scanner(System.in);
        double eingezahlterGesamtbetrag;
        double eingeworfeneMuenze = 0.0;
        double[] gueltigeMuenzen = {0.05, 0.1, 0.2, 0.5, 1.0, 2.0, 5.0, 10.0, 20.0};
        boolean ungueltig = true;

        eingezahlterGesamtbetrag = 0.0;
        while (eingezahlterGesamtbetrag < zuZahlenderBetrag) {
            System.out.format("Noch zu zahlen: %4.2f EURO\n", (zuZahlenderBetrag - eingezahlterGesamtbetrag));

            while (ungueltig) {
                System.out.print("Eingabe (mind. 5Ct, hoechstens 20 Euro): ");

                eingeworfeneMuenze = tastatur.nextDouble();
                for (double d : gueltigeMuenzen) {
                    if (d == eingeworfeneMuenze) {
                        ungueltig = false;
                        break;
                    }
                }
                if (ungueltig) {
                    System.out.println("\n Ungueltige Muenze!\n");
                    eingeworfeneMuenze = 0;
                }
            }
            eingezahlterGesamtbetrag += eingeworfeneMuenze;
            ungueltig = true;

        }
        return eingezahlterGesamtbetrag;

    }

    public static void fahrkartenAusgeben() {
        // Fahrscheinausgabe
        // -----------------
        System.out.println("\nFahrschein wird ausgegeben");
        warte(250);
        System.out.println("\n\n");
    }

    public static void bufferLeeren(Muenze[] muenzBuffer) {
        int anzahl = 0;
        for (int i = 0; i < 3; i++) {
            if (muenzBuffer[i].wert() != 0) {
                anzahl++;
            }
        }

        for (int i = 0; i < anzahl; i++) {
            System.out.print("   * * *    ");
        }
        System.out.println();
        for (int i = 0; i < anzahl; i++) {
            System.out.print(" *       *  ");
        }
        System.out.println();
        for (int i = 0; i < anzahl; i++) {
            System.out.printf("*    %-2d   * ", muenzBuffer[i].wert());
        }
        System.out.println();
        for (int i = 0; i < anzahl; i++) {
            System.out.print("*   " + muenzBuffer[i].einheit() + "  * ");
        }
        System.out.println();
        for (int i = 0; i < anzahl; i++) {
            System.out.print(" *       *  ");
        }
        System.out.println();
        for (int i = 0; i < anzahl; i++) {
            System.out.print("   * * *    ");
        }
        System.out.println();

    }

    public static void rueckgeldAusgeben(double zuZahlenderBetrag, double eingezahlterGesamtbetrag) {


        // Rückgeldberechnung und -Ausgabe
        // -------------------------------
        double rueckgabebetrag;
        int anzahlMuenzen = 0;
        Muenze[] muenzBuffer = new Muenze[3];
        for (int i = 0; i < 3; i++) {
            muenzBuffer[i] = new Muenze(0, "EURO");
        }


        rueckgabebetrag = eingezahlterGesamtbetrag - zuZahlenderBetrag;

        if (rueckgabebetrag > 0.0) {
            System.out.format("Der Rueckgabebetrag in Hoehe von %4.2f EURO %n", rueckgabebetrag);
            System.out.println("wird in folgenden Muenzen ausgezahlt:");

            while (rueckgabebetrag >= 2.0) // 2 EURO-Muenzen
            {
                muenzBuffer[anzahlMuenzen] = new Muenze(2, "EURO");
                rueckgabebetrag -= 2.0;
                anzahlMuenzen++;
                if (anzahlMuenzen == 3) {
                    bufferLeeren(muenzBuffer);
                    anzahlMuenzen = 0;
                    for (int i = 0; i < 3; i++) {
                        muenzBuffer[i] = new Muenze(0, "EURO");
                    }
                }
            }
            while (rueckgabebetrag >= 1.0) // 1 EURO-Muenzen
            {
                muenzBuffer[anzahlMuenzen] = new Muenze(1, "EURO");
                rueckgabebetrag -= 1.0;
                anzahlMuenzen++;
                if (anzahlMuenzen == 3) {
                    bufferLeeren(muenzBuffer);
                    anzahlMuenzen = 0;
                    for (int i = 0; i < 3; i++) {
                        muenzBuffer[i] = new Muenze(0, "EURO");
                    }
                }
            }
            while (rueckgabebetrag >= 0.5) // 50 CENT-Muenzen
            {
                muenzBuffer[anzahlMuenzen] = new Muenze(50, "CENT");
                rueckgabebetrag -= 0.5;
                anzahlMuenzen++;
                if (anzahlMuenzen == 3) {
                    bufferLeeren(muenzBuffer);
                    anzahlMuenzen = 0;
                    for (int i = 0; i < 3; i++) {
                        muenzBuffer[i] = new Muenze(0, "EURO");
                    }
                }
            }
            while (rueckgabebetrag >= 0.2) // 20 CENT-Muenzen
            {
                muenzBuffer[anzahlMuenzen] = new Muenze(20, "CENT");
                rueckgabebetrag -= 0.2;
                anzahlMuenzen++;
                if (anzahlMuenzen == 3) {
                    bufferLeeren(muenzBuffer);
                    anzahlMuenzen = 0;
                    for (int i = 0; i < 3; i++) {
                        muenzBuffer[i] = new Muenze(0, "EURO");
                    }
                }
            }
            while (rueckgabebetrag >= 0.1) // 10 CENT-Muenzen
            {
                muenzBuffer[anzahlMuenzen] = new Muenze(10, "CENT");
                rueckgabebetrag -= 0.1;
                anzahlMuenzen++;
                if (anzahlMuenzen == 3) {
                    bufferLeeren(muenzBuffer);
                    anzahlMuenzen = 0;
                    for (int i = 0; i < 3; i++) {
                        muenzBuffer[i] = new Muenze(0, "EURO");
                    }
                }
            }
            while (rueckgabebetrag >= 0.05)// 5 CENT-Muenzen
            {
                muenzBuffer[anzahlMuenzen] = new Muenze(5, "CENT");
                rueckgabebetrag -= 0.05;
                anzahlMuenzen++;
                if (anzahlMuenzen == 3) {
                    bufferLeeren(muenzBuffer);
                    anzahlMuenzen = 0;
                    for (int i = 0; i < 3; i++) {
                        muenzBuffer[i] = new Muenze(0, "EURO");
                    }
                }
            }
        }

        bufferLeeren(muenzBuffer);

        System.out.println("""

                Vergessen Sie nicht, den Fahrschein
                vor Fahrtantritt entwerten zu lassen!
                Wir wuenschen Ihnen eine gute Fahrt.""");
    }

    public static void warte(int millisekunden) {
        for (int i = 0; i < 8; i++) {
            System.out.print("=");
            try {
                Thread.sleep(millisekunden);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        double zuZahlenderBetrag;
        double eingezahlterGesamtbetrag;
        HashMap<String, Double> Fahrkarten = new LinkedHashMap<>();

        begruessen();
        warte(3000);
        System.out.println("Fahrkartenbestellvorgang:\n=====================");
        zuZahlenderBetrag = fahrkartenbestellungErfassen(Fahrkarten);
        eingezahlterGesamtbetrag = fahrkartenBezahlen(zuZahlenderBetrag);
        fahrkartenAusgeben();
        rueckgeldAusgeben(zuZahlenderBetrag, eingezahlterGesamtbetrag);
    }
}