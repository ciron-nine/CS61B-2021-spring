package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import org.antlr.v4.runtime.misc.Pair;

import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    private static int route(int x, int y, int width, TETile[][] tiles) {


        return 0;
    }

    private static Pair<Integer, Integer> init_room(int x, int y, int length, int width, TETile[][] tiles) {
        for(int i = x; i < x + width; i ++) {
            tiles[i][y] = Tileset.WALL;
        }
        for(int i = y + 1; i < y + length; i ++) {
            tiles[x][i] = Tileset.WALL;
        }
        for(int i = x; i < x + width; i ++) {
            tiles[i][y + length - 1] = Tileset.WALL;
        }
        for(int i = y + 1; i < y + length; i ++) {
            tiles[x + width - 1][i] = Tileset.WALL;
        }
        for(int i = x + 1; i < x + width - 1; i ++) {
            for(int j = y + 1; j < y + length - 1;j ++) {
                tiles[i][j] = Tileset.FLOOR;
            }
        }
        tiles[x + 1][y] = Tileset.LOCKED_DOOR;
        int FLOOR_x = x + width / 2;
        int FLOOR_y = y + length - 1;
        tiles[FLOOR_x][FLOOR_y] = Tileset.FLOOR;
        Pair<Integer, Integer> pair = new Pair<>(FLOOR_x, FLOOR_y);
        return pair;
    }
    private static void make_new_route(int x, int y, TETile[][] tiles) {

    }

    private static void new_world(Random random, TETile[][] tiles) {
        int init_x = random.nextInt(60);
        int init_y = random.nextInt(20);
        int init_width = 3 + random.nextInt( 7);
        int init_height = 3 + random.nextInt(2);
        Pair<Integer, Integer> init_room = init_room(init_x, init_y, init_height, init_width, tiles);

        int room_count = random.nextInt(7);

        int pool_count = random.nextInt(5);
        int sum_count = room_count + pool_count;
        while (sum_count != 0) {
            int select = random.nextInt(1);
            if(select == 1) {
                sum_count --;
                pool_count --;
            }
            else {
                room_count --;
                sum_count --;
            }
        }

    }

    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        boolean is_new = false;
        long seed = 0;
        for(int i = 0; i < input.length(); i ++) {
            if(input.charAt(i) == 'N' || input.charAt(i) == 'n') {
                is_new = true;
                i ++;
                while(input.charAt(i) != 'S' && input.charAt(i) != 's') {
                    seed = seed * 10 + input.charAt(i) - '0';
                    i ++;
                }
            }
        }
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
        Random SEED = new Random(seed);
        new_world(SEED, finalWorldFrame);
        
        return finalWorldFrame;
    }
}
