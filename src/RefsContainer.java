import java.util.HashSet;

public class RefsContainer {
    private static HashSet<String> refs = new HashSet<>();

    public static boolean registerRef(String ref) {
        return refs.add(ref);
    }
}
