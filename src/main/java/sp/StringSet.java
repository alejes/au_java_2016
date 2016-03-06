package main.java.sp;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexey on 21.02.2016.
 */
public final class StringSet implements Trie, StreamSerializable {
    private final HashMap<Character, StringSet> set;
    private int size;
    private boolean terminal;

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
            if (terminal) {
                return false;
            } else {
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
            if (succ) {
                ++size;
            }
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
        return removeFrom(element, 0);
    }

    private boolean removeFrom(String element, int from) {

        if (from == element.length()) {
            boolean back = terminal;
            terminal = false;
            size -= back ? 1 : 0;
            return back;
        } else {
            StringSet node = set.get(element.charAt(from));
            if (node == null) {
                return false;
            } else {
                boolean res = node.removeFrom(element, from + 1);
                if (res) {
                    size -= 1;
                }
                if (size == 0) {
                    set.clear();
                }
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


    @Override
    public void serialize(OutputStream out) throws IOException {
        DataOutputStream os = new DataOutputStream(out);
        os.writeInt(size);
        os.writeBoolean(terminal);

        os.writeInt(set.size());
        for (Map.Entry<Character, StringSet> item : set.entrySet()) {
            Character key = item.getKey();
            StringSet value = item.getValue();
            os.writeChar(key);
            value.serialize(os);
        }
    }

    @Override
    public void deserialize(InputStream in) throws IOException {
        set.clear();
        DataInputStream os = new DataInputStream(in);
        size = os.readInt();
        terminal = os.readBoolean();
        int count = os.readInt();

        for (int i = 0; i < count; ++i) {
            Character key = os.readChar();
            StringSet nw = new StringSet();
            set.put(key, nw);
            nw.deserialize(in);
        }

    }
}