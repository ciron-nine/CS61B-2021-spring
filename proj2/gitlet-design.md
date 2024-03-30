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


## Algorithms

## Persistence

