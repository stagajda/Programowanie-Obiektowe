//package optymalnabudowa;
import java.util.Scanner;
public class InputReader {
    public static InputData wczytajWejscie(){
        InputData wejscie = new InputData();
        Scanner reader = new Scanner(System.in);
        wejscie.C = reader.nextInt();
        wejscie.cenaPreta = new int[wejscie.C];
        wejscie.dlugoscPretaWCenniku = new int[wejscie.C];
        for (int i = 0;i < wejscie.C;i++) {
            wejscie.dlugoscPretaWCenniku[i] = reader.nextInt();
            wejscie.cenaPreta[i] = reader.nextInt();
        }
        wejscie.P = reader.nextInt();
        wejscie.dlugoscPretaProjekt = new int[wejscie.P];
        for (int i = 0; i < wejscie.P; i++) {
            wejscie.dlugoscPretaProjekt[i] = reader.nextInt();
        }
        wejscie.strategia = reader.nextLine();
        wejscie.strategia = reader.nextLine();
        return wejscie;
    }
}
