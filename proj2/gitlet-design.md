# Gitlet Design Document

**Name**: cirno-nine

## Classes and Data Structures

### Blop

#### Variable

1. name: store the file name.
2. data: use String to store file data.


### Commit

#### Variable

1. sha_name: store sha1, sha1 enctropted use timestamp and commit message.
2. parent_sha_name: just use
3. parent: parent Commit reference
4. message: use commit message
5. map: use String file_name cast to BLop blop.
6. sec_par: sec_parent, default is null.

#### Method or called Function

1. instructor:like the name
2. lca_commit: use to figure out what the latest common ancestor, for given two Commit
3. lca: use String to figure.
4. print_log: print log message use sha_name, commit message, etc.

### Repository

#### Variable

1. DIRS: as we can see,staged_dir store add staged files
2. REMOVAL_DIR: store staged remove files.
3. LOG_DIR: store commit files
4. BLOP_DIR: store file use class Blop(But commit map contains blop) (not used)
5. REMOTE_DIR: store remote name and where dir
6. BRANCH_DIR: store branch information store as Commit class
7. not dirs:
8. like head_pointer: store now commit
9. like current_branch: store now branch name

#### methods

1. a lot of command methods like: add, commit, push

## Algorithms

1. LCA(?): maybe, recursive method, used in merge method.

## Persistence

```angular2html
CWD                             <==== Whatever the current working directory is.
└── .gitlet                     <==== All persistant data is stored within here
    ├── stage                   <==== All added staged files  stored in this directory
    ├── removal                 <==== All removal staged files are stored in this directory
    ├── log                     <==== All commit files are stored in this directory
    ├── branch                  <==== All branch files are stored in this directory
    ├── remote                  <==== All remote files are stored in this directory
    ├── Head_pointer            <==== Head commit pointer (is a file)
    └── current_branch          <==== now branch name (is a file)
```

