package optymalnabudowa;
import java.util.Vector;

public class StrategiaMinimalistyczna implements Strategia{
    static void rozwiaz(InputData dane){
        Vector<Vector <Integer> > wynik = new Vector < Vector <Integer> >();
        int kosztZakupu = 0;
        int sumaOdpadow = 0;
        int dostepnaDlugoscPreta;
        int najdluzszyRozwazanyZCennika = dane.C - 1;
        boolean czyKupiony[] = new boolean[dane.P];
        for (int i = dane.P - 1; i >= 0; i--) {
            if(!czyKupiony[i]){
                // aktualizujemy dlugosc najdluzszego rozwazanego
                while (najdluzszyRozwazanyZCennika > 0) {
                    if(dane.dlugoscPretaWCenniku[najdluzszyRozwazanyZCennika - 1] >= dane.dlugoscPretaProjekt[i]){
                        najdluzszyRozwazanyZCennika--;
                    }
                    else{
                        break;
                    }    
                }
                // kupujemy najkrotszy bedacy wiekszy od itego z projektu
                kosztZakupu += dane.cenaPreta[najdluzszyRozwazanyZCennika];
                dostepnaDlugoscPreta = dane.dlugoscPretaWCenniku[najdluzszyRozwazanyZCennika];
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