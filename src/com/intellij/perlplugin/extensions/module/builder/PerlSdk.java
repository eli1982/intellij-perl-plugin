package com.intellij.perlplugin.extensions.module.builder;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.RootProvider;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Created by ELI-HOME on 05-May-15.
 */
public class PerlSdk implements Sdk {

    @NonNls
    private final String libPath;
    PerlSdkTypeId perlSdkTypeId = new PerlSdkTypeId();

    public PerlSdk(String libPath) {
        this.libPath = libPath;
    }

    private static String getConvertedHomePath(Sdk sdk) {
        String path = sdk.getHomePath().replace('/', File.separatorChar);
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }

        return path;
    }

    public String suggestHomePath() {
        return libPath;
    }

    public boolean isValidSdkHome(String path) {
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return new File(path + "perl.exe").exists() || new File(path + "perl.sh").exists();
    }

    public String suggestSdkName(String currentSdkName, String sdkHome) {
        return currentSdkName;
    }

    @NotNull
    @Override
    public SdkTypeId getSdkType() {
        return perlSdkTypeId;
    }

    @NotNull
    @Override
    public String getName() {
        return perlSdkTypeId.getName();
    }

    @Nullable
    @Override
    public String getVersionString() {
        return PerlSdkType.getFullVersionString(getHomePath());
    }

    @Nullable
    @Override
    public String getHomePath() {
        return libPath;
    }

    @Nullable
    @Override
    public VirtualFile getHomeDirectory() {
        return VirtualFileManager.getInstance().refreshAndFindFileByUrl(libPath);
    }

    @NotNull
    @Override
    public RootProvider getRootProvider() {
        return null;
    }

    @NotNull
    @Override
    public SdkModificator getSdkModificator() {
        return new PerlSdkModificator();
    }

    @Nullable
    @Override
    public SdkAdditionalData getSdkAdditionalData() {
        return null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Nullable
    @Override
    public <T> T getUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T t) {

    }
}
