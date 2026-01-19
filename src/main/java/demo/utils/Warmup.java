/*
 * Copyright 2026, APT Group, Department of Computer Science,
 * The University of Manchester.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package demo.utils;

import java.io.OutputStream;
import java.io.PrintStream;

@FunctionalInterface
public interface Warmup {
    void run(int size) throws Exception;

    default void warmup(int size, int iterations, boolean quiet) {
        PrintStream original = System.out;
        if (quiet) {
            System.setOut(new PrintStream(OutputStream.nullOutputStream()));
        }
        try {
            for (int i = 0; i < iterations; i++) {
                run(size);
            }
        } catch (Exception e) {
            throw new RuntimeException("Warmup failed", e);
        } finally {
            if (quiet) {
                System.setOut(original);
            }
        }
    }

    default void warmup(int size, int iterations) {
        warmup(size, iterations, true);
    }

    default void warmup(int size) {
        warmup(size, 1, true);
    }
}
