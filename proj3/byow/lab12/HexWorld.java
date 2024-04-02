package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    private static final int WIDTH = 100;
    private static final int HEIGHT = 80;

    private static TETile randomTile(int tileNum) {
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.AVATAR;
            case 3: return Tileset.GRASS;
            case 4: return Tileset.MOUNTAIN;
            case 5: return Tileset.SAND;
            case 6: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }
    private static void print_hex(int size, int x, int y, TETile[][] tiles) {
        if(size == 1) {
            return ;
        }
        int tileNum = RANDOM.nextInt(7);
        int C = 2 * size + (size - 2);
        int length = 0;
        int curx = x, cury = y;
        int prex = x;
        while (length < size) {
            for(int i = 0; i < C; i ++) {
                tiles[cury][curx + i] = randomTile(tileNum);
            }
            C -= 2;
            cury --;
            curx = prex + 1;
            prex = curx;
            length ++;
        }
        C = 2 * size + (size - 2);
        length = 0;
        cury = y + 1; curx = x;
        prex = x;
        while (length < size) {
            for(int i = 0; i < C; i ++) {
                tiles[cury][curx + i] = randomTile(tileNum);
            }
            C -= 2;
            cury ++;
            curx = prex + 1;
            prex = curx;
            length ++;
        }
    }

    private static void HEX19(int size, TETile[][] tiles) {
        int C = 2 * size + (size - 2);
        int wid = 3 * C + 2 * size + 4;
        int high = 1 + size;
        wid = wid / 2;
        wid -= size;
        print_hex(size, wid, high, tiles);

        high += size;
        wid += size - 1;
        wid -= C;
        int wid2_1 = wid;
        print_hex(size, wid, high, tiles);

        wid += C + size;
        int wid2_2 = wid;
        print_hex(size,wid, high,  tiles);

        int wid1_1 = wid2_1 + size - 1 - C;
        int wid1_2 = wid1_1 + C + size;
        int wid1_3 = wid1_2 + C + size;
        high += size;
        print_hex(size, wid1_1, high,  tiles);
        print_hex(size, wid1_2, high,  tiles);
        print_hex(size, wid1_3, high,  tiles);

        high += size;
        print_hex(size, wid2_1, high, tiles);
        print_hex(size, wid2_2, high, tiles);

        high += size;
        print_hex(size, wid1_1, high,  tiles);
        print_hex(size, wid1_2, high,  tiles);
        print_hex(size, wid1_3, high,  tiles);

        high += size;
        print_hex(size, wid2_1, high, tiles);
        print_hex(size, wid2_2, high, tiles);

        high += size;
        print_hex(size, wid1_1, high,  tiles);
        print_hex(size, wid1_2, high,  tiles);
        print_hex(size, wid1_3, high,  tiles);

        high += size;
        print_hex(size, wid2_1, high, tiles);
        print_hex(size, wid2_2, high, tiles);

        high += size;
        wid = 3 * C + 2 * size + 4;
        wid = wid / 2;
        wid -= size;
        print_hex(size, wid, high,tiles);


    }
    public static void main(String args[]) {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        HEX19(5, world);

        ter.renderFrame(world);
    }
}
