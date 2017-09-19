Cleaning Up Git Repositories.

Step 0. Preparation
Remove origin repositories.
Make frequent backups.

Step 1: Remove all tests.
Removing all tests is done first because the chances are high commits will get removed as well. If a commit contains only a test, then running this command will remove it.

    git filter-branch -f \
        --tree-filter 'rm -rf src/test' \
        --prune-empty \
        --tag-name-filter cat \
        -- --all
    
This rewrites the SHA hashes of the commits so viewing it in QGit will make the repository look like this:

[old-references]

To solve this we'll run this command:

    git for-each-ref --format="%(refname)" refs/original/ | xargs -n 1 git update-ref -d 
    
Looking at the repository we now see a nice graph without old references:

[new-references]

On to the next step.

Step 2: Setting the pom correctly for every commit.
Again. We use a filter-branch, but this time, we use a cp command:

    git filter-branch -f \
        --tree-filter 'cp C:/Temp/agpl-prep/cleanup/pom-1.0.3-SN.xml pom.xml' \
        --prune-empty \
        --tag-name-filter cat \
        72468384a0f87839a9e3512fadfd4fb3daa48fad..develop

Same as before, running a filter-branch, rewrites the commits, so let's filter out the old commits, using the for-each command from before.

This was the easy part. Let's git into the nitty gritty of git.

Step 3: Setting up the release procedures.
1. Check out the merge master into develop commit and make a new branch at that point. Let's call it "mastermerge".

    git checkout -b mastermerge 72468384a0f87839a9e3512fadfd4fb3daa48fad
    
[3-mastermerge.png]

At this point we need to copy the 1.0.2 pom into this commit:

    cp C:/Temp/agpl-prep/cleanup/pom-1.0.2-V.xml pom.xml

Add the file to the commit and amend the commit:

    git add pom.xml
    git commit --amend --no-edit
    
This is doing some funky stuff in git:

[3-copiedpom.png]

Let's fix this.

2. Graft 1.0.3 SNAPSHOT onto the mastermerge

    echo 3e7faa948b71e7c58e5be18ce1864d33c2cbad0d 209905ff1af71325da585a971b771c7ceae5a7b2 >> .git/info/grafts
    
[3.2graftedmastermerge.png]

