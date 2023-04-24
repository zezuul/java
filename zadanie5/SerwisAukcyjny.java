//Julia Zezula listopad 2022

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SerwisAukcyjny implements Aukcja {
    //map of all of the users
    Map<String, Powiadomienie> users = new HashMap<>();
    //map of przedmioty aukcji
    Map<Integer, PrzedmiotAukcjiClass> things = new HashMap<>();
    //map of subscribents
    Map<Integer, ArrayList<String>> notifications = new HashMap<>();


    Map<Integer, ArrayList<Offer>> offers = new HashMap<>();

    /**
     * Metoda do dodawania użytkowika do systemu aukcyjnego. Użytkownicy rozróżniani
     * są za pomocą ich unikalnego username.
     *
     * @param username unikalne nazwa użytkownika
     * @param kontakt  obiekt za pomocą należy powiadamiać tego użytkownika, gdy
     *                 ktoś inny przebije ofertę na przedmiot, którym użytkownik
     *                 jest zainteresowany.
     */
    public void dodajUżytkownika(String username, Powiadomienie kontakt) {
        //add new user to map
        users.put(username, kontakt);
    }

    /**
     * Metoda pozwala na dodanie przedmiotu do serwisu aukcyjnego.
     *
     * @param przedmiot dodawany do serwisu przedmiot
     */
    public void dodajPrzedmiotAukcji(PrzedmiotAukcji przedmiot) {
        Integer key = Integer.valueOf(przedmiot.identyfikator());
        if (!things.containsKey(key)) {
            things.put(key, new PrzedmiotAukcjiClass(przedmiot));
            notifications.put(key, new ArrayList<String>());
            offers.put(key, new ArrayList<Offer>());
        } else {
            System.out.println("Thing already exists!");
        }

    }

    /**
     * Użytkownik o podanym username zgłasza zainteresowanie przedmiotem aktucji o
     * podanym identyfikatorze. Od chwili wykonania tej metody użytkownik jest
     * powiadamiany każdorazowo, gdy jego oferta zostanie przebita.
     *
     * @param username                      nazwa użytkownika serwisu
     * @param identyfikatorPrzedmiotuAukcji identyfikator przedmiotu aukcji
     */
    public void subskrypcjaPowiadomień(String username, int identyfikatorPrzedmiotuAukcji) {
        Integer key = Integer.valueOf(identyfikatorPrzedmiotuAukcji);
        if (notifications.containsKey(key)) {
            ArrayList<String> not = notifications.get(key);
            if (!not.contains(username)) {
                not.add(username);
            } else {
                System.out.println("Notification already exists!");
            }
        } else {
            System.out.println("Things unknown for notifications");
        }
    }

    /**
     * Metoda kończy obserwację danego przedmiotu przez użytkownika o podanym
     * username. Rezygnacja z powiadomień oznacza zaprzestanie wysyłania
     * powiadomień.
     *
     * @param username                      nazwa użytkownika serwisu
     * @param identyfikatorPrzedmiotuAukcji identyfikator przedmiotu aukcji
     */
    public void rezygnacjaZPowiadomień(String username, int identyfikatorPrzedmiotuAukcji) {
        Integer key = Integer.valueOf(identyfikatorPrzedmiotuAukcji);
        if (notifications.containsKey(key)) {
            ArrayList<String> not = notifications.get(key);
            if (not.contains(username)) {
                not.remove(username);
            } else {
                System.out.println("No Notification");
            }
        } else {
            System.out.println("Things unknown for notifications");
        }
    }

    /**
     * Użytkownik przekazuje ofertę zakupu przedmiotu za podaną kwotę. Wszystkie
     * osoby obserwujące ten sam przedmiot, a oferujące niższą kwotę powinny zostać
     * automatycznie powiadomione o przebiciu ich oferty.
     *
     * @param username                      nazwa użytkownika serwisu
     * @param identyfikatorPrzedmiotuAukcji identyfikator przedmiotu aukcji
     * @param oferowanaKwota                zaoferowana kwota w groszach
     */
    public void oferta(String username, int identyfikatorPrzedmiotuAukcji, int oferowanaKwota) {
        Integer key = Integer.valueOf(identyfikatorPrzedmiotuAukcji);
        if (offers.containsKey(key)) {
            ArrayList<Offer> offer = offers.get(key);
            offer.add(new Offer(username, oferowanaKwota));
            PrzedmiotAukcjiClass pa = things.get(key);
            if (pa.isOpen && oferowanaKwota > pa.aktualnaOferta()) {
                pa.offer = oferowanaKwota;
                pa.winningUser = username;
                ArrayList<String> usersListToNotify = notifications.get(key);
                for (String userToNotify : usersListToNotify) {
                    if (userToNotify.compareTo(username) != 0) {
                        users.get(userToNotify).przebitoTwojąOfertę(pa);
                    }
                }
            }

        } else {
            System.out.println("Things unknown for offers");
        }
    }

    /**
     * Za pomocą tej metody zamykana jest aukcja. Najlepsza oferta wygrywa. Nowe
     * oferty są ignorowane.
     *
     * @param identyfikatorPrzedmiotuAukcji identyfikator przedmiotu aukcji
     */
    public void koniecAukcji(int identyfikatorPrzedmiotuAukcji) {
        Integer key = Integer.valueOf(identyfikatorPrzedmiotuAukcji);
        if (things.containsKey(key)) {
            PrzedmiotAukcjiClass pa = things.get(key);
            pa.isOpen = false;
        } else {
            System.out.println("Things unknown for offers");
        }
    }

    /**
     * Metoda pozwala poznać nazwę użytkownika, który zaoferował nawyższą kwotę za
     * przedmiot aukcji. Jeśli aukcja została zakończona, metoda pozwala poznać dane
     * osoby, która aukcję na dany przedmiot wygrała.
     *
     * @param identyfikatorPrzedmiotuAukcji identyfikator przedmiotu aukcji
     * @return nazwa użytkownika wygrywającego aukcję
     */
    public String ktoWygrywa(int identyfikatorPrzedmiotuAukcji) {
        Integer key = Integer.valueOf(identyfikatorPrzedmiotuAukcji);
        if (things.containsKey(key)) {
            PrzedmiotAukcjiClass pa = things.get(key);
            return pa.winningUser;
        } else {
            System.out.println("Things unknown for offers");
            return null;
        }
    }

    /**
     * Metoda pozwala poznać najlepszą ofertę za dany przedmiot. Kwota podawna jest
     * w groszach.
     *
     * @param identyfikatorPrzedmiotuAukcji identyfikator przedmiotu aukcji
     * @return najwyższa ofera za przedmiot
     */
    public int najwyższaOferta(int identyfikatorPrzedmiotuAukcji) {
        Integer key = Integer.valueOf(identyfikatorPrzedmiotuAukcji);
        if (things.containsKey(key)) {
            PrzedmiotAukcjiClass pa = things.get(key);
            return pa.offer;
        } else {
            System.out.println("Things unknown for offers");
            return 0;
        }
    }

    class Offer {
        String username;
        int offer;

        public Offer(String u, int o) {
            username = u;
            offer = o;
        }

        public String getUsername() {
            return username;
        }

        public int getOffer() {
            return offer;
        }

    }

    class PrzedmiotAukcjiClass implements Aukcja.PrzedmiotAukcji {
        /**
         * Unikalny, liczbowy identyfikator przedmiotu. Każdy przedmiot aukcji będzie
         * posiadać inny identyfikator.
         *
         * @return identyfikator liczbowy
         */

        int id;
        String name;
        int offer;
        int price;
        String winningUser = null;

        boolean isOpen = true;

        public PrzedmiotAukcjiClass(int id_now, String name_now, int price_now) {
            id = id_now;
            name = name_now;
            price = price_now;
        }

        public PrzedmiotAukcjiClass(PrzedmiotAukcji p) {
            id = p.identyfikator();
            name = p.nazwaPrzedmiotu();
            price = p.aktualnaCena();
        }

        public int identyfikator() {
            return id;
        }

        /**
         * Nazwa przedmiotu aukcji
         *
         * @return nazwa przedmiotu
         */
        public String nazwaPrzedmiotu() {
            return name;
        }

        /**
         * Nowa oferta za przedmiot. Dla proszczenia, aby uniknąć ułamków, oferta
         * podawana jest w groszach. W przypadku użycia w powiadomieniach metoda
         * przekazuje ofertę, która przebiła kwotę zaoferowaną przez użytkownika serwisu
         * aukcyjnego. Niekoniecznie aktualna oferta będzie większa od aktualnej ceny.
         *
         * @return aktualna oferta w groszach
         */

        public int aktualnaOferta() {
            return offer;
        }

        /**
         * Aktualna cena za przedmiot. Dla proszczenia, aby uniknąć ułamków, cena
         * podawana jest w groszach. W momencie dodawania przedmiotu do aukcji pozwala
         * na przekazanie ceny minimalnej. W przypadku użycia w powiadomieniach metoda
         * przekazuje ofertę, która aktualnie jest najwyższa.
         *
         * @return aktualna cena w groszach
         */
        public int aktualnaCena() {
            return price;
        }
    }

}