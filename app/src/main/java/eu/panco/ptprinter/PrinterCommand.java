package eu.panco.ptprinter;

public class PrinterCommand {

    public static final byte[] reset = new byte[] {0x1b, 0x40};
    public static final byte[] new_line = new byte[] {0x0a};
    public static final byte[] cr = new byte[] {0x0d};
    public static final byte[] tab = new byte[] {0x09};
    public static final byte[] set_tab = new byte[] {0x1b, 0x44, 8, 16, 24, 0x00};
    public static final byte[] bold_off = new byte[] {0x1b, 0x45, 0};
    public static final byte[] bold_on = new byte[] {0x1b, 0x45, 1};
    public static final byte[] format_double_height = new byte[] {0x1b, 0x21, 0x10};
    public static final byte[] format_double_width = new byte[] {0x1b, 0x21, 0x20};
    public static final byte[] font_normal = new byte[] {0x1b, 0x4d, 0};
    public static final byte[] font_small = new byte[] {0x1b, 0x4d, 1};
    public static final byte[] font_big = new byte[] {0x1d, 0x21, 0x60};
    public static final byte[] feed = new byte[] {0x1b, 0x64, 5};
    public static final byte[] align_left = new byte[] {0x1b, 0x61, 0};
    public static final byte[] align_center = new byte[] {0x1b, 0x61, 1};
    public static final byte[] align_right = new byte[] {0x1b, 0x61, 2};
    public static final byte[] black_white_reverse_print_off = new byte[] {0x1d, 0x42, 0x00};
    public static final byte[] black_white_reverse_print_on = new byte[] {0x1d, 0x42, 0x01};
    public static final byte[] set_charset_cp852 = new byte[] {0x1b, 0x74, 18};
    public static final byte[] print_nv = new byte[] {0x1c, 0x70, 1, 0};
    public static final byte[] spacing_default = new byte[] {0x1b, 0x20, 0x00};
    public static final byte[] spacing_double = new byte[] {0x1b, 0x20, 0x02};

    //public static final byte[] pulse = new byte[] {0x1b, 0x70, 0x00, 0x96, 0x96};

}
