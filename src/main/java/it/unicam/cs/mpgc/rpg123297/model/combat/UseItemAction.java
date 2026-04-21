package it.unicam.cs.mpgc.rpg123297.model.combat;

import it.unicam.cs.mpgc.rpg123297.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg123297.model.character.Gladiator;
import it.unicam.cs.mpgc.rpg123297.model.item.Consumable;

import java.util.List;

/**
 * Azione per usare un consumabile durante il combattimento.
 * Dopo l'uso, il consumabile viene rimosso dall'inventario.
 */
public class UseItemAction implements CombatAction {

    private Consumable selectedItem;

    /** Imposta il consumabile da usare. */
    public void setSelectedItem(Consumable item) {
        this.selectedItem = item;
    }

    public Consumable getSelectedItem() {
        return selectedItem;
    }

    @Override
    public int execute(GameCharacter attacker, GameCharacter defender, CombatLog log) {
        if (selectedItem == null) {
            log.addEntry(String.format("%s non ha selezionato alcun oggetto!", attacker.getName()));
            return 0;
        }

        String effectMessage = selectedItem.applyEffect(attacker);
        log.addEntry(String.format("🧪 %s usa %s. %s",
                attacker.getName(), selectedItem.getName(), effectMessage));

        // Rimuovi il consumabile dall'inventario
        if (attacker instanceof Gladiator gladiator) {
            gladiator.getInventory().removeItem(selectedItem);
        }

        selectedItem = null; // resetta per il prossimo uso
        return 0;
    }

    @Override
    public String getName() {
        return "Usa Oggetto";
    }

    @Override
    public String getDescription() {
        return "Usa un consumabile dall'inventario.";
    }

    @Override
    public boolean isAvailable(GameCharacter character) {
        if (character instanceof Gladiator gladiator) {
            List<Consumable> consumables = gladiator.getInventory().getItemsByType(Consumable.class);
            return !consumables.isEmpty();
        }
        return false;
    }
}
