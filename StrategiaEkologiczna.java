package optymalnabudowa;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class StrategiaEkologiczna {
    public InputData dane;
    public StrategiaEkologiczna(InputData dane){
        this.dane = dane;
    }
    private boolean sameZera(Vector <Long> tab){
        boolean wyn = true;
        for(int i = 0; i < tab.size()-1; i++){
            wyn = wyn && (tab.get(i) == 0);
        }
        return wyn;
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

    long najmniejOdpadow (Vector <Long> stan, Map <Vector <Long>, Long> mapa, Map <Vector <Long>, Vector<Long>> bestSasiad) {
        long minOdpadow = Long.MAX_VALUE;
        Vector <Long> najlepszySasiedniStan = new Vector <Long>();
        if(mapa.containsKey(stan)){
            return mapa.get(stan);
        }
        long sumaWybranych = stan.get(stan.size() - 1);
        // suma wybranych przekroczyla dlugosc najwiekszego preta 
        if(sumaWybranych > dane.dlugoscPretaWCenniku[dane.C-1]) {
            return Long.MAX_VALUE;
        }

        if(sumaWybranych == 0 && sameZera(stan)) {
            mapa.put(stan,(long) 0);
            return 0;
        }
        
        // uznajemy te wybrane za grupe
        if(sumaWybranych > 0){
            Vector <Long> klon = new Vector <Long>(stan);
            klon.set(klon.size()-1,(long) 0);
            long ileOdpadowGeneruje = dane.dlugoscPretaWCenniku[dane.indeksNajkrotszegoWiekszegoOd(sumaWybranych)] - sumaWybranych;
            if(ileOdpadowGeneruje + najmniejOdpadow(klon, mapa, bestSasiad) < minOdpadow){
                minOdpadow = ileOdpadowGeneruje + najmniejOdpadow(klon, mapa, bestSasiad);
                najlepszySasiedniStan = klon;
            }
        }
        
        // probujemy dodac jakis do tych wybranych
        for(int i = 0; i < dane.P; i++) {
            if(stan.get(i) == 1) {
                Vector <Long> klon = new Vector <Long>(stan);
                klon.set(klon.size()-1, sumaWybranych + dane.dlugoscPretaProjekt[i]);
                klon.set(i,(long) 0);
                if(najmniejOdpadow(klon, mapa, bestSasiad) < minOdpadow){
                    minOdpadow = najmniejOdpadow(klon, mapa, bestSasiad);
                    najlepszySasiedniStan = klon;
                }
            }
            
        }
        mapa.put(stan, minOdpadow);
        bestSasiad.put(stan, najlepszySasiedniStan);
        return minOdpadow;
        
    }
    // stan opisuje czy i-ty pret trzeba jeszcze kupic,
    // ostatnia pozycja podaje sume dlugosci wybranych elementow
    public void rozwiaz(){
        Map <Vector <Long>, Long> mapa = new HashMap <Vector <Long>, Long>();
        Map <Vector <Long>, Vector<Long>> bestSasiad = new HashMap <Vector <Long>, Vector<Long> >();
        Vector <Long> stan = new Vector <Long>();
        for(int i = 0;i < dane.P; i++){
            stan.add((long) 1);
        }
        stan.add((long) 0);
        long sumaOdpadow = najmniejOdpadow(stan, mapa, bestSasiad);
        //System.out.print(sumaOdpadow);
        /*
        while(stan != null && !stan.isEmpty()){
            System.out.println(stan.toString());
            stan = bestSasiad.get(stan);
        }
        */
        
        // odzyskujemy wynik
        
        Vector < Vector <Long> > wyniki = new Vector < Vector <Long> >();
        wyniki.add(new Vector <Long>());
        long cenaSumaryczna = 0;
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
                int indeks = dane.indeksNajkrotszegoWiekszegoOd(sumaWybranych);
                long xd = dane.dlugoscPretaWCenniku[indeks];
                wyniki.get(wyniki.size()-1).add(xd);
                cenaSumaryczna += (long) dane.cenaPreta[indeks];
                wyniki.add(new Vector <Long>());
            }
            stan = sasiedniStan;
        }
        wyniki = odwrocWektory(wyniki);
        
        System.out.println(cenaSumaryczna);
        System.out.println(sumaOdpadow);
        for(int i = 0;i < wyniki.size() - 1;i++){
            for(int j = 0;j < wyniki.get(i).size() - 1;j++){
                System.out.print(wyniki.get(i).get(j) + " ");
            }
            System.out.println(wyniki.get(i).get(wyniki.get(i).size()-1));
        }
        
    }
    
}
// JURA