import java.util.ArrayList;

/**
 * Created by Micke on 2016-04-24.
 */
public class ShoppingList {

    private int ID;
    private String name;
    private ArrayList<ListEntry> list;

    public ShoppingList() {
        list = new ArrayList<ListEntry>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addEntry(Item item, int quantity) {
        list.add(new ListEntry(item, quantity));
    }

    public ListEntry getEntry(int index){
        return list.get(index);
    }

    public void removeEntry(int index){
        list.remove(index);
    }

}
