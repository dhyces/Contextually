package dhyces.contextually.client.textures;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.InputConstants;
import dhyces.contextually.ContextuallyCommon;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Stream;

public class KeyMappingTextureManager extends TextureAtlasHolder {
    static final Int2ObjectMap<ResourceLocation> KEY_LOCATIONS;
    static final Set<ResourceLocation> OTHERS;

    public static final ResourceLocation KEYS = ContextuallyCommon.id("textures/atlas/keys.png");
    public static final ResourceLocation UNKNOWN_LOCATION = ContextuallyCommon.id("unknown");

    public KeyMappingTextureManager(TextureManager textureManager) {
        super(textureManager, KEYS, "key");
    }

    public TextureAtlasSprite get(@NotNull ResourceLocation location) {
        return getSprite(location);
    }

    public TextureAtlasSprite get(int keyValue) {
        return getSprite(KEY_LOCATIONS.get(keyValue));
    }

    @Override
    protected Stream<ResourceLocation> getResourcesToLoad() {
        return Sets.union(Sets.newHashSet(KEY_LOCATIONS.values()), OTHERS).stream();
    }

    public static final String UNKNOWN = "unknown";
    public static final String MOUSE_BUTTON_LEFT = "mouse_left_key";
    public static final String MOUSE_BUTTON_RIGHT = "mouse_right_key";
    public static final String MOUSE_BUTTON_MIDDLE = "mouse_middle_key";
    public static final String KEY_0 = "0_key";
    public static final String KEY_1 = "1_key";
    public static final String KEY_2 = "2_key";
    public static final String KEY_3 = "3_key";
    public static final String KEY_4 = "4_key";
    public static final String KEY_5 = "5_key";
    public static final String KEY_6 = "6_key";
    public static final String KEY_7 = "7_key";
    public static final String KEY_8 = "8_key";
    public static final String KEY_9 = "9_key";
    public static final String KEY_NUMPAD_0 = "0_numpad_key";
    public static final String KEY_NUMPAD_1 = "1_numpad_key";
    public static final String KEY_NUMPAD_2 = "2_numpad_key";
    public static final String KEY_NUMPAD_3 = "3_numpad_key";
    public static final String KEY_NUMPAD_4 = "4_numpad_key";
    public static final String KEY_NUMPAD_5 = "5_numpad_key";
    public static final String KEY_NUMPAD_6 = "6_numpad_key";
    public static final String KEY_NUMPAD_7 = "7_numpad_key";
    public static final String KEY_NUMPAD_8 = "8_numpad_key";
    public static final String KEY_NUMPAD_9 = "9_numpad_key";
    public static final String KEY_A = "a_key";
    public static final String KEY_B = "b_key";
    public static final String KEY_C = "c_key";
    public static final String KEY_D = "d_key";
    public static final String KEY_E = "e_key";
    public static final String KEY_F = "f_key";
    public static final String KEY_G = "g_key";
    public static final String KEY_H = "h_key";
    public static final String KEY_I = "i_key";
    public static final String KEY_J = "j_key";
    public static final String KEY_K = "k_key";
    public static final String KEY_L = "l_key";
    public static final String KEY_M = "m_key";
    public static final String KEY_N = "n_key";
    public static final String KEY_O = "o_key";
    public static final String KEY_P = "p_key";
    public static final String KEY_Q = "q_key";
    public static final String KEY_R = "r_key";
    public static final String KEY_S = "s_key";
    public static final String KEY_T = "t_key";
    public static final String KEY_U = "u_key";
    public static final String KEY_V = "v_key";
    public static final String KEY_W = "w_key";
    public static final String KEY_X = "x_key";
    public static final String KEY_Y = "y_key";
    public static final String KEY_Z = "z_key";
    public static final String KEY_F1 = "f1_key";
    public static final String KEY_F2 = "f2_key";
    public static final String KEY_F3 = "f3_key";
    public static final String KEY_F4 = "f4_key";
    public static final String KEY_F5 = "f5_key";
    public static final String KEY_F6 = "f6_key";
    public static final String KEY_F7 = "f7_key";
    public static final String KEY_F8 = "f8_key";
    public static final String KEY_F9 = "f9_key";
    public static final String KEY_F10 = "f10_key";
    public static final String KEY_F11 = "f11_key";
    public static final String KEY_F12 = "f12_key";
    public static final String KEY_NUMLOCK = "num_lock_key";
    public static final String KEY_NUMPAD_ADD = "plus_tall_key";
    public static final String KEY_NUMPAD_ENTER = "enter_tall_key";
    public static final String KEY_NUMPAD_MULTIPLY = "asterisk_key";
    public static final String KEY_NUMPAD_DIVIDE = "slash_key";
    public static final String KEY_NUMPAD_DECIMAL = "decimal_key";
    public static final String KEY_MINUS = "minus_key";
    public static final String KEY_DOWN = "arrow_down_key";
    public static final String KEY_LEFT = "arrow_left_key";
    public static final String KEY_RIGHT = "arrow_right_key";
    public static final String KEY_UP = "arrow_up_key";
    public static final String KEY_QUOTE = "quote_key";
    public static final String KEY_BACKSLASH = "backslash_key";
    public static final String KEY_MARK_LEFT = "mark_left_key";
    public static final String KEY_MARK_RIGHT = "mark_right_key";
    public static final String KEY_ADD = "plus_key";
    public static final String TILDA_KEY = "tilda_key";
    public static final String BRACKET_LEFT_KEY = "bracket_left_key";
    public static final String BRACKET_RIGHT_KEY = "bracket_right_key";
    public static final String SEMICOLON_KEY = "semicolon_key";
    public static final String QUESTION_KEY = "question_key";
    public static final String SPACE_KEY = "space_key";
    public static final String TAB_KEY = "tab_key";
    public static final String ALT_LEFT_KEY = "alt_left_key";
    public static final String ALT_RIGHT_KEY = "alt_right_key";
    public static final String CONTROL_LEFT_KEY = "ctrl_left_key";
    public static final String CONTROL_RIGHT_KEY = "ctrl_right_key";
    public static final String SHIFT_LEFT_KEY = "shift_left_key";
    public static final String SHIFT_RIGHT_KEY = "shift_right_key";
    public static final String WIN_KEY = "win_key"; // TODO: Does this need split up?
    public static final String ENTER_KEY = "enter_key";
    public static final String ESCAPE_KEY = "esc_key";
    public static final String BACKSPACE_KEY = "backspace_key";
    public static final String DELETE_KEY = "del_key";
    public static final String END_KEY = "end_key";
    public static final String HOME_KEY = "home_key";
    public static final String INSERT_KEY = "insert_key";
    public static final String PAGE_DOWN_KEY = "page_down_key";
    public static final String PAGE_UP_KEY = "page_up_key";
    public static final String CAPS_LOCK_KEY = "caps_lock_key";
    public static final String PRINT_SCREEN_KEY = "print_screen_key";

