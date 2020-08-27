package json.object;

import json.exception.JsonNotFoundSpecificCharException;
import json.exception.JsonUnknownTokenException;
import json.iterator.JsonIterator;

import java.util.*;
import java.util.function.UnaryOperator;

public class JsonArray extends JsonIterableValue implements List<JsonValue> {
    private ArrayList<JsonValue> jsonValues = new ArrayList<>();

    @Override
    public List<JsonValue> subList(int fromIndex, int toIndex) {
        return jsonValues.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<JsonValue> spliterator() {
        return jsonValues.spliterator();
    }

    @Override
    public void replaceAll(UnaryOperator<JsonValue> operator) {
        jsonValues.replaceAll(operator);
    }

    public int size() {
        return this.jsonValues.size();
    }

    @Override
    public ListIterator<JsonValue> listIterator(int index) {
        return jsonValues.listIterator(index);
    }

    @Override
    public ListIterator<JsonValue> listIterator() {
        return jsonValues.listIterator();
    }

    @Override
    public boolean isEmpty() {
        return jsonValues.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int indexOf(Object o) {
        return jsonValues.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return jsonValues.lastIndexOf(o);
    }

    @Override
    public Iterator<JsonValue> iterator() {
        return this.jsonValues.iterator();
    }

    @Override
    public void sort(Comparator<? super JsonValue> c) {
        jsonValues.sort(c);
    }

    @Override
    public Object[] toArray() {
        return this.jsonValues.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {

        if (a instanceof JsonValue[]) {
            return this.jsonValues.toArray(a);
        }

        return a;
    }

    @Override
    public JsonValue get(int index) {
        return jsonValues.get(index);
    }

    @Override
    public JsonValue set(int index, JsonValue element) {
        return jsonValues.set(index, element);
    }

    @Override
    public boolean add(JsonValue jsonValue) {
        return this.jsonValues.add(jsonValue);
    }

    @Override
    public void add(int index, JsonValue element) {
        jsonValues.add(index, element);
    }

    @Override
    public JsonValue remove(int index) {
        return jsonValues.remove(index);
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof JsonArray) {
            return jsonValues.equals(((JsonArray) o).jsonValues);
        }

        return false;
    }

    @Override
    public boolean remove(Object o) {
        return this.jsonValues.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.jsonValues.containsAll(c);
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        return this.jsonValues.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.jsonValues.retainAll(c);
    }

    @Override
    public void clear() {
        this.jsonValues.clear();
    }

    @Override
    public boolean addAll(Collection<? extends JsonValue> c) {
        return jsonValues.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends JsonValue> c) {
        return jsonValues.addAll(index, c);
    }

    @Override
    protected String createStringCache() {
        StringBuilder sb = new StringBuilder();

        sb.append('[');

        for (JsonValue json: jsonValues) {
            sb.append(json.toString());
            sb.append(",");
        }

        // 요소가 있다면 마지막 제거
        if (this.size() > 0) {
            sb.setLength(sb.length() - 1);
        }

        sb.append(']');

        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 0;

        for (JsonValue json: jsonValues) {
            hash += json.hashCode();
            hash <<= 1;
        }

        return hash;
    }

    public static JsonArray parse(JsonIterator si) {
        JsonArray jsonArray = new JsonArray();
        boolean isFinished = false;

        si.next();
        si.skipWhiteSpaces();

        // 아무 요소 없다면 바로 리턴
        if (si.current() == ']') {
            return jsonArray;
        }

        si_loop:
        while (si.hasNext()) {

            JsonValue item = JsonValue.parse(si, true);

            jsonArray.add(item);

            si.skipWhiteSpaces();

            if (!si.hasNext()) {
                break;
            }

            switch (si.current()) {
                case ',':
                    si.next();
                    si.skipWhiteSpaces();
                    break;
                case ']':
                    si.next();
                    isFinished = true;
                    break si_loop;
                default:
                    throw new JsonUnknownTokenException(si.getPos());
            }
        }

        if (!isFinished) {
            throw new JsonNotFoundSpecificCharException(']', si.getPos());
        }

        return jsonArray;
    }

}
