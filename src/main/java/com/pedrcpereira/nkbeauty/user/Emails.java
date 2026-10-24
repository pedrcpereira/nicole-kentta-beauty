package com.pedrcpereira.nkbeauty.user;

import java.util.Locale;

public final class Emails {
    private Emails() { }

    public static String normalize(String raw) {
        if (raw == null) { return null; }

        return raw.strip().toLowerCase(Locale.ROOT);
    }
}
