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
package demo.scenarios;

import demo.kernels.SaxpyKernel;
import demo.utils.DataGenerator;
import demo.utils.Timer;
import demo.utils.Validator;
import uk.ac.manchester.tornado.api.TaskGraph;
import uk.ac.manchester.tornado.api.TornadoExecutionPlan;
import uk.ac.manchester.tornado.api.TornadoExecutionResult;
import uk.ac.manchester.tornado.api.enums.DataTransferMode;
import uk.ac.manchester.tornado.api.exceptions.TornadoExecutionPlanException;

import java.util.Arrays;

public class Scenario2_FitsInGPU_UnderDemand {

    public static void run(int size) throws TornadoExecutionPlanException {
        int iterations = 100;

        float[] x = DataGenerator.randomArray(size);
        float[] xJava = Arrays.copyOf(x, x.length);
        float[] y = DataGenerator.randomArray(size);
        float[] yJava = Arrays.copyOf(y, y.length);
        float alpha = 2.0f;

        TaskGraph tg = new TaskGraph("saxpy-s2")
                .transferToDevice(DataTransferMode.FIRST_EXECUTION, x, y)
                .task("saxpy", SaxpyKernel::saxpy, x, y, alpha)
                .transferToHost(DataTransferMode.UNDER_DEMAND, y);

        try (TornadoExecutionPlan executionPlan = new TornadoExecutionPlan(tg.snapshot())) {
            Timer t = new Timer();
            t.start();
            TornadoExecutionResult result = null;
            for (int i = 0; i < iterations; i++) {
                result = executionPlan.execute();
            }
            double ms = t.stopMillis();

            t.start();
            result.transferToHost(y);
            double transferMs = t.stopMillis();

            // Run the same computation in Java
            for (int i = 0; i < iterations; i++) {
                SaxpyKernel.saxpy(xJava, yJava, alpha);
            }

            // Validate results
            boolean status = Validator.saxpy(yJava, y);
            System.out.printf("[Scenario 2 - Transfer output under demand (last execution)] Size=%d, Iterations=%d, Total=%.2f ms, Validation=%s%n", size, iterations, ms + transferMs, Validator.isValid(status));
        }
    }
}
