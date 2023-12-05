package org.example.domain.grid;
import org.example.domain.grid.GridElements.*;

import java.util.Objects;

public class GridElementsInstanceFactory {
    public static GridElements getInstanceFromGridCharValue(String charValue){
        if(Objects.equals(charValue, "X")) return new Counter();
        if(Objects.equals(charValue, "O")) return new FreeSpace();
        if(Objects.equals(charValue, "🧅")) return new Onion();
        if(Objects.equals(charValue, "🍲")) return new Cauldron();
        if(Objects.equals(charValue, "🧼")) return new Sink();
        if(Objects.equals(charValue, "🆕")) return new NewPlate();
        if(Objects.equals(charValue, "🤲")) return new ServePlate();
        if(Objects.equals(charValue, "🔪")) return new CuttingBoard();
        if(Objects.equals(charValue, "🍽")) return new Plate();

        /*Certaines cases n'auront pas vraiment de cas d'utilisation (IE. l'extincteur). Ces cases sont donc considérées comme un contoir.
        Non utilisées:
        - Extincteur
        - Poubelle
         */
        return new Counter();
    }
}
