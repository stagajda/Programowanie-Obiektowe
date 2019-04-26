package optymalnabudowa;
import java.util.*;

public class StrategiaEkonomiczna {
    public InputData dane; 
    public StrategiaEkonomiczna(InputData dane){
        this.dane = dane;
    }
    // sprawdza, czy sa same zera na wszystkich pozycjach oprocz ostatniej
    private boolean sameZera(Vector <Long> tab){
        boolean wyn = true;
        for(int i = 0; i < tab.size()-1; i++){
            wyn = wyn && (tab.get(i) == 0);
        }
        return wyn;
    }
    // tabDlugosci i tabCen okreslaja cene i dlugosc kolejnych pretow,
    // funkcja zwraca najtanszy pret o dlugosci wiekszej lub rownej "dlugosc"
    private long cenaNajtanszegoWiekszegoOd(long dlugosc){
        long wynik = Long.MAX_VALUE;
        for(int j = 0; j < dane.C; j++){
            if(dane.dlugoscPretaWCenniku[j] >= dlugosc){
                wynik = Math.min(wynik, dane.cenaPreta[j]);
            }
        }
        assert(wynik != Long.MAX_VALUE);
        return wynik;
    }
    
    private long sumaOdpadow(Vector <Vector <Long> > wyniki){
        long wynik = 0;
        for(int i = 0; i < wyniki.size()-1;i++){
            wynik += wyniki.get(i).get(0);
            for(int j = 1; j < wyniki.get(i).size();j++){
                wynik -= wyniki.get(i).get(j);
            }
        }
        return wynik;
    }
    
    // odwraca wszystkie wektory bedace elementami wektora podanego jako parametr
    private Vector <Vector <Long> > odwrocWektory(Vector < Vector < Long > > wektor){
        long temp;
        for(int i = 0;i < wektor.size();i++){
            for(int j = 0;j < wektor.get(i).size()/2;j++){
                temp = wektor.get(i).get(wektor.get(i).size()-j-1);
                wektor.get(i).set(wektor.get(i).size()-j-1, wektor.get(i).get(j));
                wektor.get(i).set(j,temp);
            }
        }
        return wektor;
    }
    /* stan - tablica okreslajaca, czy potrzebujemy kupic dany pret
       sumaWybranych - suma dlugosci pretow, ktore polaczylismy w jedna grupe
       te prety sa widoczne w stanie jako juz kupione
       minIndeks - mozemy wybrac tylko prety o tym indeksie lub wyzszym
       rozpatrujemy wszystkie mozliwosci rekurencyjnie - 
       wybranie dodatkowych pretow do grupy wybranych
    */
    long najnizszyKoszt(Vector <Long> stan, Map <Vector <Long>, Long> mapa, Map <Vector <Long>, Vector<Long>> bestSasiad) {
        Vector <Long> najlepszySasiedniStan = new Vector <Long>();
        if(mapa.containsKey(stan)){
            return mapa.get(stan);
        }
        long sumaWybranych = stan.get(stan.size()-1);
        // nie da sie nic wybrac, wiec przerywamy i zwracamy duza wartosc
        if(sumaWybranych > dane.dlugoscPretaWCenniku[dane.C-1]) {
            return Long.MAX_VALUE;
        }
        //System.out.println(minIndeks + " " + sumaWybranych);
        long wynik = Long.MAX_VALUE;
        if(sumaWybranych == 0 && sameZera(stan)) {
            return 0;
        }
        // uznajemy te wybrane za ostateczna grupe
        if(sumaWybranych > 0) {
            Vector <Long> klon = new Vector <Long>(stan);
            klon.set(klon.size()-1, (long) 0);
            if(najnizszyKoszt(klon, mapa, bestSasiad) < wynik - cenaNajtanszegoWiekszegoOd(sumaWybranych)){
                wynik = cenaNajtanszegoWiekszegoOd(sumaWybranych) + najnizszyKoszt(klon, mapa, bestSasiad);
                najlepszySasiedniStan = klon;
            }
        }
        
        // probujemy dodac jakis do tych wybranych
        for(int i = 0; i < dane.P; i++) {
            if(stan.get(i) == 1) {
                Vector <Long> klon = new Vector <Long>(stan);
                klon.set(i,(long) 0);
                klon.set(klon.size()-1, sumaWybranych + dane.dlugoscPretaProjekt[i]);
                if(najnizszyKoszt(klon, mapa, bestSasiad) < wynik){
                    wynik = najnizszyKoszt(klon, mapa, bestSasiad);
                    najlepszySasiedniStan = klon;
                }
            }
        }
        mapa.put(stan, wynik);
        bestSasiad.put(stan, najlepszySasiedniStan);
        return wynik;
    }
    // stan to wektor - na ostatnich 2 pozycjach ma zapisany min indeks i sume wybranych
    // na pierwszych n-2 pozycjach 0 i 1 oznaczajace, czy dany pret jeszcze musimy dokupic
    public void rozwiaz(){
        Map <Vector <Long>, Long> mapa = new HashMap <Vector <Long>, Long>();
        Map <Vector <Long>, Vector<Long>> bestSasiad = new HashMap <Vector <Long>, Vector<Long> >();
        Vector <Long> stan = new Vector <Long>();
        
        for(int i = 0;i < dane.P; i++){
            stan.add((long)1);
        }       
        stan.add((long) 0);
        long najnizszyKosztZakupu = najnizszyKoszt(stan, mapa, bestSasiad);
        
        // odzyskujemy wynik - znajdujemy ktore prety kupilismy i jak podzielilismy
        
        Vector < Vector <Long> > wyniki = new Vector < Vector <Long> >();
        wyniki.add(new Vector <Long>());
        while(true){
            //System.out.println(stan.toString());
            Vector <Long> sasiedniStan = bestSasiad.get(stan);
            if(sasiedniStan == null) {
                break;
            }
            long sumaWybranychSasiad = sasiedniStan.get(dane.P);
            long sumaWybranych = stan.get(dane.P);
            // to znaczy, ze sprzedalismy wszystkie, wczesniej wybrane
            if(sumaWybranychSasiad > 0){
                wyniki.get(wyniki.size()-1).add(sumaWybranychSasiad - sumaWybranych);
            }
            if(sumaWybranychSasiad == 0){
                wyniki.get(wyniki.size()-1).add(dane.dlugoscNajtanszegoDlugosciConajmniej(sumaWybranych));
                wyniki.add(new Vector <Long>());
            }
            stan = sasiedniStan;
        }
        wyniki = odwrocWektory(wyniki);
        
        System.out.println(najnizszyKosztZakupu);
        System.out.println(sumaOdpadow(wyniki));
        for(int i = 0;i < wyniki.size() - 1;i++){
            for(int j = 0;j < wyniki.get(i).size() - 1;j++){
                System.out.print(wyniki.get(i).get(j) + " ");
            }
            System.out.println(wyniki.get(i).get(wyniki.get(i).size()-1));
        }
        
    }
}
