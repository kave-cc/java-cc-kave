/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
using System;

namespace Test
{
    class DelegateTest
    {

        public String entry1()
        {
            Func<object, string> fun = foo;
            object arg = 42;
            return fun(arg);
        }

        public String entry2()
        {
            Func<object, string> fun = (object x) => string.Format("x = {0}", x);
            object arg = 1337;
            return fun(arg);
        }

        private string foo(object x)
        {
            return string.Format("x is {0}", x);
        }
    }
}
