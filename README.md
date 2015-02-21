# intellij-perl-plugin
This is a plugin that adds support for perl language in intellij (Based on Intellij Custom Languages)

this is really the first stage... docs will be added a little later.

usage:
1.run the plugin with intellij (sdk version)
2.select the src folder (make sure to set it in project structre to src - so it would be blue)
3.select a file from the project
4.press CTRL+SHIFT+G to run the analysis ( this is done to prevent intellij sdk from disabling the plugin if it failes to load - which might happen a lot in this stage of development)

What can it do so far:
----------------------
-autocomplete packages subroutines with CTRL+SPACE
-support inhertence - so you will see subroutines of parent packages as well
-highlights subs and packages
-adds a row symbol for packages in the gutter ( the blue Class symbol)
-adds link in Gutter to inheriting files
-detect attributes in the file

There's a lot to do (here's some of the things)
-----------------------------------------------
-support ctrl click imports (multiple sources) - (references handling in PropertyReferenceContributor)
-update cache while editing files (Cache is in ModulesContainer)
-add format support (spacing in PerlFormattingModelBuilder)
-add scoping blocks (for subs etc...)
