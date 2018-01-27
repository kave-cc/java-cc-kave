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
    class RecursionTest
    {

        public string Entry1(string arg)
        {
            if (arg.Length == 1)
            {
                return arg;
            }
            else
            {
                int half = arg.Length/2;
                return Entry1(arg.Substring(half)) + Entry1(arg.Substring(0, half));
            }
        }

        public void Entry2(string arg)
        {
            if (arg != string.Empty)
            {
                int half = arg.Length/2;
                Show(arg.Substring(0, half));
            }
        }

        private void Show(string arg)
        {
            Console.WriteLine(arg);
            Entry2(arg);
        }
    }
}
