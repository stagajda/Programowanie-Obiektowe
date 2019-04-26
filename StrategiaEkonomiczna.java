//package optymalnabudowa;
import java.util.*;

public class StrategiaEkonomiczna extends Strategia {
    private InputData dane; 
    public StrategiaEkonomiczna(InputData dane){
        this.dane = dane;
    }
    
    // oblicza sume odpadow na podstawie wektora reprezentujacego wynik
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
    
    
    /* stan - wektor, ktorego pierwsze n-1 pozycji okresla, czy potrzebujemy
       kupic dany pret, (0 nie, 1 tak), a ostatnia pozycja oznacza koszt
       aktualnie wybranych pretow
       mapa - podaje informacje o najnizszym koszcie dla danego stanu
       bestSasiad - dla danego stanu podaje najkorzystniejszy sasiedni stan
    */
    long najnizszyKoszt(Vector <Long> stan, Map <Vector <Long>, Long> mapa,
                        Map <Vector <Long>, Vector<Long>> bestSasiad) {
        Vector <Long> najlepszySasiedniStan = new Vector <Long>();
        if(mapa.containsKey(stan)){
            return mapa.get(stan);
        }
        long sumaWybranych = stan.get(stan.size()-1);
        
        // nie da sie nic wybrac, wiec przerywamy i zwracamy duza wartosc
        if(sumaWybranych > dane.dlugoscPretaWCenniku[dane.C-1]) {
            return Long.MAX_VALUE;
        }

        long wynik = Long.MAX_VALUE;
        if(sumaWybranych == 0 && sameZera(stan)) {
            return 0;
        }
        /* Rozwazamy uznanie wybranych pretow za grupe i zakup najtanszego 
            pretu dluzszy od ich sumy.
        */
        if(sumaWybranych > 0) {
            Vector <Long> klon = new Vector <Long>(stan);
            klon.set(klon.size()-1, (long) 0);
            long temp = dane.cenaNajtanszegoWiekszegoOd(sumaWybranych);
            if(najnizszyKoszt(klon, mapa, bestSasiad) < wynik - temp){
                wynik = temp + najnizszyKoszt(klon, mapa, bestSasiad);
                najlepszySasiedniStan = klon;
            }
        }
        
        /* probujemy dodac jakis pret, ktorego jeszcze nie mamy
           do tych wybranych
        */
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
                wyniki.get(wyniki.size()-1).add(
                        dane.dlugoscNajtanszegoDlugosciConajmniej(sumaWybranych));
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