(check the diff of the commit that we grafter onto the mastermerge

Now, let's checkout the merge release into master commit. This one is a bit more tricky, we'll need to graft the mastermerge commit onto the new releasemerge branch. However, this is a merge commit and merge commits have two parents; in this case the last commit in the develop branch (its left parent) and the (old) merge master (its right parent) into develop commit. 

    git checkout -b releasemerge 7ee6cc9ceae07b04a6f87644a7a49e07b294c581

Now, the same cp command applies:

    cp C:/Temp/agpl-prep/cleanup/pom-1.0.2-V.xml pom.xml
    git add pom.xml
    git commit --amend --no-edit
    
This again screws up the graph, so let's graft. This time we'll need three hashes:
    child left_parent right_parent

    echo 209905ff1af71325da585a971b771c7ceae5a7b2 f2005215eb21e814cade15593c8776620739a09e adc7e661f6e702fd85d2f80d9e349ee46de6979b >> .git/info/grafts
    
Now that this one has been grafted, let's move on to 

3. The lone release commit.
This commit needs to have the pom.xml updated to 1.0.2 as well, so let's do the familiar commands:

    git checkout -b release 7b45078a95ef5ed2537f382f9e48838adabe35c5
    cp C:/Temp/agpl-prep/cleanup/pom-1.0.2-V.xml pom.xml
    git add pom.xml
    git commit --amend --no-edit
    
Grafting takes two steps: graft the release merge on this one and the previous commit on master:

    echo 0ad5057b21c57d61b27f60efa5194314b977d998 fe83b060f429f9a7b1f4318f90be3170c5fcb894 0bd92ccf1afca0167a6546e3263e5213e47885a0 >> .git/info/grafts
    
And also graft the release branch onto the previous commit in the graph: the commit in develop on which it is based:

    echo 0bd92ccf1afca0167a6546e3263e5213e47885a0 7b45078a95ef5ed2537f382f9e48838adabe35c5 >> .git/info/grafts

4. Set the tag to the correct commit.

    git checkout releasemerge
    git tag -f 1.0.2

This also fixes the floating commit that the 1.0.2 was attached to.

[3.4close.png]

5. Set pom for 1.0.2-SNAPSHOT

Checkout the commit on develop that is the base for the release branch.

    git checkout -b tempdev ceefdb4562ac8b1b0d8491db1b9fabc368b60200
    
Run a filter branch that copies the pom for 1.0.2-SNAPSHOT into pom.xml 

git filter-branch -f \
    --tree-filter 'cp C:/Temp/agpl-prep/cleanup/pom-1.0.2-SN.xml pom.xml' \
    --prune-empty \
    --tag-name-filter cat \
    afe4d0181b9924da0d6805aacc82f3a038e064f0..tempdev
    
Remember, first commit is the parent of the one we want to change. In tis case the merge master into develop commit. This again makes new references, so run the for-each command again.

This is where it might get confusing:

[3.5confusing.png]

Although we checked out the "upgrade core and licensekey dependencies" commit, that commit isn't included in the tempdev branch. This is because the filterbranch command made that an empty commit and the filter-branch filters out empty commits.

6. Graft the release procedure onto the new 1.0.2-SNAPSHOT state.
We need to do 2 grafts:    
- release branch 1st commit on top of tempdev

    7b45078a95ef5ed2537f382f9e48838adabe35c5 ce29e8c47dd7a206568b0165473080e89c5e5c43
    
- merge master into develop on top of tempdev

    209905ff1af71325da585a971b771c7ceae5a7b2 ce29e8c47dd7a206568b0165473080e89c5e5c43 0ad5057b21c57d61b27f60efa5194314b977d998

Note that for this repositioning we didn't use the previous commit in the develop branch. In our case this one became unnecessary, so we graft on top of the one before that.

Now, you can remove these branches, we're done with this release.

7. Repeat this process until you've done every version.

After doing every version, run this command;

    git filter-branch -f \
    --prune-empty \
    --tag-name-filter cat \
    -- --all
    
Followed by the for-each command.

Step 4. Including the .MD files.

I've prepared a slew of markdown files that need to be in the root of our repository.

Run this command:

    git filter-branch -f \
        --tree-filter 'cp C:/Temp/agpl-prep/cleanup/addition/**.md .' \
        --prune-empty \
        --tag-name-filter cat \
        -- --all
        
Followed by the familiar for-each command.


Step 5. Rewrite the .java files.
    git filter-branch -f \
        --tree-filter 'cp C:/Temp/agpl-prep/cleanup/addition/PdfCleanupProductInfo.java src/main/java/com/itextpdf/pdfcleanup/PdfCleanupProductInfo.java' \
        --prune-empty \
        --tag-name-filter cat \
        -- --all
        
    for-each
    
    groovy C:/Code/iText/AGPL/licensekey-modifications.groovy src/
    
    mvn clean package
    
    git filter-branch -f \
        --tree-filter 'groovy C:/Code/iText/AGPL/licensekey-modifications.groovy src/' \
        --prune-empty \
        --tag-name-filter cat \
        0e822d4c0ba78d283fcc586051d683d2fbd99b12..develop
        
    for-each
        
Step 6. Add license headers to each java file.
    git filter-branch -f \
        --tree-filter 'groovy C:/Code/iText/AGPL/inspectHeaders.groovy .' \
        --prune-empty \
        --tag-name-filter cat \
        0e822d4c0ba78d283fcc586051d683d2fbd99b12..develop
        
    for-each
    
Step 7. Set the Date and Time of the commits

    git filter-branch -f \
        --prune-empty \
        --tag-name-filter cat \
        --env-filter 'export GIT_COMMITTER_DATE="$GIT_AUTHOR_DATE" && export GIT_COMMITTER_EMAIL="$GIT_AUTHOR_EMAIL" && export GIT_COMMITTER_NAME="$GIT_AUTHOR_NAME"' \
        -- --all
        
    for-each
    
Step 8. Set the master branch to the latest release.
Step 9. Push to (new/old) origin.