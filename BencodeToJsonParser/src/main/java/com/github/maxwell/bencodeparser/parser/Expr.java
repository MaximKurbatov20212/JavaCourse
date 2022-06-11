package com.github.maxwell.bencodeparser.parser;

import java.util.List;

sealed public interface Expr {
    record BString(String value) implements Expr {}

    record BNumber(int value) implements Expr {}

    record BEntry(BString key, Expr value) implements Expr {}

    record BList(List<Expr> list) implements Expr {}

    record BDict(List<BEntry> list) implements Expr {}
}