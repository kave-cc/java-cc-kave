To avoid a cyclic dependency between cc.kave.commons and cc.kave.testcommons, we had to include all test utils
here that are required in the cc.kave.commons project. *All* other reusable test utils should be put into the
cc.kave.testcommons project!