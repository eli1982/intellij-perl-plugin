package com.intellij.perlplugin.extensions;

import com.intellij.perlplugin.language.Constants;
import com.intellij.perlplugin.language.PerlFileType;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class PerlFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(PerlFileType.INSTANCE, Constants.FILE_TYPE);
    }
}