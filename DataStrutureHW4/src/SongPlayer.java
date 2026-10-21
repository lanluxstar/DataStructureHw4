import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
public class SongPlayer {
    public static void main(String[] args) throws Exception{
    	 PrintStream console = System.out;
         PrintStream file = new PrintStream(new FileOutputStream("my_output.txt"));
         System.setOut(new PrintStream(new OutputStream() {
             public void write(int b) throws IOException {
                 console.write(b);   // show on screen
                 file.write(b);      // save to output.txt
             }
         }));
        MusicPlayer p = new MusicPlayer();

        
        System.out.println("--- 1. Build the playlist ---");
        System.out.println(p.addSong("s1", "Night Drive", "A"));
        System.out.println(p.addSong("s2", "Slow Tide", "B"));
        System.out.println(p.addSong("s3", "Copper Sky", "C"));
        System.out.println(p.addSong("s4", "Static Bloom", "D"));
        System.out.println(p.addSong("s5", "Low Orbit", "E"));
        System.out.println(p.addSong("s6", "Glass Rivers", "F"));
        System.out.println("duplicate s3: " + p.addSong("s3", "Dup", "X"));
        System.out.println("playlist: " + p.showPlaylist());

       
        System.out.println("--- 2. Look up songs ---");
        System.out.println("find s4: " + p.findSong("s4"));
        System.out.println("find s9: " + p.findSong("s9"));

    
        System.out.println("--- 3. Play and move forward ---");
        System.out.println("play s1: " + p.play("s1"));
        System.out.println("play s9: " + p.play("s9"));
        System.out.println("next: " + p.next());
        System.out.println("next: " + p.next());
        System.out.println("history: " + p.showHistory());

        
        System.out.println("--- 4. Up next ---");
        System.out.println(p.queueLater("s6"));
        System.out.println(p.queueNext("s5"));
        System.out.println(p.queueNext("s1"));
        System.out.println("queue s9: " + p.queueLater("s9"));
        System.out.println("up next: " + p.showUpNext());
        System.out.println("next: " + p.next());
        System.out.println("now playing: " + p.nowPlaying());
        System.out.println("history: " + p.showHistory());

        
        System.out.println("--- 5. Back button ---");
        System.out.println("back: " + p.back());
        System.out.println("up next: " + p.showUpNext());
        System.out.println("history: " + p.showHistory());

        
        System.out.println("--- 6. Remove songs ---");
        System.out.println("remove s5: " + p.removeSong("s5"));
        System.out.println("remove current (s3): " + p.removeSong("s3"));
        System.out.println("remove s9: " + p.removeSong("s9"));
        System.out.println("playlist: " + p.showPlaylist());
        System.out.println("up next: " + p.showUpNext());

        
        System.out.println("--- 7. Removed songs are skipped ---");
        System.out.println("next: " + p.next());
        System.out.println("next: " + p.next());
        System.out.println("next at end: " + p.next());
        System.out.println("now playing: " + p.nowPlaying());
        System.out.println("back: " + p.back());
        System.out.println("up next: " + p.showUpNext());

        
        System.out.println("--- 8. Play counts ---");
        System.out.println("s1: " + p.playCount("s1"));
        System.out.println("s3: " + p.playCount("s3"));
        System.out.println("s6: " + p.playCount("s6"));
        System.out.println("s5 (removed): " + p.playCount("s5"));

        
        System.out.println("--- 9. Empty player ---");
        MusicPlayer empty = new MusicPlayer();
        System.out.println("next: " + empty.next());
        System.out.println("back: " + empty.back());
        System.out.println("now playing: " + empty.nowPlaying());
        System.out.println("playlist: " + empty.showPlaylist());
        
        System.setOut(console);
        file.flush();
        file.close();

    }
}