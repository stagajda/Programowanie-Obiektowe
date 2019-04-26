//package optymalnabudowa;
import java.util.Vector;

public class StrategiaMaksymalistyczna extends Strategia{
    private InputData dane;
    StrategiaMaksymalistyczna(InputData dane){
        this.dane = dane;
    }
    public void rozwiaz(){
        Vector<Vector <Integer> > wynik = new Vector < Vector <Integer> >();
        long kosztZakupu = 0;
        long sumaOdpadow = 0;
        int dostepnaDlugoscPreta;
        boolean czyKupiony[] = new boolean[this.dane.P];
        for (int i = dane.P - 1; i >= 0; i--) {
            if(!czyKupiony[i]){
                // kupujemy najdluzszy pret z cennika
                kosztZakupu += dane.cenaPreta[dane.C-1];
                dostepnaDlugoscPreta = dane.dlugoscPretaWCenniku[dane.C-1];
                wynik.add(new Vector <Integer>());
                wynik.get(wynik.size()-1).add(dostepnaDlugoscPreta);
                for (int j = i; j >= 0; j--){
                    if(dane.dlugoscPretaProjekt[j] <= dostepnaDlugoscPreta && !czyKupiony[j]){
                        dostepnaDlugoscPreta -= dane.dlugoscPretaProjekt[j];
                        wynik.get(wynik.size()-1).add(dane.dlugoscPretaProjekt[j]);
                        czyKupiony[j] = true;
                    }
                }
                sumaOdpadow += dostepnaDlugoscPreta;
            }
        }
        System.out.println(kosztZakupu);
        System.out.println(sumaOdpadow);
        for(int i = 0; i < wynik.size(); i++){
            for(int j = 0; j < wynik.get(i).size()-1; j++){
                System.out.print(wynik.get(i).get(j) + " ");
            }
            System.out.println(wynik.get(i).get(wynik.get(i).size()-1));
        }
    }
}
/*
940582260
999472089
1730672418 544019615 750753049 
1730672418 767905556 777071337 
1730672418 1352795608 


*/
