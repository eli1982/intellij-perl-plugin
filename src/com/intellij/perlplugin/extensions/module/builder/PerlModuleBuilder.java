package com.intellij.perlplugin.extensions.module.builder;

import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.JavaSdkType;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.perlplugin.language.PerlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ELI-HOME on 25-Apr-15.
 */

public class PerlModuleBuilder extends ModuleBuilder implements SourcePathsBuilder {
    private Project myProject;
    private String myCompilerOutputPath;
    private List<Pair<String, String>> mySourcePaths;

    public String getBuilderId() {
        return getClass().getName();
    }

    public String getPresentableName() {
        return "Perl";
    }

    public String getDescription() {
        return "Perl Module - supports features for .pm and .pl files";
    }

    public Icon getBigIcon() {
        return PerlIcons.LANGUAGE_32;
    }

    public Icon getNodeIcon() {
        return PerlIcons.LANGUAGE;
    }

    public void setupRootModel(ModifiableRootModel rootModel) throws ConfigurationException {
        setMyProject(rootModel.getProject());
        CompilerModuleExtension compilerModuleExtension = rootModel.getModuleExtension(CompilerModuleExtension.class);
        compilerModuleExtension.setExcludeOutput(true);
        if (this.myJdk != null) {
            rootModel.setSdk(this.myJdk);
        } else {
            rootModel.inheritSdk();
        }
        ContentEntry contentEntry = doAddContentEntry(rootModel);
        if (contentEntry != null) {
            List<Pair<String, String>> sourcePaths = getSourcePaths();
            if (sourcePaths != null) {
                for (Pair<String, String> sourcePath : sourcePaths) {
                    String first = sourcePath.first;
                    new File(first).mkdirs();
                    VirtualFile sourceRoot = LocalFileSystem.getInstance().refreshAndFindFileByPath(FileUtil.toSystemIndependentName(first));
                    if (sourceRoot != null) {
                        contentEntry.addSourceFolder(sourceRoot, false, sourcePath.second);
                    }
                }
            }
        }
        if (this.myCompilerOutputPath != null) {
            String canonicalPath;
            try {
                canonicalPath = FileUtil.resolveShortWindowsName(this.myCompilerOutputPath);
            } catch (IOException e) {
                canonicalPath = this.myCompilerOutputPath;
            }
            compilerModuleExtension.setCompilerOutputPath(VfsUtil.pathToUrl(FileUtil.toSystemIndependentName(canonicalPath)));
        } else {
            compilerModuleExtension.inheritCompilerOutputPath(true);
        }
        LibraryTable libraryTable = rootModel.getModuleLibraryTable();
        for (Pair<String, String> libInfo : this.myModuleLibraries) {
            String moduleLibraryPath = libInfo.first;
            String sourceLibraryPath = libInfo.second;
            Library library = libraryTable.createLibrary();
            Library.ModifiableModel modifiableModel = library.getModifiableModel();
            modifiableModel.addRoot(getUrlByPath(moduleLibraryPath), OrderRootType.CLASSES);
            if (sourceLibraryPath != null) {
                modifiableModel.addRoot(getUrlByPath(sourceLibraryPath), OrderRootType.SOURCES);
            }
            modifiableModel.commit();
        }
    }

    public String getGroupName() {
        return "Perl";
    }

    public Project getMyProject() {
        return this.myProject;
    }

    @Nullable
    public ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
        PerlIntroWizardStep step = new PerlIntroWizardStep();
        Disposer.register(parentDisposable, step);
        return step;
    }

    public void setMyProject(Project myProject) {
        this.myProject = myProject;
    }

    private final List<Pair<String, String>> myModuleLibraries = new ArrayList();

    public final void setCompilerOutputPath(String compilerOutputPath) {
        this.myCompilerOutputPath = acceptParameter(compilerOutputPath);
    }

    public List<Pair<String, String>> getSourcePaths() {
        if (this.mySourcePaths == null) {
            List<Pair<String, String>> paths = new ArrayList();
            String path = getContentEntryPath() + File.separator + "src";
            new File(path).mkdirs();
            paths.add(Pair.create(path, ""));
            return paths;
        }
        return this.mySourcePaths;
    }

    public void setSourcePaths(List<Pair<String, String>> sourcePaths) {
        this.mySourcePaths = (sourcePaths != null ? new ArrayList(sourcePaths) : null);
    }

    public void addSourcePath(Pair<String, String> sourcePathInfo) {
        if (this.mySourcePaths == null) {
            this.mySourcePaths = new ArrayList();
        }
        this.mySourcePaths.add(sourcePathInfo);
    }

    public ModuleType getModuleType() {
        return ModuleType.EMPTY;
    }

    public boolean isSuitableSdkType(SdkTypeId sdkType) {
        return sdkType instanceof JavaSdkType;//TODO: create sdk type - based on SimpleJavaSdkType.class
    }

    @Nullable
    public ModuleWizardStep modifySettingsStep(@NotNull SettingsStep settingsStep) {
        if (settingsStep == null) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"settingsStep", "com/intellij/perlplugin/extensions/module/builder", "modifySettingsStep"}));
        }
        return ModuleType.EMPTY.modifySettingsStep(settingsStep, this);
    }

    private static String getUrlByPath(String path) {
        return VfsUtil.getUrlForLibraryRoot(new File(path));
    }

    public void addModuleLibrary(String moduleLibraryPath, String sourcePath) {
        this.myModuleLibraries.add(Pair.create(moduleLibraryPath, sourcePath));
    }

    @Nullable
    protected static String getPathForOutputPathStep() {
        return null;
    }
}
