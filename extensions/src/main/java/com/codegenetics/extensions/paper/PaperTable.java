package com.codegenetics.extensions.paper;

public class PaperTable<T> {

    @SuppressWarnings("UnusedDeclaration") PaperTable() {
    }

    PaperTable(T content) {
        mContent = content;
    }

    // Serialized content
    T mContent;
}
