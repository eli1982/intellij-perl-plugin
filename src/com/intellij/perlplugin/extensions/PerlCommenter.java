package com.intellij.perlplugin.extensions;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

/**
 * Created by eli on 9-2-15.
 */
public class PerlCommenter implements Commenter {
    @Nullable
    @Override
    public String getLineCommentPrefix() {
        return "#";
    }

    @Nullable
    @Override
    public String getBlockCommentPrefix() {
        return "=head";
    }

    @Nullable
    @Override
    public String getBlockCommentSuffix() {
        return "=cut";
    }

    @Nullable
    @Override
    public String getCommentedBlockCommentPrefix() {
        return "=head";
    }

    @Nullable
    @Override
    public String getCommentedBlockCommentSuffix() {
        return "=cut";
    }
}
