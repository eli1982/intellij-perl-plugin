# intellij-perl-plugin
This is a plugin that adds support for perl language in intellij (Based on Intellij Custom Languages)

this is really the first stage... docs will be added a little later.

Usage:
1.run the plugin with intellij (sdk version)

2.select the src folder (make sure to set it in project structure to src - so the folder will be blue)

3.press CTRL+SHIFT+G to load the cache (if your source folders were already selected on load there's no need for this step)



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
-update cache while editing files (Cache is in ModulesContainer)


-support ctrl click imports (multiple sources) - (references handling in PropertyReferenceContributor)


-add format support (spacing in PerlFormattingModelBuilder)


-add scoping blocks (for subs etc...)
