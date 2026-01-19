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

import uk.ac.manchester.tornado.api.types.arrays.FloatArray;

public class Validator {

    private static final float EPSILON = 1e-5f;

    public static String isValid (boolean status) {
        return (status) ? "PASSED" : "FAILED";
    }

    public static boolean maxReduce(float[] maxResultJava, float[] maxResult) {
        if (Math.abs(maxResult[0] - maxResultJava[0]) > EPSILON) {
            System.out.println("maxResult: " + maxResult[0] + " - maxResultJava: " + maxResultJava[0]);
            return false;
        }
        return true;
    }

    public static boolean saxpy(float[] yJava, float[] y) {
        for (int i = 0; i < y.length; i++) {
            if (Math.abs(y[i] - yJava[i]) > EPSILON) {
                System.out.println("y[" + i + "]: " + y[i] + " - yJava[" + i + "]: " + yJava[i]);
                return false;
            }
        }
        return true;
    }

    public static boolean saxpy(FloatArray yJava, FloatArray y) {
        for (int i = 0; i < y.getSize(); i++) {
            if (Math.abs(y.get(i) - yJava.get(i)) > EPSILON) {
                System.out.println("y[" + i + "]: " + y.get(i) + " - yJava[" + i + "]: " + yJava.get(i));
                return false;
            }
        }
        return true;
    }

    public static boolean saxpyWithDataRange(FloatArray yJava, FloatArray y, int offset, int size) {
        for (int i = offset; i < offset + size; i++) {
            if (Math.abs(y.get(i) - yJava.get(i)) > EPSILON) {
                System.out.println("y[" + i + "]: " + y.get(i) + " - yJava[" + i + "]: " + yJava.get(i));
                return false;
            }
        }
        return true;
    }
}
