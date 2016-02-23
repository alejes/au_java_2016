package sp;

import java.util.HashMap;

/**
 * Created by Alexey on 21.02.2016.
 */
public class StringSet implements Trie {
    private HashMap<Character, StringSet> set;
    private int size;
    boolean terminal;

    public StringSet() {
        set = new HashMap<>();
        terminal = false;
    }

    @Override
    public boolean add(String element) {
        return addFrom(element, 0);
    }

    private boolean addFrom(String element, int from) {
        if (from == element.length()) {
            if (terminal)
                return false;
            else {
                size++;
                terminal = true;
                return true;
            }
        } else {
            StringSet node = set.get(element.charAt(from));
            if (node == null) {
                node = new StringSet();
                set.put(element.charAt(from), node);
            }
            boolean succ = node.addFrom(element, from + 1);
            if (succ)
                ++size;
            return succ;
        }

    }


    @Override
    public boolean contains(String element) {
        return containsFrom(element, 0);
    }

    private boolean containsFrom(String element, int from) {
        if (from >= element.length()) {
            return terminal;
        } else {
            StringSet node = set.get(element.charAt(from));
            if (node == null) {
                return false;
            } else return node.containsFrom(element, from + 1);
        }
    }

    @Override
    public boolean remove(String element) {
        return removeFrom(element, 0) > 0;
    }

    private int removeFrom(String element, int from) {

        if (from == element.length()) {
            boolean back = terminal;
            terminal = false;
            size -= back ? 1 : 0;
            return back ? 1 : 0;
        } else {
            StringSet node = set.get(element.charAt(from));
            if (node == null) {
                return 0;
            } else {
                int res = node.removeFrom(element, from + 1);
                size -= res;
                if (size == 0)
                    set.clear();
                return res;
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        return howManyStartsWithPrefixFrom(prefix, 0);
    }

    private int howManyStartsWithPrefixFrom(String element, int from) {
        if (from >= element.length()) {
            return size;
        } else {
            StringSet node = set.get(element.charAt(from));
            if (node == null) {
                return 0;
            } else return node.howManyStartsWithPrefixFrom(element, from + 1);
        }
    }
}