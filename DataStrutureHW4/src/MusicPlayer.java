import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Song {
    String id, title, artist;
    Song(String id, String title, String artist) {
        this.id = id; this.title = title; this.artist = artist;
    }
    public String toString() { return title; }
}

class Node {
    Song song;
    Node prev, next;
    Node(Song s) { this.song = s; }
}

public class MusicPlayer {
    // --- Built: doubly linked playlist ---
    private Node head, tail;

    // --- Reused: id -> node ---
    private final Map<String, Node> library = new HashMap<>();

    // --- Reused: deque for up next ---
    private final Deque<Song> upNext = new ArrayDeque<>();

    // --- Reused: stack for history ---
    private final Deque<Song> history = new ArrayDeque<>();

    // --- Reused: play counts ---
    private final Map<String, Integer> playCounts = new HashMap<>();

    private Song current = null;

    // ---------- helpers ----------

    /** Single entry point that sets current and bumps play count. */
    private Song start(Song s) {
        if (s == null) return null;
        current = s;
        playCounts.merge(s.id, 1, Integer::sum);
        return s;
    }

    private void unlink(Node n) {
        if (n.prev != null) n.prev.next = n.next; else head = n.next;
        if (n.next != null) n.next.prev = n.prev; else tail = n.prev;
        n.prev = n.next = null;
    }

    // ---------- step 1: playlist + library ----------

    public boolean addSong(String id, String title, String artist) {
        if (library.containsKey(id)) return false;
        Node n = new Node(new Song(id, title, artist));
        if (tail == null) { head = tail = n; }
        else { tail.next = n; n.prev = tail; tail = n; }
        library.put(id, n);
        return true;
    }

    public Song findSong(String id) {
        Node n = library.get(id);
        return n == null ? null : n.song;
    }

    public List<String> showPlaylist() {
        List<String> out = new ArrayList<>();
        for (Node n = head; n != null; n = n.next) out.add(n.song.title);
        return out;
    }

    // ---------- step 2: play / playCount ----------

    public Song play(String id) {
        Node n = library.get(id);
        if (n == null) return null;
        if (current != null) history.push(current);
        return start(n.song);
    }

    public int playCount(String id) {
        return playCounts.getOrDefault(id, 0);
    }

    public Song nowPlaying() { return current; }

    // ---------- step 3: up next ----------

    public boolean queueLater(String id) {
        if (!library.containsKey(id)) return false;
        upNext.addLast(library.get(id).song);
        return true;
    }

    public boolean queueNext(String id) {
        if (!library.containsKey(id)) return false;
        upNext.addFirst(library.get(id).song);
        return true;
    }

    public List<String> showUpNext() {
        List<String> out = new ArrayList<>();
        for (Song s : upNext)
            if (library.containsKey(s.id)) out.add(s.title);
        return out;
    }

    // ---------- step 4: next ----------

    public Song next() {
        Song target = null;

        // 1) up next, skipping removed
        while (!upNext.isEmpty()) {
            Song s = upNext.pollFirst();
            if (library.containsKey(s.id)) { target = s; break; }
        }

        // 2) else the playlist node after current
        if (target == null) {
            if (current == null) return null;
            Node cur = library.get(current.id);
            if (cur == null || cur.next == null) return null;
            target = cur.next.song;
        }

        // only now mutate
        if (current != null) history.push(current);
        return start(target);
    }

    // ---------- step 5: back / history ----------

    public Song back() {
        Song target = null;
        while (!history.isEmpty()) {
            Song s = history.pop();
            if (library.containsKey(s.id)) { target = s; break; }
        }
        if (target == null) return null;

        // old current goes to FRONT of up next (not history)
        if (current != null) upNext.addFirst(current);
        return start(target);
    }

    public List<String> showHistory() {
        List<String> out = new ArrayList<>();
        for (Song s : history)
            if (library.containsKey(s.id)) out.add(s.title);
        return out;
    }

    // ---------- step 6: remove ----------

    public boolean removeSong(String id) {
        Node n = library.get(id);
        if (n == null) return false;
        if (current != null && current.id.equals(id)) return false;
        unlink(n);
        library.remove(id);
        playCounts.remove(id);
        return true;
    }
}
