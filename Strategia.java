//package optymalnabudowa;

import java.util.Vector;

public abstract class Strategia {
    public void rozwiaz(InputData dane){
        
    }
    
    // odwraca wszystkie wektory bedace elementami wektora podanego jako parametr
    protected Vector <Vector <Long> > odwrocWektory(Vector < Vector < Long > > wektor){
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
    
    protected boolean sameZera(Vector <Long> tab){
        boolean wyn = true;
        for(int i = 0; i < tab.size()-1; i++){
            wyn = wyn && (tab.get(i) == 0);
        }
        return wyn;
    }
    
}
