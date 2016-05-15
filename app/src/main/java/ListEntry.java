/**
 * Created by Micke on 2016-04-24.
 */
public class ListEntry {

    private int ID;
    private Item item;
    private int quantity;
    private boolean checked;

    public ListEntry(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
        checked = false;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
