package optymalnabudowa;

public class OptymalnaBudowa {
    public static void main(String[] args) {
        InputData wejscie = InputReader.wczytajWejscie();
        if(wejscie.strategia.equals("minimalistyczna")){
            StrategiaMinimalistyczna.rozwiaz(wejscie);
        }
        else if(wejscie.strategia.equals("maksymalistyczna")){
            StrategiaMaksymalistyczna.rozwiaz(wejscie);
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