    public static final String MOUSE_BLANK = "mouse_simple_key";
    public static final String TEN_KEY = "10_key";
    public static final String ELEVEN_KEY = "11_key";
    public static final String TWELVE_KEY = "12_key";
    public static final String BACKSPACE_ALT_KEY = "backspace_alt_key";
    public static final String COMMAND_KEY = "command_key";
    public static final String ENTER_ALT_KEY = "enter_alt_key";

    public static ResourceLocation getLocation(int keyCode) {
        return KEY_LOCATIONS.get(keyCode);
    }

    private static void add(Int2ObjectMap<ResourceLocation> map, int keyValue, String id) {
        var rl = ContextuallyCommon.id(id);
        map.put(keyValue, rl);
    }

    static {
        Int2ObjectOpenHashMap<ResourceLocation> map = new Int2ObjectOpenHashMap<>();
        add(map, -1, UNKNOWN);
        add(map, InputConstants.MOUSE_BUTTON_LEFT, MOUSE_BUTTON_LEFT);
        add(map, InputConstants.MOUSE_BUTTON_RIGHT, MOUSE_BUTTON_RIGHT);
        add(map, InputConstants.MOUSE_BUTTON_MIDDLE, MOUSE_BUTTON_MIDDLE);
        add(map, InputConstants.KEY_0, KEY_0);
        add(map, InputConstants.KEY_1, KEY_1);
        add(map, InputConstants.KEY_2, KEY_2);
        add(map, InputConstants.KEY_3, KEY_3);
        add(map, InputConstants.KEY_4, KEY_4);
        add(map, InputConstants.KEY_5, KEY_5);
        add(map, InputConstants.KEY_6, KEY_6);
        add(map, InputConstants.KEY_7, KEY_7);
        add(map, InputConstants.KEY_8, KEY_8);
        add(map, InputConstants.KEY_9, KEY_9);
        add(map, InputConstants.KEY_A, KEY_A);
        add(map, InputConstants.KEY_B, KEY_B);
        add(map, InputConstants.KEY_C, KEY_C);
        add(map, InputConstants.KEY_D, KEY_D);
        add(map, InputConstants.KEY_E, KEY_E);
        add(map, InputConstants.KEY_F, KEY_F);
        add(map, InputConstants.KEY_G, KEY_G);
        add(map, InputConstants.KEY_H, KEY_H);
        add(map, InputConstants.KEY_I, KEY_I);
        add(map, InputConstants.KEY_J, KEY_J);
        add(map, InputConstants.KEY_K, KEY_K);
        add(map, InputConstants.KEY_L, KEY_L);
        add(map, InputConstants.KEY_M, KEY_M);
        add(map, InputConstants.KEY_N, KEY_N);
        add(map, InputConstants.KEY_O, KEY_O);
        add(map, InputConstants.KEY_P, KEY_P);
        add(map, InputConstants.KEY_Q, KEY_Q);
        add(map, InputConstants.KEY_R, KEY_R);
        add(map, InputConstants.KEY_S, KEY_S);
        add(map, InputConstants.KEY_T, KEY_T);
        add(map, InputConstants.KEY_U, KEY_U);
        add(map, InputConstants.KEY_V, KEY_V);
        add(map, InputConstants.KEY_W, KEY_W);
        add(map, InputConstants.KEY_X, KEY_X);
        add(map, InputConstants.KEY_Y, KEY_Y);
        add(map, InputConstants.KEY_Z, KEY_Z);
        add(map, InputConstants.KEY_F1, KEY_F1);
        add(map, InputConstants.KEY_F2, KEY_F2);
        add(map, InputConstants.KEY_F3, KEY_F3);
        add(map, InputConstants.KEY_F4, KEY_F4);
        add(map, InputConstants.KEY_F5, KEY_F5);
        add(map, InputConstants.KEY_F6, KEY_F6);
        add(map, InputConstants.KEY_F7, KEY_F7);
        add(map, InputConstants.KEY_F8, KEY_F8);
        add(map, InputConstants.KEY_F9, KEY_F9);
        add(map, InputConstants.KEY_F10, KEY_F10);
        add(map, InputConstants.KEY_F11, KEY_F11);
        add(map, InputConstants.KEY_F12, KEY_F12);
        add(map, InputConstants.KEY_NUMLOCK, KEY_NUMLOCK);
        add(map, InputConstants.KEY_NUMPAD0, KEY_NUMPAD_0);
        add(map, InputConstants.KEY_NUMPAD1, KEY_NUMPAD_1);
        add(map, InputConstants.KEY_NUMPAD2, KEY_NUMPAD_2);
        add(map, InputConstants.KEY_NUMPAD3, KEY_NUMPAD_3);
        add(map, InputConstants.KEY_NUMPAD4, KEY_NUMPAD_4);
        add(map, InputConstants.KEY_NUMPAD5, KEY_NUMPAD_5);
        add(map, InputConstants.KEY_NUMPAD6, KEY_NUMPAD_6);
        add(map, InputConstants.KEY_NUMPAD7, KEY_NUMPAD_7);
        add(map, InputConstants.KEY_NUMPAD8, KEY_NUMPAD_8);
        add(map, InputConstants.KEY_NUMPAD9, KEY_NUMPAD_9);
        add(map, InputConstants.KEY_ADD, KEY_NUMPAD_ADD);
        add(map, InputConstants.KEY_NUMPADCOMMA, KEY_NUMPAD_DECIMAL);
        add(map, InputConstants.KEY_NUMPADENTER, KEY_NUMPAD_ENTER);
        //add(map, InputConstants.KEY_NUMPADEQUALS, KEY_NUMPAD_ENTER); // TODO: needs dedicated texture
        add(map, InputConstants.KEY_MULTIPLY, KEY_NUMPAD_MULTIPLY);
        add(map, 331, KEY_NUMPAD_DIVIDE);
        add(map, 333, KEY_MINUS);
        add(map, InputConstants.KEY_MINUS, KEY_MINUS);
        add(map, InputConstants.KEY_DOWN, KEY_DOWN);
        add(map, InputConstants.KEY_LEFT, KEY_LEFT);
        add(map, InputConstants.KEY_RIGHT, KEY_RIGHT);
        add(map, InputConstants.KEY_UP, KEY_UP);
        add(map, InputConstants.KEY_APOSTROPHE, KEY_QUOTE);
        add(map, InputConstants.KEY_BACKSLASH, KEY_BACKSLASH);
        add(map, InputConstants.KEY_COMMA, KEY_MARK_LEFT);
        add(map, InputConstants.KEY_EQUALS, KEY_ADD);
        add(map, InputConstants.KEY_GRAVE, TILDA_KEY);
        add(map, InputConstants.KEY_LBRACKET, BRACKET_LEFT_KEY);
        add(map, InputConstants.KEY_MINUS, KEY_MINUS);
        add(map, InputConstants.KEY_PERIOD, KEY_MARK_RIGHT);
        add(map, InputConstants.KEY_RBRACKET, BRACKET_RIGHT_KEY);
        add(map, InputConstants.KEY_SEMICOLON, SEMICOLON_KEY);
        add(map, InputConstants.KEY_SLASH, QUESTION_KEY);
        add(map, InputConstants.KEY_SPACE, SPACE_KEY);
        add(map, InputConstants.KEY_TAB, TAB_KEY);
        add(map, InputConstants.KEY_LALT, ALT_LEFT_KEY);
        add(map, InputConstants.KEY_LCONTROL, CONTROL_LEFT_KEY);
        add(map, InputConstants.KEY_LSHIFT, SHIFT_LEFT_KEY);
        add(map, InputConstants.KEY_LWIN, Minecraft.ON_OSX ? COMMAND_KEY : WIN_KEY);
        add(map, InputConstants.KEY_RALT, ALT_RIGHT_KEY);
        add(map, InputConstants.KEY_RCONTROL, CONTROL_RIGHT_KEY);
        add(map, InputConstants.KEY_RSHIFT, SHIFT_RIGHT_KEY);
        add(map, InputConstants.KEY_RWIN, Minecraft.ON_OSX ? COMMAND_KEY : WIN_KEY);
        add(map, InputConstants.KEY_RETURN, ENTER_KEY);
        add(map, InputConstants.KEY_ESCAPE, ESCAPE_KEY);
        add(map, InputConstants.KEY_BACKSPACE, BACKSPACE_KEY);
        add(map, InputConstants.KEY_DELETE, DELETE_KEY);
        add(map, InputConstants.KEY_END, END_KEY);
        add(map, InputConstants.KEY_HOME, HOME_KEY);
        add(map, InputConstants.KEY_INSERT, INSERT_KEY);
        add(map, InputConstants.KEY_PAGEDOWN, PAGE_DOWN_KEY);
        add(map, InputConstants.KEY_PAGEUP, PAGE_UP_KEY);
        add(map, InputConstants.KEY_CAPSLOCK, CAPS_LOCK_KEY);
        add(map, InputConstants.KEY_PRINTSCREEN, PRINT_SCREEN_KEY);
//        addKey(KEYSYM, "key.keyboard.pause", 284);
//        addKey(KEYSYM, "key.keyboard.scroll.lock", 281);
//        addKey(KEYSYM, "key.keyboard.menu", 348);
//        addKey(KEYSYM, "key.keyboard.world.1", 161);
//        addKey(KEYSYM, "key.keyboard.world.2", 162);
//        addKey(MOUSE, "key.mouse.4", 3);
//        addKey(MOUSE, "key.mouse.5", 4);
//        addKey(MOUSE, "key.mouse.6", 5);
//        addKey(MOUSE, "key.mouse.7", 6);
//        addKey(MOUSE, "key.mouse.8", 7);
//        addKey(KEYSYM, "key.keyboard.f13", 302);
//        addKey(KEYSYM, "key.keyboard.f14", 303);
//        addKey(KEYSYM, "key.keyboard.f15", 304);
//        addKey(KEYSYM, "key.keyboard.f16", 305);
//        addKey(KEYSYM, "key.keyboard.f17", 306);
//        addKey(KEYSYM, "key.keyboard.f18", 307);
//        addKey(KEYSYM, "key.keyboard.f19", 308);
//        addKey(KEYSYM, "key.keyboard.f20", 309);
//        addKey(KEYSYM, "key.keyboard.f21", 310);
//        addKey(KEYSYM, "key.keyboard.f22", 311);
//        addKey(KEYSYM, "key.keyboard.f23", 312);
//        addKey(KEYSYM, "key.keyboard.f24", 313);
//        addKey(KEYSYM, "key.keyboard.f25", 314);
        KEY_LOCATIONS = Int2ObjectMaps.unmodifiable(map);
        ImmutableSet.Builder<ResourceLocation> set = ImmutableSet.builder();
        set.add(ContextuallyCommon.id(MOUSE_BLANK));
        set.add(ContextuallyCommon.id(TEN_KEY));
        set.add(ContextuallyCommon.id(ELEVEN_KEY));
        set.add(ContextuallyCommon.id(TWELVE_KEY));
        set.add(ContextuallyCommon.id(BACKSPACE_ALT_KEY));
        set.add(ContextuallyCommon.id(ENTER_ALT_KEY));
        set.add(ContextuallyCommon.id(!Minecraft.ON_OSX ? COMMAND_KEY : WIN_KEY));
        set.add(UNKNOWN_LOCATION);
        OTHERS = set.build();
    }
}