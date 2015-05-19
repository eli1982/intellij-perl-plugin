package com.intellij.perlplugin.extensions.module.builder;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkTypeId;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ELI-HOME on 05-May-15.
 */
public class PerlSdkTypeId implements SdkTypeId {
    @NotNull
    @Override
    public String getName() {
        return "Perl SDK";
    }

    @Nullable
    @Override
    public String getVersionString(@NotNull Sdk sdk) {
        return sdk.getVersionString();
    }

    @Override
    public void saveAdditionalData(@NotNull SdkAdditionalData sdkAdditionalData, @NotNull Element element) {

    }

    @Nullable
    @Override
    public SdkAdditionalData loadAdditionalData(@NotNull Sdk sdk, Element element) {
        return null;
    }
}
