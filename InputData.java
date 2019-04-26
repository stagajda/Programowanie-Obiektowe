package optymalnabudowa;

public class InputData {
    public int C;
    public int P;
    // tablice dlugosci C
    public int cenaPreta[];
    public int dlugoscPretaWCenniku[];
    // tablica dlugosci P
    public int dlugoscPretaProjekt[];
    String strategia;
    // moze byc optymalniej
    // zwraca jego
    public long dlugoscNajtanszegoDlugosciConajmniej(long dlugosc){
        long bestCena = Long.MAX_VALUE;
        long wynikDlugosc = Long.MAX_VALUE;
        for(int i = 0; i < C;i++){
            if(dlugoscPretaWCenniku[i] >= dlugosc && cenaPreta[i] <= bestCena){
                wynikDlugosc = dlugoscPretaWCenniku[i];
                bestCena = cenaPreta[i];
            }
        }
        return wynikDlugosc;
    }
    
    // zaklada ze istnieje pret o dlugosci wiekszej od dlugosc
    public int indeksNajkrotszegoWiekszegoOd(long dlugosc){
        int wynik = 0;
        long najkrotszy = Long.MAX_VALUE;
        for(int i = 0;i < C;i++){
            if(dlugoscPretaWCenniku[i] >= dlugosc){
                if(najkrotszy > dlugoscPretaWCenniku[i]){
                    najkrotszy = dlugoscPretaWCenniku[i];
                    wynik = i;
                }
            }
        }
        return wynik;
    }
}
