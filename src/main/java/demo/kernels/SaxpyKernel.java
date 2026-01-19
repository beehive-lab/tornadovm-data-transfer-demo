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
package demo.kernels;

import uk.ac.manchester.tornado.api.annotations.Parallel;
import uk.ac.manchester.tornado.api.types.arrays.FloatArray;

public class SaxpyKernel {

    public static void saxpy(float[] x, float[] y, float alpha) {
        for (@Parallel int i = 0; i < y.length; i++) {
            y[i] = alpha * x[i] + y[i];
        }
    }

    public static void saxpy2(float[] x, float[] y, float alpha) {
        for (int i = 0; i < y.length; i++) {
            y[i] = alpha * x[i] + y[i];
        }
    }

    public static void saxpy(FloatArray x, FloatArray y, float alpha) {
        for (@Parallel int i = 0; i < y.getSize(); i++) {
            y.set(i, alpha * x.get(i) + y.get(i));
        }
    }
}
