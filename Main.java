//package optymalnabudowa;

public class Main {
    public static void main(String[] args) {
        InputData wejscie = InputReader.wczytajWejscie();
        if(wejscie.strategia.equals("minimalistyczna")){
            StrategiaMinimalistyczna strategia = new StrategiaMinimalistyczna(wejscie);
            strategia.rozwiaz();
        }
        else if(wejscie.strategia.equals("maksymalistyczna")){
            StrategiaMaksymalistyczna strategia = new StrategiaMaksymalistyczna(wejscie);
            strategia.rozwiaz();
        }
        else if(wejscie.strategia.equals("ekonomiczna")){
            StrategiaEkonomiczna strategia = new StrategiaEkonomiczna(wejscie);
            strategia.rozwiaz();  
        }
        else{
            StrategiaEkologiczna strategia = new StrategiaEkologiczna(wejscie);
            strategia.rozwiaz();  
        }
      
    }    
}
