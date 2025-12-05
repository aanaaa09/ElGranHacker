package com.ldm.elGranHacker;

import com.ldm.elGranHacker.Graficos.PixmapFormat;

public interface Pixmap {
    int getWidth();

    int getHeight();

    PixmapFormat getFormat();

    void dispose();
}

